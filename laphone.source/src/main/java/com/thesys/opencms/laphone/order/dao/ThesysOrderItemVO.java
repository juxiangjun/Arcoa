package com.thesys.opencms.laphone.order.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.thesys.opencms.dao.ThesysAbstractVO;





public class ThesysOrderItemVO extends ThesysAbstractVO{	
	
		
	private String siteId;
	private String orderId;
	private String itemId;
	private String itemName;
	private int specialPrice;
	private int quantity;
	private boolean groupFlag = false;
	private boolean couponCountFlag = false;
	private boolean couponUseFlag = false;
	
	
	private boolean cvsShipFlag = true; //只使用不儲存至DB
	private String installments = "1";//只使用不儲存至DB
	
	private List<ThesysOrderItemDetailVO> itemDetailList = new ArrayList<ThesysOrderItemDetailVO>();
	
	
	public ThesysOrderItemVO(){
		super();

	}
	public static ThesysOrderItemVO getInstance(ResultSet rs) throws SQLException{
		ThesysOrderItemVO result = new ThesysOrderItemVO();
		result.setSiteId(rs.getString("SITE_ID"));   
		result.setOrderId(rs.getString("ORD_ID"));   
		result.setItemId(rs.getString("ITEM_ID")); 
		result.setItemName(rs.getString("ITEM_NAME"));  
		result.setSpecialPrice(rs.getInt("SPEC_PRC"));    
		result.setQuantity(rs.getInt("ORD_QTY"));		   
		result.setGroupFlag("Y".equals(rs.getString("GRP_FLAG")));    
		result.setCouponCountFlag("Y".equals(rs.getString("CNT_FLAG")));    
		result.setCouponUseFlag("Y".equals(rs.getString("USE_FLAG"))); 
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
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}


	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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
	 * @return the quantity
	 */
	public int getQuantity() {
		return quantity;
	}


	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}



	/**
	 * @return the cvsShipFlag
	 */
	public boolean isCvsShipFlag() {
		return cvsShipFlag;
	}


	/**
	 * @param cvsShipFlag the cvsShipFlag to set
	 */
	public void setCvsShipFlag(boolean cvsShipFlag) {
		this.cvsShipFlag = cvsShipFlag;
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


	/**
	 * @return the couponCountFlag
	 */
	public boolean isCouponCountFlag() {
		return couponCountFlag;
	}


	/**
	 * @param couponCountFlag the couponCountFlag to set
	 */
	public void setCouponCountFlag(boolean couponCountFlag) {
		this.couponCountFlag = couponCountFlag;
	}


	/**
	 * @return the couponUseFlag
	 */
	public boolean isCouponUseFlag() {
		return couponUseFlag;
	}


	/**
	 * @param couponUseFlag the couponUseFlag to set
	 */
	public void setCouponUseFlag(boolean couponUseFlag) {
		this.couponUseFlag = couponUseFlag;
	}

	/**
	 * @return the itemDetailList
	 */
	public List<ThesysOrderItemDetailVO> getItemDetailList() {
		return itemDetailList;
	}
	/**
	 * @param itemDetailList the itemDetailList to set
	 */
	public void setItemDetailList(List<ThesysOrderItemDetailVO> itemDetailList) {
		this.itemDetailList = itemDetailList;
	}

	public String getInstallments() {
		return installments;
	}
	public void setInstallments(String installments) {
		this.installments = installments;
	}
	
}
