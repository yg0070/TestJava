package minaExamle.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import sun.net.TelnetInputStream;
import sun.net.ftp.*;
public class FtpTest {
	public static void testUpload() {
		FTPClient ftpClient = new FTPClient();
		FileInputStream fis = null;

		try {
			ftpClient.connect("localhost", 22);
			ftpClient.login("root", "root");

			File srcFile = new File("C:\\new.gif");
			fis = new FileInputStream(srcFile);
			// �����ϴ�Ŀ¼
			ftpClient.changeWorkingDirectory("/admin/pic");
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("GBK");
			// �����ļ����ͣ������ƣ�
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.storeFile("3.gif", fis);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP�ͻ��˳���", e);
		} finally {
			IOUtils.closeQuietly(fis);
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("�ر�FTP���ӷ����쳣��", e);
			}
		}
	}

	/**
	 * Description: ��FTP�������ϴ��ļ�
	 * 
	 * @Version 1.0
	 * @param url
	 *            FTP������hostname
	 * @param port
	 *            FTP�������˿�
	 * @param username
	 *            FTP��¼�˺�
	 * @param password
	 *            FTP��¼����
	 * @param path
	 *            FTP����������Ŀ¼
	 * @param filename
	 *            �ϴ���FTP�������ϵ��ļ���
	 * @param input
	 *            ������
	 * @return �ɹ�����true�����򷵻�false *
	 */
	public static boolean uploadFile(String url,// FTP������hostname
			int port,// FTP�������˿�
			String username, // FTP��¼�˺�
			String password, // FTP��¼����
			String path, // FTP����������Ŀ¼
			String filename, // �ϴ���FTP�������ϵ��ļ���
			InputStream input // ������
	) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		ftp.setControlEncoding("UTF-8");
		try {
			int reply;
			ftp.connect(url, port);// ����FTP������
			// �������Ĭ�϶˿ڣ�����ʹ��ftp.connect(url)�ķ�ʽֱ������FTP������
			ftp.login(username, password);// ��¼
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftp.makeDirectory(path);
			ftp.changeWorkingDirectory(path);
			ftp.storeFile(filename, input);
			input.close();
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}

	/**
	 * �������ļ��ϴ���FTP�������� *
	 */
	public static void upLoadFromProduction(String url,// FTP������hostname
			int port,// FTP�������˿�
			String username, // FTP��¼�˺�
			String password, // FTP��¼����
			String path, // FTP����������Ŀ¼
			String filename, // �ϴ���FTP�������ϵ��ļ���
			String orginfilename // �������ļ���
	) {
		try {
			FileInputStream in = new FileInputStream(new File(orginfilename));
			boolean flag = uploadFile(url, port, username, password, path,
					filename, in);
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*public static void main(String[] args) {
		upLoadFromProduction("127.0.0.1", 22, "root", "root", "img",
				"README222.txt", "D:/asm-4.0/doc/README.txt");
	}*/
	static FtpClient myFtp;
	static String hostname;
	static String username;
	static String password;

	/**
	 * @author cutelion 20051108 14:27
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			hostname = "127.0.0.1";
			myFtp = new FtpClient(hostname,22);
			myFtp.login("root", "root");
			myFtp.binary();
			showFileContents();
		} catch (IOException e1) {
			System.out.print(e1);
		}
	}

	public static void showFileContents() {
		int ch;
		StringBuffer buf = new StringBuffer();
		try {
			TelnetInputStream inStream = myFtp.list();
			while ((ch = inStream.read()) >= 0) {
				buf.append((char) ch);
			}
			System.out.print(new String(buf.toString().getBytes("iso-8859-1"),
					"GBK"));
			inStream.close();
			myFtp.closeServer();
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
	}

}
