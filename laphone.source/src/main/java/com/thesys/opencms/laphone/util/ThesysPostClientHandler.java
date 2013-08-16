package com.thesys.opencms.laphone.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.codec.binary.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;

public class ThesysPostClientHandler {
	
	private String siteId;
	private String serverUrl = "";
	private String sysCode = "";
	private String appCode = "";
	
	public ThesysPostClientHandler(String siteId){
		setSiteId(siteId);
		try {
			serverUrl = ThesysParamDAO.getInstance().getParam(getSiteId(), "ENCRYPTION_URL").getParamVal();
			if(!serverUrl.endsWith("/")) serverUrl += "/";
			sysCode = ThesysParamDAO.getInstance().getParam(getSiteId(), "ENCRYPTION_SYSCODE").getParamVal();
			appCode = ThesysParamDAO.getInstance().getParam(getSiteId(), "ENCRYPTION_APPCODE").getParamVal();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		String testTxt1 = "A125218812";
		String testTxt2 = "A123456789";
		try {
			ThesysPostClientHandler handler = new ThesysPostClientHandler("/sites/laphone");
			
			for(int i = 0 ;i<100;i++)System.out.println(handler.encode(testTxt1));
			
			
			String postDecodeVal1 = handler.encode(testTxt1);
			String postDecodeVal2 = handler.encode(testTxt1);
			System.out.println("postDecodeVal1:"+postDecodeVal1);
			System.out.println("postDecodeVal2:"+postDecodeVal2);
			System.out.println(postDecodeVal1.equals(postDecodeVal2));
			
			
			String postDecodeVal3 = handler.encode(testTxt2);
			String postDecodeVal4 = handler.encode(testTxt2);
			System.out.println("postDecodeVal3:"+postDecodeVal3);
			System.out.println("postDecodeVal4:"+postDecodeVal4);
			System.out.println(postDecodeVal3.equals(postDecodeVal4));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * post 加密
	 * @param txt
	 * @return
	 * @throws Exception
	 */
	public String encode(String txt) throws Exception {
		String signedData = null;
		if(txt != null){
			String url = getServerUrl()+"service/slotControl/SlotAppMapSigned.do";
			String param = "sysCode="+getSysCode() +
						   "&appCode="+getAppCode() +
						   "&kind=Text" +
						   "&content="+URLEncoder.encode(txt,"UTF-8")+//先將中文編碼
						   "&mode=SEnvelope" +
						   "&encodeMethod=BASE64" +
						   "&method=signedViaAP";
			String xmlTxt = excutePost(url, param);
			if(xmlTxt != null){
				signedData = getXmlTagVal(xmlTxt,"signedData");
				signedData = URLEncoder.encode(signedData,"UTF-8");
			}
		}
		return signedData;
	}
	
	/**
	 * 使用POST解密
	 * @param signedData
	 * @return
	 * @throws Exception 
	 */
	public String decode(String signedData) throws Exception {
		String b64decode = null;
		if(signedData != null){
			String url = getServerUrl()+"service/slotControl/SlotAppMapVerify.do";
			String param = "sysCode="+getSysCode() +
					   	   "&appCode="+getAppCode() +
						   "&kind=Text" +
						   "&signedData="+signedData+
						   "&mode=SEnvelope" +
						   "&decodeMethod=BASE64" +
						   "&method=verifyViaAP";
			String xmlTxt = excutePost(url, param);
			if(xmlTxt != null){
				String decodeData = getXmlTagVal(xmlTxt,"content"); 
				Base64 base64 = new Base64();
				b64decode = new String(base64.decode(decodeData.getBytes()));
			}
		}
		return b64decode;
	}
	
	
	/**
	 * 使用POST批次解密
	 * @param signedData
	 * @return
	 * @throws Exception 
	 */
	public String batchDecode(String signedData) throws Exception {
		String b64decode = null;
		if(signedData != null){
			String url = getServerUrl()+"service/xmlService/XMLVerify.do";
			String param = "sysCode="+getSysCode() +
						   "&appCode="+getAppCode() +
						   "&kind=Text" +
						   "&signedData="+signedData+
						   "&mode=SEnvelope" +
						   "&decodeMethod=BASE64" +
						   "&method=verifyViaAP";
			String xmlTxt = excutePost(url, param);
			if(xmlTxt != null){
				String decodeData = getXmlTagVal(xmlTxt,"content"); 
				Base64 base64 = new Base64();
				b64decode = new String(base64.decode(decodeData.getBytes()));
			}
		}
		return b64decode;
	}
	
	/**
	 * 取XML中特定tag的值
	 * @param xmlTxt
	 * @param tagName
	 * @return
	 * @throws Exception
	 */
	public String getXmlTagVal(String xmlTxt,String tagName) throws Exception{
		String tagValue = null;
		if(xmlTxt != null && tagName != null){
			ByteArrayInputStream  bis = new ByteArrayInputStream(xmlTxt.getBytes());
			InputSource  is = new InputSource (bis);
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder=factory.newDocumentBuilder();
			Document doc = builder.parse(is);
			NodeList nl = doc.getElementsByTagName(tagName);
			Element e = (Element) nl.item(0);
			tagValue = e.getTextContent();
		}
		return tagValue;
	}
	
	/**
	 * 使用Post的方法將參數傳至targetURL
	 * @param targetURL
	 * @param urlParameters
	 * @return
	 */
	public String excutePost(String targetURL, String urlParameters)
	  {
	    URL url;
	    HttpURLConnection connection = null;  
	    try {
	      //Create connection
	      url = new URL(targetURL);
	      connection = (HttpURLConnection)url.openConnection();
	      connection.setRequestMethod("POST");
	      connection.setRequestProperty("enctype","multipart/form-data");
//	      connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	     
	      connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
	      connection.setRequestProperty("Content-Language", "zh-tw");
	      
	      connection.setUseCaches (false);
	      connection.setDoInput(true);
	      connection.setDoOutput(true);

	      //Send request
	      DataOutputStream wr = new DataOutputStream (connection.getOutputStream ());
	      wr.writeBytes (urlParameters);
	      wr.flush ();
	      wr.close ();

	      //Get Response	
	      InputStream is = connection.getInputStream();
	      BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	      String line;
	      StringBuffer response = new StringBuffer(); 
	      while((line = rd.readLine()) != null) {
	        response.append(line);
	        response.append('\r');
	      }
	      rd.close();
	      return response.toString();

	    } catch (Exception e) {

	      e.printStackTrace();
	      return null;

	    } finally {

	      if(connection != null) {
	        connection.disconnect(); 
	      }
	    }
	  }
	/**
	 * @return the siteId
	 */
	public String getSiteId() {
		return siteId;
	}
	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	/**
	 * @return the serverUrl
	 */
	public String getServerUrl() {
		return serverUrl;
	}
	/**
	 * @param serverUrl the serverUrl to set
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}
	/**
	 * @return the sysCode
	 */
	public String getSysCode() {
		return sysCode;
	}
	/**
	 * @param sysCode the sysCode to set
	 */
	public void setSysCode(String sysCode) {
		this.sysCode = sysCode;
	}
	/**
	 * @return the appCode
	 */
	public String getAppCode() {
		return appCode;
	}
	/**
	 * @param appCode the appCode to set
	 */
	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	


}
