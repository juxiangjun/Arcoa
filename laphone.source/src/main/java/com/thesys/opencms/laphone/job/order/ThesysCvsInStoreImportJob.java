package com.thesys.opencms.laphone.job.order;
/**
 * 匯入進店檔
 */
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.imp.ThesysCvsInStoreImportHandler;
import com.thesys.opencms.laphone.job.ThesysAbstractJob;
import com.thesys.opencms.laphone.job.ftp.ThesysCvsFtpClient;

public class ThesysCvsInStoreImportJob extends ThesysAbstractJob {
	protected static final Log LOG = CmsLog.getLog(ThesysCvsInStoreImportJob.class);
	
	
	@Override
	public String launch(CmsObject cmso, Map parameters) throws Exception {
		setCmsObject(cmso);	
		doUpdate();
		
		return null;
	}
	
	public void doUpdate(){
		try{
			//Step 1: 從ftp server下載
			ThesysCvsFtpClient client = getCvsFtpClient();

			String folder = client.getFolder(ThesysCvsFtpClient.FILE_TYPE_F04);
			String fileName = client.getFileName(ThesysCvsFtpClient.FILE_TYPE_F04);
			//下載FTP檔案
			System.out.println("檔名"+fileName);
			InputStream in = client.download(folder+"/"+fileName);
			if(in!=null){
				//Step 2: 匯入資料
				ThesysCvsInStoreImportHandler handler = new ThesysCvsInStoreImportHandler(getCmsObject());	
				try{
					handler.updateData(in);
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			//Step 4: 關閉FTP連結
			client.disconnect();
		}catch(Exception ex){
			ex.printStackTrace();			
		}
	}
}
