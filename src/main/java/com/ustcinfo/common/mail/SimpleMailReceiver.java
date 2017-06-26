package com.ustcinfo.common.mail;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import com.sun.mail.imap.IMAPFolder;
import com.sun.mail.imap.IMAPStore;

/**
 * 简单的邮件接收类
 * 
 * @author athena
 * 
 */
public class SimpleMailReceiver {

	/**
	 * 收取收件箱里的邮件
	 * 
	 * @param props
	 *            为邮件连接所需的必要属性
	 * @param authenticator
	 *            用户验证器
	 * @return Message数组（邮件数组）
	 * @throws Exception 
	 */
	public static Message[] fetchInbox(Properties props, Authenticator authenticator){
		return fetchInbox(props, authenticator, null);
	}

	/**
	 * 收取收件箱里的邮件
	 * 
	 * @param props
	 *            收取收件箱里的邮件
	 * @param authenticator
	 *            用户验证器
	 * @param protocol
	 *            使用的收取邮件协议，有两个值"pop3"或者"imap"
	 * @return Message数组（邮件数组）
	 * @throws MessagingException 
	 */
	public static Message[] fetchInbox(Properties props, Authenticator authenticator, String protocol) {
		Message[] messages = null;
		Session session = Session.getDefaultInstance(props, authenticator);
		IMAPStore store;
		try {
			store = (IMAPStore) session.getStore("imap");
			store.connect();
			IMAPFolder folder = (IMAPFolder) store.getFolder("INBOX");
			folder.open(Folder.READ_WRITE);    
			messages = folder.getMessages(); 
			Flags flags;  
			List<Message> list = new ArrayList<Message>();
			for (Message message : messages) {  
				flags = message.getFlags(); 
				if (!flags.contains(Flags.Flag.SEEN)){
					list.add(message);
				}
					
			}
			messages = new Message[list.size()];
			int i = 0;
			for(Message message:list){
				messages[i++]=message;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return messages;
	}
}