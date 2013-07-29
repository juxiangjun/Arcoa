package com.thesys.opencms.laphone.job.order;
/**
 * 匯出訂單主檔至SAP及便利達康
 */
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;



import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.json.JSONObject;
import org.opencms.main.CmsLog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import au.com.bytecode.opencsv.CSVWriter;

import com.thesys.opencms.laphone.ThesysLaphoneDAO;
import com.thesys.opencms.laphone.job.ThesysAbstractJob;
import com.thesys.opencms.laphone.job.ftp.ThesysSapFtpClient;
import com.thesys.opencms.laphone.order.dao.ThesysOrderVO;

public class ThesysOrderExportJob extends ThesysAbstractJob{

	protected static final Log LOG = CmsLog.getLog(ThesysOrderExportJob.class);
	private Date now = new Date();
		
	private final String XML_ROOT_TAG = "ORDER_DOC"; // root tag
	private final String XML_CONTENT_TAG = "ORDER"; //有訂單資料的tag
	private final String XML_ECNO = "ECNO"; //EC 網站代號
	private final String XML_ODNO = "ODNO"; //EC 訂單號碼
	private final String XML_STNO = "STNO"; //取貨門市編號
	private final String XML_AMT = "AMT"; //代收金額
	private final String XML_CUTKNM = "CUTKNM"; //取貨人中文姓名<![CDATA[王小明]]>
	private final String XML_CUTKTL = "CUTKTL"; //取貨人電話因個資法上線，請帶空
	private final String XML_PRODNM = "PRODNM"; //商品別代碼
	private final String XML_ECWEB = "ECWEB"; //EC 網站名稱<![CDATA[達康購物網]]>
	private final String XML_ECSERTEL = "ECSERTEL"; //EC 網站客服電話
	private final String XML_REALAMT = "REALAMT"; //商品實際金額
	private final String XML_TRADETYPE = "TRADETYPE"; //交易方式識別碼
	private final String XML_SERCODE = "SERCODE"; //代收代號
	private final String XML_EDCNO = "EDCNO"; //大物流代號
	private final String XML_ORDERCOUNT_TAG = "ORDERCOUNT"; //共幾筆的TAG
	private final String XML_TOTALS ="TOTALS" ;//訂單總筆數
	private final String[] columnNames = {XML_ODNO,XML_STNO,XML_CUTKNM,XML_CUTKTL,XML_REALAMT,XML_TRADETYPE};
	
	private final int ECNO = 195 ;//EC 網站代號
	private int PRODNM = 0;//商品別代碼0 : 一般商品 1 : 票券商品
	private String ECWEB = "www.laphonetaiwan.com";//EC 網站名稱
	private String ECSERTEL = "0800-032-123" ;//EC 網站客服電話
	private int SERCODE = 963; //代收代號 963 : 一般 EC 廠商
	private String EDCNO="D05";//大物流代號
	
	
	
	

	/* (non-Javadoc)
	 * @see org.opencms.scheduler.I_CmsScheduledJob#launch(org.opencms.file.CmsObject, java.util.Map)
	 */
	@Override
	public String launch(CmsObject cmso, Map parameters) throws Exception {
		setCmsObject(cmso);		
		doPost();
        
		return null;
	}
	public static void main(String[] args){
		ThesysOrderExportJob job = new ThesysOrderExportJob();
		job.doPost();
	}
	public void doPost(){
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			conn = ThesysLaphoneDAO.getConnection();
			//Step 1：將已審核的訂單(OrderStatus=2)改成已拋轉(OrderStatus=3)
			updateOrderStatusToPosted(conn);
			//Step 2: 產生Sap訂單檔CSV
			InputStream in = generateSapOrderCsv(conn);
			//Step 3: 上傳至ftp server	
			ThesysSapFtpClient client = getSapFtpClient();
			String fileName = client.getFileName(ThesysSapFtpClient.FILE_TYPE_SAP_ORDER);
			String inboxFolder = client.getInboxFolder(ThesysSapFtpClient.FILE_TYPE_SAP_ORDER);
			client.upload(inboxFolder+"/"+fileName, in);
			//Step 4: 關閉FTP連結
			client.disconnect();
			//Step 5: 上傳至便利達康的Web Service
			System.out.println(postToCvsWebService());
			
			
		}catch(Exception ex){
			ex.printStackTrace();			
		}finally{
			ThesysLaphoneDAO.closeAll(conn, stmt, rs);
		}
	}
	/**
	 * 更新訂單狀態為已拋轉
	 * @param con
	 * @throws SQLException
	 */
	public void updateOrderStatusToPosted(Connection conn) throws SQLException{
        PreparedStatement stmt = null;
        try {
	    	String sql = "UPDATE LAPHONE_ORD_MAIN SET ORD_ST=3,POST_DATE=? WHERE SITE_ID=? AND ORD_ST=2 ";
	        
	    	stmt = conn.prepareStatement(sql);
	    	int idx = 1;
	    	stmt.setTimestamp(idx++, new java.sql.Timestamp(now.getTime()));
	    	stmt.setString(idx++, getSiteId());
	        stmt.executeUpdate();  
        } finally {
        	ThesysLaphoneDAO.closeAll(null, stmt, null);
        }
	}
	/**
	 * 產生Sap訂單檔
	 */
	public InputStream generateSapOrderCsv(Connection conn) throws Exception{
		char EOF = (char)0x00; //char:空白
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter((OutputStream)outStream );
		CSVWriter csv = new CSVWriter(writer,'\t',EOF,"\r\n");
		PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
	    	String sql = "SELECT * FROM LAPHONE_ORD_SAP_VIEW WHERE SITE_ID=? ORDER BY ORD_ID";
            int idx = 1;
	    	stmt = conn.prepareStatement(sql);
	        stmt.setString(idx++, getSiteId());
	        rs = stmt.executeQuery(); 
	        String prevOrderId = null;
	        int itemNum = 0;
	        while(rs.next()){
	        	JSONObject row = convert(rs);
		       // System.out.println(row);
		        String tmpOrderId = row.optString("ORD_ID");
		        //計算Item流水編號；16:OrderId；17:ItemNum
		        if(!tmpOrderId.equals(prevOrderId)){ //不等於上一個OrderId時，重新編號
		        	prevOrderId = tmpOrderId;   
		        	itemNum = 0;
		        }
		        if(itemNum==0){ //第一筆的時候
		        	int couponAmount = (row.getString("CP_AMT").length()==0)?0:Integer.parseInt(row.getString("CP_AMT"));
		        	if(couponAmount<0){ //有抵用折價券
		        		JSONObject cpRow = copy(row);
		        		cpRow.put("ITEM_ID", "ZPA8"); //折價券料號
		        		cpRow.put("ORD_QTY", "1"); 
		        		cpRow.put("GRP_ID", ""); //萬一為組合案，會有殘值，所以要清除組合案料號
		        		cpRow.put("ORD_AMT", couponAmount); 
						itemNum++;
			        	cpRow.put("ITEM_NUM", String.valueOf(itemNum));
				        csv.writeNext(convertToSapCsvRow(cpRow)); //寫入檔案
		        	}
		        	int balance =  (row.getString("CP_BALANCE").length()==0)?0:Integer.parseInt(row.getString("CP_BALANCE"));
		        	if(balance>0){ //折價券有餘額
		        		JSONObject bnRow = copy(row);
		        		bnRow.put("ITEM_ID", "Z225"); //折價券餘額料號
		        		bnRow.put("ORD_QTY", "1"); 
		        		bnRow.put("GRP_ID", ""); //萬一為組合案，會有殘值，所以要清除組合案料號
		        		bnRow.put("ORD_AMT", balance); 
						itemNum++;
						bnRow.put("ITEM_NUM", String.valueOf(itemNum));
				        csv.writeNext(convertToSapCsvRow(bnRow)); //寫入檔案
		        	}
		        	int shipFee =  (row.getString("SHIP_FEE").length()==0)?0:Integer.parseInt(row.getString("SHIP_FEE"));
		        	if(shipFee>0){ //有運費
		        		JSONObject spRow = copy(row);
		        		spRow.put("ITEM_ID", "ZBEST"); //折價券餘額料號
		        		spRow.put("ORD_QTY", "1"); 
		        		spRow.put("GRP_ID", ""); //萬一為組合案，會有殘值，所以要清除組合案料號
		        		spRow.put("ORD_AMT", shipFee); 
				        row.put("CP_CODE","");//清除COUPON券號
						itemNum++;
						spRow.put("ITEM_NUM", String.valueOf(itemNum));
				        csv.writeNext(convertToSapCsvRow(spRow)); //寫入檔案
		        	}
		        }		        
		        itemNum++;
		        row.put("ITEM_NUM", String.valueOf(itemNum));
		        row.put("CP_CODE","");//清除COUPON券號
		        String[] rowData = convertToSapCsvRow(row); //將resultSet轉成串列
		        csv.writeNext(rowData);
	        }
			csv.flush();
			csv.close();
			byte[] bytes = outStream.toByteArray();
			System.out.println(new String(bytes));
			InputStream in = new ByteArrayInputStream(bytes);
			return in;
        } finally {
        	ThesysLaphoneDAO.closeAll(null, stmt, rs);
        }
	}
	/**
	 * 複製JSONObject
	 * @param jsonObj
	 * @return
	 */
	private JSONObject copy(JSONObject jsonObj) throws Exception{
		JSONObject result = new JSONObject();
		Iterator<String> it = jsonObj.keys();
		while(it.hasNext()){
			String key = it.next();
			result.put(key, jsonObj.getString(key));
		}
		return result;
	}
	/**
	 * 將ResultSet轉成JSONObject
	 * @param rs
	 * @return
	 * @throws Exception
	 */
	private String[] convertToSapCsvRow(JSONObject jsonObj) throws Exception{
		String[] columnNames = {    
				"SO", //交易類型；固定填SO
				"ORD_DATE", //下單日期
				"SHIP_TYPE",	//客戶編號；-->宅配、超商
				"CVS_ST_NO", //超商代號
				"STNM", //超商門市名稱
				"STTEL", //超商電話
				"DCRONO", //路線路順
				"PAY_TYPE", //交易方式
				"REC_NAME", //客戶名稱				
				"REC_ADDR", //送貨地址
				"COMP_ADDR", //發票地址
				"INVOICE_ADDR", //發票寄送地址
				"INVOICE_TITLE", //發票抬頭或買受人名稱
				"COMP_NO", //統一編號
				"REC_MOBILE", //客戶電話
				"CARDID", //會員卡號
				"ORD_ID", //EC購物單號
				"ITEM_NUM", //EC購物item Num
				"ITEM_ID", //物料號碼 (一般商品：物料編號、組合商品：明細商品編號)
				"ORD_QTY", //訂購數量 (一般商品：訂購數量、組合商品： 訂購數量*組案數量)
				"GRP_ID", //組合案料號
				"ORD_AMT", //金額(一般商品：(商品價格*數量)、組合商品：(組合案裡的明細商品之組合價*數量)
				"CP_CODE",//EC Coupon券代號
				"PID" //身份證後四碼
		};
		int length = columnNames.length;
		String[] result = new String[length];
		for(int i=0;i<length;i++){
			String column = columnNames[i];
			result[i] = jsonObj.getString(column);
		}
		return result;
	}
	private JSONObject convert(ResultSet rs)throws Exception{
		String[] columnNames = {    
				"ORD_DATE", //下單日期
				"SHIP_TYPE",	//客戶編號；-->宅配、超商
				"CVS_ST_NO", //超商代號
				"STNM", //超商門市名稱
				"STTEL", //超商電話
				"DCRONO", //路線路順
				"PAY_TYPE", //交易方式
				"REC_NAME", //客戶名稱				
				"REC_ADDR", //送貨地址
				"COMP_ADDR", //發票地址
				"INVOICE_ADDR", //發票寄送地址
				"INVOICE_TITLE", //發票抬頭或買受人名稱
				"COMP_NO", //統一編號
				"REC_MOBILE", //客戶電話
				"CARDID", //會員卡號
				"ORD_ID", //EC購物單號
				"ITEM_ID", //物料號碼 (一般商品：物料編號、組合商品：明細商品編號)
				"ORD_QTY", //訂購數量 (一般商品：訂購數量、組合商品： 訂購數量*組案數量)
				"GRP_ID", //組合案料號
				"ORD_AMT", //金額(一般商品：(商品價格*數量)、組合商品：(組合案裡的明細商品之組合價*數量)
				"CP_CODE",//EC Coupon券代號
				"CP_AMT",//Coupon券面額
				"CP_BALANCE",//Coupon券餘額
				"SHIP_FEE",//運費
				"PID" //身份證後四碼
		};
		int length = columnNames.length;
		String shipType = null;
		JSONObject result = new JSONObject();
		result.put("SO", "SO");
		for(int i=0;i<length;i++){
			String column = columnNames[i];
			String value = "";
			if("SHIP_TYPE".equals(column)){
				shipType = rs.getString(column);
				if("W".equals(shipType)){
					value="100014"; //(宅配)
				}else if("F".equals(shipType)){
					value="100015"; //(全家)
				}else if("K".equals(shipType)){
					value="100016"; //(OK)
				}else if("L".equals(shipType)){
					value="100017"; //(萊爾富)
				}			
			}else if("PAY_TYPE".equals(column)){
				if("W".equals(shipType)){
					value = "3";//取貨不付款
				}else{
					int payType = rs.getInt(column);
					if(payType==ThesysOrderVO.PAY_TYPE_CVS){
						value = "1"; //取貨付款
					}else{
						value = "3"; //取貨不付款
					}
				}
			}else{
				value = rs.getString(column);
			}
			if(value==null) value = ""; //null改成空白
			result.put(column, value);
		}
		return result;
	}
	/**
	 * 產生便利達康F10訂單檔
	 * @return
	 * @throws Exception
	 */
	public String generateCvsOrderXml() throws Exception {
		String xmlContent = null;
		
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		//建立Xml Document
		Document document = createXmlDocument();
		Element rootElement = createRootElement(document, XML_ROOT_TAG);
		
		// 匯出將已拋轉至SAP且尚未回傳之訂單且為超商取貨之訂單
		try {
			conn = ThesysLaphoneDAO.getConnection();
			String sql = "SELECT RIGHT('00000000000'+ORD_ID,11) ODNO,CVS_ST_NO STNO,REC_NAME CUTKNM,REC_MOBILE CUTKTL,TT_AMT REALAMT,PAY_TYPE TRADETYPE  FROM LAPHONE_ORD_MAIN WHERE SITE_ID=? AND ORD_ST = ? AND SAP_ORD_NO IS NULL AND SHIP_TYPE in('K','F','L') ";
			stmt = conn.prepareStatement(sql);
			int idx = 1;
			stmt.setString(idx++, getSiteId());
			stmt.setInt(idx++, ThesysOrderVO.ORDER_STATUS_SAP_POSTED);
			rs = stmt.executeQuery();
			int totalCount = 0;
			while (rs.next()) {
				JSONObject jsonObj = convertResultSet(columnNames, rs);
				jsonObj.put(XML_ECNO, ECNO );//EC 網站代號
				jsonObj.put(XML_PRODNM, PRODNM );//商品別代碼0 : 一般商品 1 : 票券商品
				jsonObj.put(XML_ECWEB, ECWEB );//EC 網站名稱
				jsonObj.put(XML_ECSERTEL, ECSERTEL );//EC 網站客服電話
				jsonObj.put(XML_SERCODE, SERCODE); //代收代號 963 : 一般 EC 廠商
				jsonObj.put(XML_EDCNO, EDCNO);//大物流代號
				
				int payType = jsonObj.getInt(XML_TRADETYPE);
				if(ThesysOrderVO.PAY_TYPE_CVS ==payType){
					jsonObj.put(XML_TRADETYPE,1); //取貨付款
					jsonObj.put(XML_AMT, jsonObj.getInt(XML_REALAMT)); //代收金額
				}else{
					jsonObj.put(XML_TRADETYPE,3); //取貨不付款
					jsonObj.put(XML_AMT, 0); //代收金額
				}
				
				System.out.println(jsonObj);
				rootElement.appendChild(createDataElement(document,XML_CONTENT_TAG,jsonObj));
				totalCount++;
			}
			JSONObject total = new JSONObject().put(XML_TOTALS, totalCount);
			rootElement.appendChild( createDataElement(document,XML_ORDERCOUNT_TAG,total) );
			if(totalCount>0){
				xmlContent = convertXmlToString(document);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			ThesysLaphoneDAO.closeAll(conn, stmt, rs);
		}
		return xmlContent;
	}
	/**
	 * 上傳至便利達康的web service
	 * @param xmlContent
	 * @return
	 * @throws Exception
	 */
	public String postToCvsWebService()throws Exception{
		
		String xmlContent = generateCvsOrderXml();
		System.out.println(xmlContent);
		if(xmlContent!=null){ //無資料時不傳
			String endpointURL = "https://cvsweb.cvs.com.tw/webservice/service.asmx";
			String namespaceURI = "http://tempuri.org/";// 命名空間
			String soapactionURI = "http://tempuri.org/ORDERS_ADD"; // soapactionURI
			String remotemethod = "ORDERS_ADD";// 方法名
	
			org.apache.axis.client.Service service = new org.apache.axis.client.Service();
			
			org.apache.axis.client.Call call = (org.apache.axis.client.Call) service.createCall();
	
			call.addParameter(
					new javax.xml.namespace.QName(namespaceURI, "xmlStr"),
					org.apache.axis.encoding.XMLType.XSD_STRING,
					javax.xml.rpc.ParameterMode.IN);
			call.setReturnType(org.apache.axis.encoding.XMLType.XSD_SCHEMA);
	
			call.setUseSOAPAction(true);
			call.setSOAPActionURI(soapactionURI);
			call.setTargetEndpointAddress(new java.net.URL(endpointURL).toString());
			javax.xml.namespace.QName qname = new javax.xml.namespace.QName(namespaceURI, remotemethod);
			call.setOperationName(qname);
	
			org.apache.axis.types.Schema schema = (org.apache.axis.types.Schema) call.invoke(new Object[] { xmlContent });
			
			if(schema!=null){
				String res = schema.get_any()[0].getAsString();
				return res;
			}
		}
		return null;
	}
	
	
	/**
	 * 建立Xml Document
	 * @return
	 * @throws Exception
	 */
	public Document createXmlDocument()  throws Exception{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.newDocument();
		return document;
	}
	public Element createRootElement(Document document,String rootTag){
		Element rootElement = document.createElement(rootTag);//root
		document.appendChild(rootElement);
		return rootElement;
	}
	public JSONObject convertResultSet(String[] columnNames,ResultSet rs) throws Exception{
		JSONObject jsonObj = new JSONObject();
		for(int i=0;i<columnNames.length;i++){
			String column = columnNames[i];
			jsonObj.put(column, rs.getObject(column));
		}
		return jsonObj;
	}
	/**
	 * 產生有資料的Xml
	 * @param document
	 * @param rootElement
	 * @param orderData
	 * @return
	 * @throws Exception
	 */
	public Element createDataElement(Document document,String tag,JSONObject data)throws Exception{
		Element result = document.createElement(tag);//有資料的data
		Iterator<String> it = data.keys();
		while(it.hasNext()){
			String key = it.next();
			Object val = data.get(key);
			Element em = document.createElement(key);
			if(val instanceof String){
				em.appendChild(document.createCDATASection((String)val));//<![CDATA[....]]>
			}else{
				em.appendChild(document.createTextNode(String.valueOf(val)));
			}
			result.appendChild(em);
		}
		return result;
		
	}
	/**
	 * 將Xml物件轉成字串
	 * @param document
	 * @return
	 * @throws Exception
	 */
	public String convertXmlToString(Document document)  throws Exception{
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(document);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(out);
		result.setOutputStream(out);
		transformer.transform(source, result);
		return new String(out.toByteArray(),"UTF-8");
	}
	
}
