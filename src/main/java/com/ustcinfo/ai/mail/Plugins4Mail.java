package com.ustcinfo.ai.mail;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.ustcinfo.common.mail.AuthenticatorGenerator;
import com.ustcinfo.common.mail.HostType;
import com.ustcinfo.common.mail.MessageParser;
import com.ustcinfo.common.mail.SimpleMailReceiver;

@Component("plugins4Mail")
public class Plugins4Mail {
	private static Logger logger = Logger.getLogger(Plugins4Mail.class);
	public String rcv(Exchange exchange, String payload){
		logger.warn("has enter Plugins4Mail rcv method");
		MessageParser.parse(SimpleMailReceiver.fetchInbox(HostType.NETEASE.getProperties(),
				AuthenticatorGenerator.getAuthenticator("bakerstreet221b520@163.com", "ustcinfo123")));
		return payload;
	}
	public static void main(String[] args) {
		
	}

}
