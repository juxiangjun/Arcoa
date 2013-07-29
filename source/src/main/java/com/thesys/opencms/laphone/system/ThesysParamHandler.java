package com.thesys.opencms.laphone.system;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;
import com.thesys.opencms.laphone.system.dao.ThesysParamVO;

public class ThesysParamHandler extends ThesysLaphoneHandler {
	protected static final Log LOG = CmsLog.getLog(ThesysParamHandler.class);
	private String paramKey;
	private String paramVal;
	private String paramDesc;
	private String searchParamKey;
	private static HashMap<String,String> m_data = null;
	
	public ThesysParamHandler(){}
	public ThesysParamHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception {
		super(context, req, res);
		init(context, req, res);	
	}
	public void init(PageContext context, HttpServletRequest req,HttpServletResponse res) {
		try {
			if(m_data == null)
				updateParamData();
		} catch (SQLException e) {}
		super.init(context, req, res);
	}
	/**
	 * 更新 m_data 內容
	 * @throws SQLException
	 */
	private void updateParamData() throws SQLException {
		m_data = new HashMap<String,String>();
		List<ThesysParamVO> list = ThesysParamDAO.getInstance().listByAll();
		for(int i=0;i<list.size();i++) {
			ThesysParamVO vo = (ThesysParamVO)list.get(i);
			m_data.put(vo.getSiteId()+"_"+vo.getParamKey(), vo.getParamVal());
		}
	}
	
	public List<ThesysParamVO> getPageList(){
		List<ThesysParamVO> result = new ArrayList<ThesysParamVO>();
		try{
			result = ThesysParamDAO.getInstance().listByPage(getSiteId(), getSearchParamKey(), getPageSize(), getPageIndex());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	
	public int insert(){
		ThesysParamVO vo = new ThesysParamVO();
		vo.setSiteId(getSiteId());
		vo.setParamKey(getParamKey());
		vo.setParamVal(getParamVal());
		vo.setParamDesc(getParamDesc());
		try {
			int result = ThesysParamDAO.getInstance().insert(vo);
			if(result>0)
				updateParamData();
			return result;
		} catch (SQLException e) {
			LOG.error(e, e.fillInStackTrace());
			e.printStackTrace();
		}
		return 0;
	}
	
	public int delete(){
		try {
			int result = ThesysParamDAO.getInstance().delete(getSiteId(), getParamKey());
			if(result>0)
				updateParamData();
			return result;
		} catch (SQLException e) {
			LOG.error(e, e.fillInStackTrace());
			e.printStackTrace();
		}
		return 0;
	}
	
	public int update(){
		ThesysParamVO vo = new ThesysParamVO();
		vo.setSiteId(getSiteId());
		vo.setParamKey(getParamKey());
		vo.setParamVal(getParamVal());
		vo.setParamDesc(getParamDesc());
		try {
			int result = ThesysParamDAO.getInstance().update(vo);
			if(result>0)
				updateParamData();
			return result;
		} catch (SQLException e) {
			LOG.error(e, e.fillInStackTrace());
			e.printStackTrace();
		}
		return 0;
	}
	
	public ThesysParamVO getParam(){
		try {
			return ThesysParamDAO.getInstance().getParam(getSiteId(), getParamKey());
		} catch (SQLException e) {
			LOG.error(e, e.fillInStackTrace());
			e.printStackTrace();
		}
		return null;
	}
	
	public String getParamVal(String siteId, String paramKey) throws SQLException {
		if(siteId!=null && paramKey!=null){
			String key = siteId+"_"+paramKey;
			if(m_data.containsKey(key)) {
				return (String)m_data.get(siteId+"_"+paramKey);
			} else {
				ThesysParamVO vo = ThesysParamDAO.getInstance().getParam(siteId, paramKey);
				if(vo!=null)
					return vo.getParamVal();
			}
		}
		return null;
	}
	
	public String getValueByParamKey(String paramKey) throws SQLException{
		return getParamVal(getSiteId(), paramKey);
	}
	
	public int getCount(){
		try {
			return ThesysParamDAO.getInstance().count(getSiteId(), getParamKey());	
		} catch (SQLException e) {
			LOG.error(e, e.fillInStackTrace());
			e.printStackTrace();
		}
		return 0;
	}
	
	public String getParamKey() {
		return paramKey;
	}
	public void setParamKey(String paramKey) {
		if(paramKey!=null)
			paramKey = paramKey.toUpperCase().replace(" ", "_");
		this.paramKey = paramKey;
	}
	public String getParamVal() {
		return paramVal;
	}
	public void setParamVal(String paramVal) {
		this.paramVal = paramVal;
	}
	public String getParamDesc() {
		return paramDesc;
	}
	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}
	public String getSearchParamKey() {
		return searchParamKey;
	}
	public void setSearchParamKey(String searchParamKey) {
		this.searchParamKey = searchParamKey;
	}
}
