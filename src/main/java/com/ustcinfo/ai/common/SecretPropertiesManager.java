package com.ustcinfo.ai.common;

import com.ustcinfo.common.utils.MultiPropertiesReader;

public class SecretPropertiesManager {
	private static final MultiPropertiesReader MPR = new MultiPropertiesReader();
	static{
		load();
	}
	
	private static void load(){
		MPR.addFilePath("prop/secret/db.properties");
		MPR.addFilePath("prop/secret/qqdataserv-secret.properties");
		MPR.loadProperties();
	}
	
	public static String getValue(String key){
		return MPR.getValue(key);
	}
	
	public static String getValue(String key, String defaultValue) {
		return MPR.getValue(key, defaultValue);
	}
}
