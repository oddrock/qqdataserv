package com.ustcinfo.common.mail;

import javax.mail.Message;

public interface EmailManager {
	public abstract Message[] recvNewMail(
			String imapServer, String emailAccount, 
			String emailPasswd, String folderName, boolean readwriteFlag);
	
}
