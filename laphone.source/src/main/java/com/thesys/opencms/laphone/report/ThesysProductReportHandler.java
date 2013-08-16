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
import com.thesys.opencms.laphone.report.dao.ThesysProductReportDAO;

public class ThesysProductReportHandler extends ThesysLaphoneHandler {
	protected static final Log LOG = CmsLog.getLog(ThesysOrderReportHandler.class);
	
	
	private int reportType = 1; //1:產品點擊數報表；2：產品加入追蹤排名報表；3：產品銷售排名報表
	private String categoryId;
	private String itemId;
	private String itemName;
	private int productType = 0; //0:全部；1:一般商品；2：組合商品
	private String startDate;
	private String endDate;
	
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
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}
	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	/**
	 * @return the categoryId
	 */
	public String getCategoryId() {
		return categoryId;
	}
	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the productType
	 */
	public int getProductType() {
		return productType;
	}
	/**
	 * @param productType the productType to set
	 */
	public void setProductType(int productType) {
		this.productType = productType;
	}
	public ThesysProductReportHandler(){}
	public ThesysProductReportHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super.init(context, req, res);
	}
	
	public void export(OutputStream out){
		String[] headers = {"產品料號","產品類別","產品名稱","點擊數","加入追蹤數","銷售數"};
		String[] columns = {};
		switch(getReportType()){
		case 1: //產品點擊數報表
			
			columns = new String[]{"ITEM_ID","CATE_ID","ITEM_NAME","CTR_COUNT","TRK_COUNT","SELL_COUNT"};break;
		case 2: //產品加入追蹤排名報表
			columns = new String[]{"ITEM_ID","CATE_ID","ITEM_NAME","CTR_COUNT","TRK_COUNT","SELL_COUNT"};break;
		case 3: //產品銷售排名報表
			columns = new String[]{"ITEM_ID","CATE_ID","ITEM_NAME","CTR_COUNT","TRK_COUNT","SELL_COUNT"};break;
		}
		try{
			ThesysProductReportDAO.getInstance().export(out,headers,columns, getSiteId(), getReportType(), getCategoryId(), getItemId(), getItemName(), getProductType(), getStartDate(), getEndDate());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
	}
	/**
	 * 取得報表(分頁)
	 * @return
	 */
	public List<String[]> getPageList(){
		List<String[]> result = new ArrayList<String[]>();
		try{
			result = ThesysProductReportDAO.getInstance().listByPage(getSiteId(), getPageSize(), getPageIndex(),getReportType(), getCategoryId(), getItemId(), getItemName(), getProductType(), getStartDate(), getEndDate());
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
			result = ThesysProductReportDAO.getInstance().count(getSiteId(),getReportType(), getCategoryId(), getItemId(), getItemName(), getProductType(), getStartDate(), getEndDate());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	
	
	
}
