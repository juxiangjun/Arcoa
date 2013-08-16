package com.thesys.opencms.laphone.order.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.thesys.opencms.dao.ThesysAbstractVO;


public class ThesysOrderCreditVO  extends ThesysAbstractVO{	
	
	
	
	private String siteId;
	private String orderId;
	private int succ;
	private int gwsr;
	private String response_code;
	private String response_msg;
	private String process_date;
	private String process_time;
	private String od_sob;
	private String auth_code;
	private int amount;
	private int stage;
	private int stast;
	private int staed;	
	private String od_hoho;
	private int eci;
	private String rech_key;
	private String inspect;
	private int spcheck;
	private String card6no;
	private String card4no;
	private String expire_dt;
	private String inv_code;
	private String inv_error;
	
	
	
	public ThesysOrderCreditVO(){
		super();

	}
	public static ThesysOrderCreditVO getInstance(ResultSet rs) throws SQLException{
		ThesysOrderCreditVO result = new ThesysOrderCreditVO();
		result.setSiteId(rs.getString("SITE_ID"));   
		result.setOrderId(rs.getString("ORD_ID"));   
		result.setSucc(rs.getInt("SUCC"));  
		result.setGwsr(rs.getInt("GWSR"));  
		result.setResponse_code(rs.getString("RESPONSE_CODE"));  
		result.setResponse_msg(rs.getString("RESPONSE_MSG"));  
		result.setProcess_date(rs.getString("PROCESS_DATE"));  
		result.setProcess_time(rs.getString("PROCESS_TIME"));  
		result.setOd_sob(rs.getString("OD_SOB"));  
		result.setAuth_code(rs.getString("AUTH_CODE"));  
		result.setAmount(rs.getInt("AMOUNT"));  
		result.setStage(rs.getInt("STAGE"));  
		result.setStast(rs.getInt("STAST"));  
		result.setStaed(rs.getInt("STAED"));  	
		result.setOd_hoho(rs.getString("OD_HOHO"));  
		result.setEci(rs.getInt("ECI"));  
		result.setRech_key(rs.getString("RECH_KEY"));  
		result.setInspect(rs.getString("INSPECT"));  
		result.setSpcheck(rs.getInt("SPCHECK"));  
		result.setCard6no(rs.getString("CARD6NO"));  
		result.setCard4no(rs.getString("CARD4NO"));  
		result.setExpire_dt(rs.getString("EXPIRE_DT"));  
		result.setInv_code(rs.getString("INV_CODE"));  
		result.setInv_error(rs.getString("INV_ERROR"));  
    	return result;
	}

	public boolean isAuthSuccess(){
		return 1==succ;
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
	 * @return the succ
	 */
	public int getSucc() {
		return succ;
	}



	/**
	 * @param succ the succ to set
	 */
	public void setSucc(int succ) {
		this.succ = succ;
	}
	
	


	/**
	 * @return the gwsr
	 */
	public int getGwsr() {
		return gwsr;
	}



	/**
	 * @param gwsr the gwsr to set
	 */
	public void setGwsr(int gwsr) {
		this.gwsr = gwsr;
	}



	/**
	 * @return the response_code
	 */
	public String getResponse_code() {
		return response_code;
	}



	/**
	 * @param response_code the response_code to set
	 */
	public void setResponse_code(String response_code) {
		this.response_code = response_code;
	}



	/**
	 * @return the response_msg
	 */
	public String getResponse_msg() {
		return response_msg;
	}



	/**
	 * @param response_msg the response_msg to set
	 */
	public void setResponse_msg(String response_msg) {
		this.response_msg = response_msg;
	}



	/**
	 * @return the process_date
	 */
	public String getProcess_date() {
		return process_date;
	}



	/**
	 * @param process_date the process_date to set
	 */
	public void setProcess_date(String process_date) {
		this.process_date = process_date;
	}



	/**
	 * @return the process_time
	 */
	public String getProcess_time() {
		return process_time;
	}



	/**
	 * @param process_time the process_time to set
	 */
	public void setProcess_time(String process_time) {
		this.process_time = process_time;
	}



	/**
	 * @return the od_sob
	 */
	public String getOd_sob() {
		return od_sob;
	}



	/**
	 * @param od_sob the od_sob to set
	 */
	public void setOd_sob(String od_sob) {
		this.od_sob = od_sob;
	}



	/**
	 * @return the auth_code
	 */
	public String getAuth_code() {
		return auth_code;
	}



	/**
	 * @param auth_code the auth_code to set
	 */
	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}



	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}



	/**
	 * @param amount the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}



	/**
	 * @return the stage
	 */
	public int getStage() {
		return stage;
	}



	/**
	 * @param stage the stage to set
	 */
	public void setStage(int stage) {
		this.stage = stage;
	}



	/**
	 * @return the stast
	 */
	public int getStast() {
		return stast;
	}



	/**
	 * @param stast the stast to set
	 */
	public void setStast(int stast) {
		this.stast = stast;
	}



	/**
	 * @return the staed
	 */
	public int getStaed() {
		return staed;
	}



	/**
	 * @param staed the staed to set
	 */
	public void setStaed(int staed) {
		this.staed = staed;
	}



	/**
	 * @return the od_hoho
	 */
	public String getOd_hoho() {
		return od_hoho;
	}



	/**
	 * @param od_hoho the od_hoho to set
	 */
	public void setOd_hoho(String od_hoho) {
		this.od_hoho = od_hoho;
	}



	/**
	 * @return the eci
	 */
	public int getEci() {
		return eci;
	}



	/**
	 * @param eci the eci to set
	 */
	public void setEci(int eci) {
		this.eci = eci;
	}



	/**
	 * @return the rech_key
	 */
	public String getRech_key() {
		return rech_key;
	}



	/**
	 * @param rech_key the rech_key to set
	 */
	public void setRech_key(String rech_key) {
		this.rech_key = rech_key;
	}



	/**
	 * @return the inspect
	 */
	public String getInspect() {
		return inspect;
	}



	/**
	 * @param inspect the inspect to set
	 */
	public void setInspect(String inspect) {
		this.inspect = inspect;
	}



	/**
	 * @return the spcheck
	 */
	public int getSpcheck() {
		return spcheck;
	}



	/**
	 * @param spcheck the spcheck to set
	 */
	public void setSpcheck(int spcheck) {
		this.spcheck = spcheck;
	}



	/**
	 * @return the card6no
	 */
	public String getCard6no() {
		return card6no;
	}



	/**
	 * @param card6no the card6no to set
	 */
	public void setCard6no(String card6no) {
		this.card6no = card6no;
	}



	/**
	 * @return the card4no
	 */
	public String getCard4no() {
		return card4no;
	}



	/**
	 * @param card4no the card4no to set
	 */
	public void setCard4no(String card4no) {
		this.card4no = card4no;
	}



	/**
	 * @return the expire_dt
	 */
	public String getExpire_dt() {
		return expire_dt;
	}



	/**
	 * @param expire_dt the expire_dt to set
	 */
	public void setExpire_dt(String expire_dt) {
		this.expire_dt = expire_dt;
	}



	/**
	 * @return the inv_code
	 */
	public String getInv_code() {
		return inv_code;
	}



	/**
	 * @param inv_code the inv_code to set
	 */
	public void setInv_code(String inv_code) {
		this.inv_code = inv_code;
	}



	/**
	 * @return the inv_error
	 */
	public String getInv_error() {
		return inv_error;
	}



	/**
	 * @param inv_error the inv_error to set
	 */
	public void setInv_error(String inv_error) {
		this.inv_error = inv_error;
	}

	
	
	

	

	
}
