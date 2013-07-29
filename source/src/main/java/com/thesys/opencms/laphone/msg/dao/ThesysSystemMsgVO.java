package com.thesys.opencms.laphone.msg.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.thesys.opencms.dao.ThesysAbstractVO;

public class ThesysSystemMsgVO  extends ThesysAbstractVO{
	 

	private String siteId;
	private int messageId;
	private String memberId;
	private int messageType;
	private int guestMsgId;
	private String subject;
	private String content;
	private boolean viewFlag = false;
	private java.util.Date viewDate;
	private String crtUserId ;
	private java.util.Date crtDate;
	
	public ThesysSystemMsgVO(){
		super();
	}
	

	public static ThesysSystemMsgVO getInstance(ResultSet rs) throws SQLException{
		ThesysSystemMsgVO result = new ThesysSystemMsgVO();
    	//讀取單筆ROW
		result.setSiteId(rs.getString("SITE_ID"));
		result.setMessageId(rs.getInt("MSG_ID"));
		result.setMemberId(rs.getString("MEM_ID"));
		result.setMessageType(rs.getInt("MSG_TYPE"));
		result.setGuestMsgId(rs.getInt("GUEST_MSG_ID"));
		result.setSubject(rs.getString("SUBJECT"));
		result.setContent(rs.getString("CONTENT"));
		result.setViewFlag(rs.getString("VIEW_FLAG").equals("Y"));
		result.setViewDate(convert(rs.getTimestamp("VIEW_DATE")));
		result.setCrtUserId(rs.getString("CRT_USR_ID"));
		result.setCrtDate(convert(rs.getTimestamp("CRT_DATE")));
 	    return result;
	}
	
	
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public int getMessageId() {
		return messageId;
	}
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public int getMessageType() {
		return messageType;
	}
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	public int getGuestMsgId() {
		return guestMsgId;
	}


	public void setGuestMsgId(int guestMsgId) {
		this.guestMsgId = guestMsgId;
	}


	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public boolean isViewFlag() {
		return viewFlag;
	}
	public void setViewFlag(boolean viewFlag) {
		this.viewFlag = viewFlag;
	}
	public java.util.Date getViewDate() {
		return viewDate;
	}
	public void setViewDate(java.util.Date viewDate) {
		this.viewDate = viewDate;
	}
	public String getCrtUserId() {
		return crtUserId;
	}
	public void setCrtUserId(String crtUserId) {
		this.crtUserId = crtUserId;
	}
	public java.util.Date getCrtDate() {
		return crtDate;
	}
	public void setCrtDate(java.util.Date crtDate) {
		this.crtDate = crtDate;
	}


	@Override
	public String toString() {
		return "ThesysSystemMsgVO [siteId=" + siteId + ", messageId="
				+ messageId + ", memberId=" + memberId + ", messageType="
				+ messageType + ", guestMsgId=" + guestMsgId + ", subject="
				+ subject + ", content=" + content + ", viewFlag=" + viewFlag
				+ ", viewDate=" + viewDate + ", crtUserId=" + crtUserId
				+ ", crtDate=" + crtDate + "]";
	}

	
	
	
}
