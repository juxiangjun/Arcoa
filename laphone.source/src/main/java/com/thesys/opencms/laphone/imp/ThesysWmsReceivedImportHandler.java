package com.thesys.opencms.laphone.imp;
/**
 *WMS宅配客戶收貨回傳檔欄位
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.json.JSONObject;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.order.dao.ThesysOrderDAO;
import com.thesys.opencms.laphone.order.dao.ThesysOrderVO;

public class ThesysWmsReceivedImportHandler  extends ThesysAbstractCsvImportHandler{
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(ThesysSapStockImportHandler.class);

	private SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMddHHmmss"); // 資料中日期的格式
	//private final String JSON_SAP_ORDER_ID = "SAP_ORD_NO";//訂單文件號碼
	private final String JSON_ORDER_ID = "ORD_ID";//EC訂單號碼		
	private final String JSON_REC_ST = "REC_ST";//成功或失敗	
	private final String JSON_REC_DATE = "REC_DATE";//收貨日期			
	
    public ThesysWmsReceivedImportHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super.init(context, req, res);		

	}    
    public ThesysWmsReceivedImportHandler(CmsObject cmso){
    	super.init(cmso);
    }    
   	
	
	/* (non-Javadoc)
	 * @see com.thesys.opencms.laphone.ThesysCvsImportHandler#beforeImport()
	 */
	@Override
	public void beforeImport() {
		
	}
	/* (non-Javadoc)
	 * @see com.thesys.opencms.laphone.ThesysCvsImportHandler#afterImport()
	 */
	@Override
	public void afterImport() {
		
	}
	/* (non-Javadoc)
	 * @see com.thesys.opencms.laphone.ThesysCvsImportHandler#getColumnNames()
	 */
	@Override
	public String[] getColumnNames() {
		return new String[]{JSON_ORDER_ID,JSON_REC_ST,null,JSON_REC_DATE};
	}
	/**
	 * 匯入一行資料
	 * @param jsonObj
	 * @throws Exception
	 */
	public void importRow(JSONObject jsonObj) throws Exception{		
		LOG.debug(jsonObj.toString());	
//		String sapOrderId = (String)jsonObj.opt(JSON_SAP_ORDER_ID);
		String orderId = (String)jsonObj.opt(JSON_ORDER_ID);
		Date recDate = dateFmt.parse((String)jsonObj.get(JSON_REC_DATE));		
		int recStatus = Integer.parseInt((String)jsonObj.get(JSON_REC_ST));
		int orderStatus = 0;
		
		switch (recStatus) {
			case 1: //宅配成功
			case 3: //宅配失敗
				orderStatus = ThesysOrderVO.ORDER_STATUS_RECEIVED;	
				break;
			case 2: //大物流成功
			case 4: //大物流失敗
				orderStatus = ThesysOrderVO.ORDER_STATUS_UNRECEIVED;	
				break;
		}
		ThesysOrderDAO.getInstance().updateWMS(getSiteId(), orderId,orderStatus,recDate,recStatus);
  	}
	
	
}
