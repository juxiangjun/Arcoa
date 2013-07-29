package com.thesys.opencms.laphone.job.order;
/**
 * 便利達康-取貨完成檔(F05)
 */
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.imp.ThesysCvsReceivedImportHandler;
import com.thesys.opencms.laphone.job.ThesysAbstractJob;
import com.thesys.opencms.laphone.job.ftp.ThesysCvsFtpClient;

public class ThesysCvsReceivedImportJob extends ThesysAbstractJob{

	protected static final Log LOG = CmsLog.getLog(ThesysCvsReceivedImportJob.class);
		
	

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
			//過濾本日之所有檔案
			String folder = client.getFolder(ThesysCvsFtpClient.FILE_TYPE_F05);
			String fileFilter = client.getFileName(ThesysCvsFtpClient.FILE_TYPE_F05);
			//取得ftp資料夾清單
			String[] fileNames = client.listFilter(folder,fileFilter);
			if(fileNames!=null && fileNames.length>0){
				for(int i=0;i<fileNames.length;i++){
					String fileName = fileNames[i];		
					System.out.println("檔名"+fileName);
					InputStream in = client.download(fileName);
					if(in!=null){
						//Step 2: 匯入組案價格檔
						ThesysCvsReceivedImportHandler importHandler = new ThesysCvsReceivedImportHandler(getCmsObject());	
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
				}
			}
			//Step 4: 關閉FTP連結
			client.disconnect();
			
			
			
		}catch(Exception ex){
			ex.printStackTrace();			
		}
	}
	
}
