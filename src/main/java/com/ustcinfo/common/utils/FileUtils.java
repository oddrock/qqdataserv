package com.ustcinfo.common.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileUtils {
	public static String getFileNameSuffix(String fileName){
		if(fileName!=null){
			return fileName.substring(fileName.lastIndexOf(".")+1);
		}else{
			return "";
		}
	}
	
	public static String readFileContentToStr(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sb = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
            	sb.append(tempString+"\n");
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb.toString();
    }

	public static void main(String[] args) {

	}

}
