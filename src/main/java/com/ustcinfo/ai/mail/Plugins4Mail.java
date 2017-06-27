package com.ustcinfo.ai.mail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.ustcinfo.ai.common.PropertiesManager;
import com.ustcinfo.common.mail.ImapEmailManager;
import com.ustcinfo.common.mail.bean.Email;
import com.ustcinfo.common.mail.bean.EmailAttachment;
import com.ustcinfo.common.utils.DateUtils;
import com.ustcinfo.common.utils.FileUtils;
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
			logger.warn("没有任何邮件");
		}
		List<PdfTask> pdfTaskList = parsePdfTask(emails);
		for(PdfTask pdfTask : pdfTaskList){
			logger.warn("---------------");
			logger.warn(pdfTask.getBatch_no());
			logger.warn(pdfTask.getOpt());
			logger.warn(pdfTask.getFile_path());
			logger.warn(pdfTask.getTo_email());
			logger.warn("---------------");
		}
		iem.showEmails(emails, true);
		return payload;
	}
	
	/*
	 * 将收到的邮件解析为PDF转换任务
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
			String batchNo = email.getFrom()+"|"+StringUtils.substringFrontN(email.getSubject(), 32)+"|"+DateUtils.getFormatTime1(now);
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
					pdfTaskList.add(pdfTask);
				}
				
			}	
		}
		return pdfTaskList;
		
	}
	
	public static void main(String[] args){

	}
	
}
