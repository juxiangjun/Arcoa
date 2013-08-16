/**
 * 
 */
package com.thesys.opencms.laphone.cart.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.thesys.opencms.laphone.ThesysLaphoneDAO;
/**
 * @author maggie
 *
 */
public class ThesysTrackingDAO extends ThesysLaphoneDAO {
	 /** The singleton object. */
    private static ThesysTrackingDAO m_instance;
	/**
	 * 
	 */
	private ThesysTrackingDAO() {
		super.init();
	}
	
	/**
     * Singleton access.<p>
     * 
     * @return the singleton object
     */
    public static synchronized ThesysTrackingDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysTrackingDAO();
        }
        return m_instance;
    }

    public int count(String siteId,String memberId) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;
        try {
            con = getConnection();
            String sql = "SELECT count(*) FROM LAPHONE_ITEM_TRACKING where SITE_ID=? and MEM_ID=? ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            stmt.setString(2, memberId);

            rs = stmt.executeQuery();
            if (rs.next()) {
            	result = rs.getInt(1);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }
    
    public List<ThesysTrackingVO> listByPage(String siteId,String memberId,int pageSize,int pageIndex) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<ThesysTrackingVO> result = new ArrayList<ThesysTrackingVO>();
        try {
        	
            con = getConnection();
            String sql = " SELECT top "+pageSize+" * FROM LAPHONE_ITEM_TRACKING where SITE_ID=? and MEM_ID=? "+
            			 " AND ITEM_ID NOT IN ( SELECT top "+(pageSize*(pageIndex-1)) + " ITEM_ID FROM LAPHONE_ITEM_TRACKING WHERE SITE_ID=? and MEM_ID=? ORDER BY CRT_DATE DESC )"+
            			 " ORDER BY CRT_DATE DESC";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, memberId);
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, memberId);

            rs = stmt.executeQuery();
            while (rs.next()) {
            	ThesysTrackingVO item = ThesysTrackingVO.getInstance(rs);
            	result.add(item);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }
    public void delete(String siteId,String userId,String itemId) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        try {
            
            con = getConnection();
            String sql = "DELETE FROM LAPHONE_ITEM_TRACKING where SITE_ID=? and MEM_ID=? and ITEM_ID = ? ";
            stmt = con.prepareStatement(sql);  
            
            stmt.setString(1, siteId);
            stmt.setString(2, userId);
            stmt.setString(3, itemId);
            stmt.executeUpdate();

            
        } finally {
            closeAll(con, stmt, null);
        }
    	
    }
    public void deleteAll(String siteId,String memberId) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        try {
            
            con = getConnection();
            String sql = "DELETE FROM LAPHONE_ITEM_TRACKING where SITE_ID=? and MEM_ID=? ";
            stmt = con.prepareStatement(sql);           
            stmt.setString(1, siteId);
            stmt.setString(2, memberId);
            stmt.executeUpdate();

            
        } finally {
            closeAll(con, stmt, null);
        }
    	
    }
    public void insert(ThesysTrackingVO vo) throws SQLException {

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        java.util.Date now = new java.util.Date();
        try {
            con = getConnection();
            
            // 1) check item id
            String sql = "SELECT * FROM LAPHONE_ITEM_TRACKING  where SITE_ID=? and MEM_ID=? and ITEM_ID = ?";
            stmt = con.prepareStatement(sql);            
            stmt.setString(1, vo.getSiteId());
            stmt.setString(2, vo.getMemberId());
            stmt.setString(3, vo.getItemId());
            rs = stmt.executeQuery();
            if(!rs.next()){
            	closeAll(null, stmt, rs);
                // 2) Write a new entry 
            	sql = "INSERT INTO LAPHONE_ITEM_TRACKING(SITE_ID, MEM_ID,ITEM_ID, CRT_DATE) VALUES(?, ?, ?, ?)";
	            stmt = con.prepareStatement(sql); 
	            int idx = 1;
	            stmt.setString(idx++, vo.getSiteId());
	            stmt.setString(idx++, vo.getMemberId());
	            stmt.setString(idx++, vo.getItemId());
	            stmt.setTimestamp(idx++, convert(now));
	            stmt.executeUpdate();
	             
            }
	         
            
        } finally {
            closeAll(con, stmt, rs);
        }
    }

	/* (non-Javadoc)
	 * @see com.thesys.opencms.dao.ThesysAbstractDAO#getDBTableName()
	 */
	@Override
	protected String getDBTableName() {
		return "LAPHONE_ITEM_TRACKING";
	}

	

	

}
