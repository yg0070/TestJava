package minaExamle.server;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class TimeServerHandler implements IoHandler{
	@Override  
    public void exceptionCaught(IoSession arg0, Throwable arg1)  
            throws Exception {  
		System.out.println("-----------------------exceptionCaught.......-----------------------");
        //arg1.printStackTrace();  
  
    }  
	
    @Override  
    public void messageReceived(IoSession session, Object message) throws Exception {  
          System.out.println("-----------------------messageReceived-----------------------");
        String str = message.toString();
        System.out.println(str);
        /*CrackData crack=new CrackData();
    	crack.str=str;
    	crack.out=session;
    	crack.begin();*/
          
        if( str.trim().equalsIgnoreCase("quit") ) {  
            session.close(true);  
            return;  
        }  
        //Date date = new Date();
        //session.write("huilaile"+date.toString() );  
        System.out.println("Message written...");  
    }  
  
    @Override  
    public void messageSent(IoSession arg0, Object arg1) throws Exception {  
        // TODO Auto-generated method stub  
    	System.out.println("--------------messageSend...----------------");
        System.out.println("发送信息:"+arg1.toString());  
    }  
  
    @Override  
    public void sessionClosed(IoSession session) throws Exception {  
        // TODO Auto-generated method stub  
    	System.out.println("--------------sessionClosed...----------------");
        System.out.println("IP:"+session.getRemoteAddress().toString()+"断开连接");  
    }  
  
    @Override  
    public void sessionCreated(IoSession session) throws Exception {  
        // TODO Auto-generated method stub  
    	System.out.println("--------------sessionCreated...----------------");
        System.out.println("IP:"+session.getRemoteAddress().toString());  
    }  
  
    @Override  
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {  
        // TODO Auto-generated method stub  
    	System.out.println("--------------sessionIdle...----------------");
        System.out.println( "IDLE " + session.getIdleCount( status ));  
  
    }  
  
    @Override  
    public void sessionOpened(IoSession arg0) throws Exception {  
        // TODO Auto-generated method stub  
    	System.out.println("--------------sessionOpened...----------------");
    } 
   /* public static byte[] hexStr2ByteArray(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len ; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		System.out.println("长度"+hex.length());
		return result;
	}*/
}
