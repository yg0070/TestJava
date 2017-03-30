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
		//����21�Ŷ˿�,21�����ڿ���,20�����ڴ�����
        ServerSocket s = new ServerSocket(21);
        int counter=0;
        int i=0;
        List<FtpHandler> users=new ArrayList<FtpHandler>();
        for(;;)
        {
            //���ܿͻ�������
            Socket incoming = s.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
            PrintWriter out = new PrintWriter(incoming.getOutputStream(),true);//�ı��ı������
            out.println("220 ׼��Ϊ������"+",���ǵ�ǰ��  "+counter+" ����½��!");//������ȷ����ʾ

            //���������߳�
            FtpHandler h = new FtpHandler(incoming,i);
            h.start();
            users.add(h);   //�����û��̼߳��뵽��� ArrayList ��
            counter++;
            i++;
        }
	}*/
	 public static void main(String[] args){  
	        final String F_DIR = "D:/img";//��·�� 
	        final int PORT = 22;//�����˿ں�  
	        Logger.getRootLogger();  
	        Logger logger = Logger.getLogger("minaExamle");  
	          
	        try{  
	            ServerSocket s = new ServerSocket(PORT);  
	            logger.info("Connecting to server A...");  
	            logger.info("Connected Successful! Local Port:"+s.getLocalPort()+". Default Directory:'"+F_DIR+"'.");  
	          
	            while( true ){  
	                //���ܿͻ�������  
	                Socket client = s.accept();  
	                //���������߳�  
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
