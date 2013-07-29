package com.thesys.opencms.laphone.product.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.thesys.opencms.laphone.ThesysLaphoneDAO;

public class ThesysCTRDAO extends ThesysLaphoneDAO {

	 /** The singleton object. */
    private static ThesysCTRDAO m_instance;
	private PreparedStatement pstmt=null;
	private Connection conn = null;
	private ResultSet rs = null;
	 

	/**
     * Singleton access.<p>
     * 
     * @return the singleton object
     */
    public static synchronized ThesysCTRDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysCTRDAO();
        }
        return m_instance;
    }
	
	
	public int add(ThesysCTRVO vo) throws SQLException{
		int res = 0 ;
		conn = getConnection();
		try{ 
			String sql ="INSERT INTO LAPHONE_ITEM_CTR(SITE_ID ,ITEM_ID,CRT_DATE) VALUES(?,?,?)";
			pstmt = conn.prepareStatement(sql);
			int idx = 1;
			pstmt.setString(idx++,vo.getSiteId());
			pstmt.setString(idx++,vo.getItemId());
			pstmt.setTimestamp(idx++,convert(vo.getCreateDate()));
			res = pstmt.executeUpdate();
		}finally{
			closeAll(conn, pstmt, rs);
		}
		return res;
	}
	
	
	
	
	@Override
	protected String getDBTableName() {
		return "LAPHONE_ITEM_CTR";
	}

	

}
