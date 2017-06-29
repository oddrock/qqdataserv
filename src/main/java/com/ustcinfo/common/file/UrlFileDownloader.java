package com.ustcinfo.common.file;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.log4j.Logger;

/**
 * 网络URL文件下载器
 * 
 * @author oddrock
 *
 */
public class UrlFileDownloader {
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(UrlFileDownloader.class);
	
	/**
	 * 从网络Url中下载文件
	 * 
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public static void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为3秒
		conn.setConnectTimeout(3 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent", 
				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		// 得到输入流
		InputStream inputStream = conn.getInputStream();
		// 获取自己数组
		byte[] getData = readInputStream(inputStream);
		// 文件保存位置
		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		File file = new File(saveDir + File.separator + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}
	}

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	public static void main(String[] args) throws IOException {
		String url = "https://mail.qq.com/cgi-bin/ftnExs_download?k=6d383431416c2e9925e3022913630249520e0c57535654501b090404504e0407040f19500d02074b0559010503530702005e0c54354530d9f08cc788cfd784d3d1e8f1fbf7b185d4ccfe83e0e5d492aad3f7818cdcaaec1002164455536346&t=exs_ftn_download&code=68415c0f";
		downLoadFromUrl(url, "科大国创电信事业产品研发体系介绍v4.pdf", "C:/_Download/");
	}
}
