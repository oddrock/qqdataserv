package com.ustcinfo.ai.mail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.camel.Exchange;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.ustcinfo.ai.common.PropertiesManager;
import com.ustcinfo.ai.mail.bean.PdfTask;
import com.ustcinfo.common.file.UrlFileDownloader;
import com.ustcinfo.common.mail.ImapEmailManager;
import com.ustcinfo.common.mail.bean.Email;
import com.ustcinfo.common.mail.bean.EmailAttachment;
import com.ustcinfo.common.qq.file.QQFileDownloader;
import com.ustcinfo.common.qq.file.bean.QQFileHtmlUrls;
import com.ustcinfo.common.shell.RmtShellUtils;
import com.ustcinfo.common.shell.bean.RmtShellExcutorOutput;
import com.ustcinfo.common.utils.DateUtils;
import com.ustcinfo.common.utils.FileUtils;
import com.ustcinfo.common.utils.MyBatisUtil;
import com.ustcinfo.common.utils.StringUtils;

@Component("plugins4Mail")
public class Plugins4Mail {
	private static Logger logger = Logger.getLogger(Plugins4Mail.class);
	private static final String OPT_COMPRESS = "compress";
	public String rcv(Exchange exchange, String payload){
		logger.warn("has enter Plugins4Mail rcv method");
		ImapEmailManager iem = new ImapEmailManager();
		String imapServer = PropertiesManager.getValue("mail.pdfdealer.server");
		String attachmentLocalFolder = PropertiesManager.getValue("mail.pdfdealer.attachmentlocalfolder");
		String emailAccount = PropertiesManager.getValue("mail.pdfdealer.account");
		String emailPasswd = PropertiesManager.getValue("mail.pdfdealer.passwd");
		String folderName = PropertiesManager.getValue("mail.pdfdealer.foldername");
		String readwrite = PropertiesManager.getValue("mail.pdfdealer.readwrite");
		boolean readwriteFlag = false;
		if("true".equalsIgnoreCase(readwrite.trim())){
			readwriteFlag = true;
		}
		Email[] emails = iem.parseEmail(iem.recvNewMail(imapServer, emailAccount, emailPasswd, folderName, readwriteFlag), attachmentLocalFolder);
		if(emails.length==0){
			//logger.warn("没有任何邮件");
		}
		iem.showEmails(emails, false);
		EmailAttachment ea = null;
		String qqFileUrl = null;
		// 解析其中的QQ中转站下载连接，下载QQ中转站文件作为附件
		for(Email email: emails){
			List<QQFileHtmlUrls> list = QQFileDownloader.parseQQFileHtmlUrlsFromQQMail(email.getPlainContent());
			for(QQFileHtmlUrls e : list){	
				try {
					qqFileUrl = QQFileDownloader.parseQQFileUrlsFromQQFileHtmlUrl(e.getQqFileHtmlUrl()).get(0);
					logger.warn("开始下载：" + e.getQqFileName() + " | " + qqFileUrl);
					UrlFileDownloader.downLoadFromUrl(qqFileUrl, e.getQqFileName(), attachmentLocalFolder);
					ea = new EmailAttachment();
					ea.setName(e.getQqFileName());
					ea.setLocalFilePath(attachmentLocalFolder + File.separator + e.getQqFileName());
					email.getAttachments().add(ea);
					logger.warn("结束下载：" + e.getQqFileName() + " | " + qqFileUrl);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
		List<PdfTask> pdfTaskList = parsePdfTask(emails);
		int i = 1;
		if(pdfTaskList.size()>0){
			logger.warn("------- PDF转换任务 start --------");
			for(PdfTask pdfTask : pdfTaskList){
				logger.warn("【任务"+ i++ +"】：" + pdfTask.getBatch_no() + " | " + pdfTask.getOpt() 
						+ " | " + pdfTask.getFile_path() + " | " + pdfTask.getTo_email());			
			}
			logger.warn("------- PDF转换任务 end   --------");
		}
		savePdfTaskToDb(pdfTaskList);
		logger.warn("has leave Plugins4Mail rcv method");
		return payload;
	}
	
	/*
	 * 将收到的邮件批量解析为PDF转换任务
	 */
	private List<PdfTask> parsePdfTask(Email[] emails){
		List<PdfTask> pdfTaskList = new ArrayList<PdfTask>();
		if(emails==null) return pdfTaskList;
		List<PdfTask> tmpList;
		for(Email email:emails){
			tmpList = parsePdfTask(email);
			for(PdfTask task : tmpList){
				pdfTaskList.add(task);
			}
		}
		return pdfTaskList;
	}

	/*
	 * 将收到的一份邮件解析为PDF转换任务
	 */
	private List<PdfTask> parsePdfTask(Email email) {
		List<PdfTask> pdfTaskList = new ArrayList<PdfTask>();
		if(email==null) return pdfTaskList;
		if(email.getAttachments().size()==0) return pdfTaskList;
		String subject = email.getSubject();
		if(subject!=null){
			subject = subject.toLowerCase();	
			subject = StringUtils.replaceFirst(subject, "：", ":");
			String opt = null;
			String recvmail = null;
			// 对标题通过分号做切分，第一个分号之前的是pdf操作名，后面的是接收的邮箱
			if(subject.contains(":")){
				opt = StringUtils.removeAllSpace(subject.split(":")[0]);
				recvmail = StringUtils.removeAllSpace(subject.split(":")[1]);
			}else{
				opt = StringUtils.removeAllSpace(subject);
			}
			// 如果没有接收邮箱，就把发送邮箱当做接收邮箱
			if(recvmail==null || recvmail.length()==0){
				recvmail = email.getFrom();
			}
			boolean needOpt = false;		// 判断是否需要操作
			String std_opt = "";
			if(opt.contains("pdf") && opt.contains("压缩")){
				needOpt = true;
				std_opt = OPT_COMPRESS;
			}
			if(!needOpt){
				return pdfTaskList;
			}
			boolean auth = false;
			if(needOpt){
				// 授权用户，即这些用户给我发信，我会帮他转换
				String[] authMails = PropertiesManager
						.getValue("mail.pdfdealer.authsender").split(",");
				for(String authMail : authMails){
					if (authMail.equals(email.getFrom())){
						auth = true;
						break;
					}
				}
			}
			if(needOpt && !auth){
				logger.warn("----------------");
				logger.warn("邮件标题："+email.getSubject());
				logger.warn("from："+email.getFrom());
				logger.warn("不是授权用户，无法替其转换");
				logger.warn("----------------");
				return null;
			}
			Date now = new Date();
			PdfTask pdfTask = null;
			String batchNo = email.getFrom()
					+"|"+StringUtils.substringFrontN(email.getSubject(), 64)
					+"|"+DateUtils.getFormatTime1(now);
			for(EmailAttachment attachment : email.getAttachments()){		
				if(FileUtils.getFileNameSuffix(attachment.getLocalFilePath()).equalsIgnoreCase("pdf")){
					pdfTask = new PdfTask();
					pdfTask.setBatch_no(batchNo);
					pdfTask.setCreate_time(now);
					pdfTask.setFile_path(attachment.getLocalFilePath());
					pdfTask.setFrom_email(email.getFrom());
					pdfTask.setFrom_subject(email.getSubject());
					pdfTask.setOpt(std_opt);
					pdfTask.setTask_status("todo");
					pdfTask.setTo_email(recvmail);
					pdfTask.setUpdate_time(now);
					pdfTaskList.add(pdfTask);
				}
			}	
		}
		return pdfTaskList;
	}
	
	@SuppressWarnings("unused")
	private void savePdfTaskToDb(List<PdfTask> pdfTaskList){
		SqlSession sqlSession = MyBatisUtil.getSqlSession(true);
		String statement = "com.ustcinfo.ai.mail.bean.PdfTaskMapper.addTask";//映射sql的标识字符串
		int retResult;
		for(PdfTask pt : pdfTaskList){
	        retResult = sqlSession.insert(statement,pt);
		}
		sqlSession.close();
	}
	
	public String dotask(Exchange exchange, String payload){
		logger.warn("has enter Plugins4Mail dotask method");
		SqlSession sqlSession = MyBatisUtil.getSqlSession();
		// 每次暂时只取一条任务执行
		String statement = "com.ustcinfo.ai.mail.bean.PdfTaskMapper.getOneCompressTodoTask"; 
		List<PdfTask> list = sqlSession.selectList(statement);
		boolean hasTask = false;
		if(list.size()>0){
			PdfTask pdfTask = list.get(0);
			pdfTask.setTask_status("doing");
			pdfTask.setUpdate_time(new Date());
			pdfTask.setTry_count(pdfTask.getTry_count()+1);
			statement = "com.ustcinfo.ai.mail.bean.PdfTaskMapper.updateTaskStatus"; 
			int retResult = sqlSession.update(statement,pdfTask);
			if(retResult==1){
				list.set(0, pdfTask);
				hasTask = true;
			}
		}
		sqlSession.close();
		if(hasTask){
			PdfTask doingtask = list.get(0);
			logger.warn("start-执行转换任务："+doingtask.toString());
			boolean success = false;
			if("compress".equalsIgnoreCase(doingtask.getOpt())){
				success = compressPdf(doingtask);
			}
			sqlSession = MyBatisUtil.getSqlSession();
			if(success){
				doingtask.setTask_status("done");
				logger.warn("成功-执行转换任务："+doingtask.toString());
			}else{
				doingtask.setTask_status("todo");
				logger.warn("失败-执行转换任务："+doingtask.toString());
			}
			doingtask.setUpdate_time(new Date());
			statement = "com.ustcinfo.ai.mail.bean.PdfTaskMapper.updateTaskStatus"; 
			sqlSession.update(statement,doingtask);
			sqlSession.close();
		}
		logger.warn("has leave Plugins4Mail dotask method");
		return payload;
	}
	
	private boolean compressPdf(PdfTask pdfTask){
		RmtShellExcutorOutput output = RmtShellUtils.excuteShell(PropertiesManager.getValue("pdfdealer.linuxsrv.ip")
				,PropertiesManager.getValue("pdfdealer.linuxsrv.user")
				,PropertiesManager.getValue("pdfdealer.linuxsrv.passwd")
				,"nohup compress-pdf "+pdfTask.getFile_path()+" >/dev/null 2>&1 &");
		return output.isSuccess();
	}
}
