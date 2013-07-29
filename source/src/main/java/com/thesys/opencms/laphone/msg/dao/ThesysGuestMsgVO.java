package com.thesys.opencms.laphone.msg.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.thesys.opencms.dao.ThesysAbstractVO;

/**
 * 
 * @author maggie
 *
 */
public class ThesysGuestMsgVO  extends ThesysAbstractVO{	

	private String siteId;
	private int messageId;
	private boolean memberFlag = false;
	private String memberId;
	private String cname;
	private String phone;
	private String email;
	
	private int messageType;
	private String message;	
	private Date messageDate;
	
	private boolean replyFlag = false;
	
	private String replyContent;	
	private String replier;
	private Date replyDate;
	

	public ThesysGuestMsgVO(){
		super();

	}
	
	public static ThesysGuestMsgVO getInstance(ResultSet rs) throws SQLException{
		ThesysGuestMsgVO result = new ThesysGuestMsgVO();
		//讀取單筆ROW
		result.setSiteId(rs.getString("SITE_ID"));
		result.setMessageId(rs.getInt("MSG_ID"));
		result.setMemberFlag(rs.getString("MEM_FLAG").equals("Y"));
		result.setMemberId(rs.getString("MEM_ID"));
		result.setCname(rs.getString("CNAME"));
		result.setPhone(rs.getString("PHONE"));
		result.setEmail(rs.getString("EMAIL"));
		result.setMessageType(rs.getInt("MSG_TYPE"));
		result.setMessage(rs.getString("MSG_TXT"));
		result.setMessageDate(convert(rs.getTimestamp("MSG_DATE")));
		result.setReplyFlag(rs.getString("REPLY_FLAG").equals("Y"));
		result.setReplyContent(rs.getString("REPLY_TXT"));
		result.setReplier(rs.getString("REPLY_USR_ID"));
		result.setReplyDate(convert(rs.getTimestamp("REPLY_DATE")));
		return result;
	}
	
	/**
	 * @return the siteId
	 */
	public String getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the messageId
	 */
	public int getMessageId() {
		return messageId;
	}
	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}
	/**
	 * @return the memberFlag
	 */
	public boolean isMemberFlag() {
		return memberFlag;
	}
	/**
	 * @param memberFlag the memberFlag to set
	 */
	public void setMemberFlag(boolean memberFlag) {
		this.memberFlag = memberFlag;
	}
	/**
	 * @return the memberId
	 */
	public String getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	/**
	 * @return the cname
	 */
	public String getCname() {
		return cname;
	}
	/**
	 * @param cname the cname to set
	 */
	public void setCname(String cname) {
		this.cname = cname;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/**
	 * @return the messageType
	 */
	public int getMessageType() {
		return messageType;
	}
	/**
	 * @param messageType the messageType to set
	 */
	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}
	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	/**
	 * @return the messageDate
	 */
	public Date getMessageDate() {
		return messageDate;
	}
	/**
	 * @param messageDate the messageDate to set
	 */
	public void setMessageDate(Date messageDate) {
		this.messageDate = messageDate;
	}
	/**
	 * @return the replyFlag
	 */
	public boolean isReplyFlag() {
		return replyFlag;
	}
	/**
	 * @param replyFlag the replyFlag to set
	 */
	public void setReplyFlag(boolean replyFlag) {
		this.replyFlag = replyFlag;
	}
	/**
	 * @return the replyContent
	 */
	public String getReplyContent() {
		return replyContent;
	}
	/**
	 * @param replyContent the replyContent to set
	 */
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	/**
	 * @return the replier
	 */
	public String getReplier() {
		return replier;
	}
	/**
	 * @param replier the replier to set
	 */
	public void setReplier(String replier) {
		this.replier = replier;
	}
	/**
	 * @return the replyDate
	 */
	public Date getReplyDate() {
		return replyDate;
	}
	/**
	 * @param replyDate the replyDate to set
	 */
	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}

	
	@Override
	public String toString() {
		return "ThesysGuestMsgVO [siteId=" + siteId + ", messageId="
				+ messageId + ", memberFlag=" + memberFlag + ", memberId="
				+ memberId + ", cname=" + cname + ", phone=" + phone
				+ ", email=" + email + ", messageType=" + messageType
				+ ", message=" + message + ", messageDate=" + messageDate
				+ ", replyFlag=" + replyFlag + ", replyContent=" + replyContent
				+ ", replier=" + replier + ", replyDate=" + replyDate + "]";
	}
	
	
	
}
	