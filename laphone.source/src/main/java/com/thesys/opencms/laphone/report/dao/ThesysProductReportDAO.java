package com.thesys.opencms.laphone.report.dao;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

import com.thesys.opencms.laphone.ThesysLaphoneDAO;

public class ThesysProductReportDAO extends ThesysLaphoneDAO {
	private static ThesysProductReportDAO m_instance;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
	
	private ThesysProductReportDAO() {
		super.init();
	}
	
	public static synchronized ThesysProductReportDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysProductReportDAO();
        }
        return m_instance;
    }
	
	
	
    /**
     * 分頁報表列表
     * @param siteId
     * @param pageSize
     * @param pageIndex
     * @param orderStatus
     * @param startOrderDate
     * @param endOrderDate
     * @return
     * @throws Exception
     */
    public List<String[]> listByPage(String siteId,int pageSize,int pageIndex,int reportType,String categoryId,String itemId,
    		String itemName,int productType,String startDate, String endDate) throws Exception{
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<String[]> result = new ArrayList<String[]>();
        try {
        	conn = getConnection();
            
            stmt = generateStatement(conn, siteId, false, pageSize, pageIndex, reportType, categoryId, itemId, itemName, productType, startDate, endDate);          
            rs = stmt.executeQuery();
            while (rs.next()) {
            	String[] row = new String[6];
            	row[0] = rs.getString("CATE_ID");
            	row[1] = rs.getString("ITEM_ID");
            	row[2] = rs.getString("ITEM_NAME");
            	row[3] = String.valueOf(rs.getInt("CTR_COUNT"));
            	row[4] = String.valueOf(rs.getInt("TRK_COUNT"));
            	row[5] = String.valueOf(rs.getInt("SELL_COUNT"));
            	result.add(row);
            }
        } finally {
            closeAll(conn, stmt, rs);
        }
        return result;
    }
    /**
     * 匯出全部
     * @param columns
     * @param siteId
     * @param reportType
     * @param checkedFlag
     * @param startDate
     * @param endDate
     * @return
     * @throws Exception
     */
    public void export(OutputStream out,String[] headers,String[] columns,String siteId,int reportType, String categoryId,String itemId,
    		String itemName,int productType,String startDate, String endDate) throws Exception{
    	
    	char EOF = (char)0x00;
    	CSVWriter writer = new CSVWriter(new OutputStreamWriter(out),',',EOF , "\r\n");
    	writer.writeNext(headers);
    	
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = getConnection();
            stmt = generateStatement(conn, siteId, false, -1, 0, reportType, categoryId, itemId, itemName, productType, startDate, endDate); 
            rs = stmt.executeQuery();
            int length = columns.length;
            while (rs.next()) {
            	String[] row = new String[length];
            	for(int i=0;i<length;i++){
            		String column = columns[i];
            		if(column.endsWith("COUNT")){
            			row[i] = String.valueOf(rs.getInt(column));
            		}else{
            			row[i] = rs.getString(column);
            		}
            	}
            	writer.writeNext(row);
            }
            writer.flush();
            writer.close();
            
            out.close();
        } finally {
            closeAll(conn, stmt, rs);
        }
    }
    
    /**
     * 取得報表筆數
     * @param siteId
     * @param orderStatus
     * @param startOrderDate
     * @param endOrderDate
     * @return
     * @throws Exception
     */
    public int count(String siteId,int reportType, String categoryId,String itemId,
    		String itemName,int productType,String startDate, String endDate) throws Exception{
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int result = 0;
        try {
            conn = getConnection();
            stmt = generateStatement(conn, siteId, true, 0, 0, reportType, categoryId, itemId, itemName, productType, startDate, endDate); 
            rs = stmt.executeQuery();
            if (rs.next()) {
            	result = rs.getInt(1);
            }
        } finally {
            closeAll(conn, stmt, rs);
        }
        return result;
    }
    private PreparedStatement generateStatement(Connection conn,String siteId,boolean countFlag,int pageSize,int pageIndex,int reportType,
    		String categoryId,String itemId,String itemName,int productType,String startDate, String endDate) throws Exception{
 
    	String viewName = ""; //查詢的VIEW
    	String orderBy = ""; 
    	String subOrderByColumn = "count(ITEM_ID)"; //分頁的次查詢排序欄位
    	if(reportType==1){  //產品點擊數報表
    		viewName = " LAPHONE_ITEM_CTR_VIEW ";
	    	orderBy = " ORDER BY CTR_COUNT DESC ";
    	}else if(reportType==2){ //產品加入追蹤排名報表
    		viewName = " LAPHONE_ITEM_TRK_VIEW ";
    		orderBy = " ORDER BY TRK_COUNT DESC ";
    	}else if(reportType==3){ //產品銷售排名報表
    		viewName = " LAPHONE_ITEM_SELL_VIEW ";
    		orderBy = " ORDER BY SELL_COUNT DESC "; 
    		subOrderByColumn = "SUM(ORD_QTY)";
    	}
    	
    	String where = "WHERE SITE_ID = ? ";
    	if(categoryId!=null && !"".equals(categoryId)){
    		where += " AND CATE_ID like ? ";
    	}
    	if(itemId!=null && !"".equals(itemId)){
    		where += " AND ITEM_ID like ? ";
    	}
    	if(itemName!=null && !"".equals(itemName)){
    		where += " AND ITEM_NAME like ? ";
    	}
    	if(productType==1){
    		where += " AND GRP_FLAG <> 'Y' ";    		
    	}else if(productType==2){
    		where += " AND GRP_FLAG = 'Y' ";    		
    	}
    	if(startDate!=null && !"".equals(startDate))
    		where += " AND CRT_DATE>=? ";
	    if(endDate!=null && !"".equals(endDate))
	    	where += " AND CRT_DATE<=? ";
    	
    	String countCtrSql = "select count(ITEM_ID) from LAPHONE_ITEM_CTR where SITE_ID=KW.SITE_ID AND ITEM_ID =KW.ITEM_ID ";
    	if(startDate!=null && !"".equals(startDate))
    		countCtrSql += " AND CRT_DATE>=? ";
	    if(endDate!=null && !"".equals(endDate))
	    	countCtrSql += " AND CRT_DATE<=? ";
	    
	    String countTrackSql = "select count(ITEM_ID) from LAPHONE_ITEM_TRACKING where SITE_ID=KW.SITE_ID AND ITEM_ID =KW.ITEM_ID ";
	    if(startDate!=null && !"".equals(startDate))
	    	countTrackSql += " AND CRT_DATE>=? ";
	    if(endDate!=null && !"".equals(endDate))
	    	countTrackSql += " AND CRT_DATE<=? ";
	    
	    String countOrderSql = " SELECT ORD_ID from LAPHONE_ORD_MAIN WHERE SITE_ID = KW.SITE_ID AND ORD_ST = 6 ";
	    if(startDate!=null && !"".equals(startDate))
	    	countOrderSql += " AND ORD_DATE>=? ";
	    if(endDate!=null && !"".equals(endDate))
	    	countOrderSql += " AND ORD_DATE<=? ";	    
	    countOrderSql = " select SUM(ORD_QTY) from LAPHONE_ORD_ITEM where SITE_ID=KW.SITE_ID AND ITEM_ID =KW.ITEM_ID "+
				   " and ORD_ID IN ("+countOrderSql +" )";	 
	    
	    if(reportType==1){
	    	countCtrSql = " count(CRT_DATE) ";
	    }else if(reportType==2){
	    	countTrackSql = " count(CRT_DATE) ";
	    }else if(reportType==3){
	    	countOrderSql = " SUM(ORD_QTY) ";
	    }
    	
    	
    	String sql = "";
    	int stmtCount = 1;    	
    	if(countFlag){
    		sql = " select count(DISTINCT ITEM_ID) FROM "+viewName+where;
    	}else if(pageSize==-1){
    		sql = " SELECT ITEM_ID,CATE_ID,ITEM_NAME,("+countCtrSql+") CTR_COUNT,("+countTrackSql+") TRK_COUNT,("+countOrderSql+") SELL_COUNT "+
      			  " FROM "+viewName+" KW "+where +" GROUP BY SITE_ID,ITEM_ID,CATE_ID,ITEM_NAME "+orderBy;    		
    	}else{
    		stmtCount = 2;
    		sql = " SELECT top "+pageSize+" ITEM_ID,CATE_ID,ITEM_NAME,("+countCtrSql+") CTR_COUNT,("+countTrackSql+") TRK_COUNT,("+countOrderSql+") SELL_COUNT "+
    			  " FROM "+viewName+" KW "+where +" AND ITEM_ID NOT IN ( SELECT top "+(pageSize*(pageIndex-1))+" ITEM_ID FROM  "+viewName+where+" GROUP BY ITEM_ID ORDER BY "+subOrderByColumn+" desc )  "+
    			  " GROUP BY SITE_ID,ITEM_ID,CATE_ID,ITEM_NAME "+orderBy;    		
    	}
    	
    	PreparedStatement stmt = conn.prepareStatement(sql);
    	int idx = 1;
    	
    	//塞入統計次數的的日期查詢
    	if(!countFlag){
	    	for(int i=1;i<=2;i++){
	    		if(startDate!=null && !"".equals(startDate))
	            	stmt.setTimestamp(idx++, convert(sdf.parse(startDate + " 00:00:00.000")));
	            if(endDate!=null && !"".equals(endDate))
	            	stmt.setTimestamp(idx++, convert(sdf.parse(endDate + " 23:59:59.999")));
	    	}
    	}
    	for(int i=0;i<stmtCount;i++){

            stmt.setString(idx++, siteId);
    		if(categoryId!=null && !"".equals(categoryId)){
    			 stmt.setString(idx++,"%"+categoryId+"%");
        	}
        	if(itemId!=null && !"".equals(itemId)){
        		stmt.setString(idx++,"%"+itemId+"%");
        	}
        	if(itemName!=null && !"".equals(itemName)){
        		stmt.setString(idx++,"%"+itemName+"%");
        	}
        	if(startDate!=null && !"".equals(startDate))
            	stmt.setTimestamp(idx++, convert(sdf.parse(startDate + " 00:00:00.000")));
            if(endDate!=null && !"".equals(endDate))
            	stmt.setTimestamp(idx++, convert(sdf.parse(endDate + " 23:59:59.999")));
            
        }
    	return stmt;
    }

	@Override
	protected String getDBTableName() {
		return null;
	}

}
