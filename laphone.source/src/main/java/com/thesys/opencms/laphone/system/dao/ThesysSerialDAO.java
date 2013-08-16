/**
 * 
 */
package com.thesys.opencms.laphone.system.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.thesys.opencms.laphone.ThesysLaphoneDAO;

/**
 * @author maggie
 *
 */
public class ThesysSerialDAO extends ThesysLaphoneDAO {
	 /** The singleton object. */
    private static ThesysSerialDAO m_instance;
	/**
	 * 
	 */
	private ThesysSerialDAO() {
		super.init();
	}	
	
	/**
     * Singleton access.<p>
     * 
     * @return the singleton object
     */
    public static synchronized ThesysSerialDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysSerialDAO();
        }
        return m_instance;
    }
      
    public ThesysSerialNoVO readSerialNo(String siteId,String serialType,String prefixText,String dateText) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysSerialNoVO result = null;
        
        
        try {
            con = getConnection();
            String sql = "select * from LAPHONE_SN_DTL where SITE_ID = ?  and SN_TYPE = ? AND PRE_TXT = ? AND DTE_TXT = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            stmt.setString(2, serialType);
            stmt.setString(3, prefixText);
            stmt.setString(4, dateText);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = ThesysSerialNoVO.getInstance(rs);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
		
	}
    
	public ThesysSerialFormatVO readSerialFormat(String siteId,String serialType) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysSerialFormatVO result = null;

        try {
            con = getConnection();
            String sql = "select * from LAPHONE_SN_FMT where SITE_ID = ?  and SN_TYPE = ?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            stmt.setString(2, serialType);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = ThesysSerialFormatVO.getInstance(rs);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
		
	}
    
    public void write(ThesysSerialNoVO vo) throws SQLException {

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            
            // 1) check SerialNumber
           
            if(vo.getSerialNumber()==1){            	
                // 2) Write a new entry 
            	String sql = "INSERT INTO LAPHONE_SN_DTL(SITE_ID, SN_TYPE, PRE_TXT, DTE_TXT, SN_NUM) VALUES(?, ?, ?, ?, ?)";
	            stmt = con.prepareStatement(sql); 
	            stmt.setString(1, vo.getSiteId());
	            stmt.setString(2, vo.getSerialType());
	            stmt.setString(3, vo.getPrefixText());
	            stmt.setString(4, vo.getDateText());
	            stmt.setInt(5, vo.getSerialNumber());
            }else{
            	// 2-1) update the entry
            	String sql = "UPDATE LAPHONE_SN_DTL  SET SN_NUM= ? WHERE SITE_ID= ? and SN_TYPE= ? and PRE_TXT= ? and DTE_TXT= ?";
            	stmt = con.prepareStatement(sql); 
	            stmt.setInt(1, vo.getSerialNumber());
	            stmt.setString(2, vo.getSiteId());
	            stmt.setString(3, vo.getSerialType());
	            stmt.setString(4, vo.getPrefixText());
	            stmt.setString(5, vo.getDateText());
            }   
            int rc = stmt.executeUpdate();
            if (rc != 1) {
            	throw new SQLException("sql update failure!");
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
		return "LAPHONE_SN_FMT";
	}

	
	
	
}
