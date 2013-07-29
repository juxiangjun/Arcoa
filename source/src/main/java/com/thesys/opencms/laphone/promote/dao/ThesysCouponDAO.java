/**
 * 
 */
package com.thesys.opencms.laphone.promote.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



import com.thesys.opencms.laphone.ThesysLaphoneDAO;
/**
 * @author maggie
 *
 */
public class ThesysCouponDAO extends ThesysLaphoneDAO {
	 /** The singleton object. */
    private static ThesysCouponDAO m_instance;
	/**
	 * 
	 */
	private ThesysCouponDAO() {
		super.init();
	}
	
	/**
     * Singleton access.<p>
     * 
     * @return the singleton object
     */
    public static synchronized ThesysCouponDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysCouponDAO();
        }
        return m_instance;
    }
    
    
    public int add(ThesysCouponVO vo) throws SQLException{
		int r = 0;
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		con = getConnection();
		try{
			String sql ="INSERT INTO LAPHONE_COUPON(SITE_ID,CP_CODE,MEM_ID,CP_AMT,CP_RATE," +
					"ST_DATE,END_DATE,SRC_ORD_ID,CP_DATE,USED_ORD_ID,USED_DATE) VALUES(" +
					"?,?,?,?,?,?,?,?,?,?,?)";
			
			stmt = con.prepareStatement(sql);
			int idx = 1;
			Timestamp usedDate = null;
			if(vo.getUsedDate() != null) usedDate = convert(vo.getUsedDate());
			stmt.setString(idx++,vo.getSiteId());
			stmt.setString(idx++,vo.getCouponCode());
			stmt.setString(idx++,vo.getMemberId());
			stmt.setInt(idx++,vo.getCouponAmount());
			stmt.setInt(idx++,vo.getDiscountRate());
			stmt.setTimestamp(idx++, convert(vo.getStartDate()));
			stmt.setTimestamp(idx++, convert(vo.getEndDate()));
			stmt.setString(idx++,vo.getSourceOrderId());
			stmt.setTimestamp(idx++, convert(vo.getCouponDate()));
			stmt.setString(idx++,vo.getUsedOrderId());
			stmt.setTimestamp(idx++, usedDate);
			r = stmt.executeUpdate();
		}finally{
			closeAll(con, stmt, rs);
		}
		return r;
	}
    
    
    
    public int getUsableCount(String siteId,String memberId) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        java.util.Date now = new java.util.Date();
        int result = 0;
        try {
            con = getConnection();
            String sql = "SELECT count(*) FROM LAPHONE_COUPON where SITE_ID=? and MEM_ID=? "+
            			 " AND END_DATE > ? and ST_DATE <= ? and USED_DATE is null ";
            stmt = con.prepareStatement(sql);
        	int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, memberId);
        	stmt.setTimestamp(idx++, convert(now));
        	stmt.setTimestamp(idx++, convert(now));

            rs = stmt.executeQuery();
            if (rs.next()) {
            	result = rs.getInt(1);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }   
    public int getWeekExpireCount(String siteId,String memberId,java.util.Date endAfter) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        java.util.Date now = new java.util.Date();
        int result = 0;
        try {
            con = getConnection();
            String sql = "SELECT count(*) FROM LAPHONE_COUPON where SITE_ID=? and MEM_ID=? "+
            			 "AND END_DATE > ? AND ST_DATE <= ? AND END_DATE < ?  AND USED_DATE is null ";
            stmt = con.prepareStatement(sql);
        	int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, memberId);
        	stmt.setTimestamp(idx++, convert(now));
        	stmt.setTimestamp(idx++, convert(now));
        	stmt.setTimestamp(idx++, convert(endAfter));

            rs = stmt.executeQuery();
            if (rs.next()) {
            	result = rs.getInt(1);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }  
//    public List<ThesysCouponVO> getWeekExpireList(String siteId,String memberId,java.util.Date endAfter) throws SQLException{
//    	Connection con = null;
//        PreparedStatement stmt = null;
//        ResultSet rs = null;
//        java.util.Date now = new java.util.Date();
//        List<ThesysCouponVO> result = new ArrayList<ThesysCouponVO>();
//        try {
//            con = getConnection();
//            String sql = "SELECT * from FROM LAPHONE_COUPON where SITE_ID=? and MEM_ID=? "+
//            			 "AND END_DATE > ? and ST_DATE <= ? AND END_DATE < ? and and USED_DATE is null "+
//            			 " ORDER BY ST_DATE DESC ";
//            stmt = con.prepareStatement(sql);
//        	int idx = 1;
//            stmt.setString(idx++, siteId);
//            stmt.setString(idx++, memberId);
//        	stmt.setTimestamp(idx++, convert(now));
//        	stmt.setTimestamp(idx++, convert(now));
//        	stmt.setTimestamp(idx++, convert(endAfter));
//
//            rs = stmt.executeQuery();
//            while (rs.next()) {
//            	ThesysCouponVO coupon = ThesysCouponVO.getInstance(rs);
//                result.add(coupon);
//            }
//        } finally {
//            closeAll(con, stmt, rs);
//        }
//        return result;
//    }   
    public List<ThesysCouponVO> getUsableList(String siteId,String memberId) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        java.util.Date now = new java.util.Date();
        List<ThesysCouponVO> result = new ArrayList<ThesysCouponVO>();
        try {
            con = getConnection();
            String sql = "SELECT * FROM LAPHONE_COUPON where SITE_ID=? and MEM_ID=? "+
            			 "AND END_DATE > ? and ST_DATE <= ? and USED_DATE is null "+
            			 " ORDER BY ST_DATE DESC ";
            stmt = con.prepareStatement(sql);
        	int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, memberId);
        	stmt.setTimestamp(idx++, convert(now));
        	stmt.setTimestamp(idx++, convert(now));

            rs = stmt.executeQuery();
            while (rs.next()) {
            	ThesysCouponVO coupon = ThesysCouponVO.getInstance(rs);
                result.add(coupon);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }  
    public ThesysCouponVO read(String siteId,String couponCode) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysCouponVO result = null;
        try {
            con = getConnection();
            String sql = "SELECT * FROM LAPHONE_COUPON where SITE_ID=? and CP_CODE=? ";
            stmt = con.prepareStatement(sql);
        	int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, couponCode);

            rs = stmt.executeQuery();
            if(rs.next()) {
            	result = ThesysCouponVO.getInstance(rs);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }  
    

    public int count(String siteId,String memberId) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;
        try {
            con = getConnection();
            String sql = "SELECT count(*) FROM LAPHONE_COUPON where SITE_ID=? and MEM_ID=? ";
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
    
    public List<ThesysCouponVO> listByPage(String siteId,String memberId,int pageSize,int pageIndex) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<ThesysCouponVO> result = new ArrayList<ThesysCouponVO>();
        try {
        	
            con = getConnection();
            //僅列出六個月內的
            Calendar cal = Calendar.getInstance();
            String sql = " SELECT top "+pageSize+" * FROM LAPHONE_COUPON where SITE_ID=? and MEM_ID=? "+
            			 " AND CP_DATE > DATEADD(mm,-6,GETDATE()) AND CP_CODE NOT IN ( SELECT top "+(pageSize*(pageIndex-1)) + " CP_CODE FROM LAPHONE_COUPON WHERE SITE_ID=? and MEM_ID=? AND CP_DATE > DATEADD(mm,-6,GETDATE()) ORDER BY CP_DATE DESC )"+
            			 " ORDER BY CP_DATE DESC";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, memberId);
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, memberId);

            rs = stmt.executeQuery();
            while (rs.next()) {
            	ThesysCouponVO item = ThesysCouponVO.getInstance(rs);
            	result.add(item);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }
    public void useCoupon(Connection con,String siteId,String couponCode,String orderId,java.util.Date orderDate) throws SQLException{
    	PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            
           // 2) Write a new entry 
        	String sql = " update LAPHONE_COUPON SET USED_ORD_ID = ? , USED_DATE = ? "+
        				 " where SITE_ID=? and  CP_CODE = ? ";
            stmt = con.prepareStatement(sql); 
            int idx = 1;
            stmt.setString(idx++, orderId);
            stmt.setTimestamp(idx++, convert(orderDate));
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, couponCode);
            stmt.executeUpdate();
	         
            
        } finally {
            closeAll(con, stmt, rs);
        }
    }

	/* (non-Javadoc)
	 * @see com.thesys.opencms.dao.ThesysAbstractDAO#getDBTableName()
	 */
	@Override
	protected String getDBTableName() {
		return "LAPHONE_COUPON";
	}

	
	

}
