package com.thesys.opencms.laphone.job.ftp;


import java.text.SimpleDateFormat;

//WMS宅配客戶收貨回傳檔欄位
public class ThesysSapFtpClient extends ThesysFtpClient{
	
	
	public final static String FILE_TYPE_SAP_ORDER = "ZBSD0015"; //拋轉至SAP之訂單檔(已測試完成)
	public final static String FILE_TYPE_WMS = "WMS0001"; //配達資料	

	public final static String FILE_TYPE_ORDER_POSTED = "ZRSD0020"; //SAP銷貨完成過帳檔(已開發完成)
	public final static String FILE_TYPE_RETURN_POSTED = "ZRSD0020_SERN"; //退貨完成過帳檔 SERN
	
	/**商品資料**/
	public final static String FILE_TYPE_PRODUCT_MAIN = "ZRMM0001"; //物料主檔 (已測試完成)
	public final static String FILE_TYPE_PRODUCT_GROUP = "ZRSD0014"; //組案價格檔(已測試完成)
	public final static String FILE_TYPE_PRODUCT_PRICE = "ZRSD0005B"; //單品價格檔 (已測試完成)
	public final static String FILE_TYPE_PRODUCT_STOCK = "ZRMM0002"; //物料庫存檔 (已測試完成)
	

//	private SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd");
//	private SimpleDateFormat datetimeFmt = new SimpleDateFormat("yyyyMMddHHmmss");
	private SimpleDateFormat underlineFmt = new SimpleDateFormat("yyyyMMdd_HHmmss");
	
	
//	public static void main(String[] args){
//		try{
//			ThesysSapFtpClient client = new ThesysSapFtpClient("10.71.249.85",21,"qasftp","qasftp");
//			//client.test();
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//	}
	
	/**
	 * @param ftpHost
	 * @param ftpPort
	 * @param ftpUser
	 * @param ftpPwd
	 */
	public ThesysSapFtpClient(String ftpHost, int ftpPort,boolean tlsProtocol, String ftpUser,String ftpPwd,String rootPath) {
		super(ftpHost,ftpPort,tlsProtocol,ftpUser,ftpPwd,rootPath);
	}

	public String getInboxFolder(String fileType){
		if(FILE_TYPE_RETURN_POSTED.equals(fileType)){
			fileType = FILE_TYPE_ORDER_POSTED;
		}
		return getRootPath()+fileType+"/INBOX";
	}
	public String getBackupFolder(String fileType){
		if(FILE_TYPE_RETURN_POSTED.equals(fileType)){
			fileType = FILE_TYPE_ORDER_POSTED;
		}
		return getRootPath()+fileType+"/BACKUP";
	}
	public String getErrorFolder(String fileType){
		if(FILE_TYPE_RETURN_POSTED.equals(fileType)){
			fileType = FILE_TYPE_ORDER_POSTED;
		}
		return getRootPath()+fileType+"/ERROR";
	}
	
	
	public String getFileName(String fileType){
//		Calendar cal = Calendar.getInstance();
		java.util.Date now = new java.util.Date();
//		cal.setTime(now);
//		cal.add(Calendar.DATE, -1); //取前一天的檔案
//		java.util.Date yestoday = cal.getTime();
		
		if(FILE_TYPE_SAP_ORDER.equals(fileType)){ //EC訂單檔拋轉SAP；ESSO_YYYYMMDD_HHMMSS.txt			
			return "ESSO_"+underlineFmt.format(now)+".txt";
		}else if(FILE_TYPE_ORDER_POSTED.equals(fileType)){ //EC訂單銷貨完成過帳檔(出貨&WMS撿貨完成準備出貨) ：SECN_YYYYMMDD_HHMMSS.txt
			return "SECN_*.txt";
//		}else if(FILE_TYPE_WMS.equals(fileType)){ //配達資料：WE + YYYYMMDDHHMMSS.txt(直接掃全部)	
//			return "WE"+datetimeFmt.format(now)+".txt";
		}else if(FILE_TYPE_RETURN_POSTED.equals(fileType)){ //退貨完成過帳檔：SERN_YYYYMMDD_HHMMSS.txt
			return "SERN_*.txt";		
//		}else if(FILE_TYPE_PRODUCT_PRICE.equals(fileType)){ //單品價格檔：SERC_YYYYMMDD_HHMMSS.txt	(直接掃全部)		
//			return "SERC_"+underlineFmt.format(now)+".txt";
//		}else if(FILE_TYPE_PRODUCT_GROUP.equals(fileType)){ //組合案價格檔：SEBR_YYYYMMDD_HHMMSS.txt(直接掃全部)	
//			return "SEBR_"+underlineFmt.format(now)+".txt";	
//		}else if(FILE_TYPE_PRODUCT_MAIN.equals(fileType)){ //物料主檔(含18階層碼)：SEMA_YYYYMMDD_HHMMSS.txt	(直接掃全部)	
//			return "SEMA_"+underlineFmt.format(now)+".txt";	
//		}else if(FILE_TYPE_PRODUCT_STOCK.equals(fileType)){ //物料庫存檔：SEIN_YYYYMMDD_HHMMSS.txt(直接掃全部)	
//			return "SEIN_"+underlineFmt.format(now)+".txt";	
		
		}else{
			return null;
		}
		
		
	}
}
