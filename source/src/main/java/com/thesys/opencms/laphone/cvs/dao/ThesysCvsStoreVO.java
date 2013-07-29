package com.thesys.opencms.laphone.cvs.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.thesys.opencms.dao.ThesysAbstractVO;

public class ThesysCvsStoreVO  extends ThesysAbstractVO{

	private String siteId;
	private String storeNo;
	private String storeType;
	private String storeName;
	private String telphone;
	private String city;
	private String country;
	private String address;
	private String zipCode;
	private String dcroNo;
	private Date  startDate;
	private Date  endDate;
	private String creater;
	private Date createDate;
	
	
	public static ThesysCvsStoreVO getInstance(ResultSet rs) throws SQLException{
		
		Date startDate = null;
        Date endDate = null;
        
        if(rs.getDate("SDATE") != null)
        	startDate = new Date(rs.getDate("SDATE").getTime());
        if(rs.getDate("EDATE") != null)
        	endDate = new Date(rs.getDate("EDATE").getTime());
		
		ThesysCvsStoreVO result = new ThesysCvsStoreVO();    	
		result.setSiteId(rs.getString("SITE_ID")); 
		result.setStoreNo(rs.getString("STNO")); 
		result.setStoreType(rs.getString("STTYPE")); 
		result.setStoreName(rs.getString("STNM")); 
		result.setTelphone(rs.getString("STTEL")); 
		result.setCity(rs.getString("STCITY")); 
		result.setCountry(rs.getString("STCNTRY")); 
		result.setAddress(rs.getString("STADR")); 
		result.setZipCode(rs.getString("ZIPCD"));
		result.setDcroNo(rs.getString("DCRONO"));
		result.setStartDate(startDate);
		result.setEndDate(endDate);
		result.setCreater(rs.getString("CRT_USR_ID"));
		result.setCreateDate(convert(rs.getTimestamp("CRT_DATE")));
    	return result;
	}
	
	@Override
	public String toString() {
		return "ThesysCvsStoreVO [siteId=" + siteId + ", storeNo=" + storeNo
				+ ", storeType=" + storeType + ", storeName=" + storeName
				+ ", telphone=" + telphone + ", city=" + city + ", country="
				+ country + ", address=" + address + ", zipCode=" + zipCode
				+ ", dcroNo=" + dcroNo + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", creater=" + creater
				+ ", createDate=" + createDate + "]";
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
	 * @return the storeNo
	 */
	public String getStoreNo() {
		return storeNo;
	}
	/**
	 * @param storeNo the storeNo to set
	 */
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	/**
	 * @return the storeType
	 */
	public String getStoreType() {
		return storeType;
	}
	/**
	 * @param storeType the storeType to set
	 */
	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}
	/**
	 * @return the storeName
	 */
	public String getStoreName() {
		return storeName;
	}
	/**
	 * @param storeName the storeName to set
	 */
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	/**
	 * @return the telphone
	 */
	public String getTelphone() {
		return telphone;
	}
	/**
	 * @param telphone the telphone to set
	 */
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}
	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}
	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	
	public String getDcroNo() {
		return dcroNo;
	}
	public void setDcroNo(String dcroNo) {
		this.dcroNo = dcroNo;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
