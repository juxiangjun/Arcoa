/**
 * 
 */
package com.thesys.opencms.laphone.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.thesys.opencms.laphone.ThesysLaphoneDAO;
/**
 * @author maggie
 *
 */
public class ThesysStockDAO extends ThesysLaphoneDAO {	
	
	 /** The singleton object. */
    private static ThesysStockDAO m_instance;
	/**
	 * 
	 */
	private ThesysStockDAO() {
		super.init();
	}
	/**
     * Singleton access.<p>
     * 
     * @return the singleton object
     */
    public static synchronized ThesysStockDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysStockDAO();
        }
        return m_instance;
    }

	
	
	
	
	/**
	 * find the item quantity by itemId
	 * @param siteId the site id
	 * @param itemId the item id
	 * @return item total quantity
	 * @throws SQLException
	 */
	public ThesysStockVO read(String siteId,String itemId) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysStockVO result = null;

        try {
            con = getConnection();
            String sql ="SELECT * FROM LAPHONE_ITEM_STOCK where SITE_ID=? AND ITEM_ID = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            stmt.setString(2, itemId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = ThesysStockVO.getInstance(rs);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;		
	}
	
    public void insertOrUpdate(ThesysStockVO record) throws SQLException {

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        
        try {
        	con = getConnection();
            //check if exists
        	String sql = "SELECT * FROM LAPHONE_ITEM_STOCK where SITE_ID=? AND ITEM_ID = ?";
            stmt = con.prepareStatement(sql); 
            int idx = 1;  
            stmt.setString(idx++, record.getSiteId());
            stmt.setString(idx++, record.getItemId());
            rs = stmt.executeQuery();
            if(rs.next()){ //update
            	sql = "UPDATE LAPHONE_ITEM_STOCK SET ITEM_QTY=?,  LM_USR_ID=?, LM_DATE=?  WHERE  SITE_ID=? and ITEM_ID=?";
            	stmt = con.prepareStatement(sql);   
	            idx = 1;  
	            stmt.setInt(idx++, record.getQuantity());
	            stmt.setString(idx++, record.getLastUpdater());
	            stmt.setTimestamp(idx++, convert(record.getLastUpdateDate()));
	            stmt.setString(idx++, record.getSiteId());
	            stmt.setString(idx++, record.getItemId());
	            stmt.executeUpdate();
            }else{    //insert     
            	sql = "	INSERT INTO LAPHONE_ITEM_STOCK(SITE_ID, ITEM_ID, ITEM_QTY, CRT_USR_ID, CRT_DATE, LM_USR_ID, LM_DATE) "+
            		  "	VALUES(?, ?, ?, ?, ?, ?, ?)";
	            stmt = con.prepareStatement(sql);   
	            idx = 1;  
	            stmt.setString(idx++, record.getSiteId());
	            stmt.setString(idx++, record.getItemId());
	            stmt.setInt(idx++, record.getQuantity());
	            stmt.setString(idx++, record.getCreater());
	            stmt.setTimestamp(idx++, convert(record.getCreateDate()));
	            stmt.setString(idx++, null);
	            stmt.setTimestamp(idx++, null);
	            stmt.executeUpdate();
            }
            
        } finally {
            closeAll(con, stmt, rs);
        }
    }

	
	@Override
	protected String getDBTableName(){
		return "LAPHONE_ITEM_STOCK";

	}

}
