package com.thesys.opencms.laphone.util;
/**
 * 發送簡訊
 */
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext; 

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.json.JSONObject;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsLog;
import org.opencms.util.CmsMacroResolver;
import org.opencms.xml.content.CmsXmlContent;
import org.opencms.xml.content.CmsXmlContentFactory;

import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;


public class ThesysSMSHandler {

	protected static final Log LOG = CmsLog.getLog(ThesysSMSHandler.class);
	public static String PARAM_SERVER_URL ="SMS_SERVER_URL";
	public static String PARAM_SRCADDRESS = "SMS_SRCADDRESS";
	public static String PARAM_SYSID ="SMS_SYSID";
	
	CmsMacroResolver macroResolver = CmsMacroResolver.newInstance();
	private String configBody;
	private String configSubject;
	private List<String> moblieToList = new ArrayList<String>();
	private CmsObject cmsObject;
	
	public ThesysSMSHandler(CmsObject cmso){
		setCmsObject(cmso);
	}
	
	public ThesysSMSHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		CmsJspActionElement cms = new CmsJspActionElement(context, req, res);
		setCmsObject(cms.getCmsObject());
	}
	public void setSMSConfiguration(String path) throws Exception{		
		CmsFile file = getCmsObject().readFile(path);
		CmsXmlContent xml =  CmsXmlContentFactory.unmarshal(getCmsObject(),file);

		configSubject = xml.getStringValue(getCmsObject(),"/Subject", getCmsObject().getRequestContext().getLocale());
		configBody = xml.getStringValue(getCmsObject(),"/Body", getCmsObject().getRequestContext().getLocale());
	}
		
	public void addMacro(String key,String value){
		macroResolver.addMacro(key,value);		
	}
	public String getSMSBody(){
		return macroResolver.resolveMacros(configBody);
	}
	public String getSMSSubject(){
		return macroResolver.resolveMacros(configSubject);
	}
	public void addMoblieTo(String mobile){
		moblieToList.add(mobile);
	}
	
	public void sendSMS() throws Exception{
		sendSMS(getSMSBody(),moblieToList);
  	}
	
	public int sendSMS(String msg , List<String> mobileList ){
		
		String siteId = getCmsObject().getRequestContext().getSiteRoot();
		
		return sendSMS(siteId,msg,mobileList) ;
	}
	
	public static int sendSMS(String siteId,String msg , List<String> mobileList ){
		int res = 1;
		String XmlData, XmlData1,XmlData2 = "",XmlData3, encodeString, line="";
		int responseCode;
		String text = "";
		URL url; 
		OutputStream outStream;
		InputStream inStream;
		BufferedReader reader;
		
		try {
			String serverURL = ThesysParamDAO.getInstance().getParam(siteId, PARAM_SERVER_URL).getParamVal();
			String sysId = ThesysParamDAO.getInstance().getParam(siteId, PARAM_SYSID).getParamVal();
			String srcAddress = ThesysParamDAO.getInstance().getParam(siteId, PARAM_SRCADDRESS).getParamVal();
			text = new String(Base64.encodeBase64(msg.getBytes("UTF-8")));
			XmlData1= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
					+ "<SmsSubmitReq>" + "<SysId>"+sysId+"</SysId>"
					+ "<SrcAddress>"+srcAddress+"</SrcAddress>";
			Iterator<String> it = mobileList.iterator();
	        while(it.hasNext()){
	        	String mobile = it.next();
	        	XmlData2 += "<DestAddress>"+mobile+"</DestAddress>";
	        	LOG.info("發送SMS-手機號碼："+mobile+"\r\n");
	        }
			XmlData3= "<SmsBody>"+ text + "</SmsBody>" +
					"<DrFlag>true</DrFlag>" + "</SmsSubmitReq>";
			XmlData = XmlData1+XmlData2+XmlData3;
			encodeString = "xml=" + URLEncoder.encode(XmlData, "UTF-8");
			url = new URL(serverURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);
			conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			outStream = conn.getOutputStream();
			outStream.write(encodeString.getBytes("UTF-8"));
			conn.connect();
			responseCode = conn.getResponseCode();
			inStream = conn.getInputStream();
			reader = new BufferedReader(new InputStreamReader(inStream));
			line = null;
			while ((line = reader.readLine()) != null) {
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			res = 0 ;
		}
		return res ;
	}
	
	public void addMacroList(JSONObject jsonObj)throws Exception{
		Iterator<String> it = jsonObj.keys();
		while(it.hasNext()){
			String key = it.next();
			addMacro(key,jsonObj.getString(key));
		}
	}

	public void addMacroList(String jsonText)throws Exception{
		JSONObject jsonObj = new JSONObject(jsonText);
		Iterator<String> it = jsonObj.keys();
		while(it.hasNext()){
			String key = it.next();
			addMacro(key,jsonObj.getString(key));
		}
	}
	/**
	 * @return the macroResolver
	 */
	public CmsMacroResolver getMacroResolver() {
		return macroResolver;
	}
	/**
	 * @param macroResolver the macroResolver to set
	 */
	public void setMacroResolver(CmsMacroResolver macroResolver) {
		this.macroResolver = macroResolver;
	}
	/**
	 * @return the configBody
	 */
	public String getConfigBody() {
		return configBody;
	}
	/**
	 * @param configBody the configBody to set
	 */
	public void setConfigBody(String configBody) {
		this.configBody = configBody;
	}
	/**
	 * @return the configSubject
	 */
	public String getConfigSubject() {
		return configSubject;
	}
	/**
	 * @param configSubject the configSubject to set
	 */
	public void setConfigSubject(String configSubject) {
		this.configSubject = configSubject;
	}
	/**
	 * @return the configFromName
	 */
	
	/**
	 * @return the mailToList
	 */
	public List<String> getMoblieToList() {
		return moblieToList;
	}
	/**
	 * @param mailToList the mailToList to set
	 */
	public void setMoblieToList(List<String> moblieToList) {
		this.moblieToList = moblieToList;
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
