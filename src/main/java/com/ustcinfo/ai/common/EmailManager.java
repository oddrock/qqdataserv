package com.ustcinfo.ai.common;

import java.util.Date;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailManager {

	public static void sendEmailByDefault(String subject, String content) throws Exception{
		Properties props = new Properties();                    
	    props.setProperty("mail.transport.protocol", "smtp");   
	    props.setProperty("mail.smtp.host", PropertiesManager.getValue("mail.sender.smtp"));  
	    props.setProperty("mail.smtp.auth", "true");           
	    Session session = Session.getDefaultInstance(props);    
	    session.setDebug(Boolean.parseBoolean(PropertiesManager.getValue("mail.debug")));
	    MimeMessage message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(PropertiesManager.getValue("mail.sender.account"),
	    		PropertiesManager.getValue("mail.sender.name")));
	    for (String recv: PropertiesManager.getValue("mail.recver.account.default").split(",")){
	    	message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recv));
	    }
	    message.setSubject(subject);	
	    message.setContent(content, "text/html;charset=UTF-8");
	    message.setSentDate(new Date());	
	    message.saveChanges();	
	    Transport transport = session.getTransport();
	    transport.connect(PropertiesManager.getValue("mail.sender.account"), 
	    		PropertiesManager.getValue("mail.sender.passwd"));
	    transport.sendMessage(message, message.getAllRecipients());
	    transport.close();
	}

	public static void main(String[] args) throws Exception{
		EmailManager.sendEmailByDefault("工作上真的要小心", "积极，少犯错");
	}
}
