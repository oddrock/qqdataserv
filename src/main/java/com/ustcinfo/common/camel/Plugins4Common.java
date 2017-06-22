package com.ustcinfo.common.camel;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component("plugins4Common")
public class Plugins4Common {
	private static Logger logger = Logger.getLogger(Plugins4Common.class);
	
	public String nonsense(String payload){
		logger.warn("has enter nonsense method");
		return payload;
	}
}
