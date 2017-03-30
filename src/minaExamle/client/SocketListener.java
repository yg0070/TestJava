package minaExamle.client;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import minaExamle.server.MinaTimeServer;


public class SocketListener implements ServletContextListener {
    private MinaTimeServer server;;
     
    /**
     * ���� ��servlet������ֹwebӦ��ʱ���ø÷���
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    }
    /**
     * ��ʼ�� ��servlet��������webӦ��ʱ���ø÷���
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