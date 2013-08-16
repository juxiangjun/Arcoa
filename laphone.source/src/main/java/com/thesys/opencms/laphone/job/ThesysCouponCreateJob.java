package com.thesys.opencms.laphone.job;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneDAO;
import com.thesys.opencms.laphone.order.dao.ThesysOrderVO;
import com.thesys.opencms.laphone.system.ThesysSerialHandler;
import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;

public class ThesysCouponCreateJob  extends ThesysAbstractJob{
		
	protected static final Log LOG = CmsLog.getLog(ThesysCouponCreateJob.class);
	public final static String COUPON_RATE="COUPON_RATE"; //折價券產生趴數  系統參數名;
	private int orderDays = 30;	//查詢多少天之前訂單的天數;
	private int discountRate = 100 ; //折價券table中的比率欄位;
	
	
	/* (non-Javadoc)
	 * @see org.opencms.scheduler.I_CmsScheduledJob#launch(org.opencms.file.CmsObject, java.util.Map)
	 */
	@Override
	public String launch(CmsObject cmso, Map parameters) throws Exception {
		setCmsObject(cmso);	
		doCreate();
        
		return null;
	}
	/**
	 * 主程式
	 * @throws Exception
	 */
	public void doCreate() throws Exception{
		
		
		int i = createCoupon();
//		LOG.info("共產生"+i+"筆折價券");
		System.out.println("共產生"+i+"筆折價券");
	}
	
	//取出符合資格的訂單
	public int createCoupon(){
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//        String strNow = sdf.format(addDate(new Date(),-orderDays));
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startDate = cal.getTime();
        
//        cal.set(Calendar.MILLISECOND, 99);
        cal.add(Calendar.DATE, 30); //使用期限30天
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
       // cal.add(Calendar.MILLISECOND, -1);
        Date endDate = cal.getTime();
        
        int i = 0 ;
        try {
        	//取得折價券產生趴數
        	int couponRate = Integer.parseInt(ThesysParamDAO.getInstance().getParam(getSiteId(), COUPON_RATE).getParamVal());
        	
        	conn = ThesysLaphoneDAO.getConnection(); //八天前取貨的
            String sql = "SELECT ORD_ID,MEM_ID,CP_CNT_AMT FROM LAPHONE_ORD_MAIN WHERE SITE_ID=? AND ORD_ST=? AND CP_CNT_AMT>0 "+
            			 " AND CONVERT(varchar(10) , REC_DATE, 111 ) = CONVERT(varchar(10) , DATEADD(day,-8,getDate()), 111 ) "+
            			 " AND ORD_ID NOT IN(SELECT SRC_ORD_ID from LAPHONE_COUPON ) ";
            stmt = conn.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, getSiteId());
            stmt.setInt(idx++, ThesysOrderVO.ORDER_STATUS_RECEIVED);
            rs = stmt.executeQuery();
            
            sql ="INSERT INTO LAPHONE_COUPON(SITE_ID,CP_CODE,MEM_ID,CP_AMT,CP_RATE,ST_DATE,END_DATE," +
					"SRC_ORD_ID,CP_DATE) VALUES(?,?,?,?,?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);
            while (rs.next()) {
            	//計算Coupon
            	int couponAmount = 0;
            	int countAmount =rs.getInt("CP_CNT_AMT");
        		String srcOrderId = rs.getString("ORD_ID");
        		String memberId = rs.getString("MEM_ID");
        		if(countAmount != 0){
        			couponAmount = countAmount*couponRate/100;
        			String couponCode = ThesysSerialHandler.getNextSerialNo(getSiteId(), "COUPON");
        		
	        		idx = 1;
	        		stmt.clearParameters();
	    			stmt.setString(idx++,getSiteId());
	    			stmt.setString(idx++,couponCode);
	    			stmt.setString(idx++,memberId);
	    			stmt.setInt(idx++,couponAmount);
	    			stmt.setInt(idx++,discountRate);
	    			stmt.setTimestamp(idx++, convert(startDate));
	    			stmt.setTimestamp(idx++, convert(endDate));
	    			stmt.setString(idx++,srcOrderId);
	    			stmt.setTimestamp(idx++, convert(new Date()));
	    			stmt.addBatch();
	    			i++;
        		}
        		
            }
            stmt.executeBatch();
        } catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
			ex.printStackTrace();
		}finally{
			ThesysLaphoneDAO.closeAll(conn, stmt, rs);
		}
        return i ;
	}
	
	//util to sql date
    private java.sql.Timestamp convert(java.util.Date date){
    	java.sql.Timestamp result = null;
    	if(date!=null){
    		result = new java.sql.Timestamp(date.getTime());    		
    	}
    	return result;
    }
	
//	//取得只有日期的Date
//	private Date  getDate(Date date) {
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(date);
//		cal.set(Calendar.MILLISECOND, 0);
//		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
//				cal.get(Calendar.DAY_OF_MONTH),0,0,0);
//		return cal.getTime();
//	}
//	
//	// 加天數
//	private java.util.Date addDate(java.util.Date now, int days) {
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(now);
//		cal.add(Calendar.DATE, days);
//		return cal.getTime();
//	}
	
		
}
