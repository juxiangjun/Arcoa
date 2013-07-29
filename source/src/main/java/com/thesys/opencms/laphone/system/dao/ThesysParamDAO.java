package com.thesys.opencms.laphone.system.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.thesys.opencms.laphone.ThesysLaphoneDAO;

public class ThesysParamDAO extends ThesysLaphoneDAO {
	private static ThesysParamDAO m_instance;
	
	private ThesysParamDAO() {
		super.init();
	}
	
	/**
	 * Singleton access.<p>
	 * 
	 * @return the singleton object
	 */
	public static synchronized ThesysParamDAO getInstance() throws SQLException {
        if (m_instance == null)
            m_instance = new ThesysParamDAO();
        return m_instance;
    }
	
	public int insert(ThesysParamVO vo) throws SQLException {
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String tableName = getDBTableName();
        try {
            con = getConnection();
            String sql = "INSERT INTO "+ tableName + " (SITE_ID, PARAM_KEY, PARAM_VAL, PARAM_DESC) "+
                         "VALUES(?,?,?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, vo.getSiteId());
            stmt.setString(2, vo.getParamKey());
            stmt.setString(3, vo.getParamVal());
            stmt.setString(4, vo.getParamDesc());
            int result = stmt.executeUpdate();
            return result;
        } finally {
            closeAll(con, stmt, rs);
        }
	}
	
	public int delete(String siteId, String paramKey) throws SQLException {
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String tableName = getDBTableName();
        try {
            con = getConnection();
            String sql = "DELETE "+ tableName + " WHERE SITE_ID=? AND PARAM_KEY=?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            stmt.setString(2, paramKey);
            int result = stmt.executeUpdate();
            return result;
        } finally {
            closeAll(con, stmt, rs);
        }
	}
	
	public int update(ThesysParamVO vo) throws SQLException {
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String tableName = getDBTableName();
        try {
            con = getConnection();
            String sql = "UPDATE "+ tableName + " SET PARAM_VAL=?,PARAM_DESC=? WHERE SITE_ID=? AND PARAM_KEY=?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, vo.getParamVal());
            stmt.setString(2, vo.getParamDesc());
            stmt.setString(3, vo.getSiteId());
            stmt.setString(4, vo.getParamKey());
            int result = stmt.executeUpdate();
            return result;
        } finally {
            closeAll(con, stmt, rs);
        }
	}
	
	public ThesysParamVO getParam(String siteId, String paramKey) throws SQLException {
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String tableName = getDBTableName();
        ThesysParamVO result = null;
        try {
            con = getConnection();
            String sql = " SELECT * FROM "+tableName + " WHERE SITE_ID=? AND PARAM_KEY=?";
            stmt = con.prepareStatement(sql);
            stmt.setString(1, siteId);
            stmt.setString(2, paramKey);
            rs = stmt.executeQuery();
            if (rs.next()) {
            	result = ThesysParamVO.getInstance(rs);
            }
        } finally {
                closeAll(con, stmt, rs);
        }
        return result;
	}
	
	public int count(String siteId, String searchParamKey) throws SQLException {
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String tableName = getDBTableName();
        int result = 0;
        try {
        	con = getConnection();
            String sql = " SELECT count(*) FROM "+tableName;
            if(searchParamKey!=null && !"".equals(searchParamKey))
            	sql += " WHERE PARAM_KEY like ?";
            stmt = con.prepareStatement(sql);
            if(searchParamKey!=null && !"".equals(searchParamKey))
            	stmt.setString(1, "%" + searchParamKey + "%");
            rs = stmt.executeQuery();
            if(rs.next())
            	result = rs.getInt(1);
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
	}
	
	public List<ThesysParamVO> listByAll() throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String tableName = getDBTableName();
        List<ThesysParamVO> result = new ArrayList<ThesysParamVO>();
        try {
            con = getConnection();
            String sql = " SELECT * FROM "+tableName;
            stmt = con.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
            	ThesysParamVO item = ThesysParamVO.getInstance(rs);
            	result.add(item);
            }
        } finally {
                closeAll(con, stmt, rs);
        }
        return result;
	}
	
	public List<ThesysParamVO> listByPage(String siteId, String searchKey, int pageSize,int pageIndex) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String tableName = getDBTableName();
        List<ThesysParamVO> result = new ArrayList<ThesysParamVO>();
        try {
        	boolean searchFlag = false;
        	String searchStr = "";
        	if(searchKey!=null && !"".equals(searchKey)){
        		searchFlag = true;
        		searchStr = " AND PARAM_KEY LIKE ? ";
        	}
            con = getConnection();
            String sql = " SELECT TOP "+pageSize+" * FROM "+tableName+
		       			 " WHERE SITE_ID=? "+searchStr+" AND PARAM_KEY NOT IN ( SELECT TOP "+(pageSize*(pageIndex-1)) + " PARAM_KEY FROM "+tableName+" WHERE SITE_ID=? "+searchStr+" ORDER BY PARAM_KEY ASC)"+
		       			 " ORDER BY PARAM_KEY ASC";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            if(searchFlag) stmt.setString(idx++, "%"+searchKey+"%");
            stmt.setString(idx++, siteId);
            if(searchFlag) stmt.setString(idx++, "%"+searchKey+"%");
            rs = stmt.executeQuery();
            while (rs.next()) {
            	ThesysParamVO item = ThesysParamVO.getInstance(rs);
            	result.add(item);
            }
        } finally {
                closeAll(con, stmt, rs);
        }
        return result;
	}

	/* (non-Javadoc)
	 * @see com.thesys.opencms.dao.ThesysAbstractDAO#getDBTableName()
	 */
	@Override
	protected String getDBTableName() {
		return "LAPHONE_SYS_PARAM";
	}

	
}
