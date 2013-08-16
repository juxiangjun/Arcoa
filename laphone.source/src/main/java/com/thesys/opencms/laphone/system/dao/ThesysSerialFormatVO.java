package com.thesys.opencms.laphone.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 
 * @author maggie
 *
 */
public class ThesysSerialFormatVO {	
	public final static String SITE_ID_DEFAULT = "*";
	public final static String SERIAL_TYPE_ORDER = "ORD";
	public final static String SERIAL_TYPE_RETURN = "RTN";
	
	/** the site id **/
	private String siteId;
	
	private String  serialType;
	
	private String prefixText;
	
	private String dateFormat = "yyyymmdd";
	
	private int numberLength = 0;
	
	public ThesysSerialFormatVO(){
		super();

	}
	public static ThesysSerialFormatVO getInstance(ResultSet rs) throws SQLException{
    	ThesysSerialFormatVO result = new ThesysSerialFormatVO();

    	result.setSiteId(rs.getString("SITE_ID"));
    	result.setSerialType(rs.getString("SN_TYPE"));
    	result.setPrefixText(rs.getString("PRE_TXT"));
    	result.setDateFormat(rs.getString("DTE_FMT"));
    	result.setNumberLength(rs.getInt("NUM_LEN"));
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
	 * @return the serialType
	 */
	public String getSerialType() {
		return serialType;
	}
	/**
	 * @param serialType the serialType to set
	 */
	public void setSerialType(String serialType) {
		this.serialType = serialType;
	}
	/**
	 * @return the prefixText
	 */
	public String getPrefixText() {
		return prefixText;
	}
	/**
	 * @param prefixText the prefixText to set
	 */
	public void setPrefixText(String prefixText) {
		this.prefixText = prefixText;
	}
	
	/**
	 * @return the dateFormat
	 */
	public String getDateFormat() {
		return dateFormat;
	}
	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	/**
	 * @return the numberLength
	 */
	public int getNumberLength() {
		return numberLength;
	}
	/**
	 * @param numberLength the numberLength to set
	 */
	public void setNumberLength(int numberLength) {
		this.numberLength = numberLength;
	}
	



	
	
}
