package com.thesys.opencms.laphone.job.order;
/**
 * 匯入SAP銷貨完成過帳檔
 */
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.imp.ThesysSapPostingOrderImportHandler;
import com.thesys.opencms.laphone.job.ThesysAbstractJob;
import com.thesys.opencms.laphone.job.ftp.ThesysSapFtpClient;
import com.thesys.opencms.laphone.job.product.ThesysProductImportJob;

public class ThesysOrderPostingImportJob extends ThesysAbstractJob{

	protected static final Log LOG = CmsLog.getLog(ThesysProductImportJob.class);
	
	
	
	

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
			System.out.println("test");
			//Step 1: 從ftp server下載EC訂單銷貨完成過帳檔
			ThesysSapFtpClient client = getSapFtpClient();
			
			String inboxFolder = client.getInboxFolder(ThesysSapFtpClient.FILE_TYPE_ORDER_POSTED);
			String backupFolder = client.getBackupFolder(ThesysSapFtpClient.FILE_TYPE_ORDER_POSTED);
			String errorFolder = client.getErrorFolder(ThesysSapFtpClient.FILE_TYPE_ORDER_POSTED);
			
			//過濾本日之所有檔案
			String fileFilter = client.getFileName(ThesysSapFtpClient.FILE_TYPE_ORDER_POSTED);

			//取得ftp資料夾清單
			String[] fileNames = client.listFilter(inboxFolder,fileFilter);
			if(fileNames!=null && fileNames.length>0){
				for(int i=0;i<fileNames.length;i++){
					String fileName = fileNames[i];		
					InputStream in = client.download(inboxFolder+"/"+fileName);
					if(in!=null){
						//Step 2: 匯入SAP銷貨完成過帳檔
						ThesysSapPostingOrderImportHandler importHandler = new ThesysSapPostingOrderImportHandler(getCmsObject());
						try{
							importHandler.importFile(in, false);
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
			//Step 4: 關閉FTP連結
			client.disconnect();
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();			
		}
	}

	
	

	
}
