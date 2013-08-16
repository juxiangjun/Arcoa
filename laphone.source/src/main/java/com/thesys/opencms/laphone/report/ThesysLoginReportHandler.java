package com.thesys.opencms.laphone.report;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.report.dao.ThesysLoginReportDAO;
import com.thesys.opencms.laphone.report.dao.ThesysLoginReportVO;

public class ThesysLoginReportHandler extends ThesysLaphoneHandler{
	
	protected static final Log LOG = CmsLog.getLog(ThesysLoginReportHandler.class);
	private String searchUserId ;
	private String startDate;
	private String endDate;
	
	public ThesysLoginReportHandler(){
	}
	public ThesysLoginReportHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception {
		super(context, req, res);
	}
	
	/**
	 * 匯出日期內後台操作時間統計報表
	 * @return
	 */
	public void export(OutputStream out){
		String[] headers = new String[]{"登入IP","帳號","登入時間","登出時間"};
		String[] columns = new String[]{"LOGIN_IP","USERID","LOGIN_DATE","LOGOUT_DATE"};
		try{
			ThesysLoginReportDAO.getInstance().export(out,headers,columns,getSiteId(),getSearchUserId(),  getStartDate(), getEndDate());
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		}
	}
	
	/**
	 * 取得日期內後台操作時間統計報表(分頁)
	 * @return
	 */
	public List<ThesysLoginReportVO> getPageList(){
		List<ThesysLoginReportVO> result = new ArrayList<ThesysLoginReportVO>();
		try {														
			result = ThesysLoginReportDAO.getInstance().listByPage(getSiteId(), getPageSize(), getPageIndex(),getSearchUserId(), getStartDate(),getEndDate());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result ;
	}

	/**
	 *取得日期內後台操作時間統計數
	 * @return
	 */
	public int getCount(){
		int result =0;
		try{
			result = ThesysLoginReportDAO.getInstance().count(getSiteId(),getSearchUserId(),getStartDate(),getEndDate());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
//	
	public int loginLog(String loginIp,String sessionId){
		int r = 0 ;
		try{
				ThesysLoginReportVO vo = new ThesysLoginReportVO();
				vo.setSiteId(getSiteId());
				vo.setUserId(getUserId());
				vo.setLoginDate(new Date());
				vo.setLoginIp(loginIp);
				vo.setSessionId(sessionId);
				r = ThesysLoginReportDAO.getInstance().login(vo);
		}catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return r ;
	}
	public int logoutLog(String sessionId){
		ThesysLoginReportVO vo = new ThesysLoginReportVO(); 
		int r = 0 ;
		try {
			vo.setSiteId(getSiteId());
			vo.setUserId(getUserId());
			vo.setSessionId(sessionId);
			vo.setLogoutDate(new Date());
			r = ThesysLoginReportDAO.getInstance().logout(vo);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return r;
	}

	public String getSearchUserId() {
		return searchUserId;
	}

	public void setSearchUserId(String searchUserId) {
		this.searchUserId = searchUserId;
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
