package minaExamle.test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.ftpserver.FtpServer;

import sun.net.ftp.*;

class FtpHandler extends Thread
{
    Socket ctrlSocket;        //���ڿ��Ƶ��׽���
    Socket dataSocket;        //���ڴ�����׽���
    int id;
    String cmd = "";        //���ָ��(�ո�ǰ)
    String param = "";        //�ŵ�ǰָ��֮��Ĳ���(�ո��)
    String user;
    String remoteHost = " ";       //�ͻ�IP
    int remotePort = 0;               //�ͻ�TCP �˿ں�
    String dir = FtpServer.initDir;//��ǰĿ¼
    String rootdir = "c:/";           //Ĭ�ϸ�Ŀ¼,��checkPASS������
    int state = 0 ;                   //�û�״̬��ʶ��,��checkPASS������
    String reply;                   //���ر���
    PrintWriter ctrlOutput; 
    int type = 0;                   //�ļ�����(ascII �� bin)
    String requestfile = "";
    boolean isrest = false;
    
    //FtpHandler����
    //���췽��
    public FtpHandler(Socket s,int i)
    {
        ctrlSocket = s;
        id = i;    
    }

    //run ����
    public void run()
    {
        String str = "";
        int parseResult;                            //��cmd һһ��Ӧ�ĺ�
        
        try
        {
            BufferedReader ctrlInput = new BufferedReader
                                (new InputStreamReader(ctrlSocket.getInputStream()));
            ctrlOutput = new PrintWriter(ctrlSocket.getOutputStream(),true);
            state  = FtpState.FS_WAIT_LOGIN;          //0
            boolean finished = false;
            while(!finished)    
            {
                str = ctrlInput.readLine();            ///
                if(str == null) finished = true;    //����while
                else
                {
                    parseResult = parseInput(str);  //ָ��ת��Ϊָ���
                    System.out.println("ָ��:"+cmd+" ����:"+param);
                    System.out.print("->");
                    switch(state)                    //�û�״̬����
                    {
                        case FtpState.FS_WAIT_LOGIN:
                                finished = commandUSER();
                                break;
                        case FtpState.FS_WAIT_PASS:
                                finished = commandPASS();
                                break;
                        case FtpState.FS_LOGIN:
                        {
                            switch(parseResult)//ָ��ſ���,���������Ƿ�������еĹؼ�
                            {
                                case -1:
                                    errCMD();                    //�﷨��
                                    break;
                                case 4:
                                    finished = commandCDUP();   //����һ��Ŀ¼
                                    break;
                                case 6:
                                    finished = commandCWD();    //��ָ����Ŀ¼
                                    break;
                                case 7:
                                    finished = commandQUIT();    //�˳�
                                    break;
                                case 9:
                                    finished = commandPORT();    //�ͻ���IP:��ַ+TCP �˿ں�
                                    break;
                                case 11:
                                    finished = commandTYPE();    //�ļ���������(ascII �� bin)
                                    break;
                                case 14:
                                    finished = commandRETR();    //�ӷ������л���ļ�
                                    break;
                                case 15:
                                    finished = commandSTOR();    //��������з����ļ�
                                    break;
                                case 22:
                                    finished = commandABOR();    //�رմ���������dataSocket
                                    break;
                                case 23:
                                    finished = commandDELE();    //ɾ���������ϵ�ָ���ļ�
                                    break;
                                case 25:
                                    finished = commandMKD();    //����Ŀ¼
                                    break;
                                case 27:
                                    finished = commandLIST();    //�ļ���Ŀ¼���б�
                                    break;
                                case 26:
                                case 33:
                                    finished = commandPWD();    //"��ǰĿ¼" ��Ϣ
                                    break;
                                case 32:
                                    finished = commandNOOP();    //"������ȷ" ��Ϣ
                                    break;
                                
                            }
                        }
                            break;
                        

                    }
                } 
                ctrlOutput.println(reply);
                ctrlOutput.flush();////////////////////////////////////
                
            } 
            ctrlSocket.close();
        } 
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    //parseInput����    
    int parseInput(String s)
    {
        int p = 0;
        int i = -1;
        p = s.indexOf(" ");
        if(p == -1)                  //������޲�������(�޿ո�)
            cmd = s;
        else 
            cmd = s.substring(0,p);  //�в�������,���˲���
        
        if(p >= s.length() || p ==-1)//����޿ո�,��ո��ڶ����s������֮��
            param = "";
        else
            param = s.substring(p+1,s.length());
        cmd = cmd.toUpperCase();     //ת���� String Ϊ��д
        
          if(cmd.equals("CDUP"))
                i = 4;
        if(cmd.equals("CWD"))
                i = 6;
        if(cmd.equals("QUIT"))
                i = 7;
        if(cmd.equals("PORT"))
                i = 9;
        if(cmd.equals("TYPE"))
                i = 11;
        if(cmd.equals("RETR"))
                i = 14;
        if(cmd.equals("STOR"))
                i = 15;
        if(cmd.equals("ABOR"))
                i = 22;
        if(cmd.equals("DELE"))
                i = 23;
        if(cmd.equals("MKD"))
                i = 25;
        if(cmd.equals("PWD"))
                i = 26;
        if(cmd.equals("LIST"))
                i = 27;
          if(cmd.equals("NOOP"))
                i = 32;
        if(cmd.equals("XPWD"))
                i = 33;
     return i;
    }
    
    //validatePath����
    //�ж�·��������,���� int 
    int validatePath(String s)
    {
        File f = new File(s);        //���·��
        if(f.exists() && !f.isDirectory())
        {
            String s1 = s.toLowerCase();
            String s2 = rootdir.toLowerCase();
            if(s1.startsWith(s2))    
                return 1;            //�ļ������Ҳ���·��,����rootdir ��ʼ
            else
                return 0;            //�ļ������Ҳ���·��,����rootdir ��ʼ
        }
        f = new File(addTail(dir)+s);//����·��
        if(f.exists() && !f.isDirectory())
        {
            String s1 = (addTail(dir)+s).toLowerCase();
            String s2 = rootdir.toLowerCase();
            if(s1.startsWith(s2))
                return 2;            //�ļ������Ҳ���·��,����rootdir ��ʼ
            else 
                return 0;            //�ļ������Ҳ���·��,����rootdir ��ʼ
        }
        return 0;                    //�������
    }
    
    boolean checkPASS(String s) //��������Ƿ���ȷ,���ļ�����
    {
        for(int i = 0; i<FtpServer.usersInfo.size();i++)
        {
            if(((UserInfo)FtpServer.usersInfo.get(i)).user.equals(user) && 
                ((UserInfo)FtpServer.usersInfo.get(i)).password.equals(s))
            {
                rootdir = ((UserInfo)FtpServer.usersInfo.get(i)).workDir;
                dir = ((UserInfo)FtpServer.usersInfo.get(i)).workDir;
                return true;
            }
        }
        return false;
    }

    //commandUSER����
    //�û����Ƿ���ȷ
    boolean commandUSER()
    {
        if(cmd.equals("USER"))
        {
            reply = "331 �û�����ȷ,��Ҫ����";
            user = param;
              state = FtpState.FS_WAIT_PASS;
            return false;
        }
        else
        {
            reply = "501 �����﷨����,�û�����ƥ��";
            return true;
        }

    }

    //commandPASS ����
    //�����Ƿ���ȷ
    boolean commandPASS()
    {
        if(cmd.equals("PASS"))
        {
            if(checkPASS(param))
            {
                reply = "230 �û���¼��";
                state = FtpState.FS_LOGIN;
                System.out.println("����Ϣ: �û�: "+user+" ������: "+ remoteHost +"��¼��");
                System.out.print("->");
                return false;
            }
            else
            {
                reply = "530 û�е�¼";
                return true;
            }
        }
        else
        {
            reply = "501 �����﷨����,���벻ƥ��";
            return true;
        }

    }

    void errCMD()
    {
        reply = "500 �﷨����";
    }    
    
    boolean commandCDUP()//����һ��Ŀ¼
    {                     
        dir = FtpServer.initDir;    
        File f = new File(dir);
        if(f.getParent()!=null &&(!dir.equals(rootdir)))//�и�·�� && ���Ǹ�·��
        {
            dir = f.getParent();
            reply = "200 ������ȷ";
        }
        else
        {
            reply = "550 ��ǰĿ¼�޸�·��";
        }
        
        return false;
    }// commandCDUP() end

    boolean commandCWD()// CWD (CHANGE WORKING DIRECTORY)
    {                    //������ı乤��Ŀ¼���û�ָ����Ŀ¼
        File f = new File(param);
        String s = "";
        String s1 = "";
        if(dir.endsWith("/"))
            s = dir;
        else
            s = dir + "/";
        File f1 = new File(s+param);
        
        if(f.isDirectory() && f.exists())
        {
            if(param.equals("..") || param.equals("..\\"))
            {
                if(dir.compareToIgnoreCase(rootdir)==0)
                {
                    reply = "550 ��·��������";
                    //return false;
                }
                else
                {
                    s1 = new File(dir).getParent();
                    if(s1!=null)
                    {
                        dir = s1;
                        reply = "250 ������ļ��������, ��ǰĿ¼��Ϊ: "+dir;
                    }
                    else
                        reply = "550 ��·��������";
                }
            }
            else if(param.equals(".") || param.equals(".\\"))
            {}
            else 
            {
                dir = param;
                reply = "250 ������ļ��������, ����·����Ϊ "+dir;
            }        
        }
        else if(f1.isDirectory() && f1.exists())
        {
            dir = s+param;
            reply = "250 ������ļ��������, ����·����Ϊ "+dir;
        }
        else
            reply = "501 �����﷨����";
        
        return false;
    } // commandCDW() end

    boolean commandQUIT()
    {
        reply = "221 ����ر�����";
        return true;
    }// commandQuit() end
    
/*ʹ�ø�����ʱ���ͻ��˱��뷢�Ϳͻ������ڽ������ݵ�32λIP ��ַ��16λ ��TCP �˿ںš�
 *��Щ��Ϣ��8λΪһ�飬ʹ��ʮ���ƴ��䣬�м��ö��Ÿ�����
 */
    boolean commandPORT()
    {
        int p1 = 0;
        int p2 = 0;
        int[] a = new int[6];//���ip+tcp
        int i = 0;             //
        try
        {
            while((p2 = param.indexOf(",",p1))!=-1)//ǰ5λ
            {
                 a[i] = Integer.parseInt(param.substring(p1,p2));
                 p2 = p2+1;
                 p1 = p2;
                 i++;
            }
            a[i] = Integer.parseInt(param.substring(p1,param.length()));//���һλ
        }
        catch(NumberFormatException e)
        {
            reply = "501 �����﷨����";
            return false;
        }
        
        remoteHost = a[0]+"."+a[1]+"."+a[2]+"."+a[3];
        remotePort = a[4] * 256+a[5];
        reply = "200 ������ȷ";
        return false;
    }//commandPort() end
        
    /*LIST ����������ͻ��˷��ط������й���Ŀ¼�µ�Ŀ¼�ṹ�������ļ���Ŀ¼���б�
     *�����������ʱ���ȴ���һ����ʱ���׽�����ͻ��˷���Ŀ¼��Ϣ������׽��ֵ�Ŀ��
     *�˿ں�ȱʡΪ��Ȼ��Ϊ��ǰ����Ŀ¼����File �������øö����list()�����õ�һ
     *��������Ŀ¼�������ļ�����Ŀ¼���Ƶ��ַ������飬Ȼ������������Ƿ����ļ���
     *�����е�"."������Ŀ¼���ļ�����󣬽��õ�����������ͨ����ʱ�׽��ַ��͵��ͻ��ˡ�
     **/
    boolean commandLIST()//�ļ���Ŀ¼���б�
    {
        try
        {
            dataSocket = new Socket(remoteHost,remotePort,InetAddress.getLocalHost(),20);
            PrintWriter dout = new PrintWriter(dataSocket.getOutputStream(),true);
            if(param.equals("") || param.equals("LIST"))
            {
                ctrlOutput.println("150 �ļ�״̬����,ls�� ASCII ��ʽ����");
                File f = new File(dir);
                String[] dirStructure = f.list();//ָ��·���е��ļ�������,��������ǰ·����·��
                String fileType;
                for(int i =0; i<dirStructure.length;i++)
                {
                    if(dirStructure[i].indexOf(".")!=-1)
                    {
                        fileType = "- ";        //��Ŀ¼(��linux��)
                    }
                    else
                    {
                        fileType = "d ";        //��Ŀ¼���ļ�����Ŀ¼
                    }
                    dout.println(dirStructure[i]);//(fileType+dirStructure[i]);
                }
            } 
            dout.close();
            dataSocket.close();
            reply = "226 �����������ӽ���";
        }
        catch(Exception e)
        {
            e.printStackTrace();
            reply = "451 Requested action aborted: local error in processing";
            return false;
        }
        
        return false;
    }// commandLIST() end

    boolean commandTYPE()    //TYPE �������������������
    {
        if(param.equals("A"))
        {
            type = FtpState.FTYPE_ASCII;//0
            reply = "200 ������ȷ ,ת ASCII ģʽ";
        }
        else if(param.equals("I"))
        {
            type = FtpState.FTYPE_IMAGE;//1
            reply = "200 ������ȷ ת BINARY ģʽ";
        }
        else
            reply = "504 �����ִ�����ֲ���";
            
        return false;
    }
    
    //connamdRETR ����
    //�ӷ������л���ļ�
    boolean commandRETR()
    {
        requestfile = param;
        File f =  new File(requestfile);
          if(!f.exists())
        {
              f = new File(addTail(dir)+param);
            if(!f.exists())
            {
                   reply = "550 �ļ�������";
                   return  false;
            }
            requestfile = addTail(dir)+param;
        }
  
          if(isrest)
        {
     
        }
        else
        {
             if(type==FtpState.FTYPE_IMAGE)                //bin
            {
                try
                {
                    ctrlOutput.println("150 �ļ�״̬����,�Զ����η�ʽ���ļ�:  "+ requestfile);
                    dataSocket = new Socket(remoteHost,remotePort,InetAddress.getLocalHost(),20);
                    BufferedInputStream  fin = new BufferedInputStream(new FileInputStream(requestfile));
                      PrintStream dataOutput = new PrintStream(dataSocket.getOutputStream(),true);
                    byte[] buf = new byte[1024];         //Ŀ�껺����
                    int l = 0;
                    while((l=fin.read(buf,0,1024))!=-1)    //������δ����
                    {
                          dataOutput.write(buf,0,l);        //д���׽���
                    }
                     fin.close();
                     dataOutput.close();
                     dataSocket.close();
                     reply ="226 �����������ӽ���";

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    reply = "451 ����ʧ��: ���������";
                    return false;
                }

            }
            if(type==FtpState.FTYPE_ASCII)//ascII
            {
                  try
                {
                    ctrlOutput.println("150 Opening ASCII mode data connection for "+ requestfile);
                    dataSocket = new Socket(remoteHost,remotePort,InetAddress.getLocalHost(),20);
                    BufferedReader  fin = new BufferedReader(new FileReader(requestfile));
                      PrintWriter dataOutput = new PrintWriter(dataSocket.getOutputStream(),true);
                    String s;
                    while((s=fin.readLine())!=null)
                    {
                           dataOutput.println(s);    ///???
                    }
                     fin.close();
                     dataOutput.close();
                     dataSocket.close();
                     reply ="226 �����������ӽ���";
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                    reply = "451 ����ʧ��: ���������";
                    return false;
                }
            }
        }
          return false;

    }
    
    //commandSTOR ����
    //��������з����ļ�STOR
    boolean commandSTOR()
    {
        if(param.equals(""))
        {
            reply = "501 �����﷨����";
            return false;
        }
        requestfile = addTail(dir)+param;
        if(type == FtpState.FTYPE_IMAGE)//bin
        {
            try
            {
                ctrlOutput.println("150 Opening Binary mode data connection for "+ requestfile);
                dataSocket = new Socket(remoteHost,remotePort,InetAddress.getLocalHost(),20);
                BufferedOutputStream fout = new BufferedOutputStream(new FileOutputStream(requestfile));
                BufferedInputStream dataInput = new BufferedInputStream(dataSocket.getInputStream());
                byte[] buf = new byte[1024];
                int l = 0;
                while((l = dataInput.read(buf,0,1024))!=-1)
                {
                    fout.write(buf,0,l);
                }
                dataInput.close();
                fout.close();
                dataSocket.close();
                reply = "226 �����������ӽ���";
            }
            catch(Exception e)
            {
                e.printStackTrace();
                reply = "451 ����ʧ��: ���������";
                return false;
            }
        }
        if(type == FtpState.FTYPE_ASCII)//ascII
        {
            try
            {
                ctrlOutput.println("150 Opening ASCII mode data connection for "+ requestfile);
                dataSocket = new Socket(remoteHost,remotePort,InetAddress.getLocalHost(),20);
                PrintWriter fout = new PrintWriter(new FileOutputStream(requestfile));
                BufferedReader dataInput = new BufferedReader(new InputStreamReader(dataSocket.getInputStream()));
                String line;
                while((line = dataInput.readLine())!=null)
                {
                    fout.println(line);                    
                }
                dataInput.close();
                fout.close();
                dataSocket.close();
                reply = "226 �����������ӽ���";
            }
            catch(Exception e)
            {
                e.printStackTrace();
                reply = "451 ����ʧ��: ���������";
                return false;
            }
        }
        return false;
    }
    
    boolean commandPWD()
    {
        reply = "257 " + dir + " �ǵ�ǰĿ¼.";
        return false;
    }
    
    boolean commandNOOP()
    {
        reply = "200 ������ȷ.";
        return false;
    }
    
    //ǿ��dataSocket ��
    boolean commandABOR()
    {
        try
        {
            dataSocket.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            reply = "451 ����ʧ��: ���������";
            return false; 
        }
        reply = "421 ���񲻿���, �ر����ݴ�������";
        return false;
    }
    
    //ɾ���������ϵ�ָ���ļ�
    boolean commandDELE()
    {
        int i = validatePath(param);
        if(i == 0)
        {
            reply = "550 ����Ķ���δִ��,�ļ�������,��Ŀ¼����,������";
            return false;
        }
        if(i == 1)
        {
            File f = new File(param);
            f.delete();
        }
        if(i == 2)
        {
            File f= new File(addTail(dir)+param);
            f.delete();
        } 
        
        reply = "250 ������ļ��������,�ɹ�ɾ�����������ļ�";
        return false;

    }

    //����Ŀ¼,Ҫ����·��
    boolean commandMKD()
    {
        String s1 = param.toLowerCase();
        String s2 = rootdir.toLowerCase();
        if(s1.startsWith(s2))
        {
            File f = new File(param);
            if(f.exists())
            {
                reply = "550 ����Ķ���δִ��,Ŀ¼�Ѵ���";
                return false;
            }
            else 
            {
                f.mkdirs();
                reply = "250 ������ļ��������, Ŀ¼����";
            }
        }
        else 
        {
            File f = new File(addTail(dir)+param);
            if(f.exists())
            {
                reply = "550 ����Ķ���δִ��,Ŀ¼�Ѵ���";
                return false;
            }
            else 
            {
                f.mkdirs();
                reply = "250 ������ļ��������, Ŀ¼����";
            }
        }
        
        return false;
    }

    String addTail(String s)
    {
        if(!s.endsWith("/"))
            s = s + "/";
        return s;
    }
        
}
