package com.thesys.opencms.laphone.member;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.json.JSONObject;
import org.opencms.main.CmsLog;

import au.com.bytecode.opencsv.CSVReader;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.member.dao.ThesysMemberDAO;
import com.thesys.opencms.laphone.member.dao.ThesysMemberVO;
import com.thesys.opencms.laphone.system.ThesysSerialHandler;
import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;
import com.thesys.opencms.laphone.util.ThesysEncryption;
import com.thesys.opencms.laphone.util.ThesysSendMsgHandler;

public class ThesysMemberHandler extends ThesysLaphoneHandler {
	protected static final Log LOG = CmsLog.getLog(ThesysMemberHandler.class);

	private final int SMSDeadline = 1; // 簡訊驗證碼驗證期限(天數)
	private java.util.Date now = new java.util.Date();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMMdd");
	
	private int memberNo;
	private String accountId;
	private String idNo;
	private String username;
	private String email;
	private int gender = 0;
	private int status = -1;//代表搜尋全部
	private String cellphone;
	

	public ThesysMemberHandler() {}

	public ThesysMemberHandler(PageContext context, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		super(context, req, res);
	}
	/**
	 * 取得分頁清單
	 * @return
	 */
	public List<ThesysMemberVO> getPageList(){
		List<ThesysMemberVO> result = new ArrayList<ThesysMemberVO>();
		try{
			result = ThesysMemberDAO.getInstance().listByPage(getPageSize(), getPageIndex(), getSiteId(), getAccountId(), getUsername(), getEmail(), getGender(), getStatus(),getIdNo(),getCellphone());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}	
	/**
	 * 取得總筆數
	 * @return
	 */
	public int getCount(){
		int result =0;
		try{
			result = ThesysMemberDAO.getInstance().count(getSiteId(), getAccountId(), getUsername(), getEmail(), getGender(), getStatus(),getIdNo(),getCellphone());
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}		
	

	/**
	 * 流水號取member
	 * @return
	 */
	public ThesysMemberVO getSelectedMember(){
		return getMemberByMemberNo(memberNo);
	}

	/**
	 * 流水號取member
	 * @param sno
	 * @return
	 */
	public ThesysMemberVO getMemberByMemberNo(int memberNo) {
		ThesysMemberVO vo = null;
		try {
			vo = ThesysMemberDAO.getInstance().findByMemberNo(getSiteId(), memberNo);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
			return null;
		}
		return vo;
	}

	/**
	 * 帳號取member
	 * @param accountId
	 * @return
	 */
	public ThesysMemberVO getMemberByAccountId(String accountId) {
		ThesysMemberVO vo = null;
		try {
			vo = ThesysMemberDAO.getInstance().findByAccountId(getSiteId(),accountId);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
			return null;
		}
		return vo;
	}
	/**
	 * 舊會員，身份證取得Member(暫不使用)
	 * @param idno
	 * @return
	 */
	public ThesysMemberVO getMemberByIdNo(String idNo) {
		idNo = idNo.toUpperCase();
		ThesysMemberVO vo = null;
		try {
			vo = ThesysMemberDAO.getInstance().findByIdNo(getSiteId(),idNo);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return vo;
	}
	/**
	 * 依Email查詢會員資料
	 * @param email
	 * @return
	 */
	public ThesysMemberVO getMemberByEmail(String email) {
		ThesysMemberVO vo = null;
		try {
			vo = ThesysMemberDAO.getInstance().findByEmail(getSiteId(),email);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
			return null;
		}
		return vo;
	}
	
	/**
	 * 會員註冊
	 * @param member
	 * @return
	 */
	public boolean register(ThesysMemberVO member) {
		try {
			String cardId = ThesysSerialHandler.getNextSerialNo(getSiteId(),"MEM"); //取得卡琥
			int serialNo = ThesysSerialHandler.getNextTableSerialNo(getSiteId(),"LAPHONE_MEMBER"); //取得流水號
			String smsVerifyCode = ThesysEncryption.getRandomStr(6); //手機驗證碼
			String mailVerifyCode = ThesysEncryption.getRandomStr(6);//mail驗證碼
			String pwd = member.getPwd(); //密碼明碼
			member.setSiteId(getSiteId());
			member.setMemberNo(serialNo);
			member.setPwd(toMd5Pwd(pwd)); //加密
			member.setCreateDate(now);
			member.setSmsVerifyCode(smsVerifyCode);
			member.setSmsVerifyDeadline(addDate(now, SMSDeadline));
			member.setMailVerifyCode(mailVerifyCode);
			member.setLastChangePwdDate(now);
			member.setCardId(cardId);
			member.setOldMemberFlag(false);
			if(ThesysMemberDAO.getInstance().insert(member)==1){

				String host = ThesysParamDAO.getInstance().getParam(getSiteId(), "WEB_URL").getParamVal();

				// 發送註冊成功mail
				 JSONObject jsonObj = new JSONObject();
				 jsonObj.put("accountId",member.getAccountId());
				 jsonObj.put("phone",member.getCellphone().substring(0,4)+"***"+member.getCellphone().substring(7));
				 jsonObj.put("host",host);
				 jsonObj.put("SMSVerifiCode",smsVerifyCode);
				 jsonObj.put("cardId",cardId);
				 jsonObj.put("password",pwd);
				 
				 ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
				 msgHandler.setMsgName("register");
				 msgHandler.setJsonObj(jsonObj);
				 msgHandler.setCellphone(member.getCellphone());
				 msgHandler.setEmail(email);
				 msgHandler.sendMsg();


				// 寄送驗證信及訊息
				sendVerifyMail(member.getMemberNo(),member.getEmail() , member.getMailVerifyCode());
				
				
				
				
				return true;
			}
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return false;
	}
	

//	/**
//	 * 更新會員資料但不會存更新時間給系統重發驗證碼用
//	 * @param vo
//	 * @return
//	 */
//	public int modifyForSystem(ThesysMemberVO vo) {
//		int i = 0;
//		try {
//			if (vo != null) {//先檢查vo 是否為null 再檢查4個是否為唯一
//					i = ThesysMemberDAO.getInstance().update(vo);
//			} else {
//				i = -1;
//			}
//		} catch (Exception ex) {
//			LOG.error(ex, ex.fillInStackTrace());
//			return 0;
//		}
//		return i;
//	}
	
	/**
	 * 前台會員修改資料用
	 * @param vo
	 * @return
	 */
	public int modify(ThesysMemberVO vo){
		
		int i = 0;
		try {
			if (vo != null) {
				vo.setLastUpdatedDate(now);
				i = ThesysMemberDAO.getInstance().update(vo);
			} else {
				i = -1;
			}
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
			return 0;
		}
		return i;
	}
	/**
	 * 忘記密碼
	 * @param accountId
	 * @param pwd
	 * @return
	 */
	public boolean forgetPassword(ThesysMemberVO member){
		String newPassword = ThesysEncryption.getRandomStr(6);
		String md5Password = toMd5Pwd(newPassword);     //密碼轉亂碼
		try{
			if(ThesysMemberDAO.getInstance().updatePassword(getSiteId(), member.getMemberNo(), md5Password, new java.util.Date())==1){
				
				addSendRecord(member.getMemberNo(),2);//新增發送紀錄
				
				//SMS與Mail通知...				
				String host = ThesysParamDAO.getInstance().getParam(getSiteId(), "WEB_URL").getParamVal();	
				
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("phone",member.getCellphone().substring(0,4)+"****"+member.getCellphone().substring(8));
				jsonObj.put("host",host);
				jsonObj.put("pwd",newPassword );
				 
				ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
				msgHandler.setMsgName("forgetPwd");
				msgHandler.setJsonObj(jsonObj);
				msgHandler.setCellphone(member.getCellphone());
				msgHandler.setEmail(member.getEmail());
				msgHandler.sendMsg();
				return true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	/**
	 * 修改密碼
	 * @param accountId
	 * @param pwd
	 * @return
	 */
	public boolean changePassword(int memberNo,String accountId,String mobile,String email, String newPassword){
		String md5Password = toMd5Pwd(newPassword);     //密碼轉亂碼
		try{
			if(ThesysMemberDAO.getInstance().updatePassword(getSiteId(), memberNo, md5Password, new java.util.Date())==1){
				String star = "********************";
				newPassword = newPassword.substring(0,1)+star.substring(0,newPassword.length()-2)+newPassword.substring(newPassword.length()-1);
				//SMS與Mail通知...				
				String host = ThesysParamDAO.getInstance().getParam(getSiteId(), "WEB_URL").getParamVal();				
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String date = sdf.format(new java.util.Date());
				
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("date",date);
				jsonObj.put("host",host);
				jsonObj.put("pwd",newPassword);
				 
				ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
				msgHandler.setMsgName("changePwd");
				msgHandler.setJsonObj(jsonObj);
				msgHandler.setMemberId(accountId );//寄送訊息
				msgHandler.setCellphone(mobile);
				msgHandler.setEmail(email);
				msgHandler.sendMsg();
				return true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 設定修改密碼時間(不修改密碼時使用)
	 * @param accountId
	 * @param pwd
	 * @return
	 */
	public boolean updatePasswordDate(int memberNo){
		try{
			if(ThesysMemberDAO.getInstance().updatePasswordDate(getSiteId(), memberNo,  new java.util.Date())==1){
				return true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 一般登入
	 * @param accountId
	 * @param pwd
	 * @return
	 */
	public ThesysMemberVO login(String accountId, String pwd) {
		ThesysMemberVO member = null;
		String md5Pwd = toMd5Pwd(pwd); // 密碼轉亂碼
		member = getMemberByAccountId(accountId);
		try {
			if (member != null && md5Pwd.equals(member.getPwd())) { //檢查密碼
				if(ThesysMemberDAO.getInstance().updateLastLoginDate(getSiteId(), member.getMemberNo(), new java.util.Date())==1){ //更新登入日期
					return member; 
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		}
		return null;
	}
	
	/**
	 * 舊會員登入-使用身分證字號登入
	 * @param idNo
	 * @param pwd
	 * @return
	 */
	public ThesysMemberVO oldMemberLogin(String idNo, String pwd) {
		ThesysMemberVO member = null;
		String md5Pwd = toMd5Pwd(pwd); // 密碼轉亂碼
		try {
			member =  ThesysMemberDAO.getInstance().loginByIdNo(getSiteId(), idNo, md5Pwd);
			if(member != null){
				if(ThesysMemberDAO.getInstance().updateLastLoginDate(getSiteId(), member.getMemberNo(), new java.util.Date())==1){ //更新登入日期
					return member; 
				}
			}
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
			
		}
		return null;
	}	
	
	/**
	 * 檢查密碼是否正確 修改資料或密碼用
	 * @param accountId
	 * @param pwd
	 * @return
	 */
	public boolean checkOldPassword(int memberNo,String pwd){
		String md5Password = toMd5Pwd(pwd); // 密碼轉亂碼
		try{
			return ThesysMemberDAO.getInstance().checkPassword(getSiteId(), memberNo, md5Password);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return false;
	}

	/**
	 * 寄送認證信
	 * @param memberNo
	 * @param verifyCode
	 * @return
	 */
	public boolean sendVerifyMail(int memberNo,String email,String verifyCode){
		try{
			String host = ThesysParamDAO.getInstance().getParam(getSiteId(), "WEB_URL").getParamVal();
			// 寄送驗證信及訊息
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, 3);
			DecimalFormat fmt = new DecimalFormat("000000");
			String href = host + "account_email_verify.jsp?code=" + fmt.format(memberNo)+ verifyCode + cal.getTimeInMillis();

			JSONObject jsonObj = new JSONObject();
			jsonObj.put("href", href);
			jsonObj.put("host", host);

			ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(
					getCmsObject());
			msgHandler.setMsgName("sendVerify");
			msgHandler.setJsonObj(jsonObj);
			msgHandler.setEmail(email);
			msgHandler.sendMsg(); 
			return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	/**
	 * 產生簡訊驗證碼
	 * @param member
	 * @return 到期日
	 */
	public java.util.Date sendVerifySms(int memberNo,String mobile,String verifyCode){
		try{
			//產生簡訊到期日
			java.util.Date deadline = addDate(now, SMSDeadline);
			
			
			int executeCount = ThesysMemberDAO.getInstance().updateSmsVerifyCode(getSiteId(), memberNo, verifyCode, deadline);
			if(executeCount==1){
				//發送認證簡訊
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("SMSVerifiCode",verifyCode);
				
		
				ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
				msgHandler.setMsgName("sendVerify");
				msgHandler.setJsonObj(jsonObj);
				msgHandler.setCellphone(mobile);
				msgHandler.sendMsg();
				
				addSendRecord(memberNo,1);//新增發送紀錄
				return deadline;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return null;
	}
	/**
	 * 啟用帳號(驗證簡訊)
	 * @param memberNo
	 * @return
	 */
	public boolean enableMember(int memberNo){
		boolean enableFlag = false;
		try{			
			enableFlag = ThesysMemberDAO.getInstance().updateSmsVerifyStatus(getSiteId(), memberNo, 0, new java.util.Date())==1;
			
			if(enableFlag){
				//查詢其他同號碼的帳號，將手機號碼改成加一碼，改為未驗證後發信通知
				List<ThesysMemberVO> list = ThesysMemberDAO.getInstance().listSameCellphoneMember(getSiteId(), memberNo);
				Iterator<ThesysMemberVO> it = list.iterator();

//				String host = ThesysParamDAO.getInstance().getParam(getSiteId(), "WEB_URL").getParamVal();
				
				while(it.hasNext()){
					ThesysMemberVO member = it.next();
					//更新手機及狀態
					String cellphone = member.getCellphone()+"A";
					ThesysMemberDAO.getInstance().updateDuplicateCellphone(getSiteId(), member.getMemberNo(), cellphone);
					/* 2013.03.27 會員手機號碼重覆時不發送通知
					//寄送訊息
					JSONObject jsonObj = new JSONObject();
					jsonObj.put("accountId",member.getAccountId());
					jsonObj.put("phone",member.getCellphone());
					jsonObj.put("host",host);	
					 
					ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
					msgHandler.setMsgName("duplicateMobile");
					msgHandler.setJsonObj(jsonObj);
					msgHandler.setEmail(member.getEmail());
					msgHandler.sendMsg();
					*/
				}
			}
			
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return enableFlag;
	}
	/**
	 * 驗證email
	 * @param memberNo
	 * @return
	 */
	public boolean verifyEmail(int memberNo,String verifyCode){
		try{
			if(ThesysMemberDAO.getInstance().updateEmailVerifyStatus(getSiteId(), memberNo,verifyCode, 1)==1){	
				return true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 改變會員edm資料
	 * @param memberNo
	 * @param edm
	 * @return
	 */
	public int updateMemberEdm(int memberNo,int edm ){
		
		int flag = 0 ;
		try {
			flag = ThesysMemberDAO.getInstance().updateMemberEdm(getSiteId(), memberNo, edm);
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		}
		return flag ;
	}

	/**
	 * 舊會員登入更新帳號
	 * @return
	 */
	public boolean updateAccountId(ThesysMemberVO member){
		try{
			if(ThesysMemberDAO.getInstance().updateAccountId(getSiteId(), member.getMemberNo(), member.getAccountId(), new java.util.Date())==1){
				
				//寄送訊息
				String host = ThesysParamDAO.getInstance().getParam(getSiteId(), "WEB_URL").getParamVal();

				//以下發簡訊及mail
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("accountId",member.getAccountId());
				jsonObj.put("host",host);	
				 
				ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
				msgHandler.setMsgName("oldMemberLogin");
				msgHandler.setJsonObj(jsonObj);
				msgHandler.setEmail(member.getEmail());
				msgHandler.sendMsg();
				
				return true;
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 舊會員查詢密碼(電話+生日月日)
	 * @param idNo
	 * @param pwd
	 * @return
	 */
	public boolean forgetOldMemberPassword(ThesysMemberVO member){
		
		String cellphone = member.getCellphone();
		String birthday = member.getBirthday();
		birthday = birthday.replace("/", "");
		birthday = birthday.substring(birthday.length()-4);
		String newPassword = cellphone +birthday;
		try{
			
				//SMS與Mail通知...				
				String host = ThesysParamDAO.getInstance().getParam(getSiteId(), "WEB_URL").getParamVal();	
				
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("phone",member.getCellphone().substring(0,4)+"****"+member.getCellphone().substring(8));
				jsonObj.put("host",host);
				jsonObj.put("pwd",newPassword );
				 
				ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
				msgHandler.setMsgName("oldForgetPwd");
				msgHandler.setJsonObj(jsonObj);
				msgHandler.setCellphone(member.getCellphone());
				msgHandler.setEmail(member.getEmail());
				msgHandler.sendMsg();
				return true;
		}catch(Exception ex){
			ex.printStackTrace();
		}
		

		return false;
	}
	
	/**
	 * 增加一筆發送驗證碼或忘記密碼紀錄
	 * @param sno
	 * @param type
	 * @return
	 */
	public boolean addSendRecord(int memberNo,int type){
		try {
			if(ThesysMemberDAO.getInstance().insertSendRecord(getSiteId(), memberNo, type, new java.util.Date())==1){
				return true;
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		} 
		return false;
	}

	/**
	 * 查詢當日有幾筆發送驗證碼或忘記密碼紀錄
	 * @param sno
	 * @param type
	 * @return
	 */
	public int getSendCount(int memberNo,int type){
		try {
			return ThesysMemberDAO.getInstance().getSendCount(getSiteId(), memberNo, type, new java.util.Date());
		} catch (SQLException ex) {
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		}
		return -1;
	}
	
	/**
	 * 檢查帳號是否存在 (OK)
	 * @param cellphone
	 * @return boolean true = 存在   false = 不存在
	 */
	public boolean isAccountIdExist(String accountId){
		try {
			return ThesysMemberDAO.getInstance().isAccountIdExist(getSiteId(), accountId);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return false;
	}
	
	/**
	 * 檢查手機是否存在 (OK)
	 * @param cellphone
	 * @return boolean true = 存在   false = 不存在
	 */
	public boolean isMoblieExist(String cellphone){
		try {
			return ThesysMemberDAO.getInstance().isFieldExist(getSiteId(), "CELLPHONE",cellphone);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return false;
	}
	
	/**
	 * 檢查身分證字號否存在  (OK)
	 * @param idNo
	 * @return 
	 */
	public boolean isIdNoExist(String idNo){
		idNo = idNo.toUpperCase();
		try {
			return ThesysMemberDAO.getInstance().isFieldExist(getSiteId(), "IDNO",idNo);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return false;
	}
	
	/**
	 * 檢查email是否存在(無使用，未測試)
	 * @param email
	 * @return 
	 */
	public boolean isEmailExist(String email) {
		try {
			return ThesysMemberDAO.getInstance().isFieldExist(getSiteId(),"EMAIL",email);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return false;
	}
	
	/**
	 * 檢查email是否驗證
	 * @return
	 */
	public boolean isEmailVerify(){
		return isEmailVerify(memberNo);
	}
	
	/**
	 * 檢查email是否驗證
	 * @param accountId
	 * @return
	 */
	public boolean isEmailVerify(int memberNo){
		try {
			return ThesysMemberDAO.getInstance().getStatus(getSiteId(), memberNo,"MAILSTATUS") == 1;
		} catch (SQLException ex) {
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		}
		return false;
	}
	
	/**
	 * 取得會員狀態值
	 * @return 0:正常會員 , 1:黑名單 , 2:簡訊未驗證 ,-1:無此會員
	 */
	public int getSelectedMemberStatus(){
		return getMemberStatus(memberNo);
	}
	
	/**
	 * 取得會員狀態值
	 * @param memberNo
	 * @return 0:正常會員 , 1:黑名單 , 2:簡訊未驗證 ,-1:無此會員
	 */
	public int getMemberStatus(int memberNo){
		try {
			return ThesysMemberDAO.getInstance().getStatus(getSiteId(), memberNo,"STATUS");
		} catch (SQLException ex) {
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		}
		return 99;
	}
	
	
	
	/**
	 * 加天數
	 * @param date 日期 
	 * @param days 要加幾天
	 * @return
	 */
	public java.util.Date addDate(java.util.Date date, int days) {
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.setTime(date);
		cal.add(java.util.Calendar.DATE, days);
		return cal.getTime();
	}

	/**
	 * 將密碼轉MD5
	 * @param pwd
	 * @return
	 */
	public String toMd5Pwd(String pwd) {
		String md5Pwd = "";
		try {
			// 密碼加密
			java.security.MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
			sun.misc.BASE64Encoder base64en = new sun.misc.BASE64Encoder();
			md5Pwd = base64en.encode(md5.digest(pwd.getBytes("utf-8")));
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return md5Pwd;
	}

	/**
	 * 舊會員匯入用
	 * @param vo
	 * @return
	 */
	public int insertOldMember(ThesysMemberVO member){
		int flag = 0;
		int serialNo = ThesysSerialHandler.getNextTableSerialNo(getSiteId(),"LAPHONE_MEMBER");
		try {
			String md5Pwd = toMd5Pwd(member.getPwd()); // 密碼轉亂碼 
			member.setSiteId(getSiteId());
			member.setPwd(md5Pwd);
			member.setSmsVerifyDeadline(addDate(new java.util.Date(), SMSDeadline));
			member.setOldMemberFlag(true);
			member.setMemberNo(serialNo);
			flag = ThesysMemberDAO.getInstance().insert(member);
			
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		}
		return flag;
	}
	
	/**
	 * 更改會員狀態
	 * @param accountId
	 * @param memberStatus
	 * @param cmsuser
	 * @return
	 */
	public int updateMemberStatus(String accountId,int memberStatus,String cmsuser ){
		
		int flag = 0 ;
		try {
			if(!accountId.equals("") && accountId.trim().length() !=0 && isAccountIdExist(accountId)){
				flag = ThesysMemberDAO.getInstance().updateMemberStatus(getSiteId(), accountId, memberStatus, cmsuser);
			}else{
				flag = -1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			LOG.error(ex, ex.fillInStackTrace());
		}
		return flag ;
	}
	
	/**
	 * 後台修改會員資料
	 * @param member 會員資料
	 * @return
	 */
	public boolean backedModify(ThesysMemberVO member){
		if (member != null) {			
			try {
					int resultCount = ThesysMemberDAO.getInstance().backedUpdate(getSiteId(), member.getMemberNo(), member.getEmail(), member.getCellphone(), member.getStatus(),getUserId());
					if(resultCount==1) return true;
			} catch (Exception ex) {
				LOG.error(ex, ex.fillInStackTrace());
			}
		}
		return false;
	}
	
	/**
	 * 黑名單輸入
	 * @param is
	 * @param fmt 編碼
	 * @param cmsuser
	 * @return
	 */
	public int importBlackList(InputStream is, String fmt,String cmsuser){
		int flag = 0 ;
		int memberStatus = 1;//黑名單狀態值
		try{
			InputStreamReader in =new InputStreamReader(is,fmt);
			if(in.ready()){ //判斷有無資料
				CSVReader reader = new CSVReader(in,'\t');
				String [] nextLine;
				while ((nextLine = reader.readNext()) != null) {
			        if(true){//if(rowId>=1){ //跳過第一筆
			        	flag = flag + updateMemberStatus(nextLine[0],memberStatus,cmsuser);
			    	}
			    }
			}
			in.close();
		}catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return flag ;
	}
	
	
	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getMemberNo() {
		return memberNo;
	}

	public void setMemberNo(int memberNo) {
		this.memberNo = memberNo;
	}

	/**
	 * @return the cellphone
	 */
	public String getCellphone() {
		return cellphone;
	}

	/**
	 * @param cellphone the cellphone to set
	 */
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

}