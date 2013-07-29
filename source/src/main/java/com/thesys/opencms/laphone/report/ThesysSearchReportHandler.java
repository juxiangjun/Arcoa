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
import com.thesys.opencms.laphone.report.dao.ThesysSearchReportDAO;

public class ThesysSearchReportHandler extends ThesysLaphoneHandler {
	protected static final Log LOG = CmsLog.getLog(ThesysOrderReportHandler.class);
	
	
	private String startDate;
	private String endDate;
	
	public ThesysSearchReportHandler(){}
	public ThesysSearchReportHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super.init(context, req, res);
	}
	
	/**
	 * 匯出日期內關鍵字搜尋報表
	 * @return
	 */
	public void export(OutputStream out){
		String[] headers = new String[]{"關鍵字","查詢日期"};
		String[] columns = new String[]{"KEYWORD","CRT_DATE"};
		try{
			ThesysSearchReportDAO.getInstance().export(out,headers,columns, getSiteId(),  getStartDate(), getEndDate());
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		}
	}
	
	/**
	 * 取得日期內關鍵字搜尋報表(分頁)
	 * @return
	 */
	public List<String[]> getPageList(){
		List<String[]> result = new ArrayList<String[]>();
		try {
			result = ThesysSearchReportDAO.getInstance().listByPage(getSiteId(), getPageSize(), getPageIndex(), getStartDate(),getEndDate());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result ;
	}
	
	/**
	 * 取得日期內關鍵字搜尋數
	 * @return
	 */
	public int getCount(){
		int result =0;
		try{
			result = ThesysSearchReportDAO.getInstance().count(getSiteId(),getStartDate(),getEndDate());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	
	public String getStartDate() {
		return startDate;
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
	
	
}
