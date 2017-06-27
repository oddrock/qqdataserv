package com.ustcinfo.common.mail;

import javax.mail.Message;

public interface EmailManager {
	public abstract Message[] recvNewMail(
			String recvServer, String emailAccount, 
			String emailPasswd, String folderName, boolean readwriteFlag);
	
}
