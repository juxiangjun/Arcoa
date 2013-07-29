package com.thesys.opencms.laphone.report;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.order.dao.ThesysOrderVO;
import com.thesys.opencms.laphone.report.dao.ThesysOrderReportDAO;

public class ThesysOrderReportHandler extends ThesysLaphoneHandler {
	protected static final Log LOG = CmsLog.getLog(ThesysOrderReportHandler.class);
	
	
	private String startDate;
	private String endDate;
	private int reportType = 1; //1:訂單收款管理；2：每日到貨未取異常表；3：e
	private String receiptFlag = "All"; //是否已填寫收款報告單；All：全部；Yes：已填；No：未填
	private String orderId;
	
	public ThesysOrderReportHandler(){}
	public ThesysOrderReportHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super.init(context, req, res);
	}
	
	public void export(OutputStream out){
		String[] headers = {};
		String[] columns = {};
		switch(getReportType()){
		case 1:  
			headers = new String[]{"訂單編號","訂單日期","會員帳號","付款方式","訂單金額"};
			columns = new String[]{"ORD_ID","ORD_DATE","MEM_ID","PAY_TYPE","ORD_AMT"};break;
		case 2: 
			headers = new String[]{"訂單編號","會員帳號","提貨人","到店日期","訂單金額"};
			columns = new String[]{"ORD_ID","MEM_ID","REC_NAME","CVS_ARR_DATE","ORD_AMT"};break;
		case 3:
			headers = new String[]{"訂購日期","訂單編號","發票號碼","訂單金額","發票抬頭","寄送地址","發票寄送日"};
			columns = new String[]{"ORD_DATE","ORD_ID","INVOICE_NO","ORD_AMT","INVOICE_TITLE","INVOICE_ADDR","INVOICE_DATE"};break;
		}
		try{
			if(getReportType()==2){
				ThesysOrderReportDAO.getInstance().export(out,headers,columns, getSiteId(), getReportType(), getReceiptFlag(), getStartDate(), getEndDate());
			}else{ //匯出Excel
				ThesysOrderReportDAO.getInstance().exportExcel(out,headers,columns, getSiteId(), getReportType(), getReceiptFlag(), getStartDate(), getEndDate());
								
			}
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
	}
	/**
	 * 取得報表(分頁)
	 * @return
	 */
	public List<ThesysOrderVO> getPageList(){
		List<ThesysOrderVO> result = new ArrayList<ThesysOrderVO>();
		try{
			result = ThesysOrderReportDAO.getInstance().listByPage(getSiteId(), getPageSize(), getPageIndex(),getReportType(), getReceiptFlag(), getStartDate(), getEndDate());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	/**
	 * 取得報表筆數
	 * @return
	 */
	public int getCount(){
		int result =0;
		try{
			result = ThesysOrderReportDAO.getInstance().count(getSiteId(),getReportType(), getReceiptFlag(), getStartDate(), getEndDate());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	

	public String getStartDate() {
		return startDate;
	}
	/**
	 * @return the reportType
	 */
	public int getReportType() {
		return reportType;
	}
	/**
	 * @param reportType the reportType to set
	 */
	public void setReportType(int reportType) {
		this.reportType = reportType;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	
	/**
	 * @return the checkedFlag
	 */
	public String getReceiptFlag() {
		return receiptFlag;
	}
	/**
	 * @param checkedFlag the checkedFlag to set
	 */
	public void setReceiptFlag(String receiptFlag) {
		this.receiptFlag = receiptFlag;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
