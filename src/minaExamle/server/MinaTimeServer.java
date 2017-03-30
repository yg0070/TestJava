package minaExamle.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import minaExamle.common.BaseConfig;
import minaExamle.filter.ByteArrayCodecFactory;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

public class MinaTimeServer {
	private static final int PORT= BaseConfig.PORT;  
    
    public void stateServer() throws IOException {  
    	System.out.println("-----------------------¿ªÆô----------------------------");
        IoAcceptor acceptor = new NioSocketAcceptor();  
       // acceptor.getFilterChain().addLast( "logger", new LoggingFilter() );  
        //acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter( new PrefixedStringCodecFactory(Charset.forName("UTF-8")))); 
        acceptor.getFilterChain().addLast("mycoder", new ProtocolCodecFilter(new ByteArrayCodecFactory()));
        //acceptor.getFilterChain().addLast("myChin", new ProtocolCodecFilter(new TextLineCodecFactory()));
        acceptor.setHandler(  new TimeServerHandler() );  
        
        acceptor.getSessionConfig().setReadBufferSize( 2048 );  
                acceptor.getSessionConfig().setIdleTime( IdleStatus.BOTH_IDLE, 10 );  
          
        acceptor.bind(new InetSocketAddress(PORT));  
          
    }  
}
