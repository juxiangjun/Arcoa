package com.thesys.opencms.laphone.job.product;
/**
 * 匯入商品價格檔
 */
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.imp.ThesysSapProductImportHandler;
import com.thesys.opencms.laphone.job.ThesysAbstractJob;
import com.thesys.opencms.laphone.job.ftp.ThesysSapFtpClient;

public class ThesysProductPriceImportJob extends ThesysAbstractJob{

	protected static final Log LOG = CmsLog.getLog(ThesysProductPriceImportJob.class);
	
	
	
	/* (non-Javadoc)
	 * @see org.opencms.scheduler.I_CmsScheduledJob#launch(org.opencms.file.CmsObject, java.util.Map)
	 */
	@Override
	public String launch(CmsObject cmso, Map parameters) throws Exception {
		setCmsObject(cmso);	
		doImport();
        
		return null;
	}
	public void doImport(){
		try{
			//從ftp server下載ThesysProductImportJob	
			ThesysSapFtpClient client = getSapFtpClient();
			String inboxFolder = client.getInboxFolder(ThesysSapFtpClient.FILE_TYPE_PRODUCT_PRICE);
			String backupFolder = client.getBackupFolder(ThesysSapFtpClient.FILE_TYPE_PRODUCT_PRICE);
			String errorFolder = client.getErrorFolder(ThesysSapFtpClient.FILE_TYPE_PRODUCT_PRICE);
			
			//取得ftp資料夾清單
			String[] fileNames = client.list(inboxFolder);
			if(fileNames!=null && fileNames.length>0){
				for(int i=0;i<fileNames.length;i++){
					String fileName = fileNames[i];		
					System.out.println("檔名"+fileName);
					InputStream in = client.download(inboxFolder+"/"+fileName);
					if(in!=null){
						//匯入商品價格檔
						ThesysSapProductImportHandler importHandler = new ThesysSapProductImportHandler(getCmsObject());	
						try{
							importHandler.importPriceCsv(in);
							if(importHandler.getErrorCount()==0){
								//匯入成功，將檔案移至備份資料夾
								client.moveTo(inboxFolder+"/"+fileName, backupFolder+"/"+fileName);
							}else{	
								//先移動再覆蓋
								client.moveTo(inboxFolder+"/"+fileName, errorFolder+"/"+fileName);
								client.upload(errorFolder+"/"+fileName, importHandler.getResultStream());
							}							
						}catch(Exception e){
							e.printStackTrace();
							//匯入錯誤，將檔案移至錯誤資料夾
							client.moveTo(inboxFolder+"/"+fileName, errorFolder+"/"+fileName);
						}
					}
				}
			}
			//關閉FTP連結
			client.disconnect();
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();			
		}
	}

	
}
