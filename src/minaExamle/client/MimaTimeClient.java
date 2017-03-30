package minaExamle.client;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

import minaExamle.common.BaseConfig;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.prefixedstring.PrefixedStringCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class MimaTimeClient {
	public static void main(String[] args) {  
        IoConnector connector = new NioSocketConnector();  
        connector.getFilterChain().addLast( "logger", new LoggingFilter() );  
        connector.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new PrefixedStringCodecFactory(Charset.forName("UTF-8"))));  
        connector.setHandler(new TimeClientHander());  
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress("127.0.0.1",BaseConfig.PORT));  
        //�ȴ���������  
        connectFuture.awaitUninterruptibly();  
        System.out.println("���ӳɹ�");  
          
        IoSession session = connectFuture.getSession();  
          
        Scanner sc = new Scanner(System.in);  
          
        boolean quit = false;  
          
        while(!quit){  
              
            String str = sc.next();  
            if(str.equalsIgnoreCase("quit")){  
                quit = true;  
            }  
            session.write(str);  
        }  
          
        //�ر�  
        if(session!=null){  
            if(session.isConnected()){  
                session.getCloseFuture().awaitUninterruptibly();  
            }  
            connector.dispose(true);  
        }  
          
          
    }  
}
