package minaExamle.client;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

public class TimeClientHander implements IoHandler {
	@Override  
    public void exceptionCaught(IoSession arg0, Throwable arg1)  
            throws Exception {  
        // TODO Auto-generated method stub  
        arg1.printStackTrace();  
    }  
  
    @Override  
    public void messageReceived(IoSession arg0, Object message) throws Exception {  
        // TODO Auto-generated method stub  
        System.out.println("client������Ϣ:"+message.toString());  
    }  
  
    @Override  
    public void messageSent(IoSession arg0, Object message) throws Exception {  
        // TODO Auto-generated method stub  
        System.out.println("client������Ϣ"+message.toString());  
    }  
  
    @Override  
    public void sessionClosed(IoSession session) throws Exception {  
        // TODO Auto-generated method stub  
        System.out.println("client��:"+session.getRemoteAddress().toString()+"�Ͽ�����");  
    }  
  
    @Override  
    public void sessionCreated(IoSession session) throws Exception {  
        // TODO Auto-generated method stub  
        System.out.println("client��:"+session.getRemoteAddress().toString()+"��������");  
    }  
  
    @Override  
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {  
        // TODO Auto-generated method stub  
        System.out.println( "IDLE " + session.getIdleCount( status ));  
    }  
  
    @Override  
    public void sessionOpened(IoSession arg0) throws Exception {  
        // TODO Auto-generated method stub  
        System.out.println("������");  
    } 
}
