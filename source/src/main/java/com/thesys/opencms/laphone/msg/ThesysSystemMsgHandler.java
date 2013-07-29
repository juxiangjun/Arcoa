package com.thesys.opencms.laphone.msg;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.msg.dao.ThesysSystemMsgDAO;
import com.thesys.opencms.laphone.msg.dao.ThesysSystemMsgVO;
import com.thesys.opencms.laphone.system.ThesysSerialHandler;

public class ThesysSystemMsgHandler extends ThesysLaphoneHandler {
	protected static final Log LOG = CmsLog.getLog(ThesysSystemMsgHandler.class);
	/**系統時間**/
	private java.util.Date now = new java.util.Date();
	/**流水號**/
//	private ThesysSerialHandler serialHandler = null;
	
	private int messageId;
	private int messageType;
	private String memberId;
	private java.util.Date  beforedate =getBefLimit(3); //預設取得3個月前的日期

	public ThesysSystemMsgHandler(){}
	
	public ThesysSystemMsgHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super(context, req, res);
//		serialHandler = new ThesysSerialHandler(context, req, res);
	}

	
	/**
	 * 取得分頁LIST
	 * @return
	 */
	public List<ThesysSystemMsgVO> getPageList(){
		List<ThesysSystemMsgVO> result = new ArrayList<ThesysSystemMsgVO>();
		try{
			result = ThesysSystemMsgDAO.getInstance().listByPage(getSiteId(), getPageSize(), getPageIndex(), messageType, memberId, beforedate);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}	
	
	/**
	 * 取得總筆數
	 * @return
	 */
	public int getCount(){
		int res=0;
		try {
			res = ThesysSystemMsgDAO.getInstance().count(getSiteId(), messageType, memberId,beforedate);
		} catch (SQLException ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return res;
	}
	
	/**
	 * 取的未讀信件數
	 * @return
	 */
	public int getNotReadCount(){
		return getNotReadCount(messageType, memberId);
	}
	
	/**
	 * 取的未讀信件數
	 * @param messageType
	 * @param memberId
	 * @return
	 */
	public int getNotReadCount(int messageType , String memberId){
		int res=0;
		try {
			res = ThesysSystemMsgDAO.getInstance().getNotReadCount(getSiteId(), messageType, memberId,beforedate);
		} catch (SQLException ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return res;
	}
	
	/**
	 * 取得此會員所有未讀信件數
	 * @return
	 */
	public int getAllNotReadCount(){
		return getAllNotReadCount(memberId);
	}
	
	/**
	 * 取得此會員所有未讀信件數
	 * @param memberId 
	 * @return
	 */
	public int getAllNotReadCount(String memberId){
		return getNotReadCount(1, memberId) + getNotReadCount(2, memberId);
	}
	
	
	/**
	 * 取得這一筆資料
	 * @return
	 */
	public ThesysSystemMsgVO getSelectedMessage(){
		return getMsg(messageId);
	}
	
	/**
	 * 取得這一筆資料
	 * @param messageId
	 * @return
	 */
	public ThesysSystemMsgVO getMsg(int messageId){
		ThesysSystemMsgVO vo = null;
		try {
			vo = ThesysSystemMsgDAO.getInstance().getItem(getSiteId(), messageId);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return vo;
	}
	
	/**
	 * 取依照客服訊息編號取得這一筆系統訊息
	 * @param guestMId
	 * @return
	 */
	public ThesysSystemMsgVO getMsgByGuestMsgId(int guestMsgId){
		ThesysSystemMsgVO vo = null;
		try {
			vo = ThesysSystemMsgDAO.getInstance().getItemByGuestMsgId(getSiteId(), guestMsgId);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return vo;
	}
	
	/**
	 * 取得上一筆ID
	 * @return
	 */
	public int getPrevious(){
		int preMsgId=0;
		String near = "pre";
		try {
			ThesysSystemMsgVO vo = getSelectedMessage();
			if(vo != null){
				preMsgId = ThesysSystemMsgDAO.getInstance().getNear(near,getSiteId(), messageType, vo.getMemberId(), messageId, beforedate);
			}
		} catch (SQLException ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return preMsgId;							
	}
	
	/**
	 * 取得下一筆ID
	 * @return
	 */
	public int getNext(){
		int preMsgId=0;
		String near = "next";
		try {
			ThesysSystemMsgVO vo = getSelectedMessage();
			if(vo != null){
				preMsgId = ThesysSystemMsgDAO.getInstance().getNear(near,getSiteId(), messageType, vo.getMemberId(), messageId, beforedate);
			}
		} catch (SQLException ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return preMsgId;							
	}
	
	/**
	 * 新增信件
	 * @param memberId
	 * @param messageType
	 * @param guestMsgId  如果不是客服回覆則不用帶
	 * @param subject
	 * @param content	如果是客服回覆即不用帶
	 * @param crtUserId
	 * @return
	 */
	public boolean add(String memberId,int messageType,int guestMsgId,String subject,String content,String crtUserId){
//		int serialNo = serialHandler. getNextTableSerialNo("LAPHONE_SYS_MSG");
////		int serialNo = Integer.parseInt((String.valueOf(now.getTime())).substring(4));
//		String siteId = getSiteId();
//		boolean res = false;
//		try {
//			ThesysSystemMsgVO vo = new ThesysSystemMsgVO();
//			vo.setSiteId(siteId);
//			vo.setMessageId(serialNo);
//			vo.setMemberId(memberId);
//			vo.setMessageType(messageType);
//			vo.setGuestMsgId(guestMsgId);
//			vo.setSubject(subject);
//			vo.setContent(content);
//			vo.setCrtUserId(crtUserId);
//			vo.setCrtDate(now);
//			int i =ThesysSystemMsgDAO.getInstance().insert(vo);
//			if(i == 1) res = true;
//		} catch (SQLException ex) {
//			LOG.error(ex, ex.fillInStackTrace());
//			return false;
//		}
		return add(getSiteId(), memberId, messageType, guestMsgId, subject, content, crtUserId);
	}
	/**
	 * 新增信件
	 * @param memberId
	 * @param messageType
	 * @param guestMsgId  如果不是客服回覆則不用帶
	 * @param subject
	 * @param content	如果是客服回覆即不用帶
	 * @param crtUserId
	 * @return
	 */
	public static boolean add(String siteId,String memberId,int messageType,int guestMsgId,String subject,String content,String crtUserId){
		int serialNo = ThesysSerialHandler.getNextTableSerialNo(siteId,"LAPHONE_SYS_MSG");
//		int serialNo = Integer.parseInt((String.valueOf(now.getTime())).substring(4));
//		String siteId = getSiteId();
		boolean res = false;
		try {
			ThesysSystemMsgVO vo = new ThesysSystemMsgVO();
			vo.setSiteId(siteId);
			vo.setMessageId(serialNo);
			vo.setMemberId(memberId);
			vo.setMessageType(messageType);
			vo.setGuestMsgId(guestMsgId);
			vo.setSubject(subject);
			vo.setContent(content);
			vo.setCrtUserId(crtUserId);
			vo.setCrtDate(new java.util.Date());
			int i =ThesysSystemMsgDAO.getInstance().insert(vo);
			if(i == 1) res = true;
		} catch (SQLException ex) {
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}
		return res;
	}
	
	/**
	 * 將未讀信件轉已讀
	 * @return
	 */
	public boolean view(){
		return view(messageId) ;
	}
	
	/**
	 * 將未讀信件轉已讀
	 * @param messageId
	 * @return
	 */
	public boolean view(int messageId){
		boolean res = false;
		try {
			ThesysSystemMsgVO vo = getMsg(messageId);
			if(vo != null){
				vo.setViewFlag(true);
				vo.setViewDate(now);
				int i =ThesysSystemMsgDAO.getInstance().update(vo);
				if(i == 1) res = true;
			}
		} catch (SQLException ex) {
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}
		return res ;
	}
	
	
	/**
	 * 將已讀信件轉未讀
	 * @return
	 */
	public boolean unView(){
		return unView(messageId);
	}
	
	/**
	 * 將已讀信件轉未讀
	 * @param messageId
	 * @return
	 */
	public boolean unView(int messageId){
		boolean res = false;
		try {
			ThesysSystemMsgVO vo = getMsg(messageId);
			if(vo != null){
				vo.setViewFlag(false);
				vo.setViewDate(null);
				int i =ThesysSystemMsgDAO.getInstance().update(vo);
				if(i == 1) res = true;
			}
		} catch (SQLException ex) {
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}
		return res ;
	}

	
	/**
	 *刪除
	 * @return
	 */
	public int delete(){
		return delete(messageId);
	}
	
	/**
	 * 刪除
	 * @param messageId
	 * @return
	 */
	public int delete(int messageId){
		int res = 0;
		try {
			ThesysSystemMsgVO vo = getMsg(messageId);
			if(vo != null){
				res =ThesysSystemMsgDAO.getInstance().delete(getSiteId(), messageId);
			}
		} catch (SQLException ex) {
			LOG.error(ex, ex.fillInStackTrace());
			return 0;
		}
		return res;
	}

		
	/**
	 * 取得m個月前的日期
	 * @param m
	 * @return
	 */
	public java.util.Date getBefLimit(int m){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -m);
		return cal.getTime();
	}
	

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}


	public java.util.Date getBeforedate() {
		return beforedate;
	}

	public void setBeforedate(java.util.Date beforedate) {
		this.beforedate = beforedate;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}


	
	
}
