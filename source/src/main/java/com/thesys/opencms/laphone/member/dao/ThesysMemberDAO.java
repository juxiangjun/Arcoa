package com.thesys.opencms.laphone.member.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.thesys.opencms.laphone.ThesysLaphoneDAO;

public class ThesysMemberDAO extends ThesysLaphoneDAO {
	
	private static ThesysMemberDAO m_instance;
	private final static String SQL_PASSPHRASE = "Laphone@123";
	
	private ThesysMemberDAO() {
		super.init();
	}
	
	/**
     * Singleton access.<p>
     * @return the singleton object
     */
    public static synchronized ThesysMemberDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysMemberDAO();
        }
        return m_instance;
    }
    private String getFieldsSql(){
    	
    	return " SITE_ID, SNO, ACCOUNTID, "+getDecryptSql("USERNAME")+" USERNAME, "+
   			   " IDTYPE, "+getDecryptSql("IDNO")+" IDNO, PWD, EMAIL, GENDER, "+getDecryptSql("CELLPHONE")+" CELLPHONE, "+
   			   " ZIP_CODE, ZIP_COUNTY, ZIP_AREA,FULLADDRESS, EDM, BIRTHDAY, CRT_DATE, LOGIN_DATE, LM_DATE,   "+
   			   " SMS_VERIFI_CODE, MAIL_VERIFI_CODE, MAILSTATUS, STATUS, ENG_NAME, INCOME, OCCUPATION,  "+
   			   " MARRIAGE, CELLPHONEBRANDS, EDUCATION, OFFSPRING, PREFERENCES, SMS_VERIFI_DEADLINE, LM_PWD_DATE, "+
   			   "LM_USR_ID, REGISTER_IP, CARDID, USE_DATE, OLD_MEMBER_FLAG ";

    }
    /**
     * 取得加密SQL字串
     * @param fieldName
     * @return
     */
    private String getEncryptSql(String fieldName){
    	return " Cast(EncryptByPassPhrase('"+SQL_PASSPHRASE+"',"+fieldName+") as NVARCHAR(max)) ";
    }
    /**
     * 取得解密SQL字串
     * @param fieldName
     * @return
     */
    private String getDecryptSql(String fieldName){
    	return " CONVERT(nvarchar,DecryptByPassPhrase('"+SQL_PASSPHRASE+"',Cast("+fieldName+" as VARBINARY(8000) ) )) ";
    }
    /**
     * 取得分頁資料
     * @param siteId
     * @param pageSize
     * @param pageIndex
     * @param selMap
     * @return
     * @throws Exception
     */
    public List<ThesysMemberVO> listByPage(int pageSize,int pageIndex, String siteId,String accountId,String userName,String email,int gender,int status,String idNo,String cellphone) throws Exception{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
	    List<ThesysMemberVO> result = new ArrayList<ThesysMemberVO>();
	    try {        	
            con = getConnection();
            stmt = this.generateStatement(con, false, pageSize, pageIndex, siteId, accountId, userName, email, gender, status,idNo,cellphone);
            rs = stmt.executeQuery(); 
            while (rs.next()) {
    				ThesysMemberVO item = ThesysMemberVO.getInstance(rs);
                	result.add(item);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }
    
    /**
     * 計算查詢結果總筆數
     * @param siteId
     * @param accountId
     * @param userName
     * @param email
     * @param gender
     * @param status
     * @return
     * @throws Exception
     */
    public int count(String siteId,String accountId,String userName,String email,int gender,int status,String idNo,String cellphone) throws Exception{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int result = 0;
        try {
            con = getConnection();
            stmt = generateStatement(con, true, 0,0, siteId, accountId, userName, email, gender, status,idNo,cellphone);
            rs = stmt.executeQuery();
            if(rs.next()){ 
            		result = rs.getInt(1);
            	}
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }
    /**
     * 依會員編號取得會員資料
     * @param siteId
     * @param memberNo
     * @return
     * @throws Exception
     */
    public ThesysMemberVO findByMemberNo(String siteId,int memberNo) throws Exception{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysMemberVO item = null;
        String sql ="SELECT "+getFieldsSql()+" FROM  LAPHONE_MEMBER WHERE SNO=? AND SITE_ID=?";
        try {
  			con = getConnection(); 
  			stmt = con.prepareStatement(sql);
  			stmt.setInt(1, memberNo);
  			stmt.setString(2, siteId);
  			rs = stmt.executeQuery();
            if (rs.next()) 
            	item = ThesysMemberVO.getInstance(rs);
  		}finally{
  			closeAll(con, stmt, rs);
  		}
  		return item;
    }
    
    /**
     * 依登入帳號取得會員資料
     * @param siteId
     * @param accountId
     * @return
     * @throws Exception
     */
  	public ThesysMemberVO findByAccountId(String siteId,String accountId) throws Exception{
  		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysMemberVO item = null;
        String sql ="SELECT "+getFieldsSql()+" FROM  LAPHONE_MEMBER WHERE ACCOUNTID=? AND SITE_ID=?";
        try {
  			con = getConnection(); 
  			stmt = con.prepareStatement(sql);
  			stmt.setString(1, accountId);
  			stmt.setString(2, siteId);
  			rs = stmt.executeQuery();
            if (rs.next()) 
            	item = ThesysMemberVO.getInstance(rs);
  		}finally{
  			closeAll(con, stmt, rs);
  		}
  		return item;
  	}
  	
  	/**
  	 * 依身份證字號查詢
  	 * @param siteId
  	 * @param idNoCheck
  	 * @return
  	 * @throws Exception
  	 */
  	public ThesysMemberVO findByIdNo(String siteId,String idNo) throws Exception{
  		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysMemberVO item = null;
      
        String sql ="SELECT "+getFieldsSql()+" FROM  LAPHONE_MEMBER WHERE SITE_ID=? AND "+getDecryptSql("IDNO")+"=? "; 
        try {
  			con = getConnection(); 
  			stmt = con.prepareStatement(sql);
  			stmt.setString(1, siteId);
  			stmt.setString(2, idNo);
  			rs = stmt.executeQuery();
            if (rs.next()){
            		 item = ThesysMemberVO.getInstance(rs);
            }	
  		}finally{
  			closeAll(con, stmt, rs);
  		}
  		return item;
  	}
  	
  	/**
  	 * 依email取得會員資料  	
  	 * @param siteId
  	 * @param email
  	 * @return
  	 * @throws Exception
  	 */
  	public ThesysMemberVO findByEmail(String siteId,String email) throws Exception{
  		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysMemberVO item = null;
        String sql ="SELECT "+getFieldsSql()+" FROM  LAPHONE_MEMBER WHERE SITE_ID=? and EMAIL = ? ";
        try {
  			con = getConnection(); 
  			stmt = con.prepareStatement(sql);
  			stmt.setString(1, siteId);
  			stmt.setString(2, email);
  			rs = stmt.executeQuery();
            if (rs.next()){
            	 item = ThesysMemberVO.getInstance(rs);
            }	
  		}finally{
  			closeAll(con, stmt, rs);
  		}
  		return item;
  	}
  	/**
  	 * 找出同手機號碼且已驗證過手機的會員資料	
  	 * @param siteId
  	 * @param email
  	 * @return
  	 * @throws Exception
  	 */
  	public List<ThesysMemberVO> listSameCellphoneMember(String siteId,int memberNo) throws Exception{
  		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<ThesysMemberVO> list = new ArrayList<ThesysMemberVO>();
        String sql ="SELECT "+getFieldsSql()+" FROM  LAPHONE_MEMBER WHERE SITE_ID=? and SNO <> ? and "+getDecryptSql("CELLPHONE")+" = (SELECT "+getDecryptSql("CELLPHONE")+" FROM LAPHONE_MEMBER WHERE SITE_ID=? and SNO=?) AND STATUS= 0  ";
        try {
  			con = getConnection(); 
  			stmt = con.prepareStatement(sql);
  			stmt.setString(1, siteId);
  			stmt.setInt(2, memberNo);
  			stmt.setString(3, siteId);
  			stmt.setInt(4, memberNo);
  			rs = stmt.executeQuery();
            while (rs.next()){
            	list.add(ThesysMemberVO.getInstance(rs));
            }	
  		}finally{
  			closeAll(con, stmt, rs);
  		}
  		return list;
  	}
  	
  	/**
  	 * 舊會員登入-身分證字號
  	 * @param siteId
  	 * @param idno
  	 * @param pwd
  	 * @return
  	 * @throws Exception
  	 */
  	public ThesysMemberVO loginByIdNo(String siteId,String idNo, String pwd) throws Exception{
  		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysMemberVO item = null;
        
        String sql ="SELECT "+getFieldsSql()+" FROM  LAPHONE_MEMBER WHERE SITE_ID=? AND "+getDecryptSql("IDNO")+"=? AND PWD=? AND OLD_MEMBER_FLAG='Y' ";
        try {
        	con = getConnection(); 
  			stmt = con.prepareStatement(sql);
  			stmt.setString(1, siteId);
  			stmt.setString(2, idNo);
  			stmt.setString(3, pwd);
  			rs = stmt.executeQuery();
            if (rs.next()){
            		 item = ThesysMemberVO.getInstance(rs);   
            }	
  		}finally{
  			closeAll(con, stmt, rs);
  		}
  		return item;
  	}
  	/**
  	 * 建立一筆會員資料
  	 * @param vo
  	 * @return
  	 * @throws Exception
  	 */
	public int insert(ThesysMemberVO vo) throws Exception{

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int i = 0;
        try {
            con = getConnection(); 
            sql = "INSERT INTO LAPHONE_MEMBER(SITE_ID,SNO,ACCOUNTID,USERNAME," +
            		"IDTYPE,IDNO,PWD,EMAIL,GENDER,CELLPHONE,ZIP_CODE,ZIP_COUNTY," +
            		"ZIP_AREA,FULLADDRESS,EDM,BIRTHDAY,CRT_DATE,LOGIN_DATE,LM_DATE," +
            		"SMS_VERIFI_CODE,MAIL_VERIFI_CODE,MAILSTATUS,STATUS,ENG_NAME,INCOME," +
            		"OCCUPATION,MARRIAGE,CELLPHONEBRANDS,EDUCATION,OFFSPRING,PREFERENCES," +
            		"SMS_VERIFI_DEADLINE,LM_PWD_DATE,LM_USR_ID,REGISTER_IP,CARDID," +
            		"USE_DATE,OLD_MEMBER_FLAG)VALUES(?,?,?,"+getEncryptSql("?")+",?,"+getEncryptSql("?")+",?,?,?,"+getEncryptSql("?")+",?,?,?," +
            		"?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            stmt = con.prepareStatement(sql);
            //取得訊息流水號
            int idx = 1;
            //寫入一筆資料
            stmt.setString(idx++, vo.getSiteId());
        	stmt.setInt(idx++, vo.getMemberNo());
    		stmt.setString(idx++,vo.getAccountId());
    		stmt.setString(idx++,vo.getUsername());
    		stmt.setInt(idx++,vo.getIdType());
    		stmt.setString(idx++,vo.getIdNo());
    		stmt.setString(idx++,vo.getPwd());
    		stmt.setString(idx++,vo.getEmail());
    		stmt.setInt(idx++,vo.getGender());
    		stmt.setString(idx++,vo.getCellphone());
    		stmt.setString(idx++,vo.getZipCode());
    		stmt.setString(idx++,vo.getZipCounty());
    		stmt.setString(idx++,vo.getZipArea());
    		stmt.setString(idx++,vo.getFullAddress());
    		stmt.setInt(idx++,vo.getEdm());

    		SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
    		stmt.setDate(idx++, new java.sql.Date(fmt.parse(vo.getBirthday()).getTime()));
    		stmt.setTimestamp(idx++,convert(vo.getCreateDate()));
    		stmt.setTimestamp(idx++,convert(vo.getLastLoginDate()));
    		stmt.setTimestamp(idx++,convert(vo.getLastUpdatedDate()));
    		stmt.setString(idx++,vo.getSmsVerifyCode());
    		stmt.setString(idx++,vo.getMailVerifyCode());
    		stmt.setInt(idx++,vo.getMailStatus());
    		stmt.setInt(idx++,vo.getStatus());
    		stmt.setString(idx++,vo.getEnglishName());
    		stmt.setInt(idx++,vo.getIncome());
    		stmt.setInt(idx++,vo.getOccupation());
    		stmt.setInt(idx++,vo.getMarriage());
    		stmt.setInt(idx++,vo.getCellphoneBrands());
    		stmt.setInt(idx++,vo.getEducation());
    		stmt.setInt(idx++,vo.getOffspring());
    		stmt.setString(idx++,vo.getPreferences());
    		stmt.setTimestamp(idx++,convert(vo.getSmsVerifyDeadline()));
    		stmt.setTimestamp(idx++,convert(vo.getLastChangePwdDate()));
    		stmt.setString(idx++,vo.getLastUpdater());
    		stmt.setString(idx++,vo.getRegisterIP());
    		stmt.setString(idx++,vo.getCardId());
    		stmt.setTimestamp(idx++,convert(vo.getUseDate()));
    		stmt.setString(idx++, vo.isOldMemberFlag()?"Y":"N");
    		i = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return i ;
	}
	
	public int update(ThesysMemberVO vo) throws Exception{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int i = 0;
        try {
            con = getConnection();
            sql = "UPDATE LAPHONE_MEMBER SET EMAIL=?,GENDER=?,CELLPHONE="+getEncryptSql("?")+",ZIP_CODE=?,ZIP_COUNTY=?,ZIP_AREA=?,FULLADDRESS=?,EDM=?," +
            		"LM_DATE=?,SMS_VERIFI_CODE=?,MAIL_VERIFI_CODE=?," +
            		"MAILSTATUS=?,STATUS=?,ENG_NAME=?,INCOME=?,OCCUPATION=?,MARRIAGE=?," +
            		"CELLPHONEBRANDS=?,EDUCATION=?,OFFSPRING=?,PREFERENCES=?," +
            		"SMS_VERIFI_DEADLINE=?,LM_USR_ID=? " +
            		" WHERE SITE_ID = ? AND SNO=?";
            stmt = con.prepareStatement(sql);            
            //寫入一筆資料
            int idx = 1;
    		stmt.setString(idx++,vo.getEmail());
    		stmt.setInt(idx++,vo.getGender());
    		stmt.setString(idx++,vo.getCellphone());
    		stmt.setString(idx++,vo.getZipCode());
    		stmt.setString(idx++,vo.getZipCounty());
    		stmt.setString(idx++,vo.getZipArea());
    		stmt.setString(idx++,vo.getFullAddress());
    		stmt.setInt(idx++,vo.getEdm());

    		stmt.setTimestamp(idx++,convert(vo.getLastUpdatedDate()));
    		stmt.setString(idx++,vo.getSmsVerifyCode());
    		stmt.setString(idx++,vo.getMailVerifyCode());
    		stmt.setInt(idx++,vo.getMailStatus());
    		stmt.setInt(idx++,vo.getStatus());
    		stmt.setString(idx++,vo.getEnglishName());
    		stmt.setInt(idx++,vo.getIncome());
    		stmt.setInt(idx++,vo.getOccupation());
    		stmt.setInt(idx++,vo.getMarriage());
    		stmt.setInt(idx++,vo.getCellphoneBrands());
    		stmt.setInt(idx++,vo.getEducation());
    		stmt.setInt(idx++,vo.getOffspring());
    		stmt.setString(idx++,vo.getPreferences());
    		stmt.setTimestamp(idx++,convert(vo.getSmsVerifyDeadline()));
    		stmt.setString(idx++,vo.getLastUpdater());
    		stmt.setString(idx++,vo.getSiteId());
        	stmt.setInt(idx++, vo.getMemberNo());
    		 i = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return i ;
	}
	
	/**
	 * 後台修改會員資料
	 * @param siteId
	 * @param memberNo
	 * @param email
	 * @param cellphone
	 * @param status
	 * @param lasterUpdater
	 * @return
	 * @throws Exception
	 */
	public int backedUpdate(String siteId,int memberNo,String email,String cellphone,int status,String lasterUpdater) throws Exception{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int resultCount = 0;
        try {
            con = getConnection();
            sql = " UPDATE LAPHONE_MEMBER SET EMAIL=?,CELLPHONE="+getEncryptSql("?")+",LM_DATE=?,STATUS=?,LM_USR_ID=? " +
            	  " WHERE SITE_ID = ? AND SNO=?";
            stmt = con.prepareStatement(sql);            
            //寫入一筆資料
            int idx = 1;
    		stmt.setString(idx++,email);
    		stmt.setString(idx++,cellphone);
    		stmt.setTimestamp(idx++,convert(new java.util.Date()));
    		stmt.setInt(idx++,status);
    		stmt.setString(idx++,lasterUpdater);
    		stmt.setString(idx++,siteId);
        	stmt.setInt(idx++, memberNo);
        	resultCount = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return resultCount;
	}
	
	/**
	 * 修改密碼  
	 * add by Maggie
	 * @param siteId
	 * @param accountId
	 * @param md5Password
	 * @param passwordDate
	 * @return
	 * @throws Exception
	 */
	public int updatePassword(String siteId,int memberNo,String md5Password,java.util.Date passwordDate ) throws Exception{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int i = 0;
        try {
            con = getConnection();
            sql = "UPDATE LAPHONE_MEMBER SET PWD=?,LM_PWD_DATE=?  WHERE SITE_ID = ? AND SNO=?";
            stmt = con.prepareStatement(sql);            
            int idx = 1;
            stmt.setString(idx++,md5Password);
            stmt.setTimestamp(idx++, convert(passwordDate));
            stmt.setString(idx++,siteId);
            stmt.setInt(idx++,memberNo);
    		 i = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return i ;
	}
	/**
	 * 檢查密碼是否正確
	 * @param siteId
	 * @param memberNo
	 * @param md5Password
	 * @return
	 * @throws Exception
	 */
	public boolean checkPassword(String siteId,int memberNo,String md5Password) throws Exception{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        try {
            con = getConnection();
            sql = "SELECT SNO FROM LAPHONE_MEMBER WHERE SITE_ID = ? AND SNO=? AND PWD = ? ";
            stmt = con.prepareStatement(sql);            
            int idx = 1;
            stmt.setString(idx++,siteId);
            stmt.setInt(idx++,memberNo);
            stmt.setString(idx++,md5Password);
    		rs = stmt.executeQuery();
    		return rs.next();
        } finally {
            closeAll(con, stmt, rs);
        }
	}
	/**
	 * 只修改密碼日期(確認不修改密碼)
	 * add by Maggie
	 * @param siteId
	 * @param accountId
	 * @param passwordDate
	 * @return
	 * @throws Exception
	 */
	public int updatePasswordDate(String siteId,int memberNo,java.util.Date passwordDate ) throws Exception{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int i = 0;
        try {
            con = getConnection();
            sql = "UPDATE LAPHONE_MEMBER SET LM_PWD_DATE=?  WHERE SITE_ID = ? AND SNO=?";
            stmt = con.prepareStatement(sql);   
            int idx = 1;
            stmt.setTimestamp(idx++, convert(passwordDate));
            stmt.setString(idx++,siteId);
            stmt.setInt(idx++,memberNo);
    		 i = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return i ;
	}
	/**
	 * 更新重覆的手機狀態
	 * @param siteId
	 * @param accountId
	 * @param loginDate
	 * @return
	 * @throws Exception
	 */
	public int updateDuplicateCellphone(String siteId,int memberNo,String cellphone ) throws Exception{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int i = 0;
        try {
            con = getConnection();
            //sql = "UPDATE LAPHONE_MEMBER SET CELLPHONE=?,STATUS=2,SMS_VERIFI_CODE=null,SMS_VERIFI_DEADLINE=null, USE_DATE=null WHERE SITE_ID = ? AND SNO=?";
            sql = "UPDATE LAPHONE_MEMBER SET CELLPHONE="+getEncryptSql("?")+",STATUS=2,SMS_VERIFI_CODE=null,SMS_VERIFI_DEADLINE=null, USE_DATE=null WHERE SITE_ID = ? AND SNO=?";
            
            stmt = con.prepareStatement(sql);   
            int idx = 1;
            stmt.setString(idx++,cellphone);
            stmt.setString(idx++,siteId);
            stmt.setInt(idx++,memberNo);
    		 i = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return i ;
	}
	/**
	 * 更新帳號啟用日期
	 * @param siteId
	 * @param accountId
	 * @param loginDate
	 * @return
	 * @throws Exception
	 */
	public int updateSmsVerifyStatus(String siteId,int memberNo,int memberStatus,java.util.Date verifyDate ) throws Exception{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int i = 0;
        try {
            con = getConnection();
            sql = "UPDATE LAPHONE_MEMBER SET STATUS=?, USE_DATE=? WHERE SITE_ID = ? AND SNO=?";
            stmt = con.prepareStatement(sql);   
            int idx = 1;
            stmt.setInt(idx++,memberStatus);
            stmt.setTimestamp(idx++, convert(verifyDate));
            stmt.setString(idx++,siteId);
            stmt.setInt(idx++,memberNo);
    		 i = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return i ;
	}
	/**
	 * 更新簡訊驗證碼
	 * @param siteId
	 * @param memberNo
	 * @param verifyCode
	 * @param deadline
	 * @return
	 * @throws Exception
	 */
	public int updateSmsVerifyCode(String siteId,int memberNo,String verifyCode,java.util.Date deadline) throws Exception{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int i = 0;
        try {
            con = getConnection();
            sql = "UPDATE LAPHONE_MEMBER SET SMS_VERIFI_CODE=?,SMS_VERIFI_DEADLINE=? WHERE SITE_ID = ? AND SNO=?";
            stmt = con.prepareStatement(sql);   
            int idx = 1;
            stmt.setString(idx++,verifyCode);
            stmt.setTimestamp(idx++, convert(deadline));
            stmt.setString(idx++,siteId);
            stmt.setInt(idx++,memberNo);
    		 i = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return i ;
	}
	/**
	 * 更新帳號啟用日期
	 * @param siteId
	 * @param accountId
	 * @param loginDate
	 * @return
	 * @throws Exception
	 */
	public int updateEmailVerifyStatus(String siteId,int memberNo,String verifyCode,int mailStatus ) throws Exception{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int i = 0;
        try {
            con = getConnection();
            sql = "UPDATE LAPHONE_MEMBER SET MAILSTATUS=? WHERE SITE_ID = ? AND SNO=? AND MAIL_VERIFI_CODE = ? ";
            stmt = con.prepareStatement(sql);   
            int idx = 1;
            stmt.setInt(idx++,mailStatus);
            stmt.setString(idx++,siteId);
            stmt.setInt(idx++,memberNo);
            stmt.setString(idx++,verifyCode);
    		 i = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return i ;
	}
	/**
	 * 更新最後登入日期
	 * @param siteId
	 * @param accountId
	 * @param loginDate
	 * @return
	 * @throws Exception
	 */
	public int updateLastLoginDate(String siteId,int memberNo,java.util.Date loginDate ) throws Exception{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int i = 0;
        try {
            con = getConnection();
            sql = "UPDATE LAPHONE_MEMBER SET LOGIN_DATE=?  WHERE SITE_ID = ? AND SNO=?";
            stmt = con.prepareStatement(sql);   
            int idx = 1;
            stmt.setTimestamp(idx++, convert(loginDate));
            stmt.setString(idx++,siteId);
            stmt.setInt(idx++,memberNo);
    		 i = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return i ;
	}

	
	/**
	 * 舊會員更新帳號
	 * @param siteId
	 * @param memberNo
	 * @param accountId
	 * @param loginDate
	 * @return
	 * @throws Exception
	 */
	public int updateAccountId(String siteId,int memberNo,String accountId,java.util.Date lastModifyDate ) throws Exception{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int i = 0;
        try {
            con = getConnection();
            sql = "UPDATE LAPHONE_MEMBER SET ACCOUNTID=?,LM_DATE = ? WHERE SITE_ID = ? AND SNO=?";
            stmt = con.prepareStatement(sql);   
            int idx = 1;
            stmt.setString(idx++,accountId);
            stmt.setTimestamp(idx++, convert(lastModifyDate));
            stmt.setString(idx++,siteId);
            stmt.setInt(idx++,memberNo);
    		 i = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return i ;
	}
	
	/**
	 * 後台更改會員狀態
	 * @param siteId
	 * @param accountId
	 * @param memberStatus
	 * @param cmsuser
	 * @return
	 * @throws Exception
	 */
	public int updateMemberStatus(String siteId,String accountId,int memberStatus,String cmsuser ) throws Exception{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int i = 0;
        try {
            con = getConnection();
            sql = "UPDATE LAPHONE_MEMBER SET STATUS=?,LM_USR_ID=?, LM_DATE=? WHERE SITE_ID = ? AND ACCOUNTID=?";
            stmt = con.prepareStatement(sql);   
            int idx = 1;
            stmt.setInt(idx++,memberStatus);
            stmt.setString(idx++, cmsuser);
            stmt.setTimestamp(idx++, convert(new java.util.Date()));
            stmt.setString(idx++,siteId);
            stmt.setString(idx++,accountId);
    		 i = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return i ;
	}
	/**
	 * 更新電子報狀態
	 * @param siteId
	 * @param memberNo
	 * @param edm
	 * @return
	 * @throws SQLException
	 */
	public int updateMemberEdm(String siteId, int memberNo, int edm) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int i = 0;
        try {
            con = getConnection();
            sql = "UPDATE LAPHONE_MEMBER SET EDM=? WHERE SITE_ID = ? AND SNO=?";
            stmt = con.prepareStatement(sql);   
            int idx = 1;
            stmt.setInt(idx++,edm);
            stmt.setString(idx++,siteId);
            stmt.setInt(idx++,memberNo);
    		 i = stmt.executeUpdate();
        } finally {
            closeAll(con, stmt, rs);
        }
        return i ;
	}
	
	
	/**
	 * 檢查欄位值是否存在
	 * @param siteId
	 * @param field
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public boolean isFieldExist(String siteId,String field,String value) throws Exception{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        if("IDNO".equals(field) || "USERNAME".equals(field) || "CELLPHONE".equals(field)){
        	field = getDecryptSql(field);
        }
        String sql ="SELECT count(*)  FROM  LAPHONE_MEMBER WHERE SITE_ID=? AND "+field+"=?";
        try {
  			con = getConnection(); 
  			stmt = con.prepareStatement(sql);
  			stmt.setString(1, siteId);
  			stmt.setString(2, value);
  			rs = stmt.executeQuery();
  			if(rs.next() && rs.getInt(1)>0) return true;
        }finally{
  			closeAll(con, stmt, rs);
  		}
  		return false;
    }
		
	/**
	 * 檢查帳號是否已存在
	 * @param siteId
	 * @param field
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public boolean isAccountIdExist(String siteId,String accountId) throws Exception{
		
		return isFieldExist(siteId,"ACCOUNTID",accountId);
    }

	/**
	 * 取得此會員該狀態值
	 * @param siteId
	 * @param accountId
	 * @param field  "MAILSTATUS" or "STATUS"
	 * @return
	 * @throws SQLException
	 */
	public int getStatus(String siteId,int memberNo,String field ) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql ="SELECT "+field+" FROM  LAPHONE_MEMBER WHERE SITE_ID=? AND SNO=?";
        try {
  			con = getConnection(); 
  			stmt = con.prepareStatement(sql);
  			stmt.setString(1, siteId);
  			stmt.setInt(2, memberNo);
  			rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(field);
        }finally{
  			closeAll(con, stmt, rs);
  		}
  		return -1;
	}

	
	
	/**
	 * 新增一筆發送記錄
	 * @param siteId
	 * @param memberNo
	 * @param sendType
	 * @param sendDate
	 * @return
	 * @throws SQLException
	 */
	public int insertSendRecord(String siteId,int memberNo,int sendType,java.util.Date sendDate) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        String sql = "";
        int i = 0;
        try {
			con = getConnection();
			sql ="INSERT INTO LAPHONE_MEMBER_SEND_RECORD(SNO,SENDTYPE,SENDDATE,SITE_ID) VALUES(?,?,?,?)";
			stmt = con.prepareStatement(sql);
			int idx = 1;
			stmt.setInt(idx++,memberNo);
			stmt.setInt(idx++,sendType);
			stmt.setDate(idx++,new java.sql.Date(sendDate.getTime())); 
			stmt.setString(idx++, siteId);
			i = stmt.executeUpdate();
		}finally{
			closeAll(con, stmt, rs);
		}
		return i;
	}
		
	/**
	 * 取得發送次數
	 * @param siteId
	 * @param sno
	 * @param sendType
	 * @param sendDate
	 * @return
	 * @throws SQLException
	 */
	public int getSendCount(String siteId ,int sno,int sendType,java.util.Date sendDate) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		String sql ="SELECT COUNT(*) FROM LAPHONE_MEMBER_SEND_RECORD WHERE SNO=? AND SENDTYPE=? AND SENDDATE=? AND SITE_ID=?";
		int c =0;
		try {
			con = getConnection();
			stmt = con.prepareStatement(sql);
			int i = 1;
			stmt.setInt(i++,sno);
			stmt.setInt(i++,sendType);
			stmt.setDate(i++,new java.sql.Date(sendDate.getTime()));
			stmt.setString(i++, siteId);
		    rs = stmt.executeQuery();
		    if(rs.next()){ 
		    	c = rs.getInt(1);
		    }
		}finally{
			closeAll(con, stmt, rs);
		}
		return c;
	}

	private PreparedStatement generateStatement(Connection conn,boolean countFlag,int pageSize,int pageIndex,
			String siteId,String accountId,String userName,String email,int gender,int status,String idNo,String cellphone) throws Exception{
 
    	String orderBy = " ORDER BY ACCOUNTID "; 
    	
    	String where = " WHERE SITE_ID = ? ";
    	if(accountId!=null && !"".equals(accountId)){
    		where += " AND ACCOUNTID like ? ";
    	}
    	if(userName!=null && !"".equals(userName)){
    		where += " AND "+getDecryptSql("USERNAME")+" like ? ";
    	}
    	if(email!=null && !"".equals(email)){
    		where += " AND EMAIL like ? ";
    	}
    	if(gender==1 || gender==2){
    		where += " AND GENDER =? ";    		
    	}
    	if(status ==0 || status==1 || status==2){
    		where += " AND STATUS = ? ";    		
    	}
    	if(idNo!=null && !"".equals(idNo)){
    		where += " AND "+getDecryptSql("IDNO")+" like ? ";
    	}

    	if(cellphone!=null && !"".equals(cellphone)){
    		where += " AND "+getDecryptSql("CELLPHONE")+" like ? ";
    	}	

    	int stmtCount = 1;    
    	String sql = "";   	
    	if(countFlag){
    		sql = " select count(SNO) FROM LAPHONE_MEMBER "+where;
    	}else if(pageSize==-1){
    		sql = " SELECT "+getFieldsSql()+
      			  " FROM LAPHONE_MEMBER "+where +" GROUP BY SITE_ID,ITEM_ID,CATE_ID,ITEM_NAME "+orderBy;    		
    	}else{
    		sql = " SELECT top "+pageSize+getFieldsSql()+" FROM LAPHONE_MEMBER "+
    			  where +" AND SNO NOT IN ( SELECT top "+(pageSize*(pageIndex-1))+" SNO FROM  LAPHONE_MEMBER "+where+orderBy+" )  "+
    			  orderBy;
    		stmtCount = 2;
    	}
    	
    	PreparedStatement stmt = conn.prepareStatement(sql);
    	int idx = 1;
    	for(int i=0;i<stmtCount;i++){
			stmt.setString(idx++, siteId);
	    	if(accountId!=null && !"".equals(accountId)){
	    		stmt.setString(idx++, "%"+accountId+"%");
	    	}
	    	if(userName!=null && !"".equals(userName)){
	    		stmt.setString(idx++,"%"+userName+"%");
	    	}
	    	if(email!=null && !"".equals(email)){
	    		stmt.setString(idx++,"%"+email+"%");
	    	}
	    	if(gender==1 || gender==2){
	    		stmt.setInt(idx++, gender);
	    	}
	    	if(status ==0 || status==1 || status==2){
	    		stmt.setInt(idx++, status);	
	    	}
	    	if(idNo!=null && !"".equals(idNo)){
	    		stmt.setString(idx++,"%"+idNo+"%");
	    	}
	    	if(cellphone!=null && !"".equals(cellphone)){
	    		stmt.setString(idx++,"%"+cellphone+"%");
	    	}
    	}
    	return stmt;
    }
    
	@Override
	protected String getDBTableName() {
		return "LAPHONE_MEMBER";
	}

	

}
