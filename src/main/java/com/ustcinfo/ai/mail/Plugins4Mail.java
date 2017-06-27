package com.ustcinfo.ai.mail;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.ustcinfo.ai.common.PropertiesManager;
import com.ustcinfo.ai.common.SecretPropertiesManager;
import com.ustcinfo.common.mail.ext.Email;
import com.ustcinfo.common.mail.ext.EmailAttachment;
import com.ustcinfo.common.mail.ext.ImapEmailManager;

@Component("plugins4Mail")
public class Plugins4Mail {
	private static Logger logger = Logger.getLogger(Plugins4Mail.class);
	public String rcv(Exchange exchange, String payload){
		logger.warn("has enter Plugins4Mail rcv method");
		ImapEmailManager iem = new ImapEmailManager();
		String imapServer = PropertiesManager.getValue("mail.pdfdealer.server");
		String attachmentLocalFolder = PropertiesManager.getValue("mail.pdfdealer.attachmentlocalfolder");
		String emailAccount = SecretPropertiesManager.getValue("mail.pdfdealer.account");
		String emailPasswd = SecretPropertiesManager.getValue("mail.pdfdealer.passwd");
		Email[] emails = iem.parseEmail(iem.recvNewMail(imapServer, emailAccount, emailPasswd), attachmentLocalFolder);
		/*for(Email e : emails){
			logger.warn("---------------");
			logger.warn(e.getSubject());
			logger.warn(e.getHtmlContent());
			logger.warn(e.getPlainContent());
			for(EmailAttachment ea : e.getAttachments()){
				logger.warn(ea.getLocalFilePath());
			}
			logger.warn("---------------");
		}*/
		return payload;
	}
	
}
