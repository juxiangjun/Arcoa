package com.thesys.opencms.laphone.product.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.thesys.opencms.dao.ThesysAbstractVO;

/**
 * @author maggie
 *
 */
public class ThesysRatingVO extends ThesysAbstractVO{
	private String siteId;
	private String itemId;
	private int rating;
	private String creater;
	private Date createDate = new java.util.Date();
	private String lastUpdater;
	private Date lastUpdateDate = new java.util.Date();

	

	public ThesysRatingVO(){
		super();

	}

	public static ThesysRatingVO getInstance(ResultSet rs) throws SQLException{
		ThesysRatingVO result = new ThesysRatingVO();
    	result.setSiteId(rs.getString("SITE_ID"));    	
    	result.setItemId(rs.getString("ITEM_ID"));    	
    	result.setRating(rs.getInt("ITEM_RATING"));
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
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * @return the creater
	 */
	public String getCreater() {
		return creater;
	}



	/**
	 * @param creater the creater to set
	 */
	public void setCreater(String creater) {
		this.creater = creater;
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



	/**
	 * @return the lastUpdater
	 */
	public String getLastUpdater() {
		return lastUpdater;
	}



	/**
	 * @param lastUpdater the lastUpdater to set
	 */
	public void setLastUpdater(String lastUpdater) {
		this.lastUpdater = lastUpdater;
	}



	/**
	 * @return the lastUpdateDate
	 */
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}



	/**
	 * @param lastUpdateDate the lastUpdateDate to set
	 */
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	
	
	
}
