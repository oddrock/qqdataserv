package com.ustcinfo.ai.wx;

import org.apache.camel.Exchange;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.jayway.jsonpath.JsonPath;
import com.ustcinfo.ai.qq.QQMsg;

@Component("plugins4WX")
public class Plugins4WX {
	private static Logger logger = Logger.getLogger(Plugins4WX.class);
	public String rcv(Exchange exchange, String payload){
		logger.warn("plugins4WX has enter rcv method");
		logger.warn(payload);
		return payload;
	}
	
	public static void main(String[] args) {
		
	}

}
