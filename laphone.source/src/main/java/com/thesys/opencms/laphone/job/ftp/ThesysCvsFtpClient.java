package com.thesys.opencms.laphone.job.ftp;


import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ThesysCvsFtpClient extends ThesysFtpClient{
	
	public final static String FILE_TYPE_F01 = "F01"; //XML 店舖資料檔
	public final static String FILE_TYPE_F04 = "F04"; //進店檔
	public final static String FILE_TYPE_F05 = "F05"; //取貨完成檔
	public final static String FILE_TYPE_F07 = "F07"; //大物流驗退檔
	public final static String FILE_TYPE_F11 = "F11"; //訂單檔回覆檔
	
	private String cvsAccount = "195";
	
	private SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
//	private SimpleDateFormat datetimeFmt = new SimpleDateFormat("yyyyMMddHHmmss");
//	
//	private ThesysFtpClient client;
//	public static void main(String[] args){
//		try{
//			ThesysCvsFtpClient client = new ThesysCvsFtpClient("cvsftp.cvs.com.tw",8821,"195","tupi55uhg8gmc4j","195");
//			//client.test();
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//		
//	}
	
	/**
	 * @param ftpHost
	 * @param ftpPort
	 * @param ftpUser
	 * @param ftpPwd
	 */
	public ThesysCvsFtpClient(String ftpHost, int ftpPort,boolean tlsProtocol, String ftpUser,String ftpPwd,String rootPath,String cvsAccount) {
		super(ftpHost,ftpPort,tlsProtocol,ftpUser,ftpPwd,rootPath);
		this.cvsAccount = cvsAccount;
	}

	public String getFolder(String fileId){
		return getRootPath()+fileId;
	}
	public String getFileName(String fileId){
		Calendar cal = Calendar.getInstance();
		java.util.Date now = new java.util.Date();
		cal.setTime(now);
		cal.add(Calendar.DATE, -1); //取前一天的檔案
		java.util.Date yestoday = cal.getTime();
		
		if(FILE_TYPE_F01.equals(fileId)){ //XML 店舖資料檔
			return FILE_TYPE_F01+"ALLCVS"+dateFmt.format(now)+".xml";
		}else if(FILE_TYPE_F04.equals(fileId)){ //進店檔
			return FILE_TYPE_F04+cvsAccount+"CVS"+dateFmt.format(now)+".xml";
		}else if(FILE_TYPE_F05.equals(fileId)){ //取貨完成檔
			return FILE_TYPE_F05+cvsAccount+"CVS"+dateFmt.format(now)+".xml";
		}else if(FILE_TYPE_F07.equals(fileId)){ //大物流驗退檔
			return FILE_TYPE_F07+cvsAccount+"CVS"+dateFmt.format(now)+".xml";
//		}else if("F10".equals(fileId)){ //訂單檔(使用Web Service)
//			return "F10"+cvsAccount+"CVS"+datetimeFmt.format(now)+".xml";
		}else if(FILE_TYPE_F11.equals(fileId)){ //訂單回覆檔
			return FILE_TYPE_F11+cvsAccount+"CVS"+dateFmt.format(yestoday)+"*.xml";
		}else{
			return null;
		}
		
		
	}
}
