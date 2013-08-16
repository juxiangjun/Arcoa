package com.thesys.opencms.laphone.job.ftp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPSClient;

public class ThesysFtpClient {
	
	private String ftpHost = "cvsftp.cvs.com.tw";
	private int ftpPort = 8821;
	private String ftpUser = "195";
	private String ftpPwd = "tupi55uhg8gmc4j";
	private boolean tlsProtocol = false;
	private String protocol = "SFTP";//"TLS";
	private String rootPath = "/";
	
	private FTPClient client;
	public static void main(String[] args){
		try{
			ThesysFtpClient client = new ThesysFtpClient("192.168.7.3",21,false,"thesys","!QW@3edc","/home/thesys/EC/");			
			client.connect();
			String inboxFolder = "/home/thesys/EC";
			String fileName = "SEMA_20130219_171550_new.txt";
//			client.moveTo(fileName, inboxFolder+"/"+fileName);
			FileInputStream in = new FileInputStream("E:\\專案文件\\全虹\\匯入格式及測試資料\\物料主檔\\SEMA_20130219_171550_new.txt");
			client.upload(inboxFolder+"/"+fileName, in);
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * @param ftpHost
	 * @param ftpPort
	 * @param ftpUser
	 * @param ftpPwd
	 */
	public ThesysFtpClient(String ftpHost, int ftpPort,boolean tlsProtocol, String ftpUser,String ftpPwd,String rootPath) {
		this.ftpHost = ftpHost;
		this.ftpPort = ftpPort;
		this.ftpUser = ftpUser;
		this.ftpPwd = ftpPwd;
		this.tlsProtocol = tlsProtocol;
		this.rootPath = rootPath;
		if(this.rootPath==null){ this.rootPath = "/";}
		else if(!this.rootPath.endsWith("/")){ this.rootPath += "/";}
		
	}
	
	public void connect() throws Exception{
		if(client==null || !client.isConnected()){
			if(tlsProtocol){ //隱函式TLS的FTP
				client = new FTPSClient(protocol,true);
			}else{
				client = new FTPClient();
			}
			client.connect(ftpHost,ftpPort);
			client.login(ftpUser, ftpPwd);
			client.setDataTimeout(3000);
			client.setFileType(FTP.BINARY_FILE_TYPE); 
			client.enterLocalPassiveMode(); 
			client.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);   
		}
		
	}
	public void disconnect() throws Exception{
		if(client!=null && client.isConnected()){
			client.disconnect();
			client = null;
		}
	}
	/**
	 * 移動檔案
	 * @param fromFolder
	 * @param toFolder
	 * @param fileName
	 * @throws Exception
	 */
	public void moveTo(String from,String to)throws Exception{
		connect();
		System.out.println(from);
		System.out.println(to);
		client.rename(from, to);
	}
	/**
	 * 上傳檔案
	 * @param fileType
	 * @return
	 * @throws Exception
	 */
	public void upload(String filePath,InputStream in)throws Exception{
		connect();
		if(!client.storeFile(filePath, in)){
			throw new Exception("上傳失敗");
		}
	}	
	/**
	 * 下載檔案
	 * @param fileType
	 * @return
	 * @throws Exception
	 */
	public InputStream download(String filePath) throws Exception{
		connect();
		System.out.println(filePath);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		if(client.retrieveFile(filePath,out)){

			System.out.println(new String(out.toByteArray(),"UTF-8"));
			return new ByteArrayInputStream(out.toByteArray());
			
		}
		//InputStream in = client.retrieveFileStream(filePath);
		return null;
		
	}	
	/**
	 * 列出資料夾內的檔案名稱(僅有檔名)
	 * @param folder
	 * @return
	 * @throws Exception
	 */
	public String[] list(String folder)throws Exception{
		connect();
		if(folder!=null && folder.length()!=0){
			client.changeWorkingDirectory(folder);
		}
		return client.listNames();
	}
	/**
	 * 列出符合條件的檔案
	 * @param filter
	 * @return
	 */
	public String[] listFilter(String folder,String filter)throws Exception{
		connect();
		if(folder!=null && folder.length()!=0){
			client.changeWorkingDirectory(folder);
		}
		return client.listNames(filter);
	}

	/**
	 * @return the ftpHost
	 */
	public String getFtpHost() {
		return ftpHost;
	}

	/**
	 * @param ftpHost the ftpHost to set
	 */
	public void setFtpHost(String ftpHost) {
		this.ftpHost = ftpHost;
	}

	/**
	 * @return the ftpPort
	 */
	public int getFtpPort() {
		return ftpPort;
	}

	/**
	 * @param ftpPort the ftpPort to set
	 */
	public void setFtpPort(int ftpPort) {
		this.ftpPort = ftpPort;
	}

	/**
	 * @return the ftpUser
	 */
	public String getFtpUser() {
		return ftpUser;
	}

	/**
	 * @param ftpUser the ftpUser to set
	 */
	public void setFtpUser(String ftpUser) {
		this.ftpUser = ftpUser;
	}

	/**
	 * @return the ftpPwd
	 */
	public String getFtpPwd() {
		return ftpPwd;
	}

	/**
	 * @param ftpPwd the ftpPwd to set
	 */
	public void setFtpPwd(String ftpPwd) {
		this.ftpPwd = ftpPwd;
	}

	/**
	 * @return the tlsProtocol
	 */
	public boolean isTlsProtocol() {
		return tlsProtocol;
	}

	/**
	 * @param tlsProtocol the tlsProtocol to set
	 */
	public void setTlsProtocol(boolean tlsProtocol) {
		this.tlsProtocol = tlsProtocol;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the rootPath
	 */
	public String getRootPath() {
		return rootPath;
	}

	/**
	 * @param rootPath the rootPath to set
	 */
	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}
	
}
