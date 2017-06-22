package com.ustcinfo.common.utils;
public class PropertiesUtil {
	private final static PropertiesReader PROP_READER = new PropertiesReader("prop/qqdataserv.properties");

	public static String getValue(String key){
		return PROP_READER.getValue(key);
	}
	
	public static String getValue(String key, String defaultValue) {
		return PROP_READER.getValue(key, defaultValue);
	}
}