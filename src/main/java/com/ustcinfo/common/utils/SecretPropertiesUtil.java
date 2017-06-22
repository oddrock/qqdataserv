package com.ustcinfo.common.utils;

/**
 * 属性文件读取工具类
 * @author oddrock
 *
 */
public class SecretPropertiesUtil {
	private final static PropertiesReader PROP_READER = new PropertiesReader("prop/secret/qqdataserv-secret.properties");

	public static String getValue(String key){
		return PROP_READER.getValue(key);
	}
	
	public static String getValue(String key, String defaultValue) {
		return PROP_READER.getValue(key, defaultValue);
	}
}
