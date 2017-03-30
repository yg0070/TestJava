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
			// 设置上传目录
			ftpClient.changeWorkingDirectory("/admin/pic");
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("GBK");
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.storeFile("3.gif", fis);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("FTP客户端出错！", e);
		} finally {
			IOUtils.closeQuietly(fis);
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}

	/**
	 * Description: 向FTP服务器上传文件
	 * 
	 * @Version 1.0
	 * @param url
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param path
	 *            FTP服务器保存目录
	 * @param filename
	 *            上传到FTP服务器上的文件名
	 * @param input
	 *            输入流
	 * @return 成功返回true，否则返回false *
	 */
	public static boolean uploadFile(String url,// FTP服务器hostname
			int port,// FTP服务器端口
			String username, // FTP登录账号
			String password, // FTP登录密码
			String path, // FTP服务器保存目录
			String filename, // 上传到FTP服务器上的文件名
			InputStream input // 输入流
	) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		ftp.setControlEncoding("UTF-8");
		try {
			int reply;
			ftp.connect(url, port);// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
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
	 * 将本地文件上传到FTP服务器上 *
	 */
	public static void upLoadFromProduction(String url,// FTP服务器hostname
			int port,// FTP服务器端口
			String username, // FTP登录账号
			String password, // FTP登录密码
			String path, // FTP服务器保存目录
			String filename, // 上传到FTP服务器上的文件名
			String orginfilename // 输入流文件名
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
