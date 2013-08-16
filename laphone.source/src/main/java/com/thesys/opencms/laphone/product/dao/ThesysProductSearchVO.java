package com.thesys.opencms.laphone.product.dao;
/**
 * 商品可被搜尋的條件
 */


import com.thesys.opencms.dao.ThesysAbstractVO;

/**
 * @author maggie
 *
 */
public class ThesysProductSearchVO extends ThesysAbstractVO{
	private String siteId;
	private String itemId;
    private String categoryId;
    private String itemName;
    private int specialPrice;
    private String colors;
    private String styles;
    private long startDate;
    private long endDate;
    private boolean onlineFlag;
    private boolean groupFlag;

	

	public ThesysProductSearchVO(){
		super();
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
	 * @return the categoryId
	 */
	public String getCategoryId() {
		return categoryId;
	}



	/**
	 * @param categoryId the categoryId to set
	 */
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}



	/**
	 * @return the itemName
	 */
	public String getItemName() {
		return itemName;
	}



	/**
	 * @param itemName the itemName to set
	 */
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}



	/**
	 * @return the specialPrice
	 */
	public int getSpecialPrice() {
		return specialPrice;
	}



	/**
	 * @param specialPrice the specialPrice to set
	 */
	public void setSpecialPrice(int specialPrice) {
		this.specialPrice = specialPrice;
	}



	/**
	 * @return the colors
	 */
	public String getColors() {
		return colors;
	}



	/**
	 * @param colors the colors to set
	 */
	public void setColors(String colors) {
		this.colors = colors;
	}



	/**
	 * @return the styles
	 */
	public String getStyles() {
		return styles;
	}



	/**
	 * @param styles the styles to set
	 */
	public void setStyles(String styles) {
		this.styles = styles;
	}



	/**
	 * @return the startDate
	 */
	public long getStartDate() {
		return startDate;
	}



	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}



	/**
	 * @return the endDate
	 */
	public long getEndDate() {
		return endDate;
	}



	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}



	/**
	 * @return the onlineFlag
	 */
	public boolean isOnlineFlag() {
		return onlineFlag;
	}



	/**
	 * @param onlineFlag the onlineFlag to set
	 */
	public void setOnlineFlag(boolean onlineFlag) {
		this.onlineFlag = onlineFlag;
	}

	/**
	 * @return the groupFlag
	 */
	public boolean isGroupFlag() {
		return groupFlag;
	}

	/**
	 * @param groupFlag the groupFlag to set
	 */
	public void setGroupFlag(boolean groupFlag) {
		this.groupFlag = groupFlag;
	}



	

	
	
	
	
}
