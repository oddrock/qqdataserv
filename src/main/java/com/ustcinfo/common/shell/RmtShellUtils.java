package com.ustcinfo.common.shell;

import com.ustcinfo.ai.common.PropertiesManager;
import com.ustcinfo.common.shell.bean.RmtShellExcutorOutput;

public class RmtShellUtils {
	public static RmtShellExcutorOutput excuteShell(
			String shellcmd, String ip, String user, String passwd){
		RmtShellExecutor exe = new RmtShellExecutor(ip, user, passwd);
        return exe.exec(shellcmd);
	}
	
	public static RmtShellExcutorOutput excuteShell(
			String shellcmd, String ip, String user, String passwd, int timeout){
		RmtShellExecutor exe = new RmtShellExecutor(ip, user, passwd, timeout);
		return exe.exec(shellcmd);
	}
	
	public static void main(String[] args){
		String cmd = "nohup compress-pdf /root/科大国创电信事业产品研发体系介绍v4.pdf >/dev/null 2>&1 &";
		cmd = "compress-pdf /root/科大国创电信事业产品研发体系介绍v4.pdf";
		RmtShellExcutorOutput output = excuteShell(
				cmd
				,PropertiesManager.getValue("pdfdealer.linuxsrv.ip")
				,PropertiesManager.getValue("pdfdealer.linuxsrv.user")
				,PropertiesManager.getValue("pdfdealer.linuxsrv.passwd")
				, 1000*60*10);
		System.out.println(output.getStdErr());
		System.out.println(output.getStdOut());
		System.out.println(output.isSuccess());
	}
}
