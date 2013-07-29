package com.thesys.opencms.laphone.job.order.notice;
/**
 * M015、M018-取消訂單列表(未拋轉SAP，未核單，已拋轉SAP，已核單，出貨中)，營支處理退款用(已測試)
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

public class ThesysNoticeOrderCancelJob extends ThesysAbstractJob {
	protected static final Log LOG = CmsLog.getLog(ThesysNoticeOrderCancelJob.class);
	
	@Override
	public String launch(CmsObject cmso, Map parameters) throws Exception {
		setCmsObject(cmso);
		doSendMail();
		return null;
	}
	
	public void doSendMail(){
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
        	conn = ThesysLaphoneDAO.getConnection();
        	String sql = " SELECT * FROM LAPHONE_ORD_MAIN WHERE ORD_ST=? "+
        				 " AND ( "+
        				 " 			( CANCELED_DATE IS NULL AND CONVERT(VARCHAR(8), DATEADD(day,-1,GETDATE()), 112) = CONVERT(VARCHAR(8), CANCEL_DATE, 112) ) "+ //直接取消
        				 " OR 		( CONVERT(VARCHAR(8), DATEADD(day,-1,GETDATE()), 112) = CONVERT(VARCHAR(8), CANCELED_DATE, 112) ) "+ //審核取消
        				 " ) ";
        	stmt = conn.prepareStatement(sql);
        	stmt.setInt(1, ThesysOrderVO.ORDER_STATUS_CANCELED);//取消訂單狀態
        	rs = stmt.executeQuery();
        	ThesysOrderVO orderVO = new ThesysOrderVO();
        	StringBuffer emailContent = new StringBuffer();
        	emailContent.append("<table border=\"1\" style=\"border-collapse:collapse;\"><tr><td align=\"center\">會員帳號</td><td align=\"center\">會員姓名</td><td align=\"center\">白天連絡電話</td><td align=\"center\">EC訂單編號</td><td align=\"center\">訂購時間</td><td align=\"center\">付款方式</td><td align=\"center\">取貨方式</td><td align=\"center\">訂單金額</td><td align=\"center\">取消訂單原因</td><td align=\"center\">訂單原狀態</td></tr>");
        	while (rs.next()) {
        		String memId = rs.getString("MEM_ID");//會員編號
        		String memName = rs.getString("MEM_NAME");//會員姓名
        		String cancelPhone = rs.getString("CANCEL_PHONE");//取消訂單聯絡電話
        		String memMobile = rs.getString("MEM_MOBILE");//會員電話
        		String ordId = rs.getString("ORD_ID");//訂單編號
        		Timestamp ordDate = rs.getTimestamp("ORD_DATE");//訂購日期
        		int payType = rs.getInt("PAY_TYPE");
        		String shipType = rs.getString("SHIP_TYPE");
        		int ordAmt = rs.getInt("ORD_AMT");//訂單總金額
        		java.sql.Date checkedDate = rs.getDate("CHECKED_DATE"); //審核日期
        		java.sql.Date postDate = rs.getDate("POST_DATE"); //拋轉到SAP日期
        		java.sql.Date sapPostDate = rs.getDate("SAP_POSTING_DATE"); //SAP銷貨完成過帳日期
        		java.sql.Date cvsArrDate = rs.getDate("CVS_ARR_DATE"); //(超商)到店日期
        		
        		String cancelReason = rs.getString("CANCEL_REASON");//取消原因說明

        		//白天連絡電話
        		String phone = (cancelPhone!=null && !"".equals(cancelPhone))?cancelPhone:memMobile;
        		//付款方式
        		orderVO.setPayType(payType);
        		String payTypeName = orderVO.getPayTypeName();
        		//取貨方式
        		orderVO.setShipType(shipType);
        		String shipTypeName = orderVO.getShipTypeName();
        		
        		String status = null;
        		if(checkedDate ==null){ //核單日期==NULL
        			status = "未核單";
        		}else if(postDate == null){ //核單日期!=NULL 且 拋轉到SAP日期==NULL
        			status = "已核單";
        		}else if(postDate != null && sapPostDate==null && cvsArrDate==null){ //拋轉到SAP日期!=NULL、 SAP銷貨完成過帳日期==NULL 、 (超商)到店日期==NULL
        			status = "已拋轉SAP"; 
        		}else if(sapPostDate!=null || cvsArrDate!=null){ //SAP銷貨完成過帳日期!=NULL或 (超商)到店日期!=NULL
        			status = "出貨中"; //出貨中
        		}
        		
        		emailContent.append("<tr>");
        		emailContent.append("<td>"+memId+"</td>");
        		emailContent.append("<td>"+memName+"</td>");
        		emailContent.append("<td>"+phone+"</td>");
        		emailContent.append("<td>"+ordId+"</td>");
        		emailContent.append("<td>"+sdf.format(ordDate)+"</td>");
        		emailContent.append("<td>"+payTypeName+"</td>");
        		emailContent.append("<td>"+shipTypeName+"</td>");
        		emailContent.append("<td align=\"right\">"+ordAmt+"</td>");
        		emailContent.append("<td>"+cancelReason+"</td>");
        		emailContent.append("<td>"+status+"</td>");
        		emailContent.append("</tr>");
        	}
        	emailContent.append("</table>");
			ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
        	String opEmail = ThesysParamDAO.getInstance().getParam(getSiteId(), "OP_EMAIL").getParamVal();//營運支援部email
        	JSONObject jsonObj = new JSONObject();
        	jsonObj.put("title", "取消訂單列表(未拋轉SAP，未核單，已拋轉SAP，已核單，出貨中)，營支處理退款用");
			jsonObj.put("content", emailContent.toString());
        	msgHandler.setMsgName("orderDataList");
        	msgHandler.setJsonObj(jsonObj);
			msgHandler.setEmail(opEmail); 
			msgHandler.sendMsg();
        } catch (Exception ex) {
        	LOG.error(ex, ex.fillInStackTrace());
			ex.printStackTrace();
		}finally{
			ThesysLaphoneDAO.closeAll(conn, stmt, rs);
		}
		
	}
}
