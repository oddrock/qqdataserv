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
		String url = "http://tj-ctfs.ftn.qq.com/ftn_handler/ab0c11bdefd56733674830f47daeebabfd7a4ec716e1c9fe3d7162466aca54e850c48b40ff1cd3bfbc352d02278073fc14547305484bf594a810f03abb450e50/?fname=B10.pdf&k=09623361863a8fcf26b905794261041d505451575c030154185a0203554c0206535a1e595259531f0151060754580f04045157526466367004521d1100073644&fr=00&&txf_fid=c051f52f14e3c526418afb70012db5626aae1cd2";
		downLoadFromUrl(url, "B10.pdf", "C:/_Download/upload/");
	}
}
