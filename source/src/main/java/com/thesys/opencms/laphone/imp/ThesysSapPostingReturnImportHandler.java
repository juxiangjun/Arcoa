package com.thesys.opencms.laphone.imp;
/**
 * 退貨完成過帳檔欄位
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

public class ThesysSapPostingReturnImportHandler  extends ThesysAbstractCsvImportHandler{
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(ThesysSapStockImportHandler.class);

	private SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd"); // 資料中日期的格式
//	private final String JSON_SAP_ORDER_ID = "SAP_ORD_NO";//訂單文件號碼
	private final String JSON_ORDER_ID = "ORD_ID";//EC訂單號碼
	private final String JSON_RTN_POST_DATE = "RTN_POST_DATE";//退貨過帳日期					
	
    public ThesysSapPostingReturnImportHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super.init(context, req, res);		

	}    
    public ThesysSapPostingReturnImportHandler(CmsObject cmso){
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
//		return new String[]{JSON_SAP_ORDER_ID,JSON_ORDER_ID,JSON_RTN_POST_DATE};
		
		return new String[]{
//				JSON_SAP_ORDER_ID, //SAP訂單文件號碼
				null, //SAP退貨單文件號碼
				null, //SAP交貨單號碼
				null, //SAP請款文件號碼
				null,//SAP發票號碼 
				JSON_ORDER_ID, //EC訂單號碼
				JSON_RTN_POST_DATE //過帳日期
				};
	}
	/**
	 * 匯入一行資料
	 * @param jsonObj
	 * @throws Exception
	 */
	public void importRow(JSONObject jsonObj) throws Exception{		
		LOG.debug(jsonObj.toString());	
//    	String sapOrderId = (String) jsonObj.opt(JSON_SAP_ORDER_ID);
    	String orderId = (String) jsonObj.opt(JSON_ORDER_ID);
    	Date postDate = jsonObj.opt(JSON_RTN_POST_DATE) == null?null:dateFmt.parse((String)jsonObj.get(JSON_RTN_POST_DATE));
    	ThesysOrderDAO.getInstance().updateReturnPost(getSiteId(), orderId, postDate);
  	}
	
	
}
