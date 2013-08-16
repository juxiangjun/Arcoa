package com.thesys.opencms.laphone.product.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.thesys.opencms.dao.ThesysAbstractVO;

/**
 * @author maggie
 *
 */
public class ThesysStockVO extends ThesysAbstractVO{
	

	public static final String JSON_ITEM_ID = "itemId";
	public static final String JSON_QUANTITY = "quantity";	
	/** the site id **/
	private String siteId;
	/** the item id **/
	private String itemId;
	/** the item quantity **/
	private int quantity;
	/** the data create user**/
	private String creater;
	/** the data created date **/
	private Date createDate = new java.util.Date();
	/** the data last update user**/
	private String lastUpdater;
	/** the data created date **/
	private Date lastUpdateDate = new java.util.Date();

	

	public ThesysStockVO(){
		super();

	}

	public static ThesysStockVO getInstance(ResultSet rs) throws SQLException{
		ThesysStockVO result = new ThesysStockVO();
    	result.setSiteId(rs.getString("SITE_ID"));    	
    	result.setItemId(rs.getString("ITEM_ID"));    	
    	result.setQuantity(rs.getInt("ITEM_QTY"));
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
	 * @return the itemQuantity
	 */
	public int getQuantity() {
		return quantity;
	}



	/**
	 * @param itemQuantity the itemQuantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
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
