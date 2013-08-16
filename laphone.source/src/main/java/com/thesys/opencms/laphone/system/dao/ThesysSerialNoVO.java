package com.thesys.opencms.laphone.system.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author maggie
 *
 */
public class ThesysSerialNoVO extends ThesysSerialFormatVO{	
	
	private String dateText = "";
	
	private int SerialNumber = 0;
	
	public ThesysSerialNoVO(){
		super();

	}
	public static ThesysSerialNoVO getInstance(ResultSet rs)throws SQLException{
		ThesysSerialNoVO result = new ThesysSerialNoVO();
    	result.setSiteId(rs.getString("SITE_ID"));
    	result.setSerialType(rs.getString("SN_TYPE"));
    	result.setPrefixText(rs.getString("PRE_TXT"));
    	result.setDateText(rs.getString("DTE_TXT"));
    	result.setSerialNumber(rs.getInt("SN_NUM"));
    	return result;
	}

	/**
	 * @return the dateText
	 */
	public String getDateText() {
		return dateText;
	}

	/**
	 * @param dateText the dateText to set
	 */
	public void setDateText(String dateText) {
		this.dateText = dateText;
	}

	/**
	 * @return the serialNumber
	 */
	public int getSerialNumber() {
		return SerialNumber;
	}

	/**
	 * @param serialNumber the serialNumber to set
	 */
	public void setSerialNumber(int serialNumber) {
		SerialNumber = serialNumber;
	}
	
	


	
	
}
