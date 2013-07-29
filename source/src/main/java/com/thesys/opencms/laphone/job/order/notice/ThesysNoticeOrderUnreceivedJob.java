package com.thesys.opencms.laphone.job.order.notice;
/**
 * M017-超商未取貨退貨列表(提供營支派車取貨用)
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import org.opencms.file.CmsObject;
import org.opencms.json.JSONObject;
import com.thesys.opencms.laphone.ThesysLaphoneDAO;
import com.thesys.opencms.laphone.job.ThesysAbstractJob;
import com.thesys.opencms.laphone.order.dao.ThesysOrderVO;
import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;
import com.thesys.opencms.laphone.util.ThesysSendMsgHandler;

public class ThesysNoticeOrderUnreceivedJob extends ThesysAbstractJob {

	@Override
	public String launch(CmsObject cmso, Map parameters) throws Exception {
		setCmsObject(cmso);
		doSendMail();
		return null;
	}
	
	public void doSendMail() throws Exception{
		Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
        	conn = ThesysLaphoneDAO.getConnection();
        	//驗退日為系統日前一日
        	String sql = "SELECT * FROM LAPHONE_ORD_MAIN WHERE ORD_ST=? AND SHIP_TYPE IN('F','K','L') AND CONVERT(VARCHAR(8), DATEADD(day,-1,GETDATE()), 112) = CONVERT(VARCHAR(8), REC_DATE, 112)";
        	stmt = conn.prepareStatement(sql);
        	stmt.setInt(1, ThesysOrderVO.ORDER_STATUS_UNRECEIVED);
        	rs = stmt.executeQuery();
        	ThesysOrderVO orderVO = new ThesysOrderVO();
        	StringBuffer emailContent = new StringBuffer();
        	emailContent.append("<table border=\"1\" style=\"border-collapse:collapse;\"><tr><td align=\"center\">會員帳號</td><td align=\"center\">EC訂單編號</td><td align=\"center\">訂購時間</td><td align=\"center\">付款方式</td><td align=\"center\">訂單金額</td></tr>");
        	while (rs.next()) {
        		String memId = rs.getString("MEM_ID");//會員編號
        		String ordId = rs.getString("ORD_ID");//訂單編號
        		Timestamp ordDate = rs.getTimestamp("ORD_DATE");//訂購日期
        		int ordAmt = rs.getInt("ORD_AMT");//訂單總金額
        		int payType = rs.getInt("PAY_TYPE");

        		//付款方式
        		orderVO.setPayType(payType);
        		String payTypeName = orderVO.getPayTypeName();
        		
        		emailContent.append("<tr>");
        		emailContent.append("<td>"+memId+"</td>");
        		emailContent.append("<td>"+ordId+"</td>");
        		emailContent.append("<td>"+sdf.format(ordDate)+"</td>");
        		emailContent.append("<td>"+payTypeName+"</td>");
        		emailContent.append("<td align=\"right\">"+ordAmt+"</td>");
        		emailContent.append("</tr>");
        	}
        	emailContent.append("</table>");
        	//寄email
        	ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
        	String opEmail = ThesysParamDAO.getInstance().getParam(getSiteId(), "OP_EMAIL").getParamVal();//營運支援部email
        	JSONObject jsonObj = new JSONObject();
        	jsonObj.put("title", "超商未取貨退貨列表(提供營支派車取貨用)");
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
