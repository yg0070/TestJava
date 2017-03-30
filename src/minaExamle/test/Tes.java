package minaExamle.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;



public class Tes {
	/*public static void main(String[] args) throws IOException {
		//监听21号端口,21口用于控制,20口用于传数据
        ServerSocket s = new ServerSocket(21);
        int counter=0;
        int i=0;
        List<FtpHandler> users=new ArrayList<FtpHandler>();
        for(;;)
        {
            //接受客户端请求
            Socket incoming = s.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
            PrintWriter out = new PrintWriter(incoming.getOutputStream(),true);//文本文本输出流
            out.println("220 准备为您服务"+",你是当前第  "+counter+" 个登陆者!");//命令正确的提示

            //创建服务线程
            FtpHandler h = new FtpHandler(incoming,i);
            h.start();
            users.add(h);   //将此用户线程加入到这个 ArrayList 中
            counter++;
            i++;
        }
	}*/
	 public static void main(String[] args){  
	        final String F_DIR = "D:/img";//根路径 
	        final int PORT = 22;//监听端口号  
	        Logger.getRootLogger();  
	        Logger logger = Logger.getLogger("minaExamle");  
	          
	        try{  
	            ServerSocket s = new ServerSocket(PORT);  
	            logger.info("Connecting to server A...");  
	            logger.info("Connected Successful! Local Port:"+s.getLocalPort()+". Default Directory:'"+F_DIR+"'.");  
	          
	            while( true ){  
	                //接受客户端请求  
	                Socket client = s.accept();  
	                //创建服务线程  
	                new ClientThread(client, F_DIR).start();  
	            }  
	        } catch(Exception e) {  
	            logger.error(e.getMessage());  
	            for(StackTraceElement ste : e.getStackTrace()){  
	                logger.error(ste.toString());  
	            }  
	        }  
	    }  
}
