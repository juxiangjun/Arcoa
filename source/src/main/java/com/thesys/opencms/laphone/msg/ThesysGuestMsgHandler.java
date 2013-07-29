package com.thesys.opencms.laphone.msg;


import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.msg.dao.ThesysGuestMsgDAO;
import com.thesys.opencms.laphone.msg.dao.ThesysGuestMsgVO;
import com.thesys.opencms.laphone.system.ThesysSerialHandler;


public class ThesysGuestMsgHandler extends ThesysLaphoneHandler {
	protected static final Log LOG = CmsLog.getLog(ThesysGuestMsgHandler.class);
	private java.util.Date now = new java.util.Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
//    private ThesysSerialHandler serialHandler = null;
    
	private String accountId ;
	private String cname ;
	private String email;
	private int msgType;
	private int msgId;
	private String startDate;
	private String endDate;
	private String isReply;
    private String keyword;
    private String phone;
    private String byField = "byDate";
    private String seq = "DESC";
    
    private Map<String ,Object> selMap = null; 
    
    
    
    
	
//	@Override
//	public String getSiteId() {
//		return super.getSiteId();
////		return "/sites/laphone";
//	}
	public ThesysGuestMsgHandler(){}
	public ThesysGuestMsgHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super(context, req, res);
//		serialHandler = new ThesysSerialHandler(context, req, res);
	}
	
	public List<ThesysGuestMsgVO> getPageList(){
		List<ThesysGuestMsgVO> result = new ArrayList<ThesysGuestMsgVO>();
		try{
			selMap = getAllMap();
			result = ThesysGuestMsgDAO.getInstance().listByPage(getSiteId(), getPageSize(), getPageIndex(),selMap);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}	
	public int getCount(){
		int result =0;
		try{
			selMap = getAllMap();
			result = ThesysGuestMsgDAO.getInstance().count(getSiteId(), selMap);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}	
	
	public ThesysGuestMsgVO getMsg(){
		ThesysGuestMsgVO vo = null;
		try {
			vo = ThesysGuestMsgDAO.getInstance().getItem(getSiteId(), msgId);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return vo;
	}
	 
	//回覆單號
	public int add(boolean memberFlag,String memberId,String cname,String phone,String email,int messageType,String message){
		int serialNo = ThesysSerialHandler.getNextTableSerialNo(getSiteId(),"LAPHONE_GUEST_MSG");
		try{																		
			//建立VO
			ThesysGuestMsgVO vo = new ThesysGuestMsgVO();
			vo.setSiteId(getSiteId());
			vo.setMemberFlag(memberFlag);
			vo.setMemberId(memberId);
			vo.setCname(cname);
			vo.setPhone(phone);
			vo.setEmail(email);
			vo.setMessageType(messageType);
			vo.setMessage(message);
			vo.setMessageDate(now);
			int i =ThesysGuestMsgDAO.getInstance().insert(serialNo,vo);
			if(i != 1) serialNo = 0;
		}catch(SQLException ex){
			LOG.error(ex, ex.fillInStackTrace());
			return 0;
		}
		return serialNo;
	}
	
	public boolean reply(ThesysGuestMsgVO vo){
		boolean res= false ;
		try{																		
			//建立VO
			vo.setReplyDate(now);
			vo.setReplyFlag(true);
			int i =ThesysGuestMsgDAO.getInstance().update(vo);
			if(i == 1) res = true;
		}catch(SQLException ex){
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}
		return res;
	}
	
	
	public boolean delete(int messageId){
		try{
			ThesysGuestMsgDAO.getInstance().delete(getSiteId(), getMemberId(), messageId);
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}
		return true;
	}
	
	
	public boolean deleteAll(){
		try{
			ThesysGuestMsgDAO.getInstance().deleteAll(getSiteId(), getMemberId());
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}
		return true;
	}
	
	
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getMsgId() {
		return msgId;
	}
	public void setMsgId(int msgId) {
		this.msgId = msgId;
	}
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
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
	public String getIsReply() {
		return isReply;
	}
	public void setIsReply(String isReply) {
		this.isReply = isReply;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getByField() {
		return byField;
	}
	public void setByField(String byField) {
		this.byField = byField;
	}
	public String getSeq() {
		return seq;
	}
	public void setSeq(String seq) {
		this.seq = seq;
	}
	private  Map<String, java.lang.Object> getAllMap() throws ParseException{
		if(byField.equals("byName")) byField = "CNAME";
		else if(byField.equals("byQu")) byField = "MSG_TXT";
		else if(byField.equals("byDate")) byField = "MSG_DATE";
		
		selMap = new TreeMap<String ,Object>();
		if(cname != null && cname.trim().length() !=0 )selMap.put("CNAME", cname);
		if(email != null && email.trim().length() !=0 )selMap.put("EMAIL", email);
		if(accountId != null && accountId.trim().length() !=0 )selMap.put("MEM_ID", accountId);
		if(msgId != 0 && String.valueOf(msgId).trim().length() !=0 )selMap.put("MSG_ID", msgId);
		if(msgType != 0 )selMap.put("MSG_TYPE", msgType);
		if(phone != null && phone.trim().length() !=0 )selMap.put("PHONE", phone);
		if(isReply != null && isReply.trim().length() !=0 )selMap.put("REPLY_FLAG", isReply);
		if(endDate != null && endDate.trim().length() !=0 )selMap.put("endDate",sdf.parse(endDate).getTime());
		if(keyword != null && keyword.trim().length() !=0 )selMap.put("keyword", keyword);
		if(startDate != null && startDate.trim().length() !=0 )selMap.put("startDate",sdf.parse(startDate).getTime());
		selMap.put("byField", byField);
		selMap.put("seq", seq);
		return selMap;
	}
	
}
