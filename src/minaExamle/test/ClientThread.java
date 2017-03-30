package minaExamle.test;

import java.io.BufferedReader;    
import java.io.File;  
import java.io.FileNotFoundException;  
import java.io.IOException;  
import java.io.InputStream;  
import java.io.InputStreamReader;  
import java.io.OutputStream;  
import java.io.PrintWriter;  
import java.io.RandomAccessFile;  
import java.net.ConnectException;  
import java.net.InetAddress;  
import java.net.ServerSocket;  
import java.net.Socket;  
import java.net.UnknownHostException;  
import java.nio.charset.Charset;  
import java.util.Random;  
  
import org.apache.log4j.Logger;  
public class ClientThread extends Thread {  
    
    private Socket socketClient;//�ͻ���socket  
    private Logger logger;//��־����  
    private String dir;//����·��  
    private String pdir = "/";//���·��  
    private final static Random generator = new Random();//�����  
      
    public ClientThread(Socket client, String F_DIR){  
        this.socketClient = client;  
        this.dir = F_DIR;  
    }  
  
    @Override  
    public void run() {  
        Logger.getRootLogger();  
        logger = Logger.getLogger("com");  
          
        InputStream is = null;  
        OutputStream os = null;  
        try {  
            is = socketClient.getInputStream();  
            os = socketClient.getOutputStream();  
        } catch (IOException e) {  
            logger.error(e.getMessage());  
            for(StackTraceElement ste : e.getStackTrace()){  
                logger.error(ste.toString());  
            }  
        }  
        BufferedReader br = new BufferedReader(new InputStreamReader(is,  
                Charset.forName("UTF-8")));  
        System.out.println(br);
        PrintWriter pw = new PrintWriter(os);  
        String clientIp = socketClient.getInetAddress().toString().substring(1);//��¼�ͻ���IP  
          
        String username = "not logged in";//�û���  
        String password = "";//����  
        String command = "";//����  
        boolean loginStuts = false;//��¼״̬  
        final String LOGIN_WARNING = "530 Please log in with USER and PASS first.";  
        String str = "";//���������ַ���  
        int port_high = 0;  
        int port_low = 0;  
        String retr_ip = "";//�����ļ���IP��ַ  
          
        Socket tempsocket = null;  
          
        //��ӡ��ӭ��Ϣ  
        pw.println("220-FTP Server A version 1.0 written by Leon Guo");  
        pw.flush();  
        logger.info("("+username+") ("+clientIp+")> Connected, sending welcome message...");  
        logger.info("("+username+") ("+clientIp+")> 220-FTP Server A version 1.0 written by Leon Guo");  
          
        boolean b = true;  
        while ( b ){  
            try { 
                //��ȡ�û����������  
                command = br.readLine();  
                if(null == command) break;  
            } catch (IOException e) {  
                pw.println("331 Failed to get command");  
                pw.flush();  
                logger.info("("+username+") ("+clientIp+")> 331 Failed to get command");  
                logger.error(e.getMessage());  
                for(StackTraceElement ste : e.getStackTrace()){  
                    logger.error(ste.toString());  
                }  
                b = false;  
            }  
              
            /* 
             * ���ʿ������� 
             */  
              
            // USER����  
            if(command.toUpperCase().startsWith("USER")){  
                logger.info("(not logged in) ("+clientIp+")> "+command);  
                username = command.substring(4).trim();   
                if("".equals(username)){  
                    pw.println("501 Syntax error");  
                    pw.flush();  
                    logger.info("(not logged in) ("+clientIp+")> 501 Syntax error");  
                    username = "not logged in";  
                }  
                else{  
                    pw.println("331 Password required for " + username);  
                    pw.flush();  
                    logger.info("(not logged in) ("+clientIp+")> 331 Password required for " + username);  
                }  
                loginStuts = false;  
            } //end USER  
              
            // PASS����  
            else if(command.toUpperCase().startsWith("PASS")){  
                logger.info("(not logged in) ("+clientIp+")> "+command);  
                password = command.substring(4).trim();   
                if(username.equals("root") && password.equals("root")){  
                    pw.println("230 Logged on");  
                    pw.flush();  
                    logger.info("("+username+") ("+clientIp+")> 230 Logged on");  
//                  logger.info("�ͻ��� "+clientIp+" ͨ�� "+username+"�û���¼");  
                    loginStuts = true;  
                }  
                else{  
                    pw.println("530 Login or password incorrect!");  
                    pw.flush();  
                    logger.info("(not logged in) ("+clientIp+")> 530 Login or password incorrect!");  
                    username = "not logged in";  
                }  
            } //end PASS  
              
            // PWD����  
            else if(command.toUpperCase().startsWith("PWD")){  
                logger.info("("+username+") ("+clientIp+")> "+command);  
                if(loginStuts){  
//                  logger.info("�û�"+clientIp+"��"+username+"ִ��PWD����");  
                    pw.println("257 '"+pdir+"' is current directory");  
                    pw.flush();  
                    logger.info("("+username+") ("+clientIp+")> 257 '"+pdir+"' is current directory");  
                }  
                else{  
                    pw.println(LOGIN_WARNING);  
                    pw.flush();  
                    logger.info("("+username+") ("+clientIp+")> "+LOGIN_WARNING);  
                }  
            } //end PWD  
              
            // CWD����  
            else if(command.toUpperCase().startsWith("CWD")){  
                logger.info("("+username+") ("+clientIp+")> "+command);  
                if(loginStuts){  
                    str = command.substring(3).trim();  
                    if("".equals(str)){  
                        pw.println("250 Broken client detected, missing argument to CWD. '"+pdir+"' is current directory.");  
                        pw.flush();  
                        logger.info("("+username+") ("+clientIp+")> 250 Broken client detected, missing argument to CWD. '"+pdir+"' is current directory.");  
                    }  
                    else{  
                        //�ж�Ŀ¼�Ƿ����  
                        String tmpDir = dir + "/" + str;  
                        File file = new File(tmpDir);  
                        if(file.exists()){//Ŀ¼����  
                            dir = dir + "/" + str;  
                            if("/".equals(pdir)){  
                                pdir = pdir + str;  
                            }  
                            else{  
                                pdir = pdir + "/" + str;  
                            }  
//                          logger.info("�û�"+clientIp+"��"+username+"ִ��CWD����");  
                            pw.println("250 CWD successful. '"+pdir+"' is current directory");  
                            pw.flush();  
                            logger.info("("+username+") ("+clientIp+")> 250 CWD successful. '"+pdir+"' is current directory");  
                        }  
                        else{//Ŀ¼������  
                            pw.println("550 CWD failed. '"+pdir+"': directory not found.");  
                            pw.flush();  
                            logger.info("("+username+") ("+clientIp+")> 550 CWD failed. '"+pdir+"': directory not found.");  
                        }  
                    }  
                }  
                else{  
                    pw.println(LOGIN_WARNING);  
                    pw.flush();  
                    logger.info("("+username+") ("+clientIp+")> "+LOGIN_WARNING);  
                }  
            } //end CWD  
              
            // QUIT����  
            else if(command.toUpperCase().startsWith("QUIT")){  
                logger.info("("+username+") ("+clientIp+")> "+command);  
                b = false;  
                pw.println("221 Goodbye");  
                pw.flush();  
                logger.info("("+username+") ("+clientIp+")> 221 Goodbye");  
                try {  
                    Thread.currentThread();  
                    Thread.sleep(1000);  
                } catch (InterruptedException e) {  
                    logger.error(e.getMessage());  
                    for(StackTraceElement ste : e.getStackTrace()){  
                        logger.error(ste.toString());  
                    }  
                }  
            } //end QUIT  
              
            /* 
             * ����������� 
             */  
              
            //PORT�������ģʽ��������  
            else if(command.toUpperCase().startsWith("PORT")){  
                logger.info("("+username+") ("+clientIp+")> "+command);  
                if(loginStuts){  
                    try {  
                        str = command.substring(4).trim();  
                        port_low = Integer.parseInt(str.substring(str.lastIndexOf(",")+1));  
                        port_high = Integer.parseInt(str.substring(0, str.lastIndexOf(","))  
                                .substring(str.substring(0, str.lastIndexOf(",")).lastIndexOf(",")+1));  
                        String str1 = str.substring(0, str.substring(0, str.lastIndexOf(",")).lastIndexOf(","));  
                        retr_ip = str1.replace(",", ".");  
                        try {  
                            //ʵ��������ģʽ�µ�socket  
                            tempsocket = new Socket(retr_ip,port_high * 256 + port_low);  
//                          logger.info("�û�"+clientIp+"��"+username+"ִ��PORT����");  
                            pw.println("200 port command successful");  
                            pw.flush();  
                            logger.info("("+username+") ("+clientIp+")> 200 port command successful");  
                        } catch (ConnectException ce) {  
                            pw.println("425 Can't open data connection.");  
                            pw.flush();  
                            logger.info("("+username+") ("+clientIp+")> 425 Can't open data connection.");  
                            logger.error(ce.getMessage());  
                            for(StackTraceElement ste : ce.getStackTrace()){  
                                logger.error(ste.toString());  
                            }  
                        } catch (UnknownHostException e) {  
                            logger.error(e.getMessage());  
                            for(StackTraceElement ste : e.getStackTrace()){  
                                logger.error(ste.toString());  
                            }  
                        } catch (IOException e) {  
                            logger.error(e.getMessage());  
                            for(StackTraceElement ste : e.getStackTrace()){  
                                logger.error(ste.toString());  
                            }  
                        }  
                    } catch (NumberFormatException e) {  
                        pw.println("503 Bad sequence of commands.");  
                        pw.flush();  
                        logger.info("("+username+") ("+clientIp+")> 503 Bad sequence of commands.");  
                        logger.error(e.getMessage());  
                        for(StackTraceElement ste : e.getStackTrace()){  
                            logger.error(ste.toString());  
                        }  
                    }  
                }  
                else{  
                    pw.println(LOGIN_WARNING);  
                    pw.flush();  
                    logger.info("("+username+") ("+clientIp+")> "+LOGIN_WARNING);  
                }  
            } //end PORT  
              
            //PASV�������ģʽ��������  
            else if(command.toUpperCase().startsWith("PASV")) {   
                logger.info("("+username+") ("+clientIp+")> "+command);  
                if(loginStuts){  
                    ServerSocket ss = null;  
                    while( true ){  
                        //��ȡ���������ж˿�  
                        port_high = 1 + generator.nextInt(20);  
                        port_low = 100 + generator.nextInt(1000);  
                        try {  
                            //�������󶨶˿�  
                            ss = new ServerSocket(port_high * 256 + port_low);  
                            break;  
                        } catch (IOException e) {  
                            continue;  
                        }  
                    }  
//                  logger.info("�û�"+clientIp+"��"+username+"ִ��PASV����");  
                    InetAddress i = null;  
                    try {  
                        i = InetAddress.getLocalHost();  
                    } catch (UnknownHostException e1) {  
                        e1.printStackTrace();  
                    }  
                    pw.println("227 Entering Passive Mode ("+i.getHostAddress().replace(".", ",")+","+port_high+","+port_low+")");  
                    pw.flush();  
                    logger.info("("+username+") ("+clientIp+")> 227 Entering Passive Mode ("+i.getHostAddress().replace(".", ",")+","+port_high+","+port_low+")");  
                    try {  
                        //����ģʽ�µ�socket  
                        tempsocket = ss.accept();  
                        ss.close();  
                    } catch (IOException e) {  
                        logger.error(e.getMessage());  
                        for(StackTraceElement ste : e.getStackTrace()){  
                            logger.error(ste.toString());  
                        }  
                    }  
                }  
                else{  
                    pw.println(LOGIN_WARNING);  
                    pw.flush();  
                    logger.info("("+username+") ("+clientIp+")> "+LOGIN_WARNING);  
                }  
            } //end PASV  
              
            //RETR����  
            else if(command.toUpperCase().startsWith("RETR")){  
                logger.info("("+username+") ("+clientIp+")> "+command);  
                if(loginStuts){  
                    str = command.substring(4).trim();  
                    if("".equals(str)){  
                        pw.println("501 Syntax error");  
                        pw.flush();  
                        logger.info("("+username+") ("+clientIp+")> 501 Syntax error");  
                    }  
                    else {  
                        try {  
                            pw.println("150 Opening data channel for file transfer.");  
                            pw.flush();  
                            logger.info("("+username+") ("+clientIp+")> 150 Opening data channel for file transfer.");  
                            RandomAccessFile outfile = null;  
                            OutputStream outsocket = null;  
                            try {  
                                //�������ж�ȡ��������д�루��ѡ������������ļ��������ļ�����ָ������  
                                outfile = new RandomAccessFile(dir+"/"+str,"r");  
                                outsocket = tempsocket.getOutputStream();   
                            } catch (FileNotFoundException e) {   
                                logger.error(e.getMessage());  
                                for(StackTraceElement ste : e.getStackTrace()){  
                                    logger.error(ste.toString());  
                                }  
                            } catch (IOException e) {  
                                logger.error(e.getMessage());  
                                for(StackTraceElement ste : e.getStackTrace()){  
                                    logger.error(ste.toString());  
                                }  
                            }   
                            byte bytebuffer[]= new byte[1024];   
                            int length;   
                            try{   
                                while((length = outfile.read(bytebuffer)) != -1){   
                                    outsocket.write(bytebuffer, 0, length);   
                                }   
                                outsocket.close();   
                                outfile.close();   
                                tempsocket.close();   
                            }   
                            catch(IOException e){  
                                logger.error(e.getMessage());  
                                for(StackTraceElement ste : e.getStackTrace()){  
                                    logger.error(ste.toString());  
                                }  
                            }  
                              
//                          logger.info("�û�"+clientIp+"��"+username+"ִ��RETR����");  
                            pw.println("226 Transfer OK");  
                            pw.flush();  
                            logger.info("("+username+") ("+clientIp+")> 226 Transfer OK");  
                        } catch (Exception e){  
                            pw.println("503 Bad sequence of commands.");  
                            pw.flush();  
                            logger.info("("+username+") ("+clientIp+")> 503 Bad sequence of commands.");  
                            logger.error(e.getMessage());  
                            for(StackTraceElement ste : e.getStackTrace()){  
                                logger.error(ste.toString());  
                            }  
                        }  
                    }  
                }  
                else{  
                    pw.println(LOGIN_WARNING);  
                    pw.flush();  
                    logger.info("("+username+") ("+clientIp+")> "+LOGIN_WARNING);  
                }  
            }//end RETR  
              
            //STOR����  
            else if(command.toUpperCase().startsWith("STOR")){   
                logger.info("("+username+") ("+clientIp+")> "+command);  
                if(loginStuts){  
                    str = command.substring(4).trim();  
                    if("".equals(str)){  
                        pw.println("501 Syntax error");  
                        pw.flush();  
                        logger.info("("+username+") ("+clientIp+")> 501 Syntax error");  
                    }  
                    else {  
                        try {  
                            pw.println("150 Opening data channel for file transfer.");   
                            pw.flush();  
                            logger.info("("+username+") ("+clientIp+")> 150 Opening data channel for file transfer.");  
                            RandomAccessFile infile = null;  
                            InputStream insocket = null;  
                            try {  
                                infile = new RandomAccessFile(dir+"/"+str,"rw");  
                                insocket = tempsocket.getInputStream();   
                            } catch (FileNotFoundException e) {   
                                logger.error(e.getMessage());   
                                for(StackTraceElement ste : e.getStackTrace()){  
                                    logger.error(ste.toString());  
                                }  
                            } catch (IOException e) {  
                                logger.error(e.getMessage());   
                                for(StackTraceElement ste : e.getStackTrace()){  
                                    logger.error(ste.toString());  
                                }  
                            }   
                            byte bytebuffer[] = new byte[1024];   
                            int length;   
                            try{  
                                while((length =insocket.read(bytebuffer) )!= -1){   
                                    infile.write(bytebuffer, 0, length);   
                                }  
                                insocket.close();   
                                infile.close();   
                                tempsocket.close();   
                            }   
                            catch(IOException e){  
                                logger.error(e.getMessage());  
                                for(StackTraceElement ste : e.getStackTrace()){  
                                    logger.error(ste.toString());  
                                }  
                            }  
                              
//                          logger.info("�û�"+clientIp+"��"+username+"ִ��STOR����");  
                            pw.println("226 Transfer OK");  
                            pw.flush();  
                            logger.info("("+username+") ("+clientIp+")> 226 Transfer OK");  
                        } catch (Exception e){  
                            pw.println("503 Bad sequence of commands.");  
                            pw.flush();  
                            logger.info("("+username+") ("+clientIp+")> 503 Bad sequence of commands.");  
                            logger.error(e.getMessage());  
                            for(StackTraceElement ste : e.getStackTrace()){  
                                logger.error(ste.toString());  
                            }  
                        }  
                    }  
                } else {  
                    pw.println(LOGIN_WARNING);  
                    pw.flush();  
                    logger.info("("+username+") ("+clientIp+")> "+LOGIN_WARNING);  
                }  
            } //end STOR  
              
            //NLST����  
            else if(command.toUpperCase().startsWith("NLST")) {   
                logger.info("("+username+") ("+clientIp+")> "+command);  
                if(loginStuts){  
                    try {  
                        pw.println("150 Opening data channel for directory list.");   
                        pw.flush();  
                        logger.info("("+username+") ("+clientIp+")> 150 Opening data channel for directory list.");  
                        PrintWriter pwr = null;  
                        try {  
                            pwr= new PrintWriter(tempsocket.getOutputStream(),true);  
                        } catch (IOException e1) {  
                            e1.printStackTrace();  
                        }   
                        File file = new File(dir);   
                        String[] dirstructure = new String[10];   
                        dirstructure= file.list();   
                        for(int i=0;i<dirstructure.length;i++){  
                            pwr.println(dirstructure[i]);   
                        }  
                        try {  
                            tempsocket.close();  
                            pwr.close();  
                        } catch (IOException e) {  
                            logger.error(e.getMessage());  
                            for(StackTraceElement ste : e.getStackTrace()){  
                                logger.error(ste.toString());  
                            }  
                        }   
//                      logger.info("�û�"+clientIp+"��"+username+"ִ��NLST����");  
                        pw.println("226 Transfer OK");   
                        pw.flush();  
                        logger.info("("+username+") ("+clientIp+")> 226 Transfer OK");  
                    } catch (Exception e){  
                        pw.println("503 Bad sequence of commands.");  
                        pw.flush();  
                        logger.info("("+username+") ("+clientIp+")> 503 Bad sequence of commands.");  
                        logger.error(e.getMessage());  
                        for(StackTraceElement ste : e.getStackTrace()){  
                            logger.error(ste.toString());  
                        }  
                    }  
                }else{  
                    pw.println(LOGIN_WARNING);  
                    pw.flush();  
                    logger.info("("+username+") ("+clientIp+")> "+LOGIN_WARNING);  
                }  
            } //end NLST  
              
            //LIST����  
            else if(command.toUpperCase().startsWith("LIST")) {   
                logger.info("("+username+") ("+clientIp+")> "+command);  
                if(loginStuts){  
                    try{  
                        pw.println("150 Opening data channel for directory list.");   
                        pw.flush();  
                        logger.info("("+username+") ("+clientIp+")> 150 Opening data channel for directory list.");  
                        PrintWriter pwr = null;  
                        try {  
                            pwr= new PrintWriter(tempsocket.getOutputStream(),true);  
                        } catch (IOException e) {  
                            logger.error(e.getMessage());  
                            for(StackTraceElement ste : e.getStackTrace()){  
                                logger.error(ste.toString());  
                            }  
                        }   
                        FtpUtil.getDetailList(pwr, dir);  
                        try {  
                            tempsocket.close();  
                            pwr.close();  
                        } catch (IOException e) {  
                            logger.error(e.getMessage());  
                            for(StackTraceElement ste : e.getStackTrace()){  
                                logger.error(ste.toString());  
                            }  
                        }   
//                      logger.info("�û�"+clientIp+"��"+username+"ִ��LIST����");  
                        pw.println("226 Transfer OK");   
                        pw.flush();  
                        logger.info("("+username+") ("+clientIp+")> 226 Transfer OK");  
                    } catch (Exception e){  
                        pw.println("503 Bad sequence of commands.");  
                        pw.flush();  
                        logger.info("("+username+") ("+clientIp+")> 503 Bad sequence of commands.");  
                        logger.error(e.getMessage());  
                        for(StackTraceElement ste : e.getStackTrace()){  
                            logger.error(ste.toString());  
                        }  
                    }  
                } else {  
                    pw.println(LOGIN_WARNING);  
                    pw.flush();  
                    logger.info("("+username+") ("+clientIp+")> "+LOGIN_WARNING);  
                }  
            } //end LIST  
              
            // ����Ƿ�����  
            else{  
                logger.info("("+username+") ("+clientIp+")> "+command);  
                pw.println("500 Syntax error, command unrecognized.");  
                pw.flush();  
                logger.info("("+username+") ("+clientIp+")> 500 Syntax error, command unrecognized.");  
            }  
              
        } //end while  
          
        try {  
            logger.info("("+username+") ("+clientIp+")> disconnected.");  
//          logger.info("�û�"+clientIp+"��"+username+"�˳�");  
            br.close();  
            socketClient.close();  
            pw.close();  
            if(null != tempsocket){  
                tempsocket.close();  
            }  
        } catch (IOException e) {  
            logger.error(e.getMessage());  
            for(StackTraceElement ste : e.getStackTrace()){  
                logger.error(ste.toString());  
            }  
        }  
    }  
  
} 
