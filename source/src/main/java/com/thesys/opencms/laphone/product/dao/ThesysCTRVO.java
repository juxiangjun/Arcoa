package com.thesys.opencms.laphone.product.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.thesys.opencms.dao.ThesysAbstractVO;

public class ThesysCTRVO extends ThesysAbstractVO{

	private String siteId;
	private String itemId;
	private Date createDate;
	
	public static ThesysRatingVO getInstance(ResultSet rs) throws SQLException{
		ThesysRatingVO result = new ThesysRatingVO();
    	result.setSiteId(rs.getString("SITE_ID"));    	
    	result.setItemId(rs.getString("ITEM_ID"));    	
    	result.setRating(rs.getInt("CRT_DATE"));
    	return result;
	}
	
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	
	
}
