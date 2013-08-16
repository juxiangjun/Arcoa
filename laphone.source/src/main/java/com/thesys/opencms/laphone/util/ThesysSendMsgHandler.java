package com.thesys.opencms.laphone.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.json.JSONObject;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.member.ThesysMemberHandler;
import com.thesys.opencms.laphone.msg.ThesysSystemMsgHandler;
import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;
import com.thesys.opencms.mail.ThesysMailHandler;

public class ThesysSendMsgHandler {

	protected static final Log LOG = CmsLog.getLog(ThesysMemberHandler.class);
	
	private String mailMsgPath ="/_config_/email/";
	private String smsMsgPath = "/_config_/sms/";
	
	//這些參數判斷需要發送哪些訊息，如果不為null及為發送
	private String memberId;
	private String cellphone ;
	private String email;
	
	//以下為mail 及簡訊用
	private final String PARAM_CS_NAME = "CS_NAME"; //系統參數名
	private final String PARAM_CS_EMAIL = "CS_EMAIL" ; //系統參數名
	private String csName;
	private String csMail;
	private String msgName; //簡訊與mail的主檔案名
	private JSONObject jsonObj;
	
	//以下為系統訊息用，如果都為null則跟mail一樣
	private String subject;   
	private String msg;
	
	
	private CmsObject cmsObject;
	public ThesysSendMsgHandler(){}
	
	public ThesysSendMsgHandler(PageContext context,HttpServletRequest req,HttpServletResponse res){
		CmsJspActionElement cms = new CmsJspActionElement(context, req, res);
		setCmsObject(cms.getCmsObject());
	}
	public ThesysSendMsgHandler(CmsObject cmso){
		setCmsObject(cmso);
	}
	public void sendMsg() {
		try {
			if (email != null)sendMail();
			if (cellphone != null)sendSMS();
			if (memberId != null)sendSysMsg();
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
	}
	
	/**
	 * 發送email
	 * @throws Exception
	 */
	public void sendMail() throws Exception{
		if(csMail == null)getParamCSMail();//如果沒有設定發送的mail，則將系統參數中的客服mail帶入
		
		ThesysMailHandler handler = new ThesysMailHandler(getCmsObject());
		handler.setMailConfiguration(mailMsgPath+msgName+".html");
		handler.setConfigFromName(csName);
		handler.setConfigFromMail(csMail);
		handler.addMacroList(jsonObj);
		if(email.indexOf(";")>=0){
			String[] emails = email.split(";");
			for(int i=0;i<emails.length;i++){
				handler.addMailTo(emails[i]);
			}
		}else{
			handler.addMailTo(email);
		}
		handler.sendHtmlMail();
		//如果沒有字名設定subject與msg ，系統訊息內容與MAIL訊息內容相同
		if(memberId != null && subject ==null && msg == null){	 
			subject = handler.getMailSubject();
			msg =handler.getMailBody();
		}
	}
	
	/**
	 * 發送簡訊
	 * @throws Exception
	 */
	public void sendSMS() throws Exception{
		ThesysSMSHandler handler = new ThesysSMSHandler(getCmsObject());
		handler.setSMSConfiguration(smsMsgPath+msgName+".html");
		handler.addMacroList(jsonObj);
		handler.addMoblieTo(cellphone);
		handler.sendSMS();
	}
	
	/**
	 * 發送系統訊息
	 * @throws Exception
	 */
	public void sendSysMsg() throws Exception {
		String siteId = getCmsObject().getRequestContext().getSiteRoot();
		ThesysSystemMsgHandler.add(siteId,memberId,1,0,subject,msg,null);
	}
	
	/**
	 * 將系統參數中的客服mail帶入
	 * @throws Exception
	 */
	public void getParamCSMail() throws Exception{
		String siteId = getCmsObject().getRequestContext().getSiteRoot();
		this.csName =  ThesysParamDAO.getInstance().getParam(siteId, PARAM_CS_NAME).getParamVal();
		this.csMail = ThesysParamDAO.getInstance().getParam(siteId, PARAM_CS_EMAIL).getParamVal();
	}
	
	public JSONObject getJsonObj() {
		return jsonObj;
	}

	public void setJsonObj(JSONObject jsonObj) {
		this.jsonObj = jsonObj;
	}

	public String getCellphone() {
		return cellphone;
	}
	
	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getMsgName() {
		return msgName;
	}
	
	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}
	

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setCsName(String csName) {
		this.csName = csName;
	}

	public void setCsMail(String csMail) {
		this.csMail = csMail;
	}

	/**
	 * @return the cmsObject
	 */
	public CmsObject getCmsObject() {
		return cmsObject;
	}

	/**
	 * @param cmsObject the cmsObject to set
	 */
	public void setCmsObject(CmsObject cmsObject) {
		this.cmsObject = cmsObject;
	}
}
