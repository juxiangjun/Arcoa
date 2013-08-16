<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="java.io.*,org.apache.commons.net.ftp.*,com.thesys.opencms.laphone.job.*,java.net.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%>
<%
CmsJspActionElement cms = new CmsJspActionElement(pageContext,request,response);
%>
<%

/*
com.thesys.opencms.laphone.job.order.ThesysOrderFailureJob job = new com.thesys.opencms.laphone.job.order.ThesysOrderFailureJob(); 
try{
	//job.doPost();
	job.launch(cms.getCmsObject(),null);
	//out.println(job.generateCvsOrderXml());
}catch(Exception e){
	out.println(e.getMessage());
}
*/
com.thesys.opencms.laphone.job.ThesysCouponCreateJob job = new com.thesys.opencms.laphone.job.ThesysCouponCreateJob(); 
try{
	//job.doPost();
	job.launch(cms.getCmsObject(),null);
	//out.println(job.generateCvsOrderXml());
}catch(Exception e){
	out.println(e.getMessage());
}
/*
com.thesys.opencms.laphone.job.order.ThesysSapOrderExportJob job = new com.thesys.opencms.laphone.job.order.ThesysSapOrderExportJob(); 
try{
	//job.doPost();
	job.launch(cms.getCmsObject(),null);
	//out.println(job.generateCvsOrderXml());
}catch(Exception e){
	out.println(e.getMessage());
}
*/
//com.thesys.opencms.laphone.job.order.ThesysPostingOrderImportJob job = new com.thesys.opencms.laphone.job.order.ThesysPostingOrderImportJob(); 
/*com.thesys.opencms.laphone.job.order.ThesysWmsImportJob job = new com.thesys.opencms.laphone.job.order.ThesysWmsImportJob(); 
try{
	job.launch(cms.getCmsObject(),null);
	//job.doImport();
	out.println("完成");
	//out.println(job.generateCvsOrderXml());
}catch(Exception e){
	out.println(e.getMessage());
}*/
/*
//com.thesys.opencms.laphone.job.product.ThesysProductImportJob job = new com.thesys.opencms.laphone.job.product.ThesysProductImportJob(); 
//com.thesys.opencms.laphone.job.product.ThesysProductPriceImportJob job = new com.thesys.opencms.laphone.job.product.ThesysProductPriceImportJob(); 
com.thesys.opencms.laphone.job.product.ThesysProductGroupImportJob job = new com.thesys.opencms.laphone.job.product.ThesysProductGroupImportJob(); 
//com.thesys.opencms.laphone.job.product.ThesysProductStockImportJob job = new com.thesys.opencms.laphone.job.product.ThesysProductStockImportJob(); 
//com.thesys.opencms.laphone.job.order.ThesysPostingOrderImportJob job = new com.thesys.opencms.laphone.job.order.ThesysPostingOrderImportJob();
//com.thesys.opencms.laphone.job.ThesysCvsStoreImportJob job = new com.thesys.opencms.laphone.job.ThesysCvsStoreImportJob();

try{
	//job.launch(cms.getCmsObject(),null);
}catch(Exception e){
	out.println(e.getMessage());
}*/

/*
FTPClient client = new FTPClient();
client.connect("10.71.249.85",21);
client.login("qasftp", "qasftp");
client.setDataTimeout(3000);
client.setFileType(FTP.BINARY_FILE_TYPE); 
client.enterLocalPassiveMode(); 
client.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);  
client.changeWorkingDirectory("/EC/ZRSD0020/INBOX"); 
String[] files = client.listNames("SECN_*.txt");
for(int i=0;i<files.length;i++){
	out.println(files[i]);
}
 */ 
 /* 
//InputStream in = client.retrieveFileStream("/EC/ZRMM0002/INBOX/SEIN_20121220_181239.TXT");
//in.close();
//in = null;
OutputStream stream = new ByteArrayOutputStream();
out.println(client.retrieveFile("/EC/ZRMM0002/INBOX/SEIN_20121220_181239.TXT",stream));

out.println(client.rename("/EC/ZRMM0002/INBOX/SEIN_20121220_181239.TXT","/EC/ZRMM0002/BACKUP/SEIN_20121220_181239.TXT"));


client.disconnect();


	
FTPSClient client = new FTPSClient("TLS",true);
client.connect("cvsftp.cvs.com.tw",8821);
client.login("195", "tupi55uhg8gmc4j");
client.setDataTimeout(3000);
client.setFileType(FTP.BINARY_FILE_TYPE); 
client.enterLocalPassiveMode(); 
client.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);   
String[] files = client.listNames("/F11/F11195CVS20121222*.xml");
for(int i=0;i<files.length;i++){
	out.println(files[i]);
}
*/		
%>