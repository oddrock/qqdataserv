package com.ustcinfo.ai.mail.bean;

import java.util.Date;

/**
 * PDF转换任务表
 * @author oddrock
 *
 */
public class PdfTask {
	private String batch_no;
	private String file_path;
	private String opt;
	private String task_status;
	private String from_email;
	private String from_subject;
	private String to_email;
	private Date create_time;
	private Date update_time;
	public String getBatch_no() {
		return batch_no;
	}
	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}
	public String getFile_path() {
		return file_path;
	}
	public void setFile_path(String file_path) {
		this.file_path = file_path;
	}
	public String getOpt() {
		return opt;
	}
	public void setOpt(String opt) {
		this.opt = opt;
	}
	public String getTask_status() {
		return task_status;
	}
	public void setTask_status(String task_status) {
		this.task_status = task_status;
	}
	public String getFrom_email() {
		return from_email;
	}
	public void setFrom_email(String from_email) {
		this.from_email = from_email;
	}
	public String getFrom_subject() {
		return from_subject;
	}
	public void setFrom_subject(String from_subject) {
		this.from_subject = from_subject;
	}
	public String getTo_email() {
		return to_email;
	}
	public void setTo_email(String to_email) {
		this.to_email = to_email;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	@Override
	public String toString() {
		return "PdfTask [batch_no=" + batch_no + ", file_path=" + file_path
				+ ", opt=" + opt + ", task_status=" + task_status
				+ ", from_email=" + from_email + ", from_subject="
				+ from_subject + ", to_email=" + to_email + ", create_time="
				+ create_time + ", update_time=" + update_time + "]";
	}
}
