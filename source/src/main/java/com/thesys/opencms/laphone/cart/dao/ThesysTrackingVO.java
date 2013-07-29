package com.thesys.opencms.laphone.cart.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.thesys.opencms.dao.ThesysAbstractVO;

/**
 * tracking item vo
 * @author maggie
 *
 */
public class ThesysTrackingVO  extends ThesysAbstractVO{
	public static final String DB_SITE_ID = "SITE_ID";
	public static final String DB_MEM_ID = "MEM_ID";
	public static final String DB_ITEM_ID = "ITEM_ID";
	public static final String DB_CREATE_DATE = "CRT_DATE";
	
	public static final String JSON_ITEM_ID = "itemId";	
	
	/** the site id **/
	private String siteId;
	private String memberId;
	private String itemId;	
	private Date createDate;	

	
	public ThesysTrackingVO(){
		super();
	}
	public static ThesysTrackingVO getInstance(ResultSet rs) throws SQLException{
		ThesysTrackingVO result = new ThesysTrackingVO();
    	result.setSiteId(rs.getString(DB_SITE_ID));    	
    	result.setMemberId(rs.getString(DB_MEM_ID));
    	result.setItemId(rs.getString(DB_ITEM_ID));
    	result.setCreateDate(convert(rs.getTimestamp(DB_CREATE_DATE)));
    	return result;
	}
	public static ThesysTrackingVO getInstance(String siteId,String memberId,String itemId) throws SQLException{
		ThesysTrackingVO result = new ThesysTrackingVO();
    	result.setSiteId(siteId);    	
    	result.setMemberId(memberId);
    	result.setItemId(itemId);
    	return result;
	}
	/**
	 * @return the siteId
	 */
	public String getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the memberId
	 */
	public String getMemberId() {
		return memberId;
	}
	/**
	 * @param memberId the memberId to set
	 */
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
}
