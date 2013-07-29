/**
 * 
 */
package com.thesys.opencms.laphone.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.thesys.opencms.laphone.ThesysLaphoneDAO;
/**
 * @author maggie
 *
 */
public class ThesysRatingDAO extends ThesysLaphoneDAO {	
	
	 /** The singleton object. */
    private static ThesysRatingDAO m_instance;
	/**
	 * 
	 */
	private ThesysRatingDAO() {
		super.init();
	}
	public static void main(String[] args){
		try{
//		int val = ThesysRatingDAO.getInstance().read("/sites/laphone", "78034-23").getRating();
//		System.out.println(val);
			List<String> list = ThesysRatingDAO.getInstance().between("/sites/laphone", 0, 50);
			Iterator<String> it = list.iterator();
			while(it.hasNext()){ System.out.println(it.next());}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
     * Singleton access.<p>
     * 
     * @return the singleton object
     */
    public static synchronized ThesysRatingDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysRatingDAO();
        }
        return m_instance;
    }

	public List<String> between(String siteId,int start,int end)  throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<String> result = new ArrayList<String>();

        try {
            con = getConnection();
            String sql ="SELECT ITEM_ID FROM LAPHONE_ITEM_RATING where SITE_ID=? AND ITEM_RATING BETWEEN ? AND ? ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            stmt.setInt(2, start);
            stmt.setInt(3, end);
            rs = stmt.executeQuery();
            while (rs.next()) {
                result.add(rs.getString(1));
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;		
	}
	
	
	
	/**
	 * find the item quantity by itemId
	 * @param siteId the site id
	 * @param itemId the item id
	 * @return item total quantity
	 * @throws SQLException
	 */
	public ThesysRatingVO read(String siteId,String itemId) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysRatingVO result = null;

        try {
            con = getConnection();
            String sql ="SELECT * FROM LAPHONE_ITEM_RATING where SITE_ID=? AND ITEM_ID = ? ";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            stmt.setString(2, itemId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                result = ThesysRatingVO.getInstance(rs);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;		
	}
	
    public void insertOrUpdate(ThesysRatingVO record) throws SQLException {

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        
        try {
        	con = getConnection();
            //check if exists
        	String sql = "SELECT * FROM LAPHONE_ITEM_RATING where SITE_ID=? AND ITEM_ID = ?";
            stmt = con.prepareStatement(sql); 
            int idx = 1;  
            stmt.setString(idx++, record.getSiteId());
            stmt.setString(idx++, record.getItemId());
            rs = stmt.executeQuery();
            if(rs.next()){ //update
            	sql = "UPDATE LAPHONE_ITEM_RATING SET ITEM_RATING=ITEM_RATING+?,  LM_USR_ID=?, LM_DATE=?  WHERE  SITE_ID=? and ITEM_ID=?";
            	stmt = con.prepareStatement(sql);   
	            idx = 1;  
	            stmt.setInt(idx++, record.getRating());
	            stmt.setString(idx++, record.getLastUpdater());
	            stmt.setTimestamp(idx++, convert(record.getLastUpdateDate()));
	            stmt.setString(idx++, record.getSiteId());
	            stmt.setString(idx++, record.getItemId());
	            stmt.executeUpdate();
            }else{    //insert     
            	sql = "	INSERT INTO LAPHONE_ITEM_RATING(SITE_ID, ITEM_ID, ITEM_RATING, CRT_USR_ID, CRT_DATE, LM_USR_ID, LM_DATE) "+
            		  "	VALUES(?, ?, ?, ?, ?, ?, ?)";
	            stmt = con.prepareStatement(sql);   
	            idx = 1;  
	            stmt.setString(idx++, record.getSiteId());
	            stmt.setString(idx++, record.getItemId());
	            stmt.setInt(idx++, record.getRating());
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
