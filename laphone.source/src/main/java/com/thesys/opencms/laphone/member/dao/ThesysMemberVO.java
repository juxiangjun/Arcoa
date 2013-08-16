package com.thesys.opencms.laphone.member.dao;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.thesys.opencms.dao.ThesysAbstractVO;

public class ThesysMemberVO extends ThesysAbstractVO{
	
	/** E-mail驗證狀態：未驗證 **/
	public final static int MAIL_STATUS_NOT_VERIFIED = 0;
	/** E-mail驗證狀態：已驗證 **/
	public final static int MAIL_STATUS_VERIFIED = 0;
	/** 會員驗證狀態：已驗證 **/
	public final static int STATUS_VERIFIED = 0;
	/** 會員驗證狀態：黑名單 **/
	public final static int STATUS_BLACK = 1;
	/** 會員驗證狀態：未驗證 **/
	public final static int STATUS_NOT_VERIFIED = 2;
	
	private String siteId;
	private int memberNo;
	private String accountId; 
	private String username;
	private int idType;
	private String idNo;
	private String pwd;
	private String email;
	private int gender;
	private String cellphone;
	private String zipCode;
	private String zipCounty;
	private String zipArea;
	private String fullAddress;
	private int edm =4;
	private String birthday;
	private String smsVerifyCode;
	private String mailVerifyCode;
	private int mailStatus=0; //mail狀態預設0未驗證
	private int status=2;//狀態預設2 簡訊未驗證
	private String englishName;
	private int income;
	private int occupation;
	private int marriage;
	private int cellphoneBrands;
	private int education;
	private int offspring;
	private String preferences;
	private java.util.Date createDate;
	private java.util.Date lastLoginDate;
	private java.util.Date lastUpdatedDate;
	private java.util.Date smsVerifyDeadline;
	private java.util.Date lastChangePwdDate;
	private String lastUpdater;
	private String registerIP;
	private String cardId;
	private java.util.Date useDate;
	private boolean oldMemberFlag ;
	private boolean emailFlag = false;
	private boolean mobileFlag = true;
	
	
	public ThesysMemberVO(){}
	
	public static ThesysMemberVO getInstance(ResultSet rs) throws Exception{
		ThesysMemberVO vo = new ThesysMemberVO();
		vo.setSiteId(rs.getString("SITE_ID"));
		vo.setMemberNo(rs.getInt("SNO"));
		vo.setAccountId(rs.getString("ACCOUNTID"));
		vo.setUsername(rs.getString("USERNAME"));
		vo.setIdType(rs.getInt("IDTYPE"));
		vo.setIdNo(rs.getString("IDNO"));
		vo.setPwd(rs.getString("PWD"));
		vo.setEmail(rs.getString("EMAIL"));
		vo.setGender(rs.getInt("GENDER"));
		vo.setCellphone(rs.getString("CELLPHONE"));
		vo.setZipCode(rs.getString("ZIP_CODE"));
		vo.setZipCounty(rs.getString("ZIP_COUNTY"));
		vo.setZipArea(rs.getString("ZIP_AREA"));
		vo.setFullAddress(rs.getString("FULLADDRESS"));
		vo.setEdm(rs.getInt("EDM"));
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
		String birthday = fmt.format(new java.util.Date(rs.getDate("BIRTHDAY").getTime()));
		vo.setBirthday(birthday);
		vo.setSmsVerifyCode(rs.getString("SMS_VERIFI_CODE"));
		vo.setMailVerifyCode(rs.getString("MAIL_VERIFI_CODE"));
		vo.setMailStatus(rs.getInt("MAILSTATUS"));
		vo.setStatus(rs.getInt("STATUS"));
		vo.setEnglishName(rs.getString("ENG_NAME"));
		vo.setIncome(rs.getInt("INCOME"));
		vo.setOccupation(rs.getInt("OCCUPATION"));
		vo.setMarriage(rs.getInt("MARRIAGE"));
		vo.setCellphoneBrands(rs.getInt("CELLPHONEBRANDS"));
		vo.setEducation(rs.getInt("EDUCATION"));
		vo.setOffspring(rs.getInt("OFFSPRING"));
		vo.setPreferences(rs.getString("PREFERENCES"));
		vo.setCreateDate(convert(rs.getTimestamp("CRT_DATE")));
		vo.setLastLoginDate(convert(rs.getTimestamp("LOGIN_DATE")));
		vo.setLastUpdater(rs.getString("LM_USR_ID"));
		vo.setLastUpdatedDate(convert(rs.getTimestamp("LM_DATE")));
		vo.setSmsVerifyDeadline(convert(rs.getTimestamp("SMS_VERIFI_DEADLINE")));
		vo.setLastChangePwdDate(convert(rs.getTimestamp("LM_PWD_DATE")));
		vo.setRegisterIP(rs.getString("REGISTER_IP"));
		vo.setCardId(rs.getString("CARDID"));
		vo.setUseDate(convert(rs.getTimestamp("USE_DATE")));
		vo.setOldMemberFlag(rs.getString("OLD_MEMBER_FLAG").equals("Y"));
		vo.setEmailFlag(rs.getInt("EDM") == 1 || rs.getInt("EDM") == 2 );
		vo.setMobileFlag(rs.getInt("EDM") == 1 || rs.getInt("EDM") == 3 );	
		return vo;
	}
	/**
	 * 最後修改密碼日期是否超過90天
	 * @return
	 */
	public boolean isOverPasswordDate(){
		Calendar cal = Calendar.getInstance();
		cal.setTime(new java.util.Date());
		cal.add(Calendar.DATE, -90); //超過90天;
		if(getLastChangePwdDate().before(cal.getTime())){
			return true;
		}else{
			return false;
		}
	}

	public int getMemberNo() {
		return memberNo;
	}



	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}



	public String getAccountId() {
		return accountId;
	}



	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public int getIdType() {
		return idType;
	}



	public void setIdType(int idType) {
		this.idType = idType;
	}



	public String getIdNo() {
		return idNo;
	}



	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}



	public String getPwd() {
		return pwd;
	}



	public void setPwd(String pwd) {
		this.pwd = pwd;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public int getGender() {
		return gender;
	}



	public void setGender(int gender) {
		this.gender = gender;
	}



	public String getCellphone() {
		return cellphone;
	}



	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}



	public String getZipCode() {
		return zipCode;
	}



	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}



	public String getZipCounty() {
		return zipCounty;
	}



	public void setZipCounty(String zipCounty) {
		this.zipCounty = zipCounty;
	}



	public String getZipArea() {
		return zipArea;
	}



	public void setZipArea(String zipArea) {
		this.zipArea = zipArea;
	}



	public String getFullAddress() {
		return fullAddress;
	}



	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}



	public int getEdm() {
		return edm;
	}



	public void setEdm(int edm) {
		this.edm = edm;
	}



	public String getBirthday() {
		return birthday;
	}



	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}



	public String getSmsVerifyCode() {
		return smsVerifyCode;
	}



	public void setSmsVerifyCode(String smsVerifyCode) {
		this.smsVerifyCode = smsVerifyCode;
	}



	public String getMailVerifyCode() {
		return mailVerifyCode;
	}



	public void setMailVerifyCode(String mailVerifyCode) {
		this.mailVerifyCode = mailVerifyCode;
	}



	public int getMailStatus() {
		return mailStatus;
	}



	public void setMailStatus(int mailStatus) {
		this.mailStatus = mailStatus;
	}



	public int getStatus() {
		return status;
	}



	public void setStatus(int status) {
		this.status = status;
	}



	public String getEnglishName() {
		return englishName;
	}



	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}



	public int getIncome() {
		return income;
	}



	public void setIncome(int income) {
		this.income = income;
	}



	public int getOccupation() {
		return occupation;
	}



	public void setOccupation(int occupation) {
		this.occupation = occupation;
	}



	public int getMarriage() {
		return marriage;
	}



	public void setMarriage(int marriage) {
		this.marriage = marriage;
	}



	public int getCellphoneBrands() {
		return cellphoneBrands;
	}



	public void setCellphoneBrands(int cellphoneBrands) {
		this.cellphoneBrands = cellphoneBrands;
	}



	public int getEducation() {
		return education;
	}



	public void setEducation(int education) {
		this.education = education;
	}



	public int getOffspring() {
		return offspring;
	}



	public void setOffspring(int offspring) {
		this.offspring = offspring;
	}



	public String getPreferences() {
		return preferences;
	}



	public void setPreferences(String preferences) {
		this.preferences = preferences;
	}



	public java.util.Date getCreateDate() {
		return createDate;
	}



	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}



	public java.util.Date getLastLoginDate() {
		return lastLoginDate;
	}



	public void setLastLoginDate(java.util.Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}



	public java.util.Date getLastUpdatedDate() {
		return lastUpdatedDate;
	}



	public void setLastUpdatedDate(java.util.Date lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}



	public java.util.Date getSmsVerifyDeadline() {
		return smsVerifyDeadline;
	}



	public void setSmsVerifyDeadline(java.util.Date smsVerifyDeadline) {
		this.smsVerifyDeadline = smsVerifyDeadline;
	}



	public java.util.Date getLastChangePwdDate() {
		return lastChangePwdDate;
	}



	public void setLastChangePwdDate(java.util.Date lastChangePwdDate) {
		this.lastChangePwdDate = lastChangePwdDate;
	}



	public String getLastUpdater() {
		return lastUpdater;
	}



	public void setLastUpdater(String lastUpdater) {
		this.lastUpdater = lastUpdater;
	}



	public String getRegisterIP() {
		return registerIP;
	}



	public void setRegisterIP(String registerIP) {
		this.registerIP = registerIP;
	}



	public String getCardId() {
		return cardId;
	}



	public void setCardId(String cardId) {
		this.cardId = cardId;
	}



	public java.util.Date getUseDate() {
		return useDate;
	}



	public void setUseDate(java.util.Date useDate) {
		this.useDate = useDate;
	}



	public boolean isOldMemberFlag() {
		return oldMemberFlag;
	}



	public void setOldMemberFlag(boolean oldMemberFlag) {
		this.oldMemberFlag = oldMemberFlag;
	}

	public boolean isEmailFlag() {
		return emailFlag;
	}



	public void setEmailFlag(boolean emailFlag) {
		this.emailFlag = emailFlag;
	}



	public boolean isMobileFlag() {
		return mobileFlag;
	}



	public void setMobileFlag(boolean mobileFlag) {
		this.mobileFlag = mobileFlag;
	}



	public String getSiteId() {
		return siteId;
	}



	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ThesysMemberVO [siteId=" + siteId + ", memberNo=" + memberNo
				+ ", accountId=" + accountId + ", memberName=" + username
				+ ", idType=" + idType + ", idNo=" + idNo + ", pwd=" + pwd
				+ ", email=" + email + ", gender=" + gender + ", cellphone="
				+ cellphone + ", zipCode=" + zipCode + ", zipCounty="
				+ zipCounty + ", zipArea=" + zipArea + ", fullAddress="
				+ fullAddress + ", edm=" + edm + ", birthDay=" + birthday
				+ ", smsVerifyCode=" + smsVerifyCode + ", mailVerifyCode="
				+ mailVerifyCode + ", mailStatus=" + mailStatus + ", status="
				+ status + ", englishName=" + englishName + ", income="
				+ income + ", occupation=" + occupation + ", marriage="
				+ marriage + ", cellphoneBrands=" + cellphoneBrands
				+ ", education=" + education + ", offspring=" + offspring
				+ ", preferences=" + preferences + ", crtDate=" + createDate
				+ ", lastLoginDate=" + lastLoginDate + ", lastUpdatedDate="
				+ lastUpdatedDate + ", smsVerifyDeadline=" + smsVerifyDeadline
				+ ", lastChangePwdDate=" + lastChangePwdDate + ", lastUpdater="
				+ lastUpdater + ", registerIP=" + registerIP + ", cardId="
				+ cardId + ", useDate=" + useDate + ", oldMemberFlag="
				+ oldMemberFlag + ", emailFlag=" + emailFlag + ", mobileFlag="
				+ mobileFlag + "]";
	}







	
	
	
}
