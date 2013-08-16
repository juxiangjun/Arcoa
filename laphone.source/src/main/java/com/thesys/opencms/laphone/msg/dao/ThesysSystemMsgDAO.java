package com.thesys.opencms.laphone.msg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.thesys.opencms.laphone.ThesysLaphoneDAO;

public class ThesysSystemMsgDAO extends ThesysLaphoneDAO {

	 /** The singleton object. */
    private static ThesysSystemMsgDAO m_instance;
	
    private ThesysSystemMsgDAO() {
		super.init();
	}
	 
	/**
     * Singleton access.<p>
     * @return the singleton object
     */
    public static synchronized ThesysSystemMsgDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysSystemMsgDAO();
        }
        return m_instance;
    }
	
	public int insert(ThesysSystemMsgVO vo) throws SQLException{
		int res = 0;
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            con = getConnection(); 
            sql = "INSERT INTO LAPHONE_SYS_MSG(SITE_ID,MSG_ID,MEM_ID," +
            		"MSG_TYPE,GUEST_MSG_ID,SUBJECT,CONTENT,VIEW_FLAG," +
            		"VIEW_DATE,CRT_USR_ID,CRT_DATE" +
            		")VALUES(?,?,?,?,?,?,?,?,?,?,?)";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, vo.getSiteId());
            stmt.setInt(idx++,vo.getMessageId());
            stmt.setString(idx++, vo.getMemberId());
            stmt.setInt(idx++, vo.getMessageType());
            stmt.setInt(idx++, vo.getGuestMsgId());
            stmt.setString(idx++, vo.getSubject());
            stmt.setString(idx++, vo.getContent());
            stmt.setString(idx++, vo.isViewFlag()?"Y":"N");
            stmt.setTimestamp(idx++, convert(vo.getViewDate()));
            stmt.setString(idx++, vo.getCrtUserId());
            stmt.setTimestamp(idx++, convert(vo.getCrtDate()));
            res = stmt.executeUpdate();
          } finally {
            closeAll(con, stmt, rs);
        }
		return res;
	}
	
	public int update(ThesysSystemMsgVO vo) throws SQLException{
		int res = 0;
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            con = getConnection();            
            sql = "UPDATE LAPHONE_SYS_MSG SET SITE_ID=?,MEM_ID=?,MSG_TYPE=?,GUEST_MSG_ID=?," +
            		"SUBJECT=?,CONTENT=?,VIEW_FLAG=?,VIEW_DATE=?" +
            		" WHERE MSG_ID=?";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, vo.getSiteId());
            stmt.setString(idx++, vo.getMemberId());
            stmt.setInt(idx++, vo.getMessageType());
            stmt.setInt(idx++, vo.getGuestMsgId());
            stmt.setString(idx++, vo.getSubject());
            stmt.setString(idx++, vo.getContent());
            stmt.setString(idx++, vo.isViewFlag()?"Y":"N");
            stmt.setTimestamp(idx++, convert(vo.getViewDate()));
            
            stmt.setInt(idx++,vo.getMessageId());
    		res = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return res ;
	}

	public int delete(String siteId ,int messageId) throws SQLException{
		int res = 0;
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            con = getConnection();            
            sql = "DELETE FROM LAPHONE_SYS_MSG WHERE  SITE_ID=? AND MSG_ID=?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            stmt.setInt(2,messageId);
    		res = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return res ;
	}
	
	public List<ThesysSystemMsgVO> listByPage(String siteId,int pageSize,int pageIndex,int messageType,String memberId,java.util.Date dete) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
	    List<ThesysSystemMsgVO> result = new ArrayList<ThesysSystemMsgVO>();
        try {        	
            con = getConnection();
            String sql = " SELECT top "+pageSize+" * FROM LAPHONE_SYS_MSG where SITE_ID=? AND MSG_TYPE=? AND MEM_ID=? AND CRT_DATE>?"+
            			 " AND MSG_ID NOT IN ( SELECT top "+(pageSize*(pageIndex-1)) + " MSG_ID FROM LAPHONE_SYS_MSG WHERE  SITE_ID=? AND MSG_TYPE=? AND MEM_ID=? AND CRT_DATE>? ORDER BY CRT_DATE DESC )"+
            			 " ORDER BY CRT_DATE DESC";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            for(int i =0 ;i<2;i++){
            	stmt.setString(idx++, siteId);
            	stmt.setInt(idx++, messageType);
            	stmt.setString(idx++, memberId);
				stmt.setTimestamp(idx++, convert(dete));      
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
            	ThesysSystemMsgVO item = ThesysSystemMsgVO.getInstance(rs);
            	result.add(item);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }
	
	public int count(String siteId,int messageType,String memberId,java.util.Date dete) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int result = 0;
        try {
            con = getConnection();
            String sql = "SELECT COUNT(*) FROM LAPHONE_SYS_MSG where SITE_ID=? AND MSG_TYPE=? AND MEM_ID=? AND CRT_DATE>? ";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setInt(idx++, messageType);
            stmt.setString(idx++, memberId);
            stmt.setTimestamp(idx++, convert(dete));
            rs = stmt.executeQuery();
            if (rs.next()) {
            	result = rs.getInt(1);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
	}
	
	/**
	 * 取得未讀信件數
	 * @param siteId 站點
	 * @param msgType 信件種類
	 * @param memberId 會員Id
	 * @param date 日期之後
	 * @return 未讀信件數
	 * @throws SQLException
	 */
	public int getNotReadCount(String siteId,int msgType,String memberId ,java.util.Date date) throws SQLException{
		int count =0 ;
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "SELECT COUNT(*) FROM LAPHONE_SYS_MSG where SITE_ID=? AND MSG_TYPE=? AND MEM_ID=? AND CRT_DATE>? AND VIEW_FLAG='N'";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setInt(idx++, msgType);
            stmt.setString(idx++, memberId);
            stmt.setTimestamp(idx++, convert(date));
            rs = stmt.executeQuery();
            if (rs.next()) {
            	count = rs.getInt(1);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
		return count;
	}
	
	public ThesysSystemMsgVO getItem(String siteId,int messageId) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysSystemMsgVO item = null;
        try {        	
            con = getConnection();
            String sql = " SELECT * FROM LAPHONE_SYS_MSG WHERE SITE_ID=? AND MSG_ID=? ";
            stmt = con.prepareStatement(sql);
            int idx = 1;
           	stmt.setString(idx++, siteId);
           	stmt.setInt(idx++, messageId);      
           	rs = stmt.executeQuery();
            if (rs.next()) {
            	item = ThesysSystemMsgVO.getInstance(rs);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return item;
    }
	
	/**
	 * 取依照客服訊息編號取得這一筆系統訊息
	 * @param siteId
	 * @param guestMId
	 * @return
	 * @throws SQLException
	 */
	public ThesysSystemMsgVO getItemByGuestMsgId(String siteId,int guestMId) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysSystemMsgVO item = null;
        try {        	
            con = getConnection();
            String sql = " SELECT * FROM LAPHONE_SYS_MSG WHERE SITE_ID=? AND GUEST_MSG_ID=? ";
            stmt = con.prepareStatement(sql);
            int idx = 1;
           	stmt.setString(idx++, siteId);
           	stmt.setInt(idx++, guestMId);      
           	rs = stmt.executeQuery();
            if (rs.next()) {
            	item = ThesysSystemMsgVO.getInstance(rs);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return item;
    }
	
	/**
	 * 取得前後一筆msgId
	 * @param near "pre" 上一筆 "next" 下一筆
	 * @param siteId 站點
	 * @param messageType 訊息種類
	 * @param memberId 會員ID
	 * @param messageId 訊息ID
	 * @param dete 日期之後
	 * @return
	 * @throws SQLException
	 */
	public int getNear(String near,String siteId,int messageType,String memberId,int messageId,java.util.Date dete) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = null;
        int result = 0;
        try {
            con = getConnection();
            if(near.equals("pre"))
            	sql = "SELECT TOP 1 MSG_ID FROM LAPHONE_SYS_MSG where SITE_ID=? AND CRT_DATE>? AND MSG_TYPE=? AND MEM_ID=? AND MSG_ID<?  ORDER BY MSG_ID DESC";
            if(near.equals("next"))
            	sql = "SELECT TOP 1 MSG_ID FROM LAPHONE_SYS_MSG where SITE_ID=? AND CRT_DATE>? AND MSG_TYPE=? AND MEM_ID=? AND MSG_ID>?  ORDER BY MSG_ID ASC";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setTimestamp(idx++, convert(dete));
            stmt.setInt(idx++, messageType);
            stmt.setString(idx++, memberId);
            stmt.setInt(idx++, messageId);
            rs = stmt.executeQuery();
            if (rs.next()) {
            	result = rs.getInt(1);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
	}
	
	
	
	//@Override
	protected String getDBTableName() {
		return "LAPHONE_SYS_MSG";
	}
	
	
}
