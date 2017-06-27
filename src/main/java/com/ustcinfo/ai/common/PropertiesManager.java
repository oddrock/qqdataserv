package com.ustcinfo.ai.common;

import com.ustcinfo.common.utils.MultiPropertiesReader;

public class PropertiesManager {
	private static final MultiPropertiesReader MPR = new MultiPropertiesReader();
	static{
		load();
	}
	
	private static void load(){
		MPR.addFilePath("prop/qqdataserv.properties");
		MPR.addFilePath("prop/df-mail-cfg.properties");
		MPR.addFilePath("prop/df-common-cfg.properties");
		MPR.addFilePath("prop/df-test-cfg.properties");
		MPR.addFilePath("prop/df-qq-cfg.properties");
		MPR.addFilePath("prop/df-wx-cfg.properties");
		MPR.loadProperties();
	}
	
	public static String getValue(String key){
		return MPR.getValue(key);
	}
	
	public static String getValue(String key, String defaultValue) {
		return MPR.getValue(key, defaultValue);
	}
}
