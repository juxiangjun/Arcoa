package com.thesys.opencms.laphone.job.order.notice;
/**
 * M019-每日通知七日前已完成之訂單列表(宅配/超商取貨)，過鑑賞期-通知寄發票(已測試)
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
public class ThesysNoticeOrderInvoiceJob extends ThesysAbstractJob {
//	private final String PARAM_CS_NAME = "CS_NAME"; //系統參數名
//	private final String PARAM_CS_EMAIL = "CS_EMAIL" ; //系統參數名
	
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
        	//已取貨且系統日期大於取貨日期七天的訂單
        	String sql = "SELECT * FROM LAPHONE_ORD_MAIN WHERE ORD_ST=? AND CONVERT(VARCHAR(8), DATEADD(day,-7,GETDATE()), 112) = CONVERT(VARCHAR(8), REC_DATE, 112)";
        	stmt = conn.prepareStatement(sql);
        	stmt.setInt(1, ThesysOrderVO.ORDER_STATUS_RECEIVED);
        	rs = stmt.executeQuery();
        	StringBuffer emailContent = new StringBuffer();
        	emailContent.append("<table border=\"1\" style=\"border-collapse:collapse;\"><tr><td align=\"center\">會員帳號</td><td align=\"center\">會員姓名</td><td align=\"center\">白天連絡電話</td><td align=\"center\">訂單編號</td><td align=\"center\">訂購時間</td><td align=\"center\">訂單金額</td></tr>");
        	
        	while (rs.next()) {
        		String memId = rs.getString("MEM_ID");//會員編號
        		String memName = rs.getString("MEM_NAME");//會員姓名
        		String memMobile = rs.getString("MEM_MOBILE");//會員電話
        		String ordId = rs.getString("ORD_ID");//訂單編號
        		Timestamp ordDate = rs.getTimestamp("ORD_DATE");//訂購日期
        		int ordAmt = rs.getInt("ORD_AMT");//訂單總金額
        		
        		emailContent.append("<tr>");
        		emailContent.append("<td>"+memId+"</td>");
        		emailContent.append("<td>"+memName+"</td>");
        		emailContent.append("<td>"+memMobile+"</td>");
        		emailContent.append("<td>"+ordId+"</td>");
        		emailContent.append("<td>"+sdf.format(ordDate)+"</td>");
        		emailContent.append("<td align=\"right\">"+ordAmt+"</td>");
        		emailContent.append("</tr>");
        	}
        	emailContent.append("</table>");
        	//寄通知寄發票列表
//        	String csName =  ThesysParamDAO.getInstance().getParam(getSiteId(), PARAM_CS_NAME).getParamVal();//系統參數名
//    		String csMail = ThesysParamDAO.getInstance().getParam(getSiteId(), PARAM_CS_EMAIL).getParamVal();//系統參數名
//        	String opEmail = ThesysParamDAO.getInstance().getParam(getSiteId(), "OP_EMAIL").getParamVal();//營運支援部email
//        	String faEmail = ThesysParamDAO.getInstance().getParam(getSiteId(), "FA_EMAIL").getParamVal();//財會部email
//    		JSONObject jsonObj = new JSONObject();
//			jsonObj.put("content", emailContent.toString());			
//			
//    		ThesysMailHandler handler = new ThesysMailHandler(getCmsObject());
//    		handler.setMailConfiguration("/_config_/email/orderDataList.html");
//
//    		handler.setConfigFromName(csName);
//    		handler.setConfigFromMail(csMail);
//    		handler.addMacroList(jsonObj);
//    		handler.addMailTo(opEmail);
//    		handler.addMailTo(faEmail);
//    		handler.sendHtmlMail();
    		ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
        	String opEmail = ThesysParamDAO.getInstance().getParam(getSiteId(), "OP_EMAIL").getParamVal();//營運支援部email
        	String faEmail = ThesysParamDAO.getInstance().getParam(getSiteId(), "FA_EMAIL").getParamVal();//財會部email
        	JSONObject jsonObj = new JSONObject();
        	jsonObj.put("title", "每日通知七日前已完成之訂單列表(宅配/超商取貨)，過鑑賞期-通知寄發票");
			jsonObj.put("content", emailContent.toString());
        	msgHandler.setMsgName("orderDataList");
        	msgHandler.setJsonObj(jsonObj);
			msgHandler.setEmail(opEmail+";"+faEmail); 
			msgHandler.sendMsg();
        } catch (Exception ex) {
        	LOG.error(ex, ex.fillInStackTrace());
			ex.printStackTrace();
		}finally{
			ThesysLaphoneDAO.closeAll(conn, stmt, rs);
		}
	}
}
