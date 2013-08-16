package com.thesys.opencms.laphone.imp;
/**
 * 匯入SAP銷貨完成過帳檔
 */

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;



import org.opencms.file.CmsObject;
import org.opencms.json.JSONObject;


import com.thesys.opencms.laphone.order.dao.ThesysOrderDAO;
import com.thesys.opencms.laphone.order.dao.ThesysOrderVO;

public class ThesysSapPostingOrderImportHandler  extends ThesysAbstractCsvImportHandler{
	
	
	private SimpleDateFormat dateFmt = new SimpleDateFormat("yyyyMMdd"); // 資料中日期的格式
    private final String JSON_SAP_ORDER_NO = "SAP_ORD_NO";//訂單文件號碼
    private final String JSON_SAP_SHIP_NO = "SAP_SHIP_NO";//SAP交貨單號碼
    private final String JSON_SAP_BILLING_NO = "SAP_BILLING_NO";//SAP請款文件號碼
	private final String JSON_INVOICE_NO = "INVOICE_NO";//發票號碼		
	private final String JSON_ORDER_ID = "ORD_ID";//EC訂單號碼
	private final String JSON_SAP_POST_DATE = "SAP_POST_DATE";//過帳日期	
    
    public ThesysSapPostingOrderImportHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super.init(context, req, res);		

	}    
    public ThesysSapPostingOrderImportHandler(CmsObject cmso){
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
		return new String[]{
				JSON_SAP_ORDER_NO, //SAP訂單文件號碼
				JSON_SAP_SHIP_NO, //SAP交貨單號碼
				JSON_SAP_BILLING_NO, //SAP請款文件號碼
				JSON_INVOICE_NO,//SAP發票號碼 
				JSON_ORDER_ID, //EC訂單號碼
				JSON_SAP_POST_DATE //過帳日期
				};
	}
	/**
	 * 匯入一行資料
	 * @param jsonObj
	 * @throws Exception
	 */
	public void importRow(JSONObject jsonObj) throws Exception{		

    	String orderId = jsonObj.getString(JSON_ORDER_ID);
    	String sapOrderNo = jsonObj.getString(JSON_SAP_ORDER_NO);
    	String sapShipNo = jsonObj.getString(JSON_SAP_SHIP_NO);
    	String sapBillingNo = jsonObj.getString(JSON_SAP_BILLING_NO);
    	Date postDate = dateFmt.parse((String)jsonObj.get(JSON_SAP_POST_DATE));
    	String invoiceNo = jsonObj.getString(JSON_INVOICE_NO);
    	ThesysOrderDAO.getInstance().updateSapOrder(getSiteId(), orderId, ThesysOrderVO.ORDER_STATUS_SHIPPED, sapOrderNo, sapShipNo, sapBillingNo, postDate, invoiceNo);
		
	}
	
	
}
