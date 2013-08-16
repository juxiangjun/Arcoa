package com.thesys.opencms.laphone.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.thesys.opencms.dao.ThesysAbstractVO;
import com.thesys.opencms.laphone.promote.dao.ThesysCouponVO;

/**
 * @author kevin
 *
 */
public class ThesysParamVO extends ThesysAbstractVO{
	protected static final String DB_SITE_ID = "SITE_ID";
	protected static final String DB_PARAM_KEY = "PARAM_KEY";
	protected static final String DB_PARAM_VAL = "PARAM_VAL";
	protected static final String DB_PARAM_DESC = "PARAM_DESC";
	
	private String siteId;
	private String paramKey;
	private String paramVal;
	private String paramDesc;
	
	public ThesysParamVO(){
		super();
	}
	
	public static ThesysParamVO getInstance(ResultSet rs) throws SQLException{
		ThesysParamVO result = new ThesysParamVO();
    	result.setSiteId(rs.getString(DB_SITE_ID));
    	result.setParamKey(rs.getString(DB_PARAM_KEY));
    	result.setParamVal(rs.getString(DB_PARAM_VAL));
    	result.setParamDesc(rs.getString(DB_PARAM_DESC));
 	    return result;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
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
	
}
