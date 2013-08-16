package com.thesys.opencms.laphone.promote.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.thesys.opencms.dao.ThesysAbstractVO;

public class ThesysCouponVO  extends ThesysAbstractVO{	
	protected static final String DB_SITE_ID = "SITE_ID";
	protected static final String DB_COUPON_CODE = "CP_CODE";
	protected static final String DB_MEMBER_ID = "MEM_ID";
	protected static final String DB_COUPON_AMOUNT = "CP_AMT";
	protected static final String DB_DISCOUNT_RATE = "CP_RATE";
	protected static final String DB_START_DATE = "ST_DATE";
	protected static final String DB_END_DATE = "END_DATE";
	protected static final String DB_SOURCE_ORDER_ID = "SRC_ORD_ID";
	protected static final String DB_COUPON_DATE = "CP_DATE";
	protected static final String DB_USED_ORDER_ID = "USED_ORD_ID";
	protected static final String DB_USED_DATE = "USED_DATE";

	

	
	private String siteId;
	private String memberId;
	private String couponCode;
	private int couponAmount;
	private int discountRate = 100;
	private Date startDate;
	private Date endDate;
	private String sourceOrderId;
	private Date couponDate;
	private String usedOrderId;
	private Date usedDate;
	
	public ThesysCouponVO(){
		super();

	}
//	public static ThesysCouponVO getInstance(String jsonText) throws Exception{
//		ThesysCouponVO result = new ThesysCouponVO();
//		JSONObject jsonObj = new JSONObject(jsonText);
//		Iterator<String> it = jsonObj.keys();
//		while(it.hasNext()){
//			String key = it.next();
//			if(key.equals(JSON_COUPON_ID)){
//				result.setCouponId(jsonObj.getInt(key));	
//			}else if(key.equals(JSON_COUPON_CODE)){
//				result.setCouponCode(jsonObj.getString(key));
//			}else if(key.equals(JSON_COUPON_AMOUNT)){
//				result.setCouponAmount(jsonObj.getInt(key));
//			}else if(key.equals(JSON_DISCOUNT_RATE)){
//				result.setDiscountRate(jsonObj.getInt(key));
//			}else if(key.equals(JSON_START_DATE)){
//				long date = jsonObj.getLong(key);
//				if(date!=0)
//					result.setStartDate(new java.util.Date(date));
//			}else if(key.equals(JSON_END_DATE)){
//				long date = jsonObj.getLong(key);
//				if(date!=0)
//					result.setEndDate(new java.util.Date(date));			
//			}else if(key.equals(JSON_MEMBER_ID)){
//				result.setMemberId(jsonObj.getString(key));
//			}
//		}
//		return result;
//	}
	
	public static ThesysCouponVO getInstance(ResultSet rs) throws SQLException{
		ThesysCouponVO result = new ThesysCouponVO();
    	result.setSiteId(rs.getString(DB_SITE_ID));	
    	result.setCouponCode(rs.getString(DB_COUPON_CODE)); 
    	result.setMemberId(rs.getString(DB_MEMBER_ID)); 	
    	result.setCouponAmount(rs.getInt(DB_COUPON_AMOUNT)); 	
    	result.setDiscountRate(rs.getInt(DB_DISCOUNT_RATE)); 
    	result.setStartDate(convert(rs.getTimestamp(DB_START_DATE)));
    	result.setEndDate(convert(rs.getTimestamp(DB_END_DATE)));    	 	
    	result.setSourceOrderId(rs.getString(DB_SOURCE_ORDER_ID));
    	result.setCouponDate(convert(rs.getTimestamp(DB_COUPON_DATE)));
    	result.setUsedOrderId(rs.getString(DB_USED_ORDER_ID));   
    	result.setUsedDate(convert(rs.getTimestamp(DB_USED_DATE)));  
 	    return result;
	}
	public boolean isUsable(){
		return !isExpired() && !isUsed();
	}
	public boolean isExpired(){
		return endDate.getTime() <= new java.util.Date().getTime();
	}
	public boolean isUsed(){
		return getUsedDate()!=null;
	}
	
	public String getStatus(){
		if(getUsedDate()!=null){			
			return "已使用";			
		}else if(isExpired()){
			return "已到期";
		}else{
			return "未使用";
		}
		
	}
	/**
	 * 取得限制最底消費金額
	 * @return
	 */
	public int getLimitAmount(){
		return (int) (getCouponAmount() * 100.0 /getDiscountRate());
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
	 * @return the couponDate
	 */
	public Date getCouponDate() {
		return couponDate;
	}
	/**
	 * @param couponDate the couponDate to set
	 */
	public void setCouponDate(Date couponDate) {
		this.couponDate = couponDate;
	}
	/**
	 * @return the couponCode
	 */
	public String getCouponCode() {
		return couponCode;
	}
	/**
	 * @param couponCode the couponCode to set
	 */
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	/**
	 * @return the couponAmount
	 */
	public int getCouponAmount() {
		return couponAmount;
	}
	/**
	 * @param couponAmount the couponAmount to set
	 */
	public void setCouponAmount(int couponAmount) {
		this.couponAmount = couponAmount;
	}
	/**
	 * @return the discountRate
	 */
	public int getDiscountRate() {
		return discountRate;
	}
	/**
	 * @param discountRate the discountRate to set
	 */
	public void setDiscountRate(int discountRate) {
		this.discountRate = discountRate;
	}
	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the sourceOrderId
	 */
	public String getSourceOrderId() {
		return sourceOrderId;
	}
	/**
	 * @param sourceOrderId the sourceOrderId to set
	 */
	public void setSourceOrderId(String sourceOrderId) {
		this.sourceOrderId = sourceOrderId;
	}
	/**
	 * @return the usedDate
	 */
	public Date getUsedDate() {
		return usedDate;
	}
	/**
	 * @param usedDate the usedDate to set
	 */
	public void setUsedDate(Date usedDate) {
		this.usedDate = usedDate;
	}
	/**
	 * @return the usedOrderId
	 */
	public String getUsedOrderId() {
		return usedOrderId;
	}
	/**
	 * @param usedOrderId the usedOrderId to set
	 */
	public void setUsedOrderId(String usedOrderId) {
		this.usedOrderId = usedOrderId;
	}

	
	
}
	