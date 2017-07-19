package com.ustcinfo.common.shell.bean;

public class RmtShellExcutorOutput {
	private String stdOut;
	private String stdErr;
	public String getStdOut() {
		return stdOut;
	}
	public void setStdOut(String stdOut) {
		this.stdOut = stdOut;
	}
	public String getStdErr() {
		return stdErr;
	}
	public void setStdErr(String stdErr) {
		this.stdErr = stdErr;
	}
	public String getOutputStr(){
		if(this.stdErr!=null && this.stdErr.trim().length()>0){
			return this.stdErr;
		}else{
			return this.stdOut;
		}
	}
	/**
	 * 判断命令执行是否成功
	 * @return
	 */
	public boolean isSuccess(){
		if(this.stdErr==null || this.stdErr.trim().length()==0){
			return true;
		}else{
			return false;
		}
	}
}
