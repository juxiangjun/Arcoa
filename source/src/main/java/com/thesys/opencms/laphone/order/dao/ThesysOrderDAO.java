/**
 * 
 */
package com.thesys.opencms.laphone.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.thesys.opencms.laphone.ThesysLaphoneDAO;
import com.thesys.opencms.laphone.order.dao.ThesysOrderVO;
import com.thesys.opencms.laphone.promote.dao.ThesysCouponDAO;
/**
 * @author maggie
 *
 */
public class ThesysOrderDAO extends ThesysLaphoneDAO {
	 /** The singleton object. */
    private static ThesysOrderDAO m_instance;
	/**
	 * 
	 */
	private ThesysOrderDAO() {
		super.init();
	}
	
	/**
     * Singleton access.<p>
     * 
     * @return the singleton object
     */
    public static synchronized ThesysOrderDAO getInstance() {

        if (m_instance == null) {
            m_instance = new ThesysOrderDAO();
        }
        return m_instance;
    }
    /**
     * 更新白板註記
     * @param siteId
     * @param orderId
     * @param note
     * @throws SQLException
     */
    public void appendProcessNote(String siteId,String orderId,String note) throws Exception{
    	Connection con = null;
		PreparedStatement stmt = null;
		try {
			//TODO:
			con = getConnection();
			String sql = "UPDATE LAPHONE_ORD_MAIN SET PROCESS_NOTE=(ISNULL(PROCESS_NOTE,'')+?) WHERE SITE_ID=? AND ORD_ID=? ";
	        int idx = 1;
	    	stmt = con.prepareStatement(sql);
	        stmt.setString(idx++, note);
	        stmt.setString(idx++, siteId);
	        stmt.setString(idx++, orderId);
	        int resultCount =stmt.executeUpdate();
	        if(resultCount==0){
	        	throw new Exception("無符合的資料");
	        }else if(resultCount>1){
	        	throw new Exception("更新不止一筆資料");
	        }
		} finally {
			closeAll(con, stmt, null);
		}
    }
    /**
     * 依會員ID查詢訂單筆數
     * @param siteId
     * @param memberId
     * @param searchType 查詢型態-1:未出貨及一個月內訂單,2:未出貨訂單,3:退換貨訂單,4:六個月內訂單,5:訂單編號查詢,6：已到店,7：已寄出
     * @param orderId
     * @return 訂單筆數
     * @throws SQLException
     */
    public int count(String siteId,String memberId, int searchType, String orderId) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        int result = 0;
        try {
            con = getConnection();
            String searchStr = "";
            Calendar calendar = Calendar.getInstance();
            java.util.Date sixMonthAgoDate = getSixMonthAgoDate();
            
            if(searchType == 1) {//未出貨及一個月內訂單
            	calendar.add(Calendar.MONTH, -1);
            	searchStr = " AND (ORD_ST in(0,1) OR ORD_DATE>=? ) ";
            } else if(searchType == 2) {//未出貨訂單
            	searchStr = " AND ORD_ST in (0,1) ";
            } else if(searchType == 3) {//退換貨訂單
            	searchStr = " AND ORD_ST in (10,11,12,13) ";
            } else if(searchType == 4) {//六個月內訂單 
            	calendar.add(Calendar.MONTH, -6);
            	searchStr = " AND ORD_DATE>=? ";
            } else if(searchType == 5) {//訂單編號查詢
            	searchStr = " AND ORD_ID=? ";
            } else if(searchType == 6) {//已到店
            	searchStr = " AND ORD_ST =5 ";
            } else if(searchType == 7) {//已寄出
            	searchStr = " AND ORD_ST = 4 ";
            }
            
            String sql = "SELECT count(*) FROM LAPHONE_ORD_MAIN where SITE_ID=? and MEM_ID=? AND ORD_DATE>=? " + searchStr;
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, memberId);
            stmt.setTimestamp(idx++, convert(sixMonthAgoDate));
            if(searchType == 1 || searchType == 4)
            	stmt.setTimestamp(idx++, convert(calendar.getTime()));
            else if(searchType == 5)
            	stmt.setString(idx++, orderId);
            
            rs = stmt.executeQuery();
            if (rs.next()) {
            	result = rs.getInt(1);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }
    /**
     * 訂單列表
     * @param siteId
     * @param memberId
     * @param pageSize
     * @param pageIndex
     * @param searchType 查詢型態-1:未出貨及一個月內訂單,2:未出貨訂單,3:退換貨訂單,4:六個月內訂單,5:訂單編號查詢
     * @param orderId
     * @return List<ThesysOrderVO>
     * @throws SQLException
     */
    public List<ThesysOrderVO> listByPage(String siteId,String memberId,int pageSize,int pageIndex, int searchType, String orderId) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<ThesysOrderVO> result = new ArrayList<ThesysOrderVO>();
        try {
            con = getConnection();
            String searchStr = "";
            java.util.Date sixMonthAgoDate = getSixMonthAgoDate();//會員訂單查詢僅提供六個月內的交易資料所以加搜尋條件
            Calendar calendar = Calendar.getInstance();
            
            if(searchType == 1) {//未出貨及一個月內訂單
            	calendar.add(Calendar.MONTH, -1);
            	searchStr = " AND (ORD_ST in(0,1) OR ORD_DATE>=? ) ";
            } else if(searchType == 2) {//未出貨訂單
            	searchStr = " AND ORD_ST in (0,1) ";
            } else if(searchType == 3) {//退換貨訂單
            	searchStr = " AND ORD_ST in (10,11,12,13) ";
            } else if(searchType == 4) {//六個月內訂單 
            	calendar.add(Calendar.MONTH, -6);
            	searchStr = " AND ORD_DATE>=? ";
            } else if(searchType == 5) {//訂單編號查詢
            	searchStr = " AND ORD_ID=? ";
            } else if(searchType == 6) {//已到店
            	searchStr = " AND ORD_ST =5 ";
            } else if(searchType == 7) {//已寄出
            	searchStr = " AND ORD_ST = 4 ";
            }
            java.util.Date date = calendar.getTime();

            String sql = "SELECT top "+pageSize+" * FROM LAPHONE_ORD_VIEW "+
            			 " WHERE SITE_ID=? AND MEM_ID=? AND ORD_DATE>=? "+searchStr+" AND ORD_ID NOT IN ( SELECT top "+(pageSize*(pageIndex-1))+" ORD_ID FROM LAPHONE_ORD_VIEW WHERE SITE_ID=? and MEM_ID=? AND ORD_DATE>=? "+searchStr+" ORDER BY ORD_DATE DESC )"+
            			 " ORDER BY ORD_DATE DESC";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            for(int i=0;i<2;i++){
	            stmt.setString(idx++, siteId);
	            stmt.setString(idx++, memberId);
	            stmt.setTimestamp(idx++, convert(sixMonthAgoDate));
	            if(searchType == 1 || searchType == 4)
	            	stmt.setTimestamp(idx++, convert(date));
	            else if(searchType == 5)
	            	stmt.setString(idx++, orderId);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
            	ThesysOrderVO item = ThesysOrderVO.getInstance(rs);
            	result.add(item);
            }
        }finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }
    
    /**
     * 查詢後台訂單筆數
     * @param siteId
     * @param orderStatus
     * @return 訂單筆數
     * @throws SQLException
     */
    public int countForBackend(String siteId, String orderStatus, String startOrderDate, String endOrderDate, 
    		String memberId, String memberName, String orderId,String payType) throws Exception{
    	Connection con = null;
        PreparedStatement stmt = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        ResultSet rs = null;

        int result = 0;
        try {
            con = getConnection();
            String searchStr = "";
            if(orderStatus!=null && !"".equals(orderStatus))
            	searchStr += " AND ORD_ST=? ";
            if(startOrderDate!=null && !"".equals(startOrderDate))
            	searchStr += " AND ORD_DATE>=? ";
            if(endOrderDate!=null && !"".equals(endOrderDate))
            	searchStr += " AND ORD_DATE<=? ";
            if(memberId!=null && !"".equals(memberId))
            	searchStr += " AND MEM_ID like ? ";
            if(memberName!=null && !"".equals(memberName))
            	searchStr += " AND MEMBER_NAME like ?";
            if(orderId!=null && !"".equals(orderId))
            	searchStr += " AND ORD_ID like ?";
            if(payType!=null && !"".equals(payType))
            	searchStr += " AND PAY_TYPE=? ";
            
            String sql = "SELECT COUNT(*) FROM LAPHONE_ORD_VIEW WHERE SITE_ID=? " + searchStr;
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            if(orderStatus!=null && !"".equals(orderStatus))
        		stmt.setInt(idx++, Integer.parseInt(orderStatus));
            if(startOrderDate!=null && !"".equals(startOrderDate))
            	stmt.setTimestamp(idx++, convert(sdf.parse(startOrderDate + " 00:00:00.000")));
            if(endOrderDate!=null && !"".equals(endOrderDate))
            	stmt.setTimestamp(idx++, convert(sdf.parse(endOrderDate + " 23:59:59.999")));
            if(memberId!=null && !"".equals(memberId))
            	stmt.setString(idx++, "%" + memberId + "%");
            if(memberName!=null && !"".equals(memberName))
            	stmt.setString(idx++, "%" + memberName + "%");
            if(orderId!=null && !"".equals(orderId))
            	stmt.setString(idx++, "%" + orderId + "%");
            if(payType!=null && !"".equals(payType))
        		stmt.setInt(idx++, Integer.parseInt(payType));
            rs = stmt.executeQuery();
            if (rs.next()) {
            	result = rs.getInt(1);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }
    /**
     * 查詢後台訂單列表
     * @param siteId
     * @param pageSize
     * @param pageIndex
     * @param orderStatus    查詢條件
     * @param startDate 	查詢條件-訂單日期
     * @param endDate   	查詢條件-訂單日期
     * @param memberId       查詢條件
     * @param memberName     查詢條件
     * @param orderId        查詢條件
     * @return List<ThesysOrderVO>
     * @throws Exception
     */
    public List<ThesysOrderVO> listByPageForBackend(String siteId,int pageSize,int pageIndex,String orderStatus, String startDate, 
    		String endDate, String memberId, String memberName, String orderId,String payType) throws Exception{
    	
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        List<ThesysOrderVO> result = new ArrayList<ThesysOrderVO>();
        try {
            con = getConnection();
            String searchStr = "";
            if(orderStatus!=null && !"".equals(orderStatus))
            	searchStr += " AND ORD_ST=? ";
            if(startDate!=null && !"".equals(startDate))
            	searchStr += " AND ORD_DATE>=? ";
            if(endDate!=null && !"".equals(endDate))
            	searchStr += " AND ORD_DATE<=? ";
            if(memberId!=null && !"".equals(memberId))
            	searchStr += " AND MEM_ID like ? ";
            if(memberName!=null && !"".equals(memberName))
            	searchStr += " AND MEM_NAME like ?";
            if(orderId!=null && !"".equals(orderId))
            	searchStr += " AND ORD_ID like ?";
            if(payType!=null && !"".equals(payType))
            	searchStr += " AND PAY_TYPE=? ";
            
            String sql = "SELECT top "+pageSize+" * FROM LAPHONE_ORD_VIEW"+
            			 " WHERE SITE_ID=? and ORD_ID NOT IN ( SELECT top "+(pageSize*(pageIndex-1)) + " ORD_ID FROM LAPHONE_ORD_VIEW WHERE SITE_ID=? "+searchStr+" ORDER BY ORD_DATE DESC )"+
            			 searchStr +
            			 " ORDER BY ORD_DATE DESC";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, siteId);
            for(int i=0;i<2;i++){
            	if(orderStatus!=null && !"".equals(orderStatus))
            		stmt.setInt(idx++, Integer.parseInt(orderStatus));
	            if(startDate!=null && !"".equals(startDate))
	            	stmt.setTimestamp(idx++, convert(sdf.parse(startDate + " 00:00:00.000")));
	            if(endDate!=null && !"".equals(endDate))
	            	stmt.setTimestamp(idx++, convert(sdf.parse(endDate + " 23:59:59.999")));
	            if(memberId!=null && !"".equals(memberId))
	            	stmt.setString(idx++, "%" + memberId + "%");
	            if(memberName!=null && !"".equals(memberName))
	            	stmt.setString(idx++, "%" + memberName + "%");
	            if(orderId!=null && !"".equals(orderId))
	            	stmt.setString(idx++, "%" + orderId + "%");
            	if(payType!=null && !"".equals(payType))
            		stmt.setInt(idx++, Integer.parseInt(payType));
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
            	ThesysOrderVO item = ThesysOrderVO.getInstance(rs);
            	result.add(item);
            }
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }   
    
    private java.util.Date getSixMonthAgoDate(){
    	Calendar calendar = Calendar.getInstance();
    	calendar.add(Calendar.MONTH, -6);
    	calendar.set(Calendar.HOUR, 0);
    	calendar.set(Calendar.MINUTE, 0);
    	calendar.set(Calendar.SECOND, 0);
    	calendar.set(Calendar.MILLISECOND, 0);
    	return calendar.getTime();
    }
    
    
	/**
	 * 便利達康-取貨完成
	 * @param siteId
	 * @param orderId
	 * @param recDate 實際取貨繳費日期
	 * @param accDate 結帳基準日期
	 * @return
	 * @throws SQLException
	 */
  	public int updateCvsReceived(String siteId, String orderId,Date receivedDate,Date basicDate) throws SQLException {
      	Connection con = null;
		PreparedStatement stmt = null;
		int r = 0;
		try {

			con = getConnection();
			String sql = "UPDATE LAPHONE_ORD_MAIN SET ORD_ST=?,CVS_ACC_DATE=?, REC_DATE=? WHERE SITE_ID=? AND ORD_ID=? ";
	        int idx = 1;
	    	stmt = con.prepareStatement(sql);
	        stmt.setInt(idx++,ThesysOrderVO.ORDER_STATUS_RECEIVED);
	        stmt.setTimestamp(idx++, convert(basicDate));
	    	stmt.setTimestamp(idx++, convert(receivedDate));
	        stmt.setString(idx++, siteId);
	        stmt.setString(idx++, orderId);
	        r = stmt.executeUpdate();  
		} finally {
			closeAll(con, stmt, null);
		}
		return r;
    }
  	
    
    
  	/**
  	 * 便利達康：驗退資訊更新
  	 * @param siteId
  	 * @param orderId
  	 * @param returnReason	退貨原因
  	 * @param returnDate	大物流實際驗退日
  	 * @param basicDate	結帳基準日
  	 * @return
  	 * @throws SQLException
  	 */
    public int updateCvsReturn(String siteId ,String orderId,String returnReason,Date returnDate,Date basicDate) throws SQLException {
    	Connection con = null;
        PreparedStatement stmt = null;
        int r = 0 ;
        try {
            con = getConnection();
	    	String sql = "UPDATE LAPHONE_ORD_MAIN SET ORD_ST=?,CVS_ACC_DATE=?, REC_ST=?,REC_DATE=? WHERE SITE_ID=? AND ORD_ID=? ";
	        int idx = 1;
	    	stmt = con.prepareStatement(sql);
	        stmt.setInt(idx++,ThesysOrderVO.ORDER_STATUS_UNRECEIVED);
	        stmt.setTimestamp(idx++, convert(basicDate));
	        stmt.setString(idx++, returnReason);
	    	stmt.setTimestamp(idx++, convert(returnDate));
	        stmt.setString(idx++, siteId);
	        stmt.setString(idx++, orderId);
	        r = stmt.executeUpdate();  
        } finally {
        	closeAll(con, stmt, null);
        }
        return r;
    }
    
    
    /**
     * 更新ATM_CODE
     * @param siteId
     * @param memId
     * @param ordId
     * @param atmCode
     * @return 
     * @throws SQLException
     */
    public int updateAtmCode(String siteId, String memberId, String ordId, String atmCode) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            con = getConnection();
            ThesysOrderVO vo = read(siteId, ordId);
            if(vo!=null && vo.getOrderStatus()==0){
            	String sql = "UPDATE LAPHONE_ORD_MAIN SET ORD_ST=?,ATM_CODE=?,ATM_DATE=? WHERE SITE_ID=? AND MEM_ID=? AND ORD_ID=?";
                stmt = con.prepareStatement(sql);
                int idx = 1;
                stmt.setInt(idx++, ThesysOrderVO.ORDER_STATUS_DEFAULT);
                stmt.setString(idx++, atmCode);
                stmt.setTimestamp(idx++, convert(new java.util.Date()));
                stmt.setString(idx++, siteId);
                stmt.setString(idx++, memberId);
                stmt.setString(idx++, ordId);
                return stmt.executeUpdate();
            }
            return 0;
        } finally {
            closeAll(con, stmt, rs);
        }
    }
    
    /**
     * 審核訂單
     * @param siteId
     * @param orderIds
     * @param orderStatus
     * @param orderMsg
     * @param createUserId
     * @return
     * @throws SQLException
     */
    public int checkOrder(String siteId, String orderId, int orderStatus,String userId) throws SQLException{
    	if(orderId!=null) {
	    	Connection con = null;
	        PreparedStatement stmt = null;
	        ResultSet rs = null;
	        try {
	        	con = getConnection();
	        	String sql = "UPDATE LAPHONE_ORD_MAIN SET ORD_ST = ?,CHECKED_DATE = ?,CHECKED_USR_ID = ? WHERE SITE_ID = ? AND ORD_ID = ?";
	        	stmt = con.prepareStatement(sql);
        		int idx = 1;
	            stmt.setInt(idx++, orderStatus);
	            stmt.setTimestamp(idx++, convert(new java.util.Date()));
	            stmt.setString(idx++, userId);
	            stmt.setString(idx++, siteId);
	            stmt.setString(idx++, orderId);
	            return stmt.executeUpdate();
	        } finally {
	            closeAll(con, stmt, rs);
	        }
    	}
    	return 0;
    }
    /**
     * 更新SAP訂單資料：SAP訂單編號、過帳日期、發票號碼
     * 批次更新EC訂單銷貨完成過帳檔(出貨&WMS撿貨完成準備出貨) 
     * @param siteId
     * @param orderId
     * @param orderStatus
     * @return
     */
    public int updateSapOrder(String siteId,String orderId,int orderStatus,String sapOrderNo,String sapShipNo,String sapBillingNo,java.util.Date postDate,String invoiceNo){
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "UPDATE LAPHONE_ORD_MAIN SET ORD_ST=?,SAP_ORD_NO=?,SAP_SHIP_NO=?,SAP_BILLING_NO=?,SAP_POSTING_DATE=?,INVOICE_NO=? WHERE SITE_ID=? AND ORD_ID=? ";
        	stmt = con.prepareStatement(sql);
    		stmt.clearParameters();
    		int idx = 1;
    		stmt.setInt(idx++,orderStatus);	    
            stmt.setString(idx++, sapOrderNo);     
            stmt.setString(idx++, sapShipNo);      
            stmt.setString(idx++, sapBillingNo);       
            stmt.setTimestamp(idx++, convert(postDate));
            stmt.setString(idx++, invoiceNo);     
            stmt.setString(idx++, siteId);    
            stmt.setString(idx++, orderId);
            return stmt.executeUpdate();
        } catch(Exception e){ 
        	e.printStackTrace();
        }finally {
            closeAll(con, stmt, rs);
        }
    	return 0;
    	
    }
    /**
     * 註記收款報告書日期
     * @param siteId
     * @param orderId
     * @param invoiceDate
     * @param userId
     * @return
     * @throws SQLException
     */
    public int updateReceiptDate(String siteId , String orderId,Date receiptDate,String userId) throws SQLException {
    	Connection con = null;
        PreparedStatement stmt = null;
        int r = 0 ;
        try {
            con = getConnection();
	    	String sql = "UPDATE LAPHONE_ORD_MAIN SET RECEIPT_DATE=?,RECEIPT_USR_ID=? WHERE SITE_ID=? AND ORD_ID=? ";
	        int idx = 1;
	    	stmt = con.prepareStatement(sql);
	        stmt.setTimestamp(idx++, convert(receiptDate));
	        stmt.setString(idx++, userId);
	        stmt.setString(idx++, siteId);
	        stmt.setString(idx++, orderId);
	        r = stmt.executeUpdate();  
        }  finally {
        	closeAll(con, stmt, null);
        }
        return r;
    }
    /**
     * 註記發票寄送日期
     * @param siteId
     * @param orderId
     * @param invoiceDate
     * @param userId
     * @return
     * @throws SQLException
     */
    public int updateInvoiceDate(String siteId , String orderId,Date invoiceDate,String userId) throws SQLException {
    	Connection con = null;
        PreparedStatement stmt = null;
        int r = 0 ;
        try {
            con = getConnection();
	    	String sql = "UPDATE LAPHONE_ORD_MAIN SET INVOICE_DATE=?,INVOICE_USR_ID=? WHERE SITE_ID=? AND ORD_ID=? ";
	        int idx = 1;
	    	stmt = con.prepareStatement(sql);
	        stmt.setTimestamp(idx++, convert(invoiceDate));
	        stmt.setString(idx++, userId);
	        stmt.setString(idx++, siteId);
	        stmt.setString(idx++, orderId);
	        r = stmt.executeUpdate();  
        }  finally {
        	closeAll(con, stmt, null);
        }
        return r;
    }
    /**
	 * 更新退貨完成過帳檔資訊
	 * @param siteId
	 * @param orderId
	 * @param postDate
	 * @return
     * @throws SQLException 
	 */
    public int updateReturnPost(String siteId ,String orderId,Date postDate) throws SQLException {
    	Connection con = null;
        PreparedStatement stmt = null;
        int r = 0 ;
        try {
            con = getConnection();
	    	String sql = "UPDATE LAPHONE_ORD_MAIN SET ORD_ST=?,RTN_POST_DATE=? WHERE SITE_ID=? AND ORD_ID=? ";
	        int idx = 1;
	    	stmt = con.prepareStatement(sql);
	        stmt.setInt(idx++, ThesysOrderVO.ORDER_STATUS_RETURNED);
	        stmt.setTimestamp(idx++, convert(postDate));
	        stmt.setString(idx++, siteId);
	        stmt.setString(idx++, orderId);
	        r = stmt.executeUpdate();  
        }  finally {
        	closeAll(con, stmt, null);
        }
        return r;
    }
    
    /**
     * 更新WMS宅配資訊
     * @param siteId
     * @param orderId
     * @param recDate
     * @param recStatus
     * @return
     * @throws SQLException 
     */
    public int updateWMS(String siteId ,String orderId,int orderStatus,Date recDate,int recStatus) throws SQLException {
    	Connection con = null;
        PreparedStatement stmt = null;
        
        try {
            con = getConnection();
	    	String sql = "UPDATE LAPHONE_ORD_MAIN SET ORD_ST = ?,REC_DATE=? ,REC_ST=? WHERE SITE_ID=? AND ORD_ID=? ";
	        int idx = 1;
	    	stmt = con.prepareStatement(sql);
	        stmt.setInt(idx++, orderStatus);
	        stmt.setTimestamp(idx++, convert(recDate));
	        stmt.setInt(idx++, recStatus);
	        stmt.setString(idx++, siteId);
	        stmt.setString(idx++, orderId);
	        return stmt.executeUpdate();  
        }finally {
        	closeAll(con, stmt, null);
        }
       
    }
    
    /**
     * 更新進店資料
     * @param siteId
     * @param orderId
     * @param dcst
     * @return
     * @throws SQLException 
     */
    public int updateInCvsStore(String siteId,String orderId, Date arrivalDate) throws Exception {
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql ="UPDATE LAPHONE_ORD_MAIN SET CVS_ARR_DATE=? , ORD_ST=? WHERE SITE_ID=? AND ORD_ID=?";
            stmt = con.prepareStatement(sql);
        	stmt.setTimestamp(1, convert(arrivalDate));
        	stmt.setInt(2, ThesysOrderVO.ORDER_STATUS_CVS);
        	stmt.setString(3, siteId);
        	stmt.setString(4, orderId);
        	return stmt.executeUpdate();
        } catch(Exception e){
        	if(con!=null)
        		con.rollback();
        }finally {
            closeAll(con, stmt, rs);
        }
        return 0;
	}

    /**
     * 後台審核取消訂單
     * @param siteId
     * @param orderId
     * @param orderStatus
     * @return
     */
    public int checkCanceledOrder(String siteId,String orderId,int orderStatus,String userId){
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "UPDATE LAPHONE_ORD_MAIN SET ORD_ST = ?,CANCELED_DATE = ?,CANCELED_USR_ID = ? WHERE SITE_ID = ? AND ORD_ID = ?";
        	stmt = con.prepareStatement(sql);
    		stmt.clearParameters();
    		int idx = 1;
    		stmt.setInt(idx++,orderStatus);	            
            stmt.setTimestamp(idx++, convert(new java.util.Date()));
            stmt.setString(idx++, userId);
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, orderId);
            return stmt.executeUpdate();
        } catch(Exception e){ 
        	e.printStackTrace();
        }finally {
            closeAll(con, stmt, rs);
        }
    	return 0;
    	
    }
    /**
     * 後台審核退貨
     * @param siteId
     * @param orderId
     * @param orderStatus
     * @return
     */
    public int checkReturn(String siteId,String orderId,int orderStatus,String userId){
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "UPDATE LAPHONE_ORD_MAIN SET ORD_ST = ?,RTN_CHK_DATE = ?,RTN_CHK_USR_ID = ? WHERE SITE_ID = ? AND ORD_ID = ?";
        	stmt = con.prepareStatement(sql);
    		stmt.clearParameters();
    		int idx = 1;
    		stmt.setInt(idx++,orderStatus);	            
            stmt.setTimestamp(idx++, convert(new java.util.Date()));
            stmt.setString(idx++, userId);
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, orderId);
            return stmt.executeUpdate();
        } catch(Exception e){ 
        	e.printStackTrace();
        }finally {
            closeAll(con, stmt, rs);
        }
    	return 0;
    	
    }
    /**
     * 處理退貨訂單(貨運已收取退貨商品)
     * @param siteId
     * @param orderId
     * @param orderStatus
     * @return
     */
    public int processReturn(String siteId,String orderId,int orderStatus,String userId){
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "UPDATE LAPHONE_ORD_MAIN SET ORD_ST = ?,RTN_REC_DATE = ?,RTN_REC_USR_ID = ? WHERE SITE_ID = ? AND ORD_ID = ?";
        	stmt = con.prepareStatement(sql);
    		stmt.clearParameters();
    		int idx = 1;
    		stmt.setInt(idx++,orderStatus);	            
            stmt.setTimestamp(idx++, convert(new java.util.Date()));
            stmt.setString(idx++, userId);
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, orderId);
            return stmt.executeUpdate();
        } catch(Exception e){ 
        	e.printStackTrace();
        }finally {
            closeAll(con, stmt, rs);
        }
    	return 0;
    	
    }
    
   
    
    /**
     * 取消訂單
     * @param siteId
     * @param orderId
     * @param contactName  聯絡人姓名
     * @param contactPhone 聯絡電話
     * @param reasonCode   取消原因代號
     * @param reesonTxt    取消原因
     * @return
     * @throws SQLException
     */
    public int cancelOrder(String siteId, String memberId, String orderId, int orderStatus, String contactName, String contactPhone, int reasonCode, String reasonTxt,java.util.Date cancelDate) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "UPDATE LAPHONE_ORD_MAIN SET ORD_ST = ?,CANCEL_CODE=?,CANCEL_REASON=?,CANCEL_NAME=?,CANCEL_PHONE=?,CANCEL_DATE = ? WHERE SITE_ID = ? AND ORD_ID = ?";
        	stmt = con.prepareStatement(sql);
    		int idx = 1;
    		stmt.setInt(idx++,orderStatus);	 
            stmt.setInt(idx++, reasonCode);
            stmt.setString(idx++, reasonTxt);
            stmt.setString(idx++, contactName);
            stmt.setString(idx++, contactPhone);
            stmt.setTimestamp(idx++, convert(cancelDate));
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, orderId);
            return stmt.executeUpdate();
        } catch(Exception e){ 
        	e.printStackTrace();
        }finally {
            closeAll(con, stmt, rs);
        }
    	return 0;
    }
    /**
     * 申請退貨
     * @return
     * @throws SQLException
     */
    public int returnOrder(String siteId, String orderId, int orderStatus, int returnType,int reasonCode, String reasonTxt,java.util.Date returnDate) throws SQLException{
		Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String sql = "UPDATE LAPHONE_ORD_MAIN SET ORD_ST = ?,RTN_TYPE=?,RTN_CODE=?,RTN_REASON=?,RTN_DATE = ? WHERE SITE_ID = ? AND ORD_ID = ?";
        	stmt = con.prepareStatement(sql);
    		int idx = 1;
    		stmt.setInt(idx++,orderStatus);	 
    		stmt.setInt(idx++,returnType);	 
            stmt.setInt(idx++, reasonCode);
            stmt.setString(idx++, reasonTxt);
            stmt.setTimestamp(idx++, convert(returnDate));
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, orderId);
            return stmt.executeUpdate();
        } catch(Exception e){ 
        	e.printStackTrace();
        }finally {
            closeAll(con, stmt, rs);
        }
    	return 0;
    }
    
    public ThesysOrderVO read(String siteId, String memberId, String orderId) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        ThesysOrderVO result = null;
        try {
            con = getConnection();
            String memberCondition = "";
            if(memberId!=null && !"".equals(memberId))
            	memberCondition = " AND MEM_ID=? ";
            String sql = " SELECT * FROM LAPHONE_ORD_VIEW WHERE SITE_ID=? AND ORD_ID=? " + memberCondition;
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, orderId);
            if(memberId!=null && !"".equals(memberId))
            	stmt.setString(idx++, memberId);

            rs = stmt.executeQuery();
            if (rs.next()) {
            	result = ThesysOrderVO.getInstance(rs);
            	
            	sql = "SELECT  * FROM LAPHONE_ORD_ITEM where SITE_ID=? and ORD_ID=? ";
            	stmt = con.prepareStatement(sql);
                idx = 1;
                stmt.setString(idx++, siteId);
                stmt.setString(idx++, orderId);
                rs = stmt.executeQuery();
                List<ThesysOrderItemVO> items = new ArrayList<ThesysOrderItemVO>();
                while(rs.next()){
                	items.add(ThesysOrderItemVO.getInstance(rs));
                }
                result.setOrderItems(items);
                
                if(result.getPayType()==ThesysOrderVO.PAY_TYPE_CREDIT || result.getPayType()==ThesysOrderVO.PAY_TYPE_INSTALLMENT || result.getPayType()==ThesysOrderVO.PAY_TYPE_CHINATRUST  ){
                	//讀取信用卡資料
                	sql = "SELECT  * FROM LAPHONE_ORD_CREDIT where SITE_ID=? and ORD_ID=? ";
                	stmt = con.prepareStatement(sql);
                    idx = 1;
                    stmt.setString(idx++, siteId);
                    stmt.setString(idx++, orderId);
                    rs = stmt.executeQuery();
                    if(rs.next()){
                    	ThesysOrderCreditVO credit = ThesysOrderCreditVO.getInstance(rs);
                    	result.setCredit(credit);
                    }
                	
                }
            	
            }
            
        } finally {
            closeAll(con, stmt, rs);
        }
        return result;
    }

    /**
     * 取得所有尚未核可的訂單中特定商品的數量
     * @param siteId
     * @param itemId 商品編號
     * @return
     * @throws SQLException
     */
    public int getCountFormUncheckedItem(String siteId, String itemId) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int c = 0;
        try {
            con = getConnection();
            String sql = "SELECT SUM(ORD_QTY) " +
            			 "FROM LAPHONE_ORD_MAIN MAIN , LAPHONE_ORD_ITEM ITEM  " +
            			 "WHERE MAIN.SITE_ID = ITEM.SITE_ID AND " +
            			 	   "MAIN.ORD_ID = ITEM.ORD_ID AND " +
            				   "MAIN.SITE_ID=? AND " +
            				   "ITEM.ITEM_ID=? AND " +
            				   "(MAIN.ORD_ST=-1 or MAIN.ORD_ST=0 or MAIN.ORD_ST=1) ";
            stmt = con.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, siteId);
            stmt.setString(idx++, itemId);
            rs = stmt.executeQuery();
            if(rs.next()){
  		    	c = rs.getInt(1);
  		    }
        } finally {
            closeAll(con, stmt, rs);
        }
        return c;
    }

    public ThesysOrderVO read(String siteId,String orderId) throws SQLException{
    	return read(siteId, null, orderId);
    }
    /**
     * 寫入信用卡交易記錄
     * @param vo
     * @throws SQLException
     */
    public void insertCredit(ThesysOrderCreditVO vo) throws SQLException{
    	Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            
            String sql = " INSERT INTO LAPHONE_ORD_CREDIT(SITE_ID, ORD_ID, SUCC, GWSR, RESPONSE_CODE, RESPONSE_MSG, "+
		   				 " PROCESS_DATE, PROCESS_TIME, OD_SOB, AUTH_CODE, AMOUNT, STAGE, STAST, STAED, OD_HOHO, ECI, "+
		   				 " RECH_KEY, INSPECT, SPCHECK, CARD6NO, CARD4NO, EXPIRE_DT, INV_CODE, INV_ERROR) "+
		   				 " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            stmt = con.prepareStatement(sql); 
            int idx = 1;
            stmt.setString(idx++, vo.getSiteId());
            stmt.setString(idx++, vo.getOrderId());
            stmt.setInt(idx++, vo.getSucc());
            stmt.setInt(idx++, vo.getGwsr());
            stmt.setString(idx++, vo.getResponse_code());
            stmt.setString(idx++, vo.getResponse_msg());
            stmt.setString(idx++, vo.getProcess_date());
            stmt.setString(idx++, vo.getProcess_time());
            stmt.setString(idx++, vo.getOd_sob());
            stmt.setString(idx++, vo.getAuth_code());
            stmt.setInt(idx++, vo.getAmount());
            stmt.setInt(idx++, vo.getStage());
            stmt.setInt(idx++, vo.getStast());
            stmt.setInt(idx++, vo.getStaed());            
            stmt.setString(idx++, vo.getOd_hoho());
            stmt.setInt(idx++, vo.getEci());
            stmt.setString(idx++, vo.getRech_key());
            stmt.setString(idx++, vo.getInspect());
            stmt.setInt(idx++, vo.getSpcheck());
            stmt.setString(idx++, vo.getCard6no());
            stmt.setString(idx++, vo.getCard4no());
            stmt.setString(idx++, vo.getExpire_dt());
            stmt.setString(idx++, vo.getInv_code());
            stmt.setString(idx++, vo.getInv_error());
            if(stmt.executeUpdate()>0){ //更新狀態
            	sql = "UPDATE LAPHONE_ORD_MAIN set ORD_ST = ?,ORD_MSG=?,FAILURE_DATE = ? where SITE_ID=? and ORD_ID=?";
            	stmt = con.prepareStatement(sql); 
                idx = 1;
                stmt.setInt(idx++, vo.isAuthSuccess()?ThesysOrderVO.ORDER_STATUS_DEFAULT:ThesysOrderVO.ORDER_STATUS_FAILURE);
                stmt.setString(idx++, vo.isAuthSuccess()?"":"信用卡授權失敗");  
                stmt.setTimestamp(idx++, vo.isAuthSuccess()?null:convert(new java.util.Date()));
                stmt.setString(idx++, vo.getSiteId());
                stmt.setString(idx++, vo.getOrderId());
                stmt.executeUpdate();
            }
        } finally {
            closeAll(con, stmt, rs);
        }
    }
    public void insert(ThesysOrderVO vo) throws Exception {

        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        java.util.Date now = new java.util.Date();
        vo.setOrderDate(now);
        try {
            con = getConnection();
            
            
            // 1) Write a new entry 
        	String sql = " INSERT INTO LAPHONE_ORD_MAIN(SITE_ID, ORD_ID, MEM_ID,MEM_PID,MEM_NAME,MEM_MOBILE,MEM_EMAIL, ORD_ST,ORD_DATE, SHIP_TYPE, "+
        				 " PAY_TYPE, TT_QTY, TT_AMT, CP_CODE, CP_AMT, SHIP_FEE, ORD_AMT, CP_CNT_AMT, CP_USE_AMT, "+
        				 " REC_NAME, REC_PHONE, REC_MOBILE, REC_EMAIL,REC_ZIPCODE,REC_COUNTY,REC_AREA,REC_ADDR,REC_TIME,REC_NOTE,"+
        				 " CVS_ST_NO, INVOICE_TYPE, INVOICE_ADDR, INVOICE_BUYER, COMP_NO, COMP_NAME, COMP_ADDR) "+
        				 " VALUES( ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?, ?,?, ?, ?, ?, ?, ?,?,?)";
            stmt = con.prepareStatement(sql); 
            int idx = 1;
            stmt.setString(idx++, vo.getSiteId());
            stmt.setString(idx++, vo.getOrderId());
            stmt.setString(idx++, vo.getMemberId()); 
            stmt.setString(idx++, vo.getMemberPid());  
            stmt.setString(idx++, vo.getMemberName());  
            stmt.setString(idx++, vo.getMemberMobile());  
            stmt.setString(idx++, vo.getMemberEmail());  
            stmt.setInt(idx++, vo.getOrderStatus());           
            stmt.setTimestamp(idx++, convert(vo.getOrderDate()));
            stmt.setString(idx++, vo.getShipType()); 
            stmt.setInt(idx++, vo.getPayType()); 
            stmt.setInt(idx++, vo.getTotalQuantity()); 
            stmt.setInt(idx++, vo.getTotalAmount()); 
            stmt.setString(idx++, vo.getCouponCode()); 
            stmt.setInt(idx++, vo.getCouponAmount()); 
            stmt.setInt(idx++, vo.getShipFee()); 
            stmt.setInt(idx++, vo.getOrderAmount()); 
            stmt.setInt(idx++, vo.getCouponCountAmount()); 
            stmt.setInt(idx++, vo.getCouponUseAmount()); 
            stmt.setString(idx++, vo.getReceiver()); 
            stmt.setString(idx++, vo.getRecPhone()); 
            stmt.setString(idx++, vo.getRecMobile()); 
            stmt.setString(idx++, vo.getRecEmail()); 
            stmt.setString(idx++, vo.getRecZipCode()); 
            stmt.setString(idx++, vo.getRecCounty()); 
            stmt.setString(idx++, vo.getRecArea()); 
            stmt.setString(idx++, vo.getRecAddress()); 
            stmt.setString(idx++, vo.getRecTime());  
            stmt.setString(idx++, vo.getRecNote());  
            stmt.setString(idx++, vo.getCvsStoreNo());  
            stmt.setInt(idx++, vo.getInvoiceType()); 
            stmt.setString(idx++, vo.getInvoiceAddress()); 
            stmt.setString(idx++, vo.getInvoiceBuyer()); 
            stmt.setString(idx++, vo.getCompanyNo()); 
            stmt.setString(idx++, vo.getCompanyName()); 
            stmt.setString(idx++, vo.getCompanyAddress());             
            if(stmt.executeUpdate()>0){
            
	            //2)insert order item
	            Iterator<ThesysOrderItemVO> it = vo.getOrderItems().iterator();
	            sql = " INSERT INTO LAPHONE_ORD_ITEM(SITE_ID, ORD_ID, ITEM_ID, ITEM_NAME,  "+
	            	  " SPEC_PRC, ORD_QTY,  GRP_FLAG, CNT_FLAG, USE_FLAG)  "+
	            	  " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";	            
	            stmt = con.prepareStatement(sql); 
	            
	            sql = "INSERT INTO LAPHONE_ORD_ITEM_DTL(SITE_ID, ORD_ID, ITEM_ID, DTL_ID, SPEC_PRC, GRP_PRICE, GRP_QTY,CNT_FLAG) "+
	            	  "VALUES(?, ?, ?, ?, ?, ?, ?,?)";
	            PreparedStatement subStmt = con.prepareStatement(sql);
	            while(it.hasNext()){
	            	idx = 1;
	            	ThesysOrderItemVO item = it.next();
	            	stmt.setString(idx++, vo.getSiteId());
	            	stmt.setString(idx++, vo.getOrderId());
	            	stmt.setString(idx++, item.getItemId());
	            	stmt.setString(idx++, item.getItemName());
	            	stmt.setInt(idx++, item.getSpecialPrice());
	            	stmt.setInt(idx++, item.getQuantity());
	            	stmt.setString(idx++, (item.isGroupFlag()?"Y":"N"));
	            	stmt.setString(idx++, (item.isCouponCountFlag()?"Y":"N"));
	            	stmt.setString(idx++, (item.isCouponUseFlag()?"Y":"N"));
	            	stmt.addBatch();
	            	
	            	if(item.isGroupFlag()){
	            		Iterator<ThesysOrderItemDetailVO> dit = item.getItemDetailList().iterator();
	            		while(dit.hasNext()){
	            			ThesysOrderItemDetailVO detail = dit.next();
	            			idx = 1;
	            			subStmt.setString(idx++, vo.getSiteId());
	            			subStmt.setString(idx++, vo.getOrderId());
	            			subStmt.setString(idx++, item.getItemId());
	            			subStmt.setString(idx++, detail.getDetailId());
	            			subStmt.setInt(idx++, detail.getSpecialPrice());
	            			subStmt.setInt(idx++, detail.getGroupPrice());
	            			subStmt.setInt(idx++, detail.getGroupQuantity());
	            			subStmt.setString(idx++, (detail.isCouponCountFlag()?"Y":"N"));
	            			subStmt.addBatch();
	            		}
	            		
	            	}
	            }
	            stmt.executeBatch();
	            subStmt.executeBatch();
	           
	            
	            
	            //3) update coupon
	            if(vo.getCouponAmount()>0){ //有折抵時再更新
	            	ThesysCouponDAO.getInstance().useCoupon(con, vo.getSiteId(), vo.getCouponCode(), vo.getOrderId(), vo.getOrderDate());
	            }
            }
        }catch(Exception e){
        	con.rollback();
        	throw e;
            
        } finally {
            closeAll(con, stmt, rs);
        }
    }

	/* (non-Javadoc)
	 * @see com.thesys.opencms.dao.ThesysAbstractDAO#getDBTableName()
	 */
	@Override
	protected String getDBTableName() {
		return "LAPHONE_ORD_MAIN";
	}

	

	

}
