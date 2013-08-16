/**
 * 
 */
package com.thesys.opencms.laphone.system.dao;

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
public class ThesysShipFeeDAO extends ThesysLaphoneDAO {	
	
	 /** The singleton object. */
    private static ThesysShipFeeDAO m_instance;
	/**
	 * 
	 */
	private ThesysShipFeeDAO() {
		super.init();
	}
	/**
     * Singleton access.<p>
     * 
     * @return the singleton object
     */
    public static synchronized ThesysShipFeeDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysShipFeeDAO();
        }
        return m_instance;
    }

	
	
    public List<ThesysShipFeeVO> listByFeeType(String siteId,int feeType) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ThesysShipFeeVO> result = new ArrayList<ThesysShipFeeVO>();

        try {
            con = getConnection();
            String sql ="SELECT * FROM LAPHONE_SHIP_FEE where SITE_ID=?  AND FEE_TYPE=? ORDER BY COND_END";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            stmt.setInt(2, feeType);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(ThesysShipFeeVO.getInstance(rs));
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;		
	}
	
    public int count(String siteId,int feeType) throws SQLException{
	

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int result = 0;
        try {
            con = getConnection();
            String sql = "SELECT COUNT(*) FROM LAPHONE_SHIP_FEE WHERE SITE_ID=? AND FEE_TYPE=?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            stmt.setInt(2, feeType);
            rs = stmt.executeQuery();
            if (rs.next()) {
            	result = rs.getInt(1);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }
    
    public ThesysShipFeeVO getRow(String siteId,int feeType,int conditionEnd) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysShipFeeVO result = null;
        try {
            con = getConnection();
            String sql ="SELECT top 1 * FROM LAPHONE_SHIP_FEE where SITE_ID=? AND FEE_TYPE=? AND  COND_END=? ORDER BY COND_END";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setInt(idx++, feeType);
            stmt.setInt(idx++, conditionEnd);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = ThesysShipFeeVO.getInstance(rs);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;		
	}
    
    public ThesysShipFeeVO findByCondition(String siteId,int feeType,int amount) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysShipFeeVO result = null;
        try {
            con = getConnection();
            String sql ="SELECT top 1 * FROM LAPHONE_SHIP_FEE where SITE_ID=? AND FEE_TYPE=? AND  ? < COND_END ORDER BY COND_END";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setInt(idx++, feeType);
            stmt.setInt(idx++, amount);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = ThesysShipFeeVO.getInstance(rs);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;		
	}
	
	
	
	public int insert(ThesysShipFeeVO vo) throws SQLException {

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int r = 0;
        try {
        	con = getConnection();
        	String sql = " INSERT INTO LAPHONE_SHIP_FEE(SITE_ID, FEE_TYPE, COND_END, FEE_AMT, CRT_USR_ID, CRT_DATE, LM_USR_ID, LM_DATE) "+
        		  "	  VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = con.prepareStatement(sql);   
            int idx = 1;  
            stmt.setString(idx++, vo.getSiteId());
            stmt.setInt(idx++, vo.getFeeType());
            stmt.setInt(idx++, vo.getConditionEnd()); 
            stmt.setInt(idx++, vo.getFeeAmount()); 
            stmt.setString(idx++, vo.getCreater()); 
            stmt.setTimestamp(idx++, convert(vo.getCreateDate())); 
            stmt.setString(idx++, vo.getLastUpdater()); 
            stmt.setTimestamp(idx++, convert(vo.getLastUpdatedDate())); 
            r = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return r ;
    }

	public int update(ThesysShipFeeVO vo) throws SQLException{
		int res = 0;
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            con = getConnection();            
            sql = "UPDATE LAPHONE_SHIP_FEE SET FEE_AMT=?,LM_USR_ID=?,LM_DATE=? WHERE FEE_TYPE=? AND COND_END=?";
            stmt = con.prepareStatement(sql);
            //寫入一筆資料
            int idx = 1;
            stmt.setInt(idx++, vo.getFeeAmount());
            stmt.setString(idx++, vo.getLastUpdater());
            stmt.setTimestamp(idx++, convert(vo.getLastUpdatedDate()));
            stmt.setInt(idx++, vo.getFeeType());
            stmt.setInt(idx++, vo.getConditionEnd());
    		res = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return res ;
	}
	
	public int delete(String siteId,int feeType,int conditionEnd) throws SQLException{
		int res = 0;
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            con = getConnection();            
            sql = "DELETE FROM LAPHONE_SHIP_FEE WHERE  SITE_ID=? AND FEE_TYPE=? AND  COND_END=? ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            stmt.setInt(2,feeType);
            stmt.setInt(3,conditionEnd);
    		res = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return res ;
	}
	
	

	
	@Override
	protected String getDBTableName(){
		return "LAPHONE_CVS_STORE";

	}

}
