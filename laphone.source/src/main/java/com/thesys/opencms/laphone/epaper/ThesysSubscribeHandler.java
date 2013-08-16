package com.thesys.opencms.laphone.epaper;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import au.com.bytecode.opencsv.CSVWriter;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.epaper.dao.ThesysSubscribeDAO;
import com.thesys.opencms.laphone.epaper.dao.ThesysSubscribeVO;

public class ThesysSubscribeHandler  extends ThesysLaphoneHandler {
	protected static final Log LOG = CmsLog.getLog(ThesysSubscribeHandler.class);
	
	private java.util.Date now = new java.util.Date(); //取得現在時間
	
	private String selStatus = "";
	private String email ;
	
	
	public ThesysSubscribeHandler(){}
	public ThesysSubscribeHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super(context, req, res);
	}
	
	public List<ThesysSubscribeVO> getPageList(){
		List<ThesysSubscribeVO> result = new ArrayList<ThesysSubscribeVO>();
		try{
			result = ThesysSubscribeDAO.getInstance().listByPage(getSiteId(), getPageSize(), getPageIndex(),selStatus);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}	
	
	public int getCount(){
		int result =0;
		try{
			result = ThesysSubscribeDAO.getInstance().count(getSiteId(),selStatus);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}	
	
	/**
	 * 訂閱
	 * @param String email 
	 * @param String src
	 * @return boolean  
	 */
	public boolean  subscribe(String email,String src){
		ThesysSubscribeVO vo = new ThesysSubscribeVO();
		vo.setSiteId(getSiteId());
		vo.setEmail(email);
		vo.setApplySrc(src);
		vo.setApplyTime(now);
		vo.setSubscribeFlag(true);
		int result = 0;
		try {
			result = ThesysSubscribeDAO.getInstance().add(vo);
		} catch (SQLException ex) {
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		}
		if(result == 1)return  true;
		return false;
	}
	
	

	public boolean chengStatus(ThesysSubscribeVO vo){
		vo.setLastUpdateTime(now);
		int result = 0;
		try {
			result = ThesysSubscribeDAO.getInstance().update(vo);
		} catch (SQLException ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		if(result == 0)return  false;
		return true;
	}

	public ThesysSubscribeVO getRow(){
		return getRow(email);
	}
	
	//find by email
	public ThesysSubscribeVO getRow(String email){
		ThesysSubscribeVO vo = null;
		try {
			if(email != null)
				vo = ThesysSubscribeDAO.getInstance().getRow(getSiteId(),email);
		} catch (SQLException ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return vo;
	}

   /**
	* 匯出報表
	* @param OutputStream os
	* @param selStatus 要查詢的狀態
	* 				"":全部  Y:訂閱 , N:未訂閱
	*/
	public void export(OutputStream os , String selStatus){
		try {
		 	Connection conn = ThesysSubscribeDAO.getInstance().getConnection();
			ResultSet rs = ThesysSubscribeDAO.getInstance().find(conn ,getSiteId(),selStatus);
			OutputStreamWriter writer = new OutputStreamWriter(os);
			char EOF = (char)0x00;
			CSVWriter csv = new CSVWriter(writer,',',EOF , "\r\n");
			csv.writeAll(rs , true);
			csv.flush(); 
			csv.close();
			rs.close(); 
			conn.close();
	 	} catch (Exception ex) {
	 		ex.printStackTrace();
	 		LOG.error(ex, ex.fillInStackTrace());
	 	}
	}
	
	public String getSelStatus() {
		return selStatus;
	}
	public void setSelStatus(String selStatus) {
		this.selStatus = selStatus;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

}
