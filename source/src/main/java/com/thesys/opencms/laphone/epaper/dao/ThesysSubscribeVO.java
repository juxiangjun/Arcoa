package com.thesys.opencms.laphone.epaper.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.thesys.opencms.dao.ThesysAbstractVO;

public class ThesysSubscribeVO  extends ThesysAbstractVO{
	
	private String siteId;
	private String email;
	private String applySrc ;
	private java.util.Date applyTime;
	private boolean subscribeFlag ;
	private java.util.Date lastUpdateTime;
	
	public static ThesysSubscribeVO getInstance(ResultSet rs) throws SQLException{
		ThesysSubscribeVO vo = new ThesysSubscribeVO();
		vo.setSiteId(rs.getString("SITE_ID"));
		vo.setEmail(rs.getString("EMAIL"));
		vo.setApplySrc(rs.getString("APPLY_SOURCE"));
		vo.setApplyTime(convert(rs.getTimestamp("APPLY_TIME")));
		vo.setSubscribeFlag(rs.getString("SUBSCRIBE_FLAG").equals("Y"));
		vo.setLastUpdateTime(convert(rs.getTimestamp("LAST_UPDATE_TIME")));
		return vo;
	}
	
	public ThesysSubscribeVO(){}
	
	public ThesysSubscribeVO(String siteId, String email, String applySrc,
			Date applyTime, boolean subscribeFlag, Date lastUpdateTime) {
		super();
		this.siteId = siteId;
		this.email = email;
		this.applySrc = applySrc;
		this.applyTime = applyTime;
		this.subscribeFlag = subscribeFlag;
		this.lastUpdateTime = lastUpdateTime;
	}

	@Override
	public String toString() {
		return "ThesysSubscribeVO [siteId=" + siteId + ", email=" + email
				+ ", applySrc=" + applySrc + ", applyTime=" + applyTime
				+ ", subscribeFlag=" + subscribeFlag + ", lastUpdateTime="
				+ lastUpdateTime + "]";
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public java.util.Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(java.util.Date applyTime) {
		this.applyTime = applyTime;
	}

	public boolean isSubscribeFlag() {
		return subscribeFlag;
	}

	public void setSubscribeFlag(boolean subscribeFlag) {
		this.subscribeFlag = subscribeFlag;
	}

	public java.util.Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(java.util.Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getApplySrc() {
		return applySrc;
	}

	public void setApplySrc(String applySrc) {
		this.applySrc = applySrc;
	}
}
