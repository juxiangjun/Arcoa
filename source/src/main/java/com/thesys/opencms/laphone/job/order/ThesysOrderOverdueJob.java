package com.thesys.opencms.laphone.job.order;
/**
 * 取消超過四天未付款之atm付款訂單及前一天未付款完成之信用卡訂單 (已測過) 
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.json.JSONObject;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneDAO;
import com.thesys.opencms.laphone.job.ThesysAbstractJob;
import com.thesys.opencms.laphone.order.dao.ThesysOrderVO;
import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;
import com.thesys.opencms.laphone.util.ThesysSendMsgHandler;

public class ThesysOrderOverdueJob  extends ThesysAbstractJob{
		
	protected static final Log LOG = CmsLog.getLog(ThesysOrderOverdueJob.class);
	
	/* (non-Javadoc)
	 * @see org.opencms.scheduler.I_CmsScheduledJob#launch(org.opencms.file.CmsObject, java.util.Map)
	 */
	@Override
	public String launch(CmsObject cmso, Map parameters) throws Exception {
		setCmsObject(cmso);	
		doCancel();
        
		return null;
	}
	/**
	 * 主程式
	 * @throws Exception
	 */
	public void doCancel() throws Exception{
		int i = cancelAtmOrder();
		System.out.println("共取消"+i+"筆訂單");
	}
	
	/**
	 * 取出符合資格的訂單
	 * @return
	 */
	public int cancelAtmOrder(){
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
        int i = 0 ;
        try {
        	String host = ThesysParamDAO.getInstance().getParam(getSiteId(),"WEB_URL").getParamVal();
        	//取得昨日(含)到期未付款之ATM訂單
        	
        	conn = ThesysLaphoneDAO.getConnection();
            String sql = " SELECT ORD_ID,MEM_ID,MEM_EMAIL,CONVERT(char(10), ORD_DATE, 111),PAY_TYPE FROM LAPHONE_ORD_MAIN "+
            			 " where SITE_ID = ? AND ( "+
            			 " ( ORD_ST = ? and PAY_TYPE=?  AND CONVERT(char(10), ORD_DATE, 111) <  CONVERT(char(10), DATEADD(day,-4,getDate()), 111) ) "+ //訂單日期小於系統日期4天
            			 " OR (ORD_ST = ? and (PAY_TYPE=? OR PAY_TYPE=? OR PAY_TYPE=?) AND CONVERT(char(10), ORD_DATE, 111) <=  CONVERT(char(10), getDate(), 111)) "+ //訂單日期小於系統日期
            			 " ) ORDER BY ORD_ST"; 
            stmt = conn.prepareStatement(sql);
            int idx = 1;
            stmt.setString(idx++, getSiteId());
            stmt.setInt(idx++, ThesysOrderVO.ORDER_STATUS_ATM);
            stmt.setInt(idx++, ThesysOrderVO.PAY_TYPE_ATM);
            stmt.setInt(idx++, ThesysOrderVO.ORDER_STATUS_CREDIT);
            stmt.setInt(idx++, ThesysOrderVO.PAY_TYPE_CREDIT);
            stmt.setInt(idx++, ThesysOrderVO.PAY_TYPE_INSTALLMENT);
            stmt.setInt(idx++, ThesysOrderVO.PAY_TYPE_CHINATRUST);
            rs = stmt.executeQuery();
            
            sql ="UPDATE LAPHONE_ORD_MAIN SET ORD_ST = ?,ORD_MSG = ?,FAILURE_DATE = ? " +
					" WHERE SITE_ID = ? AND ORD_ID = ?";
            stmt = conn.prepareStatement(sql);
            while (rs.next()) {            	
        		String orderId = rs.getString("ORD_ID");
        		String memberId = rs.getString("MEM_ID");
        		String email = rs.getString("MEM_EMAIL");
        		int payType =rs.getInt("PAY_TYPE");
        		idx = 1;
        		stmt.clearParameters();
    			stmt.setInt(idx++,ThesysOrderVO.ORDER_STATUS_FAILURE);
    			stmt.setString(idx++,(payType==ThesysOrderVO.PAY_TYPE_ATM)?"ATM逾期未付款":"信用卡未取得授權");
                stmt.setTimestamp(idx++, new java.sql.Timestamp(new java.util.Date().getTime()));
    			stmt.setString(idx++,getSiteId());
    			stmt.setString(idx++,orderId);
    			int result = stmt.executeUpdate();
    			if(result==1){
    				//發送訂單失敗信件	    				
    				JSONObject jsonObj = new JSONObject();
    				jsonObj.put("orderId", orderId);
    				jsonObj.put("host",host);
    				msgHandler.setMsgName("orderFailure");
    				msgHandler.setJsonObj(jsonObj);		
    		 		msgHandler.setMemberId(memberId);
    				msgHandler.setEmail(email); 
    				msgHandler.sendMsg(); 
    			}
    			i++;
        		
            }
        } catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
			ex.printStackTrace();
		}finally{
			ThesysLaphoneDAO.closeAll(conn, stmt, rs);
		}
        return i ;
	}
	
		
}
