package com.ustcinfo.common.mail.ext;

public class EmailAttachment {
	private String name;
	private String contentType;
	private String localFilePath;		// 保存在本地的文件路径
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getLocalFilePath() {
		return localFilePath;
	}
	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}
	@Override
	public String toString() {
		return "EmailAttachment [name=" + name + ", contentType=" + contentType
				+ ", localFilePath=" + localFilePath + "]";
	}
}
