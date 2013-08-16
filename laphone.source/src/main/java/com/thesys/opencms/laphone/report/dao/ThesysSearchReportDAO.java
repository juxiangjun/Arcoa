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

public class ThesysSearchReportDAO extends ThesysLaphoneDAO{

	private static ThesysSearchReportDAO m_instance;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
	
	private ThesysSearchReportDAO() {
		super.init();
	}
	
	/**
     * Singleton access.<p>
     * @return the singleton object
     */
    public static synchronized ThesysSearchReportDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysSearchReportDAO();
        }
        return m_instance;
    }
	
    public List<String[]> listByPage(String siteId,int pageSize,int pageIndex,String startDate, String endDate) throws Exception{
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String s[] =	null;
        List<String[]> result = new ArrayList<String[]>();
        String where = ((startDate!=null && !"".equals(startDate))?" AND CRT_DATE>=?":"") + 
        					((endDate!=null && !"".equals(endDate))?" AND CRT_DATE<=?":"");
        try {        	
            conn = getConnection();
            String sql = " SELECT top "+pageSize+" * FROM LAPHONE_SEARCH_LOG WHERE  SITE_ID=? "+where+" AND CRT_DATE NOT IN (SELECT TOP "+(pageSize*(pageIndex-1)) + " CRT_DATE FROM LAPHONE_SEARCH_LOG WHERE SITE_ID=? "+where+"  ORDER BY CRT_DATE)"+
            			 " ORDER BY CRT_DATE";
            stmt = conn.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            if(startDate!=null && !"".equals(startDate))stmt.setTimestamp(idx++, convert(sdf.parse(startDate + " 00:00:00.000")));
            if(endDate!=null && !"".equals(endDate))stmt.setTimestamp(idx++, convert(sdf.parse(endDate + " 23:59:59.999")));
            stmt.setString(idx++, siteId);
            if(startDate!=null && !"".equals(startDate))stmt.setTimestamp(idx++, convert(sdf.parse(startDate + " 00:00:00.000")));
            if(endDate!=null && !"".equals(endDate))stmt.setTimestamp(idx++, convert(sdf.parse(endDate + " 23:59:59.999")));
            rs = stmt.executeQuery();
            while (rs.next()) {
            	s = new String[2];
            	s[0] = rs.getString("KEYWORD");
            	s[1] = rs.getString("CRT_DATE");
            	result.add(s);
            }
        } finally {
            closeAll(conn, stmt, rs);
        }
        return result;
    }
    
    public int count(String siteId,String startDate, String endDate) throws Exception{
    		Connection conn = null;
		    PreparedStatement stmt = null;
		    ResultSet rs = null;
		    int result = 0;
		    String where = ((startDate!=null && !"".equals(startDate))?" AND CRT_DATE>=?":"") + 
								((endDate!=null && !"".equals(endDate))?" AND CRT_DATE<=?":"");
		    try {        	
		        conn = getConnection();
		        String sql = " SELECT count(*) FROM LAPHONE_SEARCH_LOG WHERE  SITE_ID=? "+where;
		        stmt = conn.prepareStatement(sql);
		        int idx = 1;
		        stmt.setString(idx++, siteId);
		        if(startDate!=null && !"".equals(startDate))stmt.setTimestamp(idx++, convert(sdf.parse(startDate + " 00:00:00.000")));
	            if(endDate!=null && !"".equals(endDate))stmt.setTimestamp(idx++, convert(sdf.parse(endDate + " 23:59:59.999")));
		        rs = stmt.executeQuery();
		        while (rs.next()) {
		        	result = rs.getInt(1);
		        }
		    } finally {
		        closeAll(conn, stmt, rs);
		    }
		    return result;
    }
    
    public void export(OutputStream out,String[] headers,String[] columns,String siteId,String startDate, String endDate) throws Exception{
	 		
    		char EOF = (char)0x00;
    		CSVWriter writer = new CSVWriter(new OutputStreamWriter(out),',',EOF , "\r\n");
    		writer.writeNext(headers);

        	SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    	Connection conn = null;
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        String where = ((startDate!=null && !"".equals(startDate))?" AND CRT_DATE>=?":"") + 
					((endDate!=null && !"".equals(endDate))?" AND CRT_DATE<=?":"");
	        try {
		          conn = getConnection();
		          String sql = " SELECT  * FROM LAPHONE_SEARCH_LOG WHERE  SITE_ID=? "+where;
		          stmt = conn.prepareStatement(sql);
		          int idx = 1;
		          stmt.setString(idx++, siteId);
		          if(startDate!=null && !"".equals(startDate))stmt.setTimestamp(idx++, convert(sdf.parse(startDate + " 00:00:00.000")));
		          if(endDate!=null && !"".equals(endDate))stmt.setTimestamp(idx++, convert(sdf.parse(endDate + " 23:59:59.999")));
		          rs = stmt.executeQuery();
	            int length = columns.length;
	            while (rs.next()) {
	            	String[] row = new String[length];
	            	for(int i=0;i<length;i++){
	            		String column = columns[i];
	            		if("CRT_DATE".equals(column)){
	            			row[i] = fmt.format(convert(rs.getTimestamp(column)));
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
    
	@Override
	protected String getDBTableName() {
		return "LAPHONE_SEARCH_LOG";
	}

}
