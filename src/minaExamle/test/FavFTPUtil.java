package minaExamle.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class FavFTPUtil {

	/**
	 * �ϴ��ļ����ɹ�Action/Controller��ʹ�ã�
	 * 
	 * @param hostname
	 *            FTP��������ַ
	 * @param port
	 *            FTP�������˿ں�
	 * @param username
	 *            FTP��¼�ʺ�
	 * @param password
	 *            FTP��¼����
	 * @param pathname
	 *            FTP����������Ŀ¼
	 * @param fileName
	 *            �ϴ���FTP����������ļ�����
	 * @param inputStream
	 *            �����ļ���
	 * @return
	 */
	public static boolean uploadFile(String hostname, int port,
			String username, String password, String pathname, String fileName,
			InputStream inputStream) {
		boolean flag = false;
		FTPClient ftpClient = new FTPClient();
		ftpClient.setControlEncoding("UTF-8");
		try {
			// ����FTP������
			ftpClient.connect(hostname, port);
			// ��¼FTP������
			ftpClient.login(username, password);
			// �Ƿ�ɹ���¼FTP������
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				return flag;
			}

			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.makeDirectory(pathname);
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.storeFile(fileName, inputStream);
			inputStream.close();
			ftpClient.logout();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return flag;
	}

	/**
	 * �ϴ��ļ����ɶ��ļ�������������
	 * 
	 * @param hostname
	 *            FTP��������ַ
	 * @param port
	 *            FTP�������˿ں�
	 * @param username
	 *            FTP��¼�ʺ�
	 * @param password
	 *            FTP��¼����
	 * @param pathname
	 *            FTP����������Ŀ¼
	 * @param filename
	 *            �ϴ���FTP����������ļ�����
	 * @param originfilename
	 *            ���ϴ��ļ������ƣ����Ե�ַ��
	 * @return
	 */
	public static boolean uploadFileFromProduction(String hostname, int port,
			String username, String password, String pathname, String filename,
			String originfilename) {
		boolean flag = false;
		try {
			InputStream inputStream = new FileInputStream(new File(
					originfilename));
			flag = uploadFile(hostname, port, username, password, pathname,
					filename, inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * �ϴ��ļ��������Խ����ļ���������������
	 * 
	 * @param hostname
	 *            FTP��������ַ
	 * @param port
	 *            FTP�������˿ں�
	 * @param username
	 *            FTP��¼�ʺ�
	 * @param password
	 *            FTP��¼����
	 * @param pathname
	 *            FTP����������Ŀ¼
	 * @param originfilename
	 *            ���ϴ��ļ������ƣ����Ե�ַ��
	 * @return
	 */
	public static boolean uploadFileFromProduction(String hostname, int port,
			String username, String password, String pathname,
			String originfilename) {
		boolean flag = false;
		try {
			String fileName = new File(originfilename).getName();
			InputStream inputStream = new FileInputStream(new File(
					originfilename));
			flag = uploadFile(hostname, port, username, password, pathname,
					fileName, inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * ɾ���ļ�
	 * 
	 * @param hostname
	 *            FTP��������ַ
	 * @param port
	 *            FTP�������˿ں�
	 * @param username
	 *            FTP��¼�ʺ�
	 * @param password
	 *            FTP��¼����
	 * @param pathname
	 *            FTP����������Ŀ¼
	 * @param filename
	 *            Ҫɾ�����ļ�����
	 * @return
	 */
	public static boolean deleteFile(String hostname, int port,
			String username, String password, String pathname, String filename) {
		boolean flag = false;
		FTPClient ftpClient = new FTPClient();
		try {
			// ����FTP������
			ftpClient.connect(hostname, port);
			// ��¼FTP������
			ftpClient.login(username, password);
			// ��֤FTP�������Ƿ��¼�ɹ�
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				return flag;
			}
			// �л�FTPĿ¼
			ftpClient.changeWorkingDirectory(pathname);
			ftpClient.dele(filename);
			ftpClient.logout();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.logout();
				} catch (IOException e) {

				}
			}
		}
		return flag;
	}

	/**
	 * �����ļ�
	 * 
	 * @param hostname
	 *            FTP��������ַ
	 * @param port
	 *            FTP�������˿ں�
	 * @param username
	 *            FTP��¼�ʺ�
	 * @param password
	 *            FTP��¼����
	 * @param pathname
	 *            FTP�������ļ�Ŀ¼
	 * @param filename
	 *            �ļ�����
	 * @param localpath
	 *            ���غ���ļ�·��
	 * @return
	 */
	public static boolean downloadFile(String hostname, int port,
			String username, String password, String pathname, String filename,
			String localpath) {
		boolean flag = false;
		FTPClient ftpClient = new FTPClient();
		try {
			// ����FTP������
			ftpClient.connect(hostname, port);
			// ��¼FTP������
			ftpClient.login(username, password);
			// ��֤FTP�������Ƿ��¼�ɹ�
			int replyCode = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(replyCode)) {
				return flag;
			}
			// �л�FTPĿ¼
			ftpClient.changeWorkingDirectory(pathname);
			FTPFile[] ftpFiles = ftpClient.listFiles();
			for (FTPFile file : ftpFiles) {
				if (filename.equalsIgnoreCase(file.getName())) {
					File localFile = new File(localpath + "/" + file.getName());
					OutputStream os = new FileOutputStream(localFile);
					ftpClient.retrieveFile(file.getName(), os);
					os.close();
				}
			}
			ftpClient.logout();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.logout();
				} catch (IOException e) {

				}
			}
		}
		return flag;
	}

	public static void main(String[] args) {
		String hostname = "127.0.0.1";
		int port = 21;
		String username = "admin";
		String password = "admin";
		String pathname = "easy";
		String filename = "easy";
		String originfilename = "E:\\googleDownloads\\easy";
		FavFTPUtil.uploadFileFromProduction(hostname, port, username, password,
				pathname, filename, originfilename);
		/*FavFTPUtil.downloadFile(hostname, port, username, password,
				pathname, filename, originfilename);*/
		// String localpath = "D:/";

		// FavFTPUtil.downloadFile(hostname, port, username, password, pathname,
		// filename, localpath);
	}

}