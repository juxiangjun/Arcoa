package com.thesys.opencms.laphone.report.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.thesys.opencms.dao.ThesysAbstractVO;

public class ThesysLoginReportVO extends ThesysAbstractVO {

	private String siteId ;
	private String userId ;
	private Date loginDate ;
	private Date logoutDate ;
	private String loginIp ;
	private String sessionId ;
	
	public ThesysLoginReportVO(){}
	
	public ThesysLoginReportVO(String siteId, String userId, Date loginDate,
			Date logoutDate, String loginIp, String sessionId) {
		super();
		this.siteId = siteId;
		this.userId = userId;
		this.loginDate = loginDate;
		this.logoutDate = logoutDate;
		this.loginIp = loginIp;
		this.sessionId = sessionId;
	}

	public static ThesysLoginReportVO getInstence(ResultSet rs) throws SQLException{
		ThesysLoginReportVO vo = new ThesysLoginReportVO();
		vo.setSiteId(rs.getString("SITE_ID"));
		vo.setUserId(rs.getString("USERID"));
		vo.setLoginDate(rs.getTimestamp("LOGIN_DATE"));
		vo.setLogoutDate(rs.getTimestamp("LOGOUT_DATE"));
		vo.setLoginIp(rs.getString("LOGIN_IP"));
		vo.setSessionId(rs.getString("SESS_ID"));
		return vo ; 
	}
	
	@Override
	public String toString() {
		return "ThesysLoginReportVO [siteId=" + siteId + ", userId=" + userId
				+ ", loginDate=" + loginDate + ", logoutDate=" + logoutDate
				+ ", loginIp=" + loginIp + ", sessionId=" + sessionId + "]";
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getLoginDate() {
		return loginDate;
	}
	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}
	public Date getLogoutDate() {
		return logoutDate;
	}
	public void setLogoutDate(Date logoutDate) {
		this.logoutDate = logoutDate;
	}
	public String getLoginIp() {
		return loginIp;
	}
	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
}
