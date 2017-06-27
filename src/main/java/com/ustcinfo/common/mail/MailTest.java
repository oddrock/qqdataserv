package com.ustcinfo.common.mail;


/**
 * 邮件测试类
 * 
 * @author athena
 * 
 */
public class MailTest {

	public static void main(String[] args){
		MessageParser.parse(SimpleMailReceiver.fetchInbox(HostType.NETEASE.getProperties(),
				AuthenticatorGenerator.getAuthenticator("邮箱", "密码")));
	}
}