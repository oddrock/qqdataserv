package com.ustcinfo.common.utils;

import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.ustcinfo.ai.common.EmailManager;

public class EmailUtils {
	/**
	 * 
	 * @param senderEmailAccount	发送方邮箱地址
	 * @param senderName			发送方名称（会在邮件中作为发送方显示出来）
	 * @param senderEmailPasswd		发送方邮箱密码（如果是网易邮箱且开通了授权，就是授权码）
	 * @param senderEmailSmtp		发送方SMTP地址
	 * @param recverEmailAccount	接收方邮箱地址
	 * @param subject				邮件题目
	 * @param content				邮件内容
	 * @throws Exception
	 */
	public static void sendEmail(String senderEmailAccount, String senderEmailPasswd, 
			String senderEmailSmtp, String senderName,
			String[] recverEmailAccount, String subject, String content, boolean debug) throws Exception{
    	Properties props = new Properties();                    
        props.setProperty("mail.transport.protocol", "smtp");   
        props.setProperty("mail.smtp.host", senderEmailSmtp);  
        props.setProperty("mail.smtp.auth", "true");           
        Session session = Session.getDefaultInstance(props);    
        session.setDebug(debug);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmailAccount,senderName));
        for (String recv: recverEmailAccount){
        	message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recv));
        }
        message.setSubject(subject);	
        message.setContent(content, "text/html;charset=UTF-8");
        message.setSentDate(new Date());	
        message.saveChanges();	
        Transport transport = session.getTransport();
        transport.connect(senderEmailAccount, senderEmailPasswd);
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
    }
	
	public static void main(String[] args) throws Exception {
		/*EmailUtils.sendEmail(PropertiesUtil.getValue("mail.sender.account"), 
				PropertiesUtil.getValue("mail.sender.passwd"), 
				PropertiesUtil.getValue("mail.sender.smtp"), 
				PropertiesUtil.getValue("mail.sender.name"),
				PropertiesUtil.getValue("mail.recver.account.default").split(","), 
				"1工作上真的要小心", "赵刚的要求可要注意啊");*/
		
    }
}
