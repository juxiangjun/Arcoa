package com.thesys.opencms.laphone.imp;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import jxl.Sheet;
import jxl.Workbook;



import org.apache.axis.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsProperty;
import org.opencms.file.CmsResourceFilter;
import org.opencms.json.JSONArray;
import org.opencms.json.JSONObject;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsLog;
import org.opencms.main.OpenCms;
import org.opencms.xml.CmsXmlContentDefinition;
import org.opencms.xml.CmsXmlEntityResolver;
import org.opencms.xml.CmsXmlException;
import org.opencms.xml.content.CmsXmlContent;
import org.opencms.xml.content.CmsXmlContentFactory;
import org.opencms.xml.types.I_CmsXmlContentValue;

import com.fol.utils.DateUtil;
import com.thesys.opencms.laphone.product.ThesysRatingHandler;
import com.thesys.opencms.laphone.system.ThesysParamHandler;
import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;
import com.thesys.opencms.laphone.util.ThesysSendMsgHandler;



import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;


public class ThesysSapProductImportHandler{
	private final String JSON_SAP_CATEGORY_CODE = "SapCategoryCode";
	private final String JSON_PRODUCT_CODE = "ProductCode";					
	private final String JSON_SAP_PRODUCT_CODE = "SapProductCode";
	private final String JSON_PRODUCT_NAME = "ProductName";			
	private final String JSON_PRODUCT_COLOR = "ProductColor";			
	private final String JSON_WATERMARK = "Watermark";					
	private final String JSON_DESCRIPTION = "Description";					
	private final String JSON_INFORMATION = "Information";				
	private final String JSON_SEO_KEYWORD = "SeoKeywords";			
	private final String JSON_ORIGINAL_PRICE = "OriginalPrice";		
	private final String JSON_SPECIAL_PRICE = "SpecialPrice";					
	private final String JSON_SERIES_PRODUCT_CODE = "SeriesProductCode";//同系列商品	
	private final String JSON_RELATION_PRODUCT_CODE = "RelationProductCode";//關聯商品
	private final String JSON_COLOR_CODE = "ColorCode";	
	private final String JSON_STYLE = "Style";
	private final String JSON_RATING = "Rating";	
	private final String JSON_COUPON_COUNT_FLAG = "CouponCountFlag";
	private final String JSON_COUPON_USE_FLAG = "CouponUseFlag";
//	private final String JSON_COUPON_TYPE = "CouponType"; //物料群組
//	private final String JSON_CVS_SHIP_FLAG = "CVSShipFlag";
	private final String JSON_INSTALLMENT = "Installment";										
	private final String JSON_ONLINE_DATE = "OnlineDate";		
	private final String JSON_OFFLINE_DATE = "OfflineDate";
	private final String JSON_SELL_FLAG = "SellFlag";
	private final String JSON_ONLINE_FLAG = "OnlineFlag";	
	private final String JSON_GROUP_FLAG = "GroupFlag";
	
	private final String JSON_MAIN_CATEGORY = "MainCategory"; //大類
	private final String JSON_SUB_CATEGORY = "SubCategory"; //中類
	
	private final String JSON_GROUP_ITEM = "GroupItem";		
	private final String JSON_GROUP_SAP_PRODUCT_CODE = "GroupSapProductCode";
	private final String JSON_GROUP_PRICE = "GroupPrice";		
	private final String JSON_GROUP_SPECIAL_PRICE = "SpecialPrice";		
	private final String JSON_GROUP_QUANTITY = "GroupQuantity";	
	
	/**增加资料首次与最近一次汇入时间*/
	private final String JSON_LAST_IMPORTED_DATE = "LastImportedDate";
	
	private int processCount;
	private int successCount;
	private int errorCount;
	private String errorMessage;
	private InputStream resultStream;
	private String priceAlertMsg = new String(); //價格警示訊息
	
	private DateUtil dateUtil = new DateUtil();
	
	private boolean modifyCategoryFlag = false; //判斷是否修正資料的flag
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(ThesysSapProductImportHandler.class);
    private CmsObject cmsObject;
    
    private double change_rate;
    
    
    public ThesysSapProductImportHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
    	CmsJspActionElement cms = new CmsJspActionElement(context,req,res);
    	init(cms.getCmsObject());
	}    
    public ThesysSapProductImportHandler(CmsObject cmso){
    	init(cmso);
    }    
    private void init(CmsObject cmso){
    	this.cmsObject = cmso;
    	OpenCms.getSearchManager().setOfflineUpdateFrequency(1000); //設定後台搜尋頻率為1秒
    	ThesysParamHandler paramhandler = new ThesysParamHandler();
    	try {
			this.change_rate = Double.valueOf(paramhandler.getParamVal("/sites/laphone", "PRODUCT_WARN_THRESHOLD").split(";")[0]);
		} catch (Exception e) {
			LOG.error("Get change rate failed");
		}
    }
    private void clearLog(){
    	processCount=0;
    	successCount=0;
    	errorCount=0;
    	errorMessage="";
    	resultStream = null;
    }
   	/**
	 * 匯入價格主檔
	 */
	public void importPriceCsv(InputStream stream)throws Exception{
		importCsvFile(stream,2);
		sendPriceAlertMail();
	}
	/**
	 * 價格異常通知
	 * @throws Exception
	 */
	public void sendPriceAlertMail()throws Exception{
		if(priceAlertMsg.length()>0){
			//寄發通知信
			priceAlertMsg = "<table border='1'><tr><td>物料編號</td><td>原會員價</td><td>新會員價</td></tr>"+priceAlertMsg+"</table>";
			ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(getCmsObject());
        	String email = ThesysParamDAO.getInstance().getParam(getSiteId(), "PRODUCT_EMAIL").getParamVal();//商品管理email
        	JSONObject json = new JSONObject();
        	json.put("title", "商品價格異常通知");
        	json.put("content", priceAlertMsg);
        	msgHandler.setMsgName("orderDataList");
        	msgHandler.setJsonObj(json);
			msgHandler.setEmail(email); 
			msgHandler.sendMsg();
		}
	}
	/**
	 * 預設匯入物料主檔
	 */
	public void importProductCsv(InputStream stream)throws Exception{
		importCsvFile(stream,1);
	}
	/**
	 * 匯入物料主檔以修正大類及中類
	 */
	public void importCategoryCsv(InputStream stream)throws Exception{
		modifyCategoryFlag = true;
		importCsvFile(stream,1);
	}
	/**
	 * 匯入商品基本資料
	 * @param stream
	 * @return
	 * @throws Exception
	 */
	public void importProductExcel(InputStream stream)throws Exception{
		clearLog(); //清除記錄		

		
		Workbook book = Workbook.getWorkbook(stream);
		Sheet sheet = book.getSheet(0);
		int colNum = sheet.getColumns(); 
		int rowNum = sheet.getRows();  
		for(int row=1;row<rowNum;row++){
			processCount ++;
			String [] nextLine = new String[colNum];
			for(int col=0;col<colNum;col++){
				String colVal = sheet.getCell(col,row).getContents();   
				nextLine[col] = colVal;
			}
    		try{
		    	JSONObject jsonObj = convertToJSONObject(nextLine,5);
		    	importExcelRow(jsonObj);
		    	successCount++;
		    	
    		}catch(Exception ex){
    			LOG.error(ex, ex.fillInStackTrace());
    			errorMessage +="第 "+(row+1)+"行:"+ex.getMessage()+"\\r\\n";
    			errorCount++;
    		}
		
		}	

		stream.close();

	}
	/**
	 * 匯入組案價格檔
	 */
	public void importGroupCsv(InputStream stream)throws Exception{
		clearLog(); //清除記錄
		InputStreamReader in =new InputStreamReader(stream,"UTF-8");

		CSVReader reader = new CSVReader(in,'\t');
		String [] nextLine;

		JSONObject groupJsonObj = null;
		String groupId = null;
	    while ((nextLine = reader.readNext()) != null) {
	    	processCount++;
    		try{
		    	JSONObject jsonObj = convertToJSONObject(nextLine,3);
		    	
		    	
		    	String tmpGroupId = jsonObj.getString(JSON_SAP_PRODUCT_CODE); //組案商品編號
		    	
		    	
		    	if(!tmpGroupId.equals(groupId)){ //新的groupId時，先寫入xml再換新物件
		    		if(groupId!=null){
		    			//寫入db
		    			importGroupRow(groupJsonObj);
		    		}
		    		groupId = tmpGroupId;
		    		groupJsonObj = new JSONObject();

		    		groupJsonObj.put(JSON_SAP_PRODUCT_CODE, jsonObj.getString(JSON_SAP_PRODUCT_CODE));
		    		groupJsonObj.put(JSON_PRODUCT_CODE, jsonObj.getString(JSON_PRODUCT_CODE));
		    		groupJsonObj.put(JSON_GROUP_ITEM, new JSONArray());
		    	}
		    	//加入群組商品
		    	JSONArray additionArr = (JSONArray)groupJsonObj.get(JSON_GROUP_ITEM);
		    	
		    	//建立群組商品
		    	JSONObject additionObj = new JSONObject();
		    	additionObj.put(JSON_GROUP_SAP_PRODUCT_CODE, jsonObj.getString(JSON_GROUP_SAP_PRODUCT_CODE));
		    	additionObj.put(JSON_GROUP_SPECIAL_PRICE, jsonObj.getInt(JSON_GROUP_SPECIAL_PRICE));
		    	additionObj.put(JSON_GROUP_PRICE, jsonObj.getInt(JSON_GROUP_PRICE));
		    	additionObj.put(JSON_GROUP_QUANTITY, jsonObj.getInt(JSON_GROUP_QUANTITY));
		    	
		    	additionArr.put(additionObj);				    	
		    	groupJsonObj.put(JSON_GROUP_ITEM, additionArr);
		    	successCount++;
    		}catch(Exception ex){
    			ex.printStackTrace();
    			LOG.error(ex, ex.fillInStackTrace());
    			errorMessage +="第"+processCount+"行:"+ex.getMessage()+"\\r\\n";
    			errorCount++;
    		}
    		
	    }
	    //寫入最後一筆
		importGroupRow(groupJsonObj);
	    reader.close();    

			
	    in.close();
	    
	    sendPriceAlertMail();

	}
	 /**
	  * 匯入Csv格式檔案
	  * @param stream
	  * @param fileType
	  * @throws Exception
	  */
	private void importCsvFile(InputStream stream,int fileType)throws Exception{

		
		clearLog(); //清除記錄
		char empty = '\0'; 
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outStream),'\t',empty);
		InputStreamReader in =new InputStreamReader(stream,"UTF-8");

		CSVReader reader = new CSVReader(in,'\t');
		String [] nextLine;

		while ((nextLine = reader.readNext()) != null) {
			ArrayList<String> list = new ArrayList<String>();
		    Collections.addAll(list, nextLine);

			processCount++;
    		try{
		    	JSONObject jsonObj = convertToJSONObject(nextLine,fileType);
		    	importRow(jsonObj);
		    	list.add("匯入成功");
		    	successCount++;
    		}catch(Exception ex){
    			LOG.error(ex, ex.fillInStackTrace());
    			errorMessage +="第"+processCount+"行:"+ex.getMessage()+"\\r\\n";
    			list.add(ex.getMessage());
    			errorCount++;
    			
    		}
    		String[] result = new String[list.size()];    		
    		csvWriter.writeNext(list.toArray(result));
	    }
	    reader.close();
	    csvWriter.flush();
	    csvWriter.close();
	    resultStream = new ByteArrayInputStream(outStream.toByteArray());
	    outStream.close();
		
	    in.close();

	}
	/**
	 * 將csv一行資料轉成json
	 * @param line
	 * @return
	 * @throws Exception
	 */
	public JSONObject convertToJSONObject(String [] line,int fileType) throws Exception{
		//物料主檔
		String[] columnNames = {};
		switch(fileType){
			case 1: //物料主檔
				columnNames = new String[]{JSON_SAP_PRODUCT_CODE, //料號
								null,//物料名稱
								null,//物料類型
								null,//基礎記量單位
								JSON_MAIN_CATEGORY,//物料群組(01 手機類;03配件類)
								JSON_SAP_CATEGORY_CODE //產品階層
								};
				break;
			case 2: //單品價格檔
				columnNames = new String[]{JSON_SAP_PRODUCT_CODE, //料號
						null,//客戶價
						null,//經銷一般價格
						null,//經銷盤商價
						JSON_ORIGINAL_PRICE,//門市價格
						null,//遠傳掛招價
						JSON_SPECIAL_PRICE //會員價
						};
				
				break;
			case 3: //組案價格檔
				columnNames = new String[]{JSON_SAP_PRODUCT_CODE, //組合品料號
						null,//工廠
						null,//銷售組織
						null,//配銷通路
						null,//銷售據點
						null,//銷售群組
						null,//價格清單
						null,//客戶代號
						null,//組合替代物料
						JSON_GROUP_SAP_PRODUCT_CODE,//單品料號
						JSON_GROUP_QUANTITY,//數量
						JSON_GROUP_PRICE, //組合價-金額
						null,//起始日
						null,//結束日
						null,//優先級別
						JSON_GROUP_SPECIAL_PRICE,//該物料之原價(會員價)
						};
				
				break;
			case 5: //商品基本資料
				columnNames = new String[]{JSON_SAP_PRODUCT_CODE, //商品料號
						JSON_PRODUCT_NAME,//商品名稱
						JSON_ONLINE_FLAG,//商品上、下架
						JSON_SELL_FLAG,//商品銷售狀態
						JSON_WATERMARK,//商品浮水印
						JSON_DESCRIPTION, //商品短敘述
						JSON_INFORMATION,//商品長敘述
						JSON_RELATION_PRODUCT_CODE,//相關商品
						JSON_RATING,//商品人氣						
						JSON_PRODUCT_COLOR,//商品顏色
						JSON_COUPON_COUNT_FLAG,//產生抵用券
						JSON_COUPON_USE_FLAG,//可使用抵用券
						JSON_STYLE,//商品風格
						JSON_COLOR_CODE,//顏色搜尋
						JSON_INSTALLMENT,//分期付款期數
						JSON_SEO_KEYWORD,//關鍵字
						JSON_SAP_CATEGORY_CODE,//商品分類
						JSON_SERIES_PRODUCT_CODE//同系列不同顏色商品
						};
				
				break;
		
		
		}
		JSONObject jsonObj = new JSONObject();
    	for(int i=0;i<columnNames.length;i++){    		
    		if(columnNames[i]!=null && line[i].trim().length()>0){
    			jsonObj.put(columnNames[i], line[i].trim());		    		
    		}
    	}
    	if(jsonObj.length()==0){
    		throw new Exception("空白資料列");    		
    	} 
    	if(!jsonObj.has(JSON_SAP_PRODUCT_CODE)){
    		throw new Exception("商品料號空白");   
    	}

    	//料號轉EC-產品編號   
    	String productCode = jsonObj.getString(JSON_SAP_PRODUCT_CODE).replaceAll("\\+", "_").replaceAll("-", "_");
    	jsonObj.put(JSON_PRODUCT_CODE, productCode);
    	
    	if(jsonObj.has(JSON_MAIN_CATEGORY)){
	    	String couponType = jsonObj.getString(JSON_MAIN_CATEGORY);
	    	if("01".equals(couponType)){//手機類，可計算不可折抵
	    		jsonObj.put(JSON_COUPON_COUNT_FLAG, true);
	    		jsonObj.put(JSON_COUPON_USE_FLAG, false);
	    	}else if("03".equals(couponType)){//配件類，可折抵不可計算
	    		jsonObj.put(JSON_COUPON_COUNT_FLAG, false);
	    		jsonObj.put(JSON_COUPON_USE_FLAG, true);
	    	}else{
	    		jsonObj.put(JSON_COUPON_COUNT_FLAG, false);
	    		jsonObj.put(JSON_COUPON_USE_FLAG, false);
	    	}
//	    	jsonObj.remove(JSON_COUPON_TYPE);
    	}
    	if(jsonObj.has(JSON_SAP_CATEGORY_CODE)){ //補上中類
    		String categoryCode = jsonObj.getString(JSON_SAP_CATEGORY_CODE);
    		jsonObj.put(JSON_SUB_CATEGORY, categoryCode.substring(1,5));
    	}
    	if(jsonObj.has(JSON_INFORMATION)){
    		String txt = jsonObj.getString(JSON_INFORMATION).replaceAll("\r\n", "\n").replaceAll("\n", "<br/>");
    		jsonObj.put(JSON_INFORMATION,txt);
    		
    	}
    	//檢查金額不可為0
    	if(jsonObj.has(JSON_SPECIAL_PRICE)){
    		if(jsonObj.getInt(JSON_SPECIAL_PRICE)==0){
				throw new Exception("特價金額不可為0");
			}
    		
    	}
    	
    	return jsonObj;
		
	}
	public void importExcelRow(JSONObject jsonObj) throws Exception{
		String errorMsg = "";
		//欄位檢查
		String productCode = jsonObj.optString(JSON_SAP_PRODUCT_CODE);
		if(productCode==null || productCode.length()==0){
			errorMsg += "商品料號空白；";
		}
		String productName = jsonObj.optString(JSON_PRODUCT_NAME);
		if(productName==null || productName.length()==0){
			errorMsg += "商品名稱空白；";
		}
		//檢查商品上下架狀態
		String onlineFlag = jsonObj.optString(JSON_ONLINE_FLAG);
    	if(onlineFlag==null || onlineFlag.length()==0){
    		errorMsg += "商品上下架狀態空白；";
    	}else if(!"Y".equals(onlineFlag) && !"N".equals(onlineFlag)){
    		errorMsg += "商品上下架狀態應該Y或N；";
		}else{
			jsonObj.put(JSON_ONLINE_FLAG, "Y".equals(onlineFlag)); //轉型
		}
    	//檢查銷售狀態
		String sellFlag = jsonObj.optString(JSON_SELL_FLAG);
    	if(sellFlag==null || sellFlag.length()==0){
    		errorMsg += "商品銷售狀態空白；";
    	}else if(!"Y".equals(sellFlag) && !"N".equals(sellFlag)){
    		errorMsg += "商品銷售狀態應該Y或N；";
		}else{
			jsonObj.put(JSON_SELL_FLAG, "Y".equals(sellFlag)); //轉型
		}
    	//檢查浮水印
    	String watermark = jsonObj.optString(JSON_WATERMARK);
    	if(watermark==null || watermark.length()==0){ //可不填
    		watermark = "0";
    		jsonObj.put(JSON_WATERMARK, watermark);
    	}
    	if(!"0".equals(watermark) && !"1".equals(watermark) && !"2".equals(watermark) && !"3".equals(watermark) && !"4".equals(watermark)){
    		errorMsg += "商品浮水印應該0、1、2、3、4；";
		}
		String description = jsonObj.optString(JSON_DESCRIPTION);
		if(description==null || description.length()==0){
			errorMsg += "商品簡介空白；";
		}

		String information = jsonObj.optString(JSON_INFORMATION);
		if(information==null || information.length()==0){
			errorMsg += "商品詳細介紹空白；";
		}
		String relationCode = jsonObj.optString(JSON_RELATION_PRODUCT_CODE);
		if(relationCode!=null && relationCode.length()!=0){ //可不填
			String[] relationArr = relationCode.split(";");
			if(relationArr!=null && relationArr.length>3){
				errorMsg += "相關商品最多三筆；";
			}
		}
		String rating = jsonObj.optString(JSON_RATING);
		if(rating!=null && rating.length()!=0){ //可不填
			try{
				if(Integer.parseInt(rating)<0){				
					errorMsg += "商品人氣應為大於0；";
				}
			}catch(Exception e){
				errorMsg += "商品人氣應為數字；";
			}
		}	
		String productColor = jsonObj.optString(JSON_PRODUCT_COLOR);
		if(productColor==null || productColor.length()==0){
			errorMsg += "商品顏色空白；";
		}else if(productColor.length()!=2){ 
				errorMsg += "商品顏色應為2碼；";
		}
		//檢查產生抵用券
		String countFlag = jsonObj.optString(JSON_COUPON_COUNT_FLAG);
    	if(countFlag!=null && countFlag.length()!=0){ //可不填
    		if(!"Y".equals(countFlag) && !"N".equals(countFlag)){
    			errorMsg += "產生抵用券應該空白、Y或N；";
    		}else{
    			jsonObj.put(JSON_COUPON_COUNT_FLAG, "Y".equals(countFlag)); //轉型
    		}
		}
    	//檢查可使用抵用券
		String useFlag = jsonObj.optString(JSON_COUPON_USE_FLAG);
    	if(useFlag!=null && useFlag.length()!=0){ //可不填
    		if(!"Y".equals(useFlag) && !"N".equals(useFlag)){
    			errorMsg += "可使用抵用券應該空白、Y或N；";
    		}else{
    			jsonObj.put(JSON_COUPON_USE_FLAG, "Y".equals(useFlag)); //轉型
    		}
		}
    	//檢查商品風格
		String style = jsonObj.optString(JSON_STYLE);
    	if(useFlag!=null && useFlag.length()!=0){ //可不填
    		if(!"H".equals(style) && !"S".equals(style)  && !"F".equals(style)  && !"D".equals(style) && !"V".equals(style) && !"M".equals(style) && !"K".equals(style) && !"L".equals(style)){
    			errorMsg += "商品風格應該空白、H、S、F、D、V、M、K或L；";
    		}
		}
		//分期付款期數
		String installment = jsonObj.optString(JSON_INSTALLMENT);
		if(installment==null || installment.length()==0){ 
			errorMsg += "分期付款期數空白；";
		}else{
			try{
				boolean hasOne = false;
				String[] installmentArray = installment.split(";");
				for(String installmentNo:installmentArray){
					int tmp = Integer.parseInt(installmentNo);
					if(tmp == 1 ) hasOne = true;
				}
				if(!hasOne){
					errorMsg += "分期付款期數中一定要有1期；";
				}else if(installment.endsWith(";")){
					errorMsg += "分期付款期數格式應為[1;3;6]；";
				}	
			}catch(Exception e){
				errorMsg += "分期付款期數格式應為[1;3;6]；";
			}
		}
		if(errorMsg.length()>0) throw new Exception(errorMsg);		
		
		importRow(jsonObj);
	}
	/**
	 * 匯入一行資料
	 * @param jsonObj
	 * @throws Exception
	 */
	public void importRow(JSONObject jsonObj) throws Exception{		
		int rating = 0;		
		if(jsonObj.has(JSON_RATING)){
			rating = jsonObj.getInt(JSON_RATING);
			jsonObj.remove(JSON_RATING);
		}
		
		
		/**加入汇入时间*/
		
		String now = String.valueOf(dateUtil.getNowLong());
		jsonObj.put(JSON_LAST_IMPORTED_DATE, now);
		
		createProductXml(jsonObj);
		
		if(rating!=0){ //加上評價
			ThesysRatingHandler.add(getSiteId(),getUserId(),jsonObj.getString(JSON_SAP_PRODUCT_CODE),rating);
		}
		
	}
	/**
	 * 匯入一行資料
	 * @param jsonObj
	 * @throws Exception
	 */
	private void importGroupRow(JSONObject jsonObj) throws Exception{	
		//計算特價金額
		JSONArray arr = jsonObj.getJSONArray(JSON_GROUP_ITEM);
		int size = arr.length();
		int specialPrice = 0;
		int originalPrice = 0;
		for(int i=0;i<size;i++){
			JSONObject obj = arr.getJSONObject(i);
			specialPrice += obj.getInt(JSON_GROUP_QUANTITY)*obj.getInt(JSON_GROUP_PRICE);	//特惠價=組合商品之組合價加總
			originalPrice += obj.getInt(JSON_GROUP_QUANTITY)*obj.getInt(JSON_SPECIAL_PRICE);		//原價=組合商品之會員價加總
		}
		//檢查金額不可為0
		if(specialPrice==0){
			throw new Exception("特價金額不可為0");
		}
		jsonObj.put(JSON_SPECIAL_PRICE, String.valueOf(specialPrice));
		jsonObj.put(JSON_ORIGINAL_PRICE, String.valueOf(originalPrice));
		jsonObj.put(JSON_GROUP_FLAG, true);
		jsonObj.put(JSON_MAIN_CATEGORY, "combine");//TODO: 大類為組合商品
		importRow(jsonObj);
		
	}
	private void createProductXml(JSONObject jsonObj) throws Exception{
		
		String productCode = jsonObj.getString(JSON_PRODUCT_CODE);
		// 讀取xml是否已存在
		String xmlPath =  "/product/"+productCode +".html";
		
		CmsFile xmlFile = null;
		CmsXmlContent xmlContent = null;
		boolean isExist = getCmsObject().existsResource(xmlPath,CmsResourceFilter.ALL);
		if(isExist){
			try{
				getCmsObject().unlockResource(xmlPath);
			}catch(Exception ex){};
			getCmsObject().lockResource(xmlPath);
			xmlFile = getCmsObject().readFile(xmlPath, CmsResourceFilter.ALL);
			xmlContent = CmsXmlContentFactory.unmarshal(getCmsObject(), xmlFile);
			
			//驗證schema是否正確
			CmsXmlEntityResolver resolver = new CmsXmlEntityResolver(getCmsObject());            
			try {
				xmlContent.validateXmlStructure(resolver); 
            } catch (CmsXmlException e) {
            	//xml不正確，修復後重取
            	xmlContent.setAutoCorrectionEnabled(true); // enable "auto correction mode" - this is required or the XML structure will not be fully corrected
    			xmlContent.correctXmlStructure(getCmsObject());  // now correct the XML  
    			xmlContent = CmsXmlContentFactory.unmarshal(getCmsObject(), xmlContent.marshal(), xmlContent.getEncoding(),resolver); 
            }
			
		}else{		
			xmlFile = new CmsFile(getCmsObject().createResource(xmlPath, OpenCms.getResourceManager().getResourceType("ThesysLaphoneProduct").getTypeId(),null,null));
			
			//產生xml格式
			String schema = "/system/modules/com.thesys.opencms.laphone/schemas/ThesysLaphoneProduct.xsd";
			CmsXmlContentDefinition contentDefinition = CmsXmlContentDefinition.unmarshal(getCmsObject(), schema );
			xmlContent = CmsXmlContentFactory.createDocument(getCmsObject(), getCmsObject().getRequestContext().getLocale(), "UTF8", contentDefinition);
			
			
		}
		//預設起始日為 0~2556028800000，加上修改或新增註記
		java.util.List<CmsProperty> properties = new java.util.ArrayList<CmsProperty>();
		
		if(jsonObj.has(JSON_PRODUCT_NAME)){ //為Excel 匯入才有預設上架日期			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new java.util.Date());
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			jsonObj.put(JSON_ONLINE_DATE, String.valueOf(cal.getTimeInMillis())); //即時上架
			jsonObj.put(JSON_OFFLINE_DATE, "2556028800000"); //預設下架日期
			
			if(isExist){ //若檔案存在
				String onlineDate = getXmlElement(xmlContent,"/"+JSON_ONLINE_DATE , 0).getStringValue(getCmsObject());
				if(onlineDate!=null && onlineDate.length()>0){
					jsonObj.put(JSON_ONLINE_DATE,onlineDate);
				}
				String offlineDate = getXmlElement(xmlContent,"/"+JSON_OFFLINE_DATE , 0).getStringValue(getCmsObject());
				if(offlineDate!=null && offlineDate.length()>0){
					jsonObj.put(JSON_OFFLINE_DATE,offlineDate);
				}
			}
		}
//		//資料修正時，需判斷若無商品名稱，清除預設上架日期
//		if(modifyCategoryFlag){
//			String productName = getXmlElement(xmlContent,"/"+JSON_PRODUCT_NAME , 0).getStringValue(getCmsObject());
//			if(productName==null || productName.length()==0){
//				jsonObj.put(JSON_ONLINE_DATE,"");
//			}
//		}
		if(jsonObj.has(JSON_SPECIAL_PRICE) && isExist){ //匯入會員價時，當會員價低於前次會員價1/2時，出警示
			String priceTxt = getXmlElement(xmlContent,"/"+JSON_SPECIAL_PRICE , 0).getStringValue(getCmsObject());
			if(priceTxt!=null && priceTxt.length()>0){
				double oldPrice = Double.parseDouble(priceTxt);
				double newPrice = jsonObj.getDouble(JSON_SPECIAL_PRICE);
				double discount =  newPrice/oldPrice;
				if(discount< change_rate){ //有問題警示
					properties.add(new CmsProperty("laphone.price-alert","true","true"));//增加售價警示
					//記錄價格異常訊息
					priceAlertMsg += "<tr><td>"+jsonObj.getString(JSON_SAP_PRODUCT_CODE)+"</td><td>"+(int)oldPrice+"</td><td>"+(int)newPrice+"</td></tr>";
				}else{
					properties.add(new CmsProperty("laphone.price-alert","",""));//移除售價警示
				}
			}
			
		}
		Iterator<String> it = jsonObj.keys();
		while(it.hasNext()){
			String fieldName = it.next();
			if(JSON_GROUP_ITEM.equals(fieldName)){
				removeAllTag(xmlContent, JSON_GROUP_ITEM);
				JSONArray arr = jsonObj.getJSONArray(JSON_GROUP_ITEM);
				int size = arr.length();
				for(int i=0;i<size;i++){
					this.addTag(xmlContent, JSON_GROUP_ITEM, arr.getJSONObject(i));
					
				}
			}else{
				setXmlContentValue(xmlContent,fieldName,jsonObj.getString(fieldName),0);
			}
		}
		
		//寫至檔案
		byte[] content = xmlContent.marshal();		
		xmlFile.setContents(content);	
		String fileStatus = getCmsObject().readPropertyObject(xmlPath, "FileStatus", false).getValue();
		
		if(isExist){			
			if(!"new".equals(fileStatus) && !modifyCategoryFlag){ //修正類別時，不修改註記
				properties.add(new CmsProperty("FileStatus","changed" ,	"changed" ));//加上修改註記
			}
		}else{
			properties.add(new CmsProperty("FileStatus","new" ,"new" ));//加上新增註記
		}
		
		if(jsonObj.has(JSON_PRODUCT_NAME)){ //為Excel 匯入
			Calendar cal = Calendar.getInstance();
			cal.setTime(new java.util.Date());
			cal.set(Calendar.HOUR, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			properties.add(new CmsProperty("laphone.excel-date",String.valueOf(cal.getTimeInMillis()) ,String.valueOf(cal.getTimeInMillis())));//加上匯入日期
			properties.add(new CmsProperty("laphone.check-date",String.valueOf(cal.getTimeInMillis()) ,String.valueOf(cal.getTimeInMillis())));//加上匯入日期(檢查是否超過七日)
		}
		getCmsObject().writePropertyObjects(xmlPath,properties);
		getCmsObject().writeFile(xmlFile);
		getCmsObject().unlockResource(xmlPath);
		if(modifyCategoryFlag && "published".equals(fileStatus)){ //修正類別時,自動發佈原本已發佈的資抖
			OpenCms.getPublishManager().publishResource(getCmsObject(), xmlPath);
		}
		
	}
	/**
	 * 增加tag
	 * @param xmlContent
	 * @param tagName
	 * @param jsonObj
	 * @throws Exception
	 */
	private void addTag(CmsXmlContent xmlContent,String tagName,JSONObject jsonObj) throws Exception{
		
		Locale locale = getCmsObject().getRequestContext().getLocale();
		int indexCount = xmlContent.getIndexCount(tagName, locale);
		Iterator<String> it = jsonObj.sortedKeys();
		while(it.hasNext()){
			String key = it.next();
			String val = jsonObj.getString(key);
			I_CmsXmlContentValue elem = getXmlElement(xmlContent,tagName,indexCount);
			getXmlElement(xmlContent,elem.getPath()+"/"+key,0).setStringValue(getCmsObject(), val);
		}
		
	}
//	/**
//	 * 移除單一Tag
//	 * @param xmlContent
//	 * @param tagName
//	 * @param index
//	 * @throws Exception
//	 */
//	private void removeTag(CmsXmlContent xmlContent,String tagName,int index) throws Exception{
//		
//		Locale locale = getCmsObject().getRequestContext().getLocale();
//		xmlContent.removeValue(tagName, locale, index-1);	
//		
//	}
	/**
	 * 移除所有Tag
	 * @param xmlContent
	 * @param tagName
	 * @throws Exception
	 */
	private void removeAllTag(CmsXmlContent xmlContent,String tagName) throws Exception{
		Locale locale = getCmsObject().getRequestContext().getLocale();
		int indexCount = xmlContent.getIndexCount(tagName, locale);
		for(int i=1;i<=indexCount;i++){
			try{
				xmlContent.removeValue(tagName, locale, 0);
			}catch(Exception ex){
				ex.printStackTrace();
				
			}
		}
		
	}
	/**
	 * 設定xml tag值
	 * @param xmlContent
	 * @param tagName
	 * @param tagValue
	 * @param index
	 */
	private void setXmlContentValue(CmsXmlContent xmlContent,String tagName,String tagValue,int index){
		getXmlElement(xmlContent,"/"+tagName,index).setStringValue(getCmsObject(),tagValue);
		
	}
	/**
	 * 讀取Xml tag
	 * @param xmlContent
	 * @param xpath
	 * @param index
	 * @return
	 */
	private I_CmsXmlContentValue getXmlElement(CmsXmlContent xmlContent,String xpath,int index){
		 I_CmsXmlContentValue elem = xmlContent.getValue(xpath, getCmsObject().getRequestContext().getLocale(),index);
		 if(elem==null){
			 elem = xmlContent.addValue(getCmsObject(), xpath, getCmsObject().getRequestContext().getLocale(), index);
		 }
		 return elem;
	}
	
	/**
	 * @return the siteId
	 */
	public String getSiteId() {
		return getCmsObject().getRequestContext().getSiteRoot();
	}



	/**
	 * @return the userId
	 */
	public String getUserId() {
		return getCmsObject().getRequestContext().currentUser().getName();
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
	/**
	 * @return the processCount
	 */
	public int getProcessCount() {
		return processCount;
	}
	/**
	 * @param processCount the processCount to set
	 */
	public void setProcessCount(int processCount) {
		this.processCount = processCount;
	}
	/**
	 * @return the successCount
	 */
	public int getSuccessCount() {
		return successCount;
	}
	/**
	 * @param successCount the successCount to set
	 */
	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
	/**
	 * @return the errorCount
	 */
	public int getErrorCount() {
		return errorCount;
	}
	/**
	 * @param errorCount the errorCount to set
	 */
	public void setErrorCount(int errorCount) {
		this.errorCount = errorCount;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * @return the resultStream
	 */
	public InputStream getResultStream() {
		return resultStream;
	}
	/**
	 * @param resultStream the resultStream to set
	 */
	public void setResultStream(InputStream resultStream) {
		this.resultStream = resultStream;
	}
	
}
