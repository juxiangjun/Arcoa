package com.thesys.opencms.laphone.job;
/**
 * 便利達康-匯入店舖資料檔
 */
import java.io.InputStream;
import java.util.Map;



import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.imp.ThesysCvsStoreImportHandler;
import com.thesys.opencms.laphone.job.ftp.ThesysCvsFtpClient;

public class ThesysCvsStoreImportJob extends ThesysAbstractJob{

	protected static final Log LOG = CmsLog.getLog(ThesysCvsStoreImportJob.class);
		
	
	

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
			//Step 1: 從ftp server下載
			ThesysCvsFtpClient client = getCvsFtpClient();

			String folder = client.getFolder(ThesysCvsFtpClient.FILE_TYPE_F01);
			String fileName = client.getFileName(ThesysCvsFtpClient.FILE_TYPE_F01);
			//下載FTP檔案
			System.out.println("檔名"+fileName);
			InputStream in = client.download(folder+"/"+fileName);
			if(in!=null){
				//Step 2: 匯入資料
				ThesysCvsStoreImportHandler importHandler = new ThesysCvsStoreImportHandler(getCmsObject());	
				try{
					importHandler.importFile(in);
//							//匯入成功，將檔案移至備份資料夾
//							client.moveTo(inboxFolder+"/"+fileName, backupFolder+"/"+fileName);
					
				}catch(Exception e){
					e.printStackTrace();
//							//匯入錯誤，將檔案移至錯誤資料夾
//							client.moveTo(inboxFolder+"/"+fileName, errorFolder+"/"+fileName);
				}
			}
			//Step 4: 關閉FTP連結
			client.disconnect();
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();			
		}
	}
	
}
