package minaExamle.client;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import minaExamle.server.MinaTimeServer;


public class SocketListener implements ServletContextListener {
    private MinaTimeServer server;;
     
    /**
     * 销毁 当servlet容器终止web应用时调用该方法
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    }
    /**
     * 初始化 当servlet容器启动web应用时调用该方法
     */
    public void contextInitialized(ServletContextEvent arg0) {
		try {
			server = new MinaTimeServer();
			server.stateServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
}