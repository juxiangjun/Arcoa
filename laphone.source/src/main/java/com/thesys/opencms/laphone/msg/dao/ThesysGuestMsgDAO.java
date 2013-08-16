/**
 * 
 */
package com.thesys.opencms.laphone.msg.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;


import com.thesys.opencms.laphone.ThesysLaphoneDAO;
/**
 * @author maggie
 *
 */
public class ThesysGuestMsgDAO extends ThesysLaphoneDAO {
	 /** The singleton object. */
    private static ThesysGuestMsgDAO m_instance;

	private ThesysGuestMsgDAO() {
		super.init();
	}
	
	/**
     * Singleton access.<p>
     * @return the singleton object
     */
    public static synchronized ThesysGuestMsgDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysGuestMsgDAO();
        }
        return m_instance;
    }

    public int count(String siteId, Map<String,Object> selMap) throws SQLException{
    
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String where = getWhere(selMap);  //將map拼成where條件
        int result = 0;
        try {
            con = getConnection();
            String sql = "SELECT count(*) FROM LAPHONE_GUEST_MSG where SITE_ID=?"+where;
            stmt = con.prepareStatement(sql);
            stmt = getListStmt(1,siteId, selMap, stmt); //將條件MAP組成PreparedStatement
            rs = stmt.executeQuery();
            if (rs.next()) {
            	result = rs.getInt(1);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }
    
    
    public List<ThesysGuestMsgVO> listByPage(String siteId,int pageSize,int pageIndex, Map selMap) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String where = getWhere(selMap);  //將map拼成where條件
	    List<ThesysGuestMsgVO> result = new ArrayList<ThesysGuestMsgVO>();
        try {        	
            con = getConnection();
            String sql = " SELECT top "+pageSize+" * FROM LAPHONE_GUEST_MSG where SITE_ID=? "+where+
            			 " AND MSG_ID NOT IN ( SELECT top "+(pageSize*(pageIndex-1)) + " MSG_ID FROM LAPHONE_GUEST_MSG WHERE SITE_ID=? "+where+"ORDER BY "+selMap.get("byField")+" "+selMap.get("seq")+" )"+
            			 " ORDER BY "+selMap.get("byField")+" "+selMap.get("seq");
            stmt = con.prepareStatement(sql);
            stmt = getListStmt(2,siteId, selMap, stmt);  //將條件MAP組成PreparedStatement 2代表要轉兩次
            rs = stmt.executeQuery();
            while (rs.next()) {
            	ThesysGuestMsgVO item = ThesysGuestMsgVO.getInstance(rs);
            	result.add(item);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }
    public void delete(String siteId,String userId,int messageId) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        try {
            con = getConnection();
            String sql = "DELETE FROM LAPHONE_GUEST_MSG where SITE_ID=? and MEM_ID=? and MSG_ID = ? ";
            stmt = con.prepareStatement(sql);  
            
            stmt.setString(1, siteId);
            stmt.setString(2, userId);
            stmt.setInt(3, messageId);
            stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, null);
        }
    	
    }
    
    public ThesysGuestMsgVO getItem(String siteId,int msgId) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysGuestMsgVO item = null;
        try {        	
            con = getConnection();
            String sql = " SELECT  * FROM LAPHONE_GUEST_MSG where SITE_ID=?  AND MSG_ID=?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            stmt.setInt(2, msgId);
            rs = stmt.executeQuery();
            if (rs.next()) {
            	item = ThesysGuestMsgVO.getInstance(rs);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return item;
    }
    
    public void deleteAll(String siteId,String memberId) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        try {
            
            con = getConnection();
            String sql = "DELETE FROM LAPHONE_GUEST_MSG where SITE_ID=? and MEM_ID=? ";
            stmt = con.prepareStatement(sql);           
            stmt.setString(1, siteId);
            stmt.setString(2, memberId);
            stmt.executeUpdate();

            
        } finally {
            closeAll(con, stmt, null);
        }
    	
    }
    public int insert(int msgId ,ThesysGuestMsgVO vo) throws SQLException {

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int i = 0;
        try {
            con = getConnection(); 
            sql = "INSERT INTO LAPHONE_GUEST_MSG(MSG_ID,SITE_ID,MEM_FLAG,MEM_ID," +
            		"CNAME,PHONE,EMAIL,MSG_TYPE,MSG_TXT,MSG_DATE,REPLY_FLAG,REPLY_TXT," +
            		"REPLY_USR_ID,REPLY_DATE)VALUES(?,?,?,?,?,?,?,?,?,?," +
            		"?,?,?,?)";
            stmt = con.prepareStatement(sql);
            //取得訊息流水號
            int idx = 1;
            stmt.setInt(idx++,msgId);
            //寫入一筆資料
            voToStmt(idx,stmt, vo);
    		i = stmt.executeUpdate();
          } finally {
            closeAll(con, stmt, rs);
        }
        return i ;
    }

    public int update(ThesysGuestMsgVO vo) throws SQLException{
    	 Connection con = null;
         PreparedStatement stmt = null;
         ResultSet rs = null;
         String sql = "";
         int i = 0;
         try {
             con = getConnection();
             sql = "UPDATE LAPHONE_GUEST_MSG SET SITE_ID=?,MEM_FLAG=?,MEM_ID=?," +
             		"CNAME=?,PHONE=?,EMAIL=?,MSG_TYPE=?,MSG_TXT=?,MSG_DATE=?,REPLY_FLAG=?,REPLY_TXT=?," +
             		"REPLY_USR_ID=?,REPLY_DATE=? " +
             		"WHERE MSG_ID=?";
             stmt = con.prepareStatement(sql);
             int idx = 1;
             idx = voToStmt(idx,stmt, vo);
             stmt.setInt(idx++,vo.getMessageId());
     		 i = stmt.executeUpdate();
         } finally {
             closeAll(con, stmt, rs);
         }
         return i ;
    }
    
	/* (non-Javadoc)
	 * @see com.thesys.opencms.dao.ThesysAbstractDAO#getDBTableName()
	 */
	@Override
	protected String getDBTableName() {
		return "LAPHONE_GUEST_MSG";
	}

	
	
	/**
	 * 將搜尋條件MAP拼成WHERE的條件
	 * @param selMap
	 * @return
	 */
	private String getWhere(Map<String,Object> selMap){
        String where = "";
		Set<String> s = selMap.keySet();
        Iterator<String> it =s.iterator();
        while (it.hasNext()) {
			String st = it.next();
					if(st.equals("byField") || st.equals("seq")){}
					else if(st.equals("keyword"))where +="and (MSG_TXT like ? or REPLY_TXT like ? )"; //兩個欄位模糊搜尋
					else if(st.equals("endDate"))where +="and MSG_DATE<? ";
					else if(st.equals("startDate"))where +="and MSG_DATE>? ";
					else where +="and "+st+"=? ";  //剩下的String 
        }
        return where;
	}
	
	
	private int voToStmt(int idx ,PreparedStatement stmt ,ThesysGuestMsgVO vo) throws SQLException{
        stmt.setString(idx++,vo.getSiteId());
        stmt.setString(idx++,vo.isMemberFlag()?"Y":"N");
		stmt.setString(idx++,vo.getMemberId());
		stmt.setString(idx++,vo.getCname());
		stmt.setString(idx++,vo.getPhone());
		stmt.setString(idx++,vo.getEmail());
		stmt.setInt(idx++,vo.getMessageType());
		stmt.setString(idx++,vo.getMessage());
		stmt.setTimestamp(idx++,convert(vo.getMessageDate()));
		stmt.setString(idx++,vo.isReplyFlag()?"Y":"N");
		stmt.setString(idx++,vo.getReplyContent());
		stmt.setString(idx++,vo.getReplier());
		stmt.setTimestamp(idx++,convert(vo.getReplyDate()));
		return idx;
	}
	
	
	/**
	 * 將搜尋條件MAP組成PreparedStatement
	 * @param times 迴圈要跑幾次
	 * @param siteId
	 * @param selMap
	 * @param stmt
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement getListStmt(int times,String siteId ,Map<String,Object> selMap ,PreparedStatement stmt) throws SQLException{
		Set<String> s = selMap.keySet();
	    Iterator<String> it =s.iterator();
		int idx = 1;
        for(int i =0;i<times;i++){
        	it =s.iterator();
        	stmt.setString(idx++, siteId);
        	 while (it.hasNext()) {
     			String st = it.next();
     			if(st.equals("byField") || st.equals("seq")){
     
     			}else if(st.equals("keyword")){
     				stmt.setString(idx++, "%"+selMap.get(st)+"%");
     				stmt.setString(idx++, "%"+selMap.get(st)+"%");
     			}else if(st.equals("MSG_TYPE") || st.equals("MSG_ID"))
     				stmt.setInt(idx++, (Integer)selMap.get(st));
     			else if(st.equals("startDate"))
     				stmt.setTimestamp(idx++, new Timestamp((Long)selMap.get(st)));
     			else if(st.equals("endDate"))
     				stmt.setTimestamp(idx++, new Timestamp((Long)selMap.get(st)));
     			else stmt.setString(idx++, (String)selMap.get(st));
             }
        }
		return stmt;
	}

}
