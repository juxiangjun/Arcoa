package com.thesys.opencms.laphone.epaper.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.thesys.opencms.laphone.ThesysLaphoneDAO;

public class ThesysEpaperDAO extends ThesysLaphoneDAO {
	
	 /** The singleton object. */
    private static ThesysEpaperDAO m_instance;
	private PreparedStatement pstmt=null;
	private Connection conn = null;
	private ResultSet rs = null;

	ThesysEpaperVO vo = new ThesysEpaperVO();
	/**
     * Singleton access.<p>
     * 
     * @return the singleton object
     */
    public static synchronized ThesysEpaperDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysEpaperDAO();
        }
        return m_instance;
    }
    

    //取得總共有幾筆資料
  	public int count(String siteId) throws SQLException{
  		int c = 0;
  		String sql ="SELECT COUNT(*)  FROM LAPHONE_EPAPER WHERE  SITE_ID=? ";
  		conn = getConnection();
  		try {
  			pstmt = conn.prepareStatement(sql);
  			pstmt.setString(1, siteId);
  		    rs = pstmt.executeQuery();
  		    if(rs.next()){
  		    	c = rs.getInt(1);
  		    }
  		}finally{
  			closeAll(conn, pstmt, rs);
  		}
  		return c;
  	}
  	
  	public List<ThesysEpaperVO> listByPage(String siteId ,int pageSize,int pageIndex) throws SQLException{
         List<ThesysEpaperVO> result = new ArrayList<ThesysEpaperVO>();
          try {        	
              conn = getConnection();
              String sql = " SELECT top "+pageSize+" * FROM LAPHONE_EPAPER WHERE  SITE_ID=? AND CRT_DATE NOT IN (SELECT TOP "+(pageSize*(pageIndex-1)) + " CRT_DATE FROM LAPHONE_EPAPER WHERE SITE_ID=?  ORDER BY CRT_DATE)"+
              			 " ORDER BY CRT_DATE";
              pstmt = conn.prepareStatement(sql);
              pstmt.setString(1, siteId);
              pstmt.setString(2, siteId);
              rs = pstmt.executeQuery();
              while (rs.next()) { 
            	  ThesysEpaperVO item = ThesysEpaperVO.getInstence(rs);
            	  result.add(item);
              }
          } finally {
              closeAll(conn, pstmt, rs);
          }
          return result;
    }

    public int add(ThesysEpaperVO vo) throws SQLException{
		int r = 0;
		conn = getConnection();
		try{
			String sql ="INSERT INTO LAPHONE_EPAPER(SITE_ID,ENO ,SUBJECT,CRT_USR_ID,CRT_DATE,RELEASE_DATE) VALUES(?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			int idx = 1;
			pstmt.setString(idx++,vo.getSiteId());
			pstmt.setString(idx++, vo.getEno());
			pstmt.setString(idx++,vo.getSubject());
			pstmt.setString(idx++,vo.getCreater());
			pstmt.setTimestamp(idx++,convert(vo.getCreateDate()));
			pstmt.setDate(idx++, new java.sql.Date(vo.getReleaseDate().getTime()));
			r = pstmt.executeUpdate();
		}finally{
			closeAll(conn, pstmt, rs);
		}
		return r;
	}
    
	public ThesysEpaperVO getRow(String siteId ,String eno) throws SQLException{
		ThesysEpaperVO vo = null;
		String sql ="SELECT * FROM LAPHONE_EPAPER WHERE SITE_ID=? AND ENO= ? ";
		conn = getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, siteId);
			pstmt.setString(2, eno);
		    rs = pstmt.executeQuery();
		    if(rs.next()){
		    	vo = ThesysEpaperVO.getInstence(rs);
		    }
		}finally{
			closeAll(conn, pstmt, rs);
		}
		return vo;
	}
   
	public int update(ThesysEpaperVO vo , String newEno) throws SQLException{
		int r = 0;
		conn = getConnection();
		try{
			String sql ="UPDATE LAPHONE_EPAPER SET ENO=?,SUBJECT=?,RELEASE_DATE=?,LM_USR_ID=?,LM_DATE=?  WHERE ENO=?";
			pstmt = conn.prepareStatement(sql);
			int idx = 1;
			pstmt.setString(idx++, newEno);
			pstmt.setString(idx++,vo.getSubject());
			pstmt.setDate(idx++, new java.sql.Date(vo.getReleaseDate().getTime()));
			pstmt.setString(idx++,vo.getLastUpdater());
			pstmt.setTimestamp(idx++,convert(vo.getLastUpdateDate()));
			pstmt.setString(idx++,vo.getEno());
			r = pstmt.executeUpdate();
		}finally{
			closeAll(conn, pstmt, rs);
		}
		return r;
	}

	public int delete(String siteId ,String eno) throws SQLException{
		String sql ="DELETE FROM  LAPHONE_EPAPER WHERE SITE_ID=? AND ENO=?";
		conn = getConnection();
		int r = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, siteId);
			pstmt.setString(2, eno);
            r = pstmt.executeUpdate(); 
		}finally{
			closeAll(conn, pstmt, rs);
		}
		return r;
	}
	
	
	
	@Override
	protected String getDBTableName() {
		return "LAPHONE_EPAPER";
	}



}
