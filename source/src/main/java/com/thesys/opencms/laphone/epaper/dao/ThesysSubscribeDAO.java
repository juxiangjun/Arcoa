package com.thesys.opencms.laphone.epaper.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.thesys.opencms.laphone.ThesysLaphoneDAO;

public class ThesysSubscribeDAO extends ThesysLaphoneDAO {
	
	 /** The singleton object. */
    private static ThesysSubscribeDAO m_instance;
	private PreparedStatement pstmt=null;
	private Connection conn = null;
	private ResultSet rs = null;

	ThesysSubscribeVO vo = new ThesysSubscribeVO();
	 

	/**
     * Singleton access.<p>
     * 
     * @return the singleton object
     */
    public static synchronized ThesysSubscribeDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysSubscribeDAO();
        }
        return m_instance;
    }

	//insert
	public int add(ThesysSubscribeVO vo) throws SQLException{
		int r = 0;
		conn = getConnection();
		try{
			String sql ="INSERT INTO LAPHONE_EPAPER_SUBSCRIBE(SITE_ID ,EMAIL,APPLY_SOURCE,APPLY_TIME,SUBSCRIBE_FLAG) VALUES(?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);								 
			int idx = 1;
			pstmt.setString(idx++,vo.getSiteId());
			pstmt.setString(idx++,vo.getEmail());
			pstmt.setString(idx++, vo.getApplySrc());
			pstmt.setTimestamp(idx++,convert(vo.getApplyTime()));
			pstmt.setString(idx++,vo.isSubscribeFlag()?"Y":"N"); 
			r = pstmt.executeUpdate();
		}finally{
			closeAll(conn, pstmt, rs);
		}
		return r;
	}		
	
	//update 
	public int update(ThesysSubscribeVO vo) throws SQLException{
		int r = 0;
		conn = getConnection();
		try{
			String sql ="UPDATE LAPHONE_EPAPER_SUBSCRIBE SET SUBSCRIBE_FLAG=?,APPLY_SOURCE=?,LAST_UPDATE_TIME=?  WHERE EMAIL=?";
			pstmt = conn.prepareStatement(sql);
			int idx = 1;
			pstmt.setString(idx++,vo.isSubscribeFlag()?"Y":"N");
			pstmt.setString(idx++, vo.getApplySrc());
			pstmt.setTimestamp(idx++,convert(vo.getLastUpdateTime()));
			pstmt.setString(idx++,vo.getEmail());
			r = pstmt.executeUpdate();
		}finally{
			closeAll(conn, pstmt, rs);
		}
		return r;
	}
	
	//取得總共有幾筆資料
	public int count(String siteId ,String selStatus) throws SQLException{
		int c = 0;
		String where ="";
		if(!selStatus.equals("")){
			where =" AND SUBSCRIBE_FLAG=? "; 
		}
		String sql ="SELECT COUNT(*)  FROM LAPHONE_EPAPER_SUBSCRIBE WHERE  SITE_ID=? "+where;
		conn = getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, siteId);
			if(!selStatus.equals("")) 
				pstmt.setString(2, selStatus);
		    rs = pstmt.executeQuery();
		    if(rs.next()){
		    	c = rs.getInt(1);
		    }
		}finally{
			closeAll(conn, pstmt, rs);
		}
		return c;
	}
	
	public List<ThesysSubscribeVO> listByPage(String siteId ,int pageSize,int pageIndex, String selStatus) throws SQLException{
        String where1 = "";
        String where2 = "";
        if(!selStatus.equals("")){
			where1 =" SUBSCRIBE_FLAG=? AND "; 
			where2 =" AND SUBSCRIBE_FLAG=? "; 
		}
	    List<ThesysSubscribeVO> result = new ArrayList<ThesysSubscribeVO>();
        try {        	
            conn = getConnection();
            String sql = " SELECT top "+pageSize+" * FROM LAPHONE_EPAPER_SUBSCRIBE WHERE SITE_ID=? AND "+where1+" EMAIL NOT IN (SELECT TOP "+(pageSize*(pageIndex-1)) + " EMAIL FROM LAPHONE_EPAPER_SUBSCRIBE WHERE SITE_ID=? "+where2+" ORDER BY APPLY_TIME)"+
            			 " ORDER BY APPLY_TIME";
            pstmt = conn.prepareStatement(sql);
            int idx =1;
            pstmt.setString(idx++, siteId);
            if(!selStatus.equals(""))
            	pstmt.setString(idx++, selStatus);
            pstmt.setString(idx++, siteId);
            if(!selStatus.equals(""))
            	pstmt.setString(idx++, selStatus);
    		
            rs = pstmt.executeQuery();
            while (rs.next()) {
            	ThesysSubscribeVO item = ThesysSubscribeVO.getInstance(rs);
            	result.add(item);
            }
        } finally {
            closeAll(conn, pstmt, rs);
        }
        return result;
    }
	
	//select all
	public ResultSet find(Connection conn , String siteId ,String selStatus) throws SQLException{
		
		String where ="";
		if(!selStatus.equals("")){
			where =" AND SUBSCRIBE_FLAG=? "; 
		}
		String sql ="select * from LAPHONE_EPAPER_SUBSCRIBE WHERE SITE_ID=? "+where;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, siteId);
			if(!selStatus.equals("")) 
				pstmt.setString(2, selStatus);
		    rs = pstmt.executeQuery();
		}finally{
			 
		}
		return rs;
	}
	
	//select by email
	public ThesysSubscribeVO getRow(String siteId ,String email) throws SQLException{
		ThesysSubscribeVO vo = null;
		String sql ="SELECT * from LAPHONE_EPAPER_SUBSCRIBE WHERE SITE_ID=? AND EMAIL= ? ";
		conn = getConnection();
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, siteId);
			pstmt.setString(2, email);
		    rs = pstmt.executeQuery();
		    if(rs.next()){
		    	vo = ThesysSubscribeVO.getInstance(rs);
		    }
		}finally{
			closeAll(conn, pstmt, rs);
		}
		return vo;
	}

	
	@Override
	protected String getDBTableName() {
		return "LAPHONE_EPAPER_SUBSCRIBE";
	}
	 
}
