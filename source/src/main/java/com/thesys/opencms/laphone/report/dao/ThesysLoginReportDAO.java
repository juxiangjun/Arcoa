package com.thesys.opencms.laphone.report.dao;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVWriter;

import com.thesys.opencms.laphone.ThesysLaphoneDAO;

public class ThesysLoginReportDAO extends ThesysLaphoneDAO{

	private static ThesysLoginReportDAO m_instance;

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
	
	private ThesysLoginReportDAO() {
		super.init();
	}
	
	/**
     * Singleton access.<p>
     * @return the singleton object
     */
    public static synchronized ThesysLoginReportDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysLoginReportDAO();
        }
        return m_instance;
    }
    
    public List<ThesysLoginReportVO> listByPage(String siteId,int pageSize,int pageIndex,String userId,String startDate, String endDate) throws Exception{
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ThesysLoginReportVO> result = new ArrayList<ThesysLoginReportVO>();
        String where = ((userId!=null && !"".equals(userId))?" AND USERID=?":"") +
        				((startDate!=null && !"".equals(startDate))?" AND LOGIN_DATE>=?":"") + 
        				((endDate!=null && !"".equals(endDate))?" AND LOGIN_DATE<=?":"");
        try {        	
            conn = getConnection();
            String sql = " SELECT top "+pageSize+" * FROM LAPHONE_LOGIN_LOG WHERE  SITE_ID=? "+where+" AND LOGIN_DATE NOT IN (SELECT TOP "+(pageSize*(pageIndex-1)) + " LOGIN_DATE FROM LAPHONE_LOGIN_LOG WHERE SITE_ID=? "+where+"  ORDER BY LOGIN_DATE desc)"+
            			 " ORDER BY LOGIN_DATE desc ";
            stmt = conn.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            if(userId!=null && !"".equals(userId))stmt.setString(idx++, userId);
            if(startDate!=null && !"".equals(startDate))stmt.setTimestamp(idx++, convert(sdf.parse(startDate + " 00:00:00.000")));
            if(endDate!=null && !"".equals(endDate))stmt.setTimestamp(idx++, convert(sdf.parse(endDate + " 23:59:59.999")));
            stmt.setString(idx++, siteId);
            if(userId!=null && !"".equals(userId))stmt.setString(idx++, userId);
            if(startDate!=null && !"".equals(startDate))stmt.setTimestamp(idx++, convert(sdf.parse(startDate + " 00:00:00.000")));
            if(endDate!=null && !"".equals(endDate))stmt.setTimestamp(idx++, convert(sdf.parse(endDate + " 23:59:59.999")));
            rs = stmt.executeQuery();
            while (rs.next()) {
            	ThesysLoginReportVO vo = ThesysLoginReportVO.getInstence(rs);
            	result.add(vo);
            }
        } finally {
            closeAll(conn, stmt, rs);
        }
        return result;
    }
    
    public int count(String siteId,String userId,String startDate, String endDate) throws Exception{
		Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    int result = 0;
	    String where = ((userId!=null && !"".equals(userId))?" AND USERID=?":"") +
				((startDate!=null && !"".equals(startDate))?" AND LOGIN_DATE>=?":"") + 
				((endDate!=null && !"".equals(endDate))?" AND LOGIN_DATE<=?":"");
	    try {        	
	        conn = getConnection();
	        String sql = " SELECT count(*) FROM LAPHONE_LOGIN_LOG WHERE  SITE_ID=? "+where;
	        stmt = conn.prepareStatement(sql);
	        int idx = 1;
	        stmt.setString(idx++, siteId);
	        if(userId!=null && !"".equals(userId))stmt.setString(idx++, userId);
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
    public void export(OutputStream out,String[] headers,String[] columns,String siteId,String userId,String startDate, String endDate) throws Exception{
 		
    	char EOF = (char)0x00;
    	CSVWriter writer = new CSVWriter(new OutputStreamWriter(out),',',EOF , "\r\n");
    	writer.writeNext(headers);

    	SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    	
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String where = ((userId!=null && !"".equals(userId))?" AND USERID=?":"") +
				((startDate!=null && !"".equals(startDate))?" AND LOGIN_DATE>=?":"") + 
				((endDate!=null && !"".equals(endDate))?" AND LOGIN_DATE<=?":"");
        try {
	          conn = getConnection();
	          String sql = " SELECT  * FROM LAPHONE_LOGIN_LOG WHERE  SITE_ID=? "+where;
	          stmt = conn.prepareStatement(sql);
	          int idx = 1;
	          stmt.setString(idx++, siteId);
	          if(userId!=null && !"".equals(userId))stmt.setString(idx++, userId);
	          if(startDate!=null && !"".equals(startDate))stmt.setTimestamp(idx++, convert(sdf.parse(startDate + " 00:00:00.000")));
	          if(endDate!=null && !"".equals(endDate))stmt.setTimestamp(idx++, convert(sdf.parse(endDate + " 23:59:59.999")));
	          rs = stmt.executeQuery();
	          int length = columns.length;
	          while (rs.next()) {
	        	  String[] row = new String[length];
	        	  for(int i=0;i<length;i++){
	        		  String column = columns[i];
	        		  if("LOGIN_DATE".equals(column) || "LOGOUT_DATE".equals(column)){
	        			  if(rs.getTimestamp(column) != null && !"".equals(rs.getTimestamp(column))){
	        				  row[i] = fmt.format(convert(rs.getTimestamp(column)));
	        			  }else{
	        				  row[i] = rs.getString(column);
	        			  }
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
    
    
	public int  login(ThesysLoginReportVO vo) throws SQLException{
		int r = 0 ;
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            con = getConnection(); 
            sql = "INSERT INTO LAPHONE_LOGIN_LOG(SITE_ID,USERID,LOGIN_DATE,LOGIN_IP,SESS_ID)VALUES(?,?,?,?,?)";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, vo.getSiteId());
            stmt.setString(idx++, vo.getUserId());
            stmt.setTimestamp(idx++, convert(vo.getLoginDate()));
            stmt.setString(idx++, vo.getLoginIp());
            stmt.setString(idx++, vo.getSessionId());
    		r = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
		return r;
	}
	
	public int logout(ThesysLoginReportVO vo) throws SQLException{
		int r = 0;
		Connection con = null;
        PreparedStatement stmt = null;
        String sql = "";
		try{
			con = getConnection(); 
			sql ="UPDATE LAPHONE_LOGIN_LOG SET LOGOUT_DATE=?  WHERE SITE_ID=? AND USERID=? AND SESS_ID=?";
			stmt = con.prepareStatement(sql);
			int idx = 1;
			stmt.setTimestamp(idx++, convert(vo.getLogoutDate()));
			stmt.setString(idx++, vo.getSiteId());
			stmt.setString(idx++, vo.getUserId());
			stmt.setString(idx++, vo.getSessionId());
			r = stmt.executeUpdate();
		}finally{
			closeAll(con, stmt, null);
		}
		return r;
	}
	
	
	
	@Override
	protected String getDBTableName() {
		return "LAPHONE_LOGIN_LOG";
	}


}
