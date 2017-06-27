package com.ustcinfo.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author oddrock
 *
 */
public class StringUtils {
	/**
	 * 替换第一个符合要求的子串
	 * @param parentStr		父字符串
	 * @param fromStr		要被替换的字符串
	 * @param toStr			用于替换的字符串
	 * @return
	 */
	public static String replaceFirst(String parentStr, String fromStr, String toStr){
		Pattern p = Pattern.compile(fromStr);
		Matcher m = p.matcher(parentStr);
		return m.replaceFirst(toStr);
	}
	
	/**
	 * 删除字符串中所有空格
	 * @param str
	 * @return
	 */
	public static String removeAllSpace(String str){
		if(str!=null){
			str = str.trim();
			str = str.replace(" ", "");
		}
		return str;
	}
	
	/**
	 * 截取前N个字符
	 * @param str
	 * @param n
	 * @return
	 */
	public static String substringFrontN(String str, int n){
		if(str==null) return str;
		int endIndex = str.length()-1;
		if(str.length()>=n){
			endIndex = n-1;
		}
		return str.substring(0, endIndex);
	}
	
	public static void main(String[] args) {
		String instring = "转换：哈哈儿:oddrock@gmail.com：这么好";
		System.out.println(instring.substring(0, 3));
	}
}
