/**
 * 
 */
package com.thesys.opencms.laphone.cvs.dao;

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
public class ThesysCvsStoreDAO extends ThesysLaphoneDAO {	
	
	 /** The singleton object. */
    private static ThesysCvsStoreDAO m_instance;
	/**
	 * 
	 */
	private ThesysCvsStoreDAO() {
		super.init();
	}
	/**
     * Singleton access.<p>
     * 
     * @return the singleton object
     */
    public static synchronized ThesysCvsStoreDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysCvsStoreDAO();
        }
        return m_instance;
    }
    public List<ThesysCvsStoreVO> list(String siteId,String storeType,String zipCode) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ThesysCvsStoreVO> result = new ArrayList<ThesysCvsStoreVO>();

        try {
            con = getConnection();
            String sql ="SELECT * FROM LAPHONE_CVS_STORE where SITE_ID=? AND STTYPE=? AND ZIPCD = ? ORDER BY STNO";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, storeType);
            stmt.setString(idx++, zipCode);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(ThesysCvsStoreVO.getInstance(rs));
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;		
	}
	
    public List<ThesysCvsStoreVO> listByPage(String siteId ,int pageSize,int pageIndex) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ThesysCvsStoreVO> result = new ArrayList<ThesysCvsStoreVO>();

        try {
            con = getConnection();
            String sql = " SELECT top "+pageSize+" * FROM LAPHONE_CVS_STORE WHERE  SITE_ID=? AND STNO NOT IN (SELECT TOP "+(pageSize*(pageIndex-1)) + " STNO FROM LAPHONE_CVS_STORE WHERE SITE_ID=?  ORDER BY STTYPE, DCRONO,STNO)"+
         			 "ORDER BY STTYPE, DCRONO,STNO";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            stmt.setString(2, siteId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(ThesysCvsStoreVO.getInstance(rs));
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;		
    }
    
    public int count(String siteId) throws SQLException{
        
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int result = 0;
        try {
            con = getConnection();
            String sql = "SELECT count(*) FROM LAPHONE_CVS_STORE where SITE_ID=?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            rs = stmt.executeQuery();
            if (rs.next()) {
            	result = rs.getInt(1);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }
    
    public List<ThesysCvsStoreVO> listAll(String siteId) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ThesysCvsStoreVO> result = new ArrayList<ThesysCvsStoreVO>();

        try {
            con = getConnection();
            String sql ="SELECT * FROM LAPHONE_CVS_STORE where SITE_ID=? ORDER BY STTYPE, STCITY, STCNTRY,STNO";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(ThesysCvsStoreVO.getInstance(rs));
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;		
	}
	
	
	public ThesysCvsStoreVO read(String siteId,String storeNo) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysCvsStoreVO result = null;

        try {
            con = getConnection();
            String sql ="SELECT * FROM LAPHONE_CVS_STORE where SITE_ID=? AND STNO = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            stmt.setString(2, storeNo);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = ThesysCvsStoreVO.getInstance(rs);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;		
	}
    public int insert(ThesysCvsStoreVO vo) throws SQLException {

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int flag = 0;
        java.sql.Date sDate = null;
        java.sql.Date eDate = null;
        
        if(vo.getStartDate() != null)
        	sDate = new java.sql.Date(vo.getStartDate().getTime());
        if(vo.getEndDate() != null)
        	eDate = new java.sql.Date(vo.getEndDate().getTime());
        
        try {
        	con = getConnection();
        	String sql = "INSERT INTO LAPHONE_CVS_STORE(" +
        					"SITE_ID,STNO, STTYPE, STNM, STTEL, " +
        					"STCITY, STCNTRY, STADR, ZIPCD,DCRONO, " +
        					"SDATE,EDATE,CRT_USR_ID,CRT_DATE)  "+
        					"VALUES(?,?,?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";
            stmt = con.prepareStatement(sql);   
            int idx = 1;  
            stmt.setString(idx++, vo.getSiteId());
            stmt.setString(idx++, vo.getStoreNo()); 
            stmt.setString(idx++, vo.getStoreType()); 
            stmt.setString(idx++, vo.getStoreName()); 
            stmt.setString(idx++, vo.getTelphone()); 
            stmt.setString(idx++, vo.getCity()); 
            stmt.setString(idx++, vo.getCountry()); 
            stmt.setString(idx++, vo.getAddress()); 
            stmt.setString(idx++, vo.getZipCode()); 
            stmt.setString(idx++, vo.getDcroNo()); 
            stmt.setDate(idx++, sDate);
            stmt.setDate(idx++, eDate);
            stmt.setString(idx++, vo.getCreater()); 
            stmt.setTimestamp(idx++, convert(vo.getCreateDate()));
            flag = stmt.executeUpdate();
         } finally {
            closeAll(con, stmt, rs);
        }
        return flag;
    }

    public int clearTable(String siteId) throws SQLException {
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int flag = 0;
        //DCRONO,SDATE,EDATE,CRT_USR_ID,CRT_DATE
        try {
        	con = getConnection();
        	String sql = "DELETE FROM LAPHONE_CVS_STORE WHERE SITE_ID=?";
            stmt = con.prepareStatement(sql);   
            stmt.setString(1, siteId);
            flag = stmt.executeUpdate();
         } finally {
            closeAll(con, stmt, rs);
        }
        return flag;
    }	
	
	
	@Override
	protected String getDBTableName(){
		return "LAPHONE_CVS_STORE";

	}

}
