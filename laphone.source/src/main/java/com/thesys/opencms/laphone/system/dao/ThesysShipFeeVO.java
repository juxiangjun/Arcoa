package com.thesys.opencms.laphone.system.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.thesys.opencms.dao.ThesysAbstractVO;

/**
 * shopping cart data vo
 * @author maggie
 *
 */
public class ThesysShipFeeVO extends ThesysAbstractVO{	
	
	private String siteId;
	private int feeType;  //取貨種類: 宅配   超商
	private int conditionEnd; //小於此數 級距
	private int feeAmount;	//	金額
	private String creater;
	private Date createDate;
	private String lastUpdater;
	private Date lastUpdatedDate;
	
	
	
	@Override
	public String toString() {
		return "ThesysShipFeeVO [siteId=" + siteId + ", feeType=" + feeType
				+ ", conditionEnd=" + conditionEnd
				+ ", feeAmount=" + feeAmount + ", creater=" + creater
				+ ", createDate=" + createDate + ", lastUpdater=" + lastUpdater
				+ ", lastUpdatedDate=" + lastUpdatedDate + "]";
	}

	public ThesysShipFeeVO(){
		super();

	}

	public static ThesysShipFeeVO getInstance(ResultSet rs) throws SQLException{
		ThesysShipFeeVO result = new ThesysShipFeeVO();
    	result.setSiteId(rs.getString("SITE_ID")); 
    	result.setFeeType(rs.getInt("FEE_TYPE"));
    	result.setConditionEnd(rs.getInt("COND_END"));
    	result.setFeeAmount(rs.getInt("FEE_AMT"));
    	result.setCreater(rs.getString("CRT_USR_ID")); 
    	result.setCreateDate(convert(rs.getTimestamp("CRT_DATE")));
    	result.setLastUpdater(rs.getString("LM_USR_ID")); 
    	result.setLastUpdatedDate(convert(rs.getTimestamp("LM_DATE")));   
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
	 * @return the feeType
	 */
	public int getFeeType() {
		return feeType;
	}

	/**
	 * @param feeType the feeType to set
	 */
	public void setFeeType(int feeType) {
		this.feeType = feeType;
	}

	/**
	 * @return the conditionEnd
	 */
	public int getConditionEnd() {
		return conditionEnd;
	}

	/**
	 * @param conditionEnd the conditionEnd to set
	 */
	public void setConditionEnd(int conditionEnd) {
		this.conditionEnd = conditionEnd;
	}

	/**
	 * @return the feeAmount
	 */
	public int getFeeAmount() {
		return feeAmount;
	}

	/**
	 * @param feeAmount the feeAmount to set
	 */
	public void setFeeAmount(int feeAmount) {
		this.feeAmount = feeAmount;
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
	 * @return the lastUpdatedDate
	 */
	public Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}

	/**
	 * @param lastUpdatedDate the lastUpdatedDate to set
	 */
	public void setLastUpdatedDate(Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}

	
	
}
