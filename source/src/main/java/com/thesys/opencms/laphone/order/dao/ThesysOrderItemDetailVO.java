package com.thesys.opencms.laphone.order.dao;


import java.sql.ResultSet;
import java.sql.SQLException;

import com.thesys.opencms.dao.ThesysAbstractVO;





public class ThesysOrderItemDetailVO extends ThesysAbstractVO{	
	
		
	private String siteId;
	private String orderId;
	private String groupId;
	private String detailId;
	private int specialPrice;
	private int groupPrice;
	private int groupQuantity;
	private boolean couponCountFlag = false;
	
	
	public ThesysOrderItemDetailVO(){
		super();

	}
	public static ThesysOrderItemDetailVO getInstance(ResultSet rs) throws SQLException{
		ThesysOrderItemDetailVO result = new ThesysOrderItemDetailVO();
		result.setSiteId(rs.getString("SITE_ID"));   
		result.setOrderId(rs.getString("ORD_ID"));   
		result.setGroupId(rs.getString("GRP_ID"));  
		result.setDetailId(rs.getString("DTL_ID"));  
		result.setSpecialPrice(rs.getInt("SPEC_PRC"));   
		result.setGroupPrice(rs.getInt("GRP_PRC"));    
		result.setGroupQuantity(rs.getInt("GRP_QTY"));	
		result.setCouponCountFlag("Y".equals(rs.getString("CNT_FLAG")));    	
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
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	/**
	 * @return the detailId
	 */
	public String getDetailId() {
		return detailId;
	}
	/**
	 * @param detailId the detailId to set
	 */
	public void setDetailId(String detailId) {
		this.detailId = detailId;
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
	 * @return the groupPrice
	 */
	public int getGroupPrice() {
		return groupPrice;
	}
	/**
	 * @param groupPrice the groupPrice to set
	 */
	public void setGroupPrice(int groupPrice) {
		this.groupPrice = groupPrice;
	}
	/**
	 * @return the groupQuantity
	 */
	public int getGroupQuantity() {
		return groupQuantity;
	}
	/**
	 * @param groupQuantity the groupQuantity to set
	 */
	public void setGroupQuantity(int groupQuantity) {
		this.groupQuantity = groupQuantity;
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
	
	
	

	

	
}
