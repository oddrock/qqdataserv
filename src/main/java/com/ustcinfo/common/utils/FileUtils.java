package com.ustcinfo.common.utils;

public class FileUtils {
	public static String getFileNameSuffix(String fileName){
		if(fileName!=null){
			return fileName.substring(fileName.lastIndexOf(".")+1);
		}else{
			return "";
		}
	}
	

	public static void main(String[] args) {

	}

}
