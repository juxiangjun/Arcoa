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
import com.thesys.opencms.laphone.member.dao.ThesysMemberVO;
/**
 * @author maggie
 *
 */
public class ThesysProductSearchDAO extends ThesysLaphoneDAO {	
	
	 /** The singleton object. */
    private static ThesysProductSearchDAO m_instance;
	/**
	 * 
	 */
	private ThesysProductSearchDAO() {
		super.init();
	}
	public static void main(String[] args){
		try{
		 //ThesysProductSearchDAO.getInstance().insertOrUpdate("/sites/laphone", "ABCD","AAAA", "中文", 300, "PK;BK", "華麗風", 11111, 22222, true);
			int count = ThesysProductSearchDAO.getInstance().count("/sites/laphone", new String[]{"AA"}, new String[]{"WT"}, 500, 1000, 10, 200, "華麗風", null);
			System.out.println(count);
			List<String> list = ThesysProductSearchDAO.getInstance().listByPage("/sites/laphone", 50, 1, 1, new String[]{"AA"}, new String[]{"WT"}, 500, 1000, 10, 200, "華麗風", null);
			Iterator<String> it = list.iterator();
			while(it.hasNext()){ System.out.println(it.next());}
//			ThesysProductSearchDAO.getInstance().updateOnlineStatus("/sites/laphone", "7-BELKIN", true);
//			System.out.println(ThesysProductSearchDAO.getInstance().isOnline("/sites/laphone", "7-BELKIN"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
     * Singleton access.<p>
     * 
     * @return the singleton object
     */
    public static synchronized ThesysProductSearchDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysProductSearchDAO();
        }
        return m_instance;
    }
    private PreparedStatement generateStatement(Connection conn,String siteId,boolean countFlag,int pageSize,int pageIndex,int sortType,
    		String[] categoryIds,String[] colors,int priceStart,int priceEnd,int ratingStart,
    		int ratingEnd,String styles,String keyword) throws SQLException{
    	
    	String where = "WHERE SITE_ID = ? AND ST_DATE<=? AND END_DATE>=? ";
    	if(categoryIds!=null){
    		int length = categoryIds.length;    		
    		if(length>0){
    			String subWhere = "";
    			for(int i=0;i<length;i++){
    				if(subWhere.length()>0) subWhere += " OR ";
    				subWhere += " CATE_ID like ? ";
    			}
    			where += " AND ( " + subWhere +" ) ";
    		}    		
    	}
    	if(colors!=null){
    		int length = colors.length;    		
    		if(length>0){
    			String subWhere = "";
    			for(int i=0;i<length;i++){
    				if(subWhere.length()>0) subWhere += " OR ";
    				subWhere += " COLORS like ? ";
    			}
    			where += " AND ( " + subWhere +" ) ";
    		}    		
    	}
    	if(priceStart>0){
    		where += " AND SPEC_PRC>=? ";
    	}
    	if(priceEnd>0){
    		where += " AND SPEC_PRC<=? ";
    	}
    	if(ratingStart>0){
    		where += " AND RATING>=? ";
    	}
    	if(ratingEnd>0){
    		where += " AND RATING<=? ";
    	}
    	if(styles!=null && styles.length()>0){    		
    			where += " AND STYLES=? ";
    	}
    	if(keyword!=null && keyword.length()>0){    		
    			where += " AND UPPER(ITEM_NAME) like UPPER(?) ";
    	}
    	String orderBy = "";
    	switch(sortType){
    	case 1: //價格由高至低
			orderBy = " ORDER BY  SPEC_PRC DESC,ITEM_ID ASC ";
			break;

    	case 2: //價格由低至高
			orderBy = " ORDER BY  SPEC_PRC ASC,ITEM_ID ASC ";
			break;

    	case 3: //銷量由高至低
			orderBy = " ORDER BY  SELL_QTY DESC,ITEM_ID ASC ";
			break;
    	case 4: //銷量由低至高
			orderBy = " ORDER BY  SELL_QTY ASC,ITEM_ID ASC ";
			break;
    	default: //預設最新
    		orderBy = " ORDER BY ST_DATE DESC,ITEM_ID ASC ";
    		break;
    	}
    	String sql = "";
    	if(countFlag){
    		sql = " select count(ITEM_ID) FROM LAPHONE_ITEM_VIEW "+where;
    	}else{
    		sql = "SELECT top "+pageSize+" ITEM_ID FROM LAPHONE_ITEM_VIEW "+where +
   			 " AND ITEM_ID NOT IN ( SELECT top "+(pageSize*(pageIndex-1))+" ITEM_ID FROM LAPHONE_ITEM_VIEW "+where+orderBy+" )"+orderBy;    		
    	}
    	
    	PreparedStatement stmt = conn.prepareStatement(sql);
    	long now = new java.util.Date().getTime();
    	int idx = 1;
    	for(int i=0;i<(countFlag?1:2);i++){
            stmt.setString(idx++, siteId);
            stmt.setLong(idx++, now);
            stmt.setLong(idx++, now);
            if(categoryIds!=null){
        		int length = categoryIds.length;    		
        		for(int j=0;j<length;j++){
    				 stmt.setString(idx++, categoryIds[j]+"%");
    			}	
        	}
            if(colors!=null){
        		int length = colors.length;    		
        		for(int j=0;j<length;j++){
    				 stmt.setString(idx++, "%"+colors[j]+"%");
    			}	
        	}
            if(priceStart>0){
        		stmt.setInt(idx++, priceStart);
        	}
        	if(priceEnd>0){
        		stmt.setInt(idx++, priceEnd);
        	}

        	if(ratingStart>0){
        		stmt.setInt(idx++, ratingStart);
        	}
        	if(ratingEnd>0){
        		stmt.setInt(idx++, ratingEnd);
        	}

        	if(styles!=null && styles.length()>0){
        		stmt.setString(idx++, styles);
        	}
        	if(keyword!=null && keyword.length()>0){    		
        		stmt.setString(idx++, "%"+keyword+"%");
        	}
            
        }
    	return stmt;
    }
    /**
     * 計算總筆數
     * @param siteId
     * @param categoryIds
     * @param colors
     * @param priceStart
     * @param priceEnd
     * @param ratingStart
     * @param ratingEnd
     * @param styles
     * @param keyword
     * @return
     * @throws SQLException
     */
    public int count(String siteId,String[] categoryIds,String[] colors,int priceStart,int priceEnd,int ratingStart,int ratingEnd,String styles,String keyword) throws SQLException{
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
        	conn = getConnection();
            
            stmt = generateStatement(conn , siteId,true, 0, 0, 0, categoryIds, colors, priceStart, priceEnd, ratingStart, ratingEnd, styles, keyword);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
            	return rs.getInt(1);
            }
        }finally {
            closeAll(conn, stmt, rs);
        }
        return 0;
    
    }    
    public List<String> listByPage(String siteId,int pageSize,int pageIndex,int sortType,
    		String[] categoryIds,String[] colors,int priceStart,int priceEnd,int ratingStart,
    		int ratingEnd,String styles,String keyword) throws SQLException{
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<String> result = new ArrayList<String>();
        try {
        	conn = getConnection();
        	
        	if(pageIndex==1 && keyword!=null && keyword.length()>0){
	        	//寫入一筆搜尋Keyword記錄
	        	String sql = "INSERT INTO LAPHONE_SEARCH_LOG(SITE_ID, KEYWORD, CRT_DATE) VALUES(?, ?, ?)";
	        	stmt = conn.prepareStatement(sql);
	        	stmt.setString(1, siteId);
	        	stmt.setString(2, keyword);
	        	stmt.setTimestamp(3, convert(new java.util.Date()));
	        	stmt.executeUpdate();
        	}
            
            stmt = generateStatement(conn,siteId,  false, pageSize, pageIndex, sortType, categoryIds, colors, priceStart, priceEnd, ratingStart, ratingEnd, styles, keyword);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
            	String itemId = rs.getString("ITEM_ID");
            	result.add(itemId);
            }
        }finally {
            closeAll(conn, stmt, rs);
        }
        return result;
    }
	
	
	/**
	 * 建立商品資訊
	 * @param siteId
	 * @param itemId
	 * @param itemName
	 * @param specialPrice
	 * @param colors
	 * @param styles
	 * @param startDate
	 * @param endDate
	 * @param onlineFlag
	 * @throws SQLException
	 */
    public void insertOrUpdate(String siteId,String itemId,String categoryId,String itemName,int specialPrice,String colors,String styles,long startDate,long endDate,boolean onlineFlag,boolean groupFlag) throws SQLException {

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        
        try {
        	con = getConnection();
            //check if exists
        	String sql = "SELECT * FROM LAPHONE_ITEM_KEYWORD where SITE_ID=? AND ITEM_ID = ?";
            stmt = con.prepareStatement(sql); 
            int idx = 1;  
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, itemId);
            rs = stmt.executeQuery();
            if(rs.next()){ //update
            	sql = "UPDATE LAPHONE_ITEM_KEYWORD SET CATE_ID=?,ITEM_NAME=?,  SPEC_PRC=?, COLORS=?,STYLES=?,ST_DATE=?,END_DATE=?,ONLINE_FLAG=?,GRP_FLAG=?  WHERE  SITE_ID=? and ITEM_ID=?";
            	stmt = con.prepareStatement(sql);   
	            idx = 1;  
	            stmt.setString(idx++, categoryId);
	            stmt.setString(idx++, itemName);
	            stmt.setInt(idx++, specialPrice);
	            stmt.setString(idx++, colors);
	            stmt.setString(idx++, styles);
	            stmt.setLong(idx++, startDate);
	            stmt.setLong(idx++, endDate);
	            stmt.setString(idx++, (onlineFlag)?"Y":"N");
	            stmt.setString(idx++, (groupFlag)?"Y":"N");
	            stmt.setString(idx++, siteId);
	            stmt.setString(idx++, itemId);
	            stmt.executeUpdate();
            }else{    //insert     
            	sql = "	INSERT INTO LAPHONE_ITEM_KEYWORD(SITE_ID, ITEM_ID, CATE_ID,ITEM_NAME, SPEC_PRC, COLORS, STYLES,ST_DATE,END_DATE,ONLINE_FLAG,GRP_FLAG) "+
            		  "	VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";
	            stmt = con.prepareStatement(sql);   
	            idx = 1;  
	            stmt.setString(idx++, siteId);
	            stmt.setString(idx++, itemId);
	            stmt.setString(idx++, categoryId);
	            stmt.setString(idx++, itemName);
	            stmt.setInt(idx++, specialPrice);
	            stmt.setString(idx++, colors);
	            stmt.setString(idx++, styles);
	            stmt.setLong(idx++, startDate);
	            stmt.setLong(idx++, endDate);
	            stmt.setString(idx++, (onlineFlag)?"Y":"N");
	            stmt.setString(idx++, (groupFlag)?"Y":"N");
	            stmt.executeUpdate();
            }
            
        } finally {
            closeAll(con, stmt, rs);
        }
    }

    /**
     * 更新是否上線的狀態
     * @param siteId
     * @param itemId
     * @param onlineFlag
     * @throws SQLException
     */
    public void updateOnlineStatus(String siteId,String itemId,boolean onlineFlag) throws SQLException {
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            con = getConnection();
            sql = "UPDATE LAPHONE_ITEM_KEYWORD SET ONLINE_FLAG=? WHERE SITE_ID = ? AND ITEM_ID=?";
            stmt = con.prepareStatement(sql);   
            int idx = 1;
            stmt.setString(idx++, (onlineFlag)?"Y":"N");
            stmt.setString(idx++,siteId);
            stmt.setString(idx++,itemId);
    		stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
    }
	
    /**
     * 查詢此商品是否上線
     * @param siteId
     * @param itemId
     * @return
     * @throws SQLException
     */
    public boolean isOnline(String siteId,String itemId) throws SQLException {
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        boolean onlineFlag = false;
        try {
            con = getConnection();
            sql = "SELECT ONLINE_FLAG FROM  LAPHONE_ITEM_KEYWORD WHERE SITE_ID=? AND ITEM_ID=?";
            stmt = con.prepareStatement(sql);   
            int idx = 1;
            stmt.setString(idx++,siteId);
            stmt.setString(idx++,itemId);
    		rs = stmt.executeQuery();
    		if (rs.next()){
    			onlineFlag = rs.getString("ONLINE_FLAG").equals("Y");
    		}
        } finally {
            closeAll(con, stmt, rs);
        }
    	return onlineFlag;
    }
	
	@Override
	protected String getDBTableName(){
		return "LAPHONE_ITEM_KEYWORD";

	}

}
