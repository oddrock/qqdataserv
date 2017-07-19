package com.ustcinfo.common.shell;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ustcinfo.common.shell.bean.RmtShellExcutorOutput;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class RmtShellExecutor {
    private static final Logger logger = LoggerFactory.getLogger(RmtShellExecutor.class);
    private Connection conn;
    private String ip;
    private String usr;
    private String psword;
    private int timeout = 1000 * 60 * 5;
    private String charset = Charset.defaultCharset().toString();


    public RmtShellExecutor(String ip, String usr, String ps) {
        this.ip = ip;
        this.usr = usr;
        this.psword = ps;
    }
    
    public RmtShellExecutor(String ip, String usr, String ps, int timeout) {
        this.ip = ip;
        this.usr = usr;
        this.psword = ps;
        this.timeout=timeout;
    }

    private boolean login() throws IOException {
        conn = new Connection(ip);
        conn.connect();
        return conn.authenticateWithPassword(usr, psword);
    }

    @SuppressWarnings("unused")
	public RmtShellExcutorOutput exec(String cmds){
    	RmtShellExcutorOutput out = new RmtShellExcutorOutput();
        InputStream stdOut = null;
        InputStream stdErr = null;
        String outStr = "";
        String outErr = "";
        int ret = -1;
        try {
            if (login()) {
                Session session = conn.openSession();
                session.execCommand(cmds);
                stdOut = new StreamGobbler(session.getStdout());
                outStr = processStream(stdOut, charset);
                out.setStdOut(outStr);
                logger.info("caijl:[INFO] outStr=" + outStr);
                stdErr = new StreamGobbler(session.getStderr());
                outErr = processStream(stdErr, charset);
                out.setStdErr(outErr);
                logger.info("caijl:[INFO] outErr=" + outErr);
                session.waitForCondition(ChannelCondition.EXIT_STATUS, timeout);
                ret = session.getExitStatus();
            } else {
                logger.error("caijl:[INFO] ssh2 login failure:" + ip);
                out.setStdErr("caijl:[INFO] ssh2 login failure:" + ip);
            }

        }catch(IOException e){
        	e.printStackTrace();
        }finally {
            if (conn != null) {
                conn.close();
            }
            if (stdOut != null)
				try {
					stdOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            if (stdErr != null)
				try {
					stdErr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        }
        return out;
    }

    private String processStream(InputStream in, String charset) throws IOException {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
    	String usr = "root";
        String password = "12345";
        String serverIP = "11.22.33.xx";
        String shPath = "/root/ab.sh";
        RmtShellExecutor exe = new RmtShellExecutor(serverIP, usr, password);
        String outInf;
        outInf = exe.exec("ls -al").getOutputStr();				// 执行Linux命令
        System.out.println("outInf= " + outInf);
        outInf = exe.exec("sh " + shPath + " xn").getOutputStr();	// 执行Shell脚本
        System.out.println("outInf= " + outInf); 
    }
}