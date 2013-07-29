package com.thesys.opencms.laphone.imp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.json.JSONObject;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.main.CmsLog;


import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;



public abstract class ThesysAbstractCsvImportHandler{

    private static final Log LOG = CmsLog.getLog(ThesysAbstractCsvImportHandler.class);

	private CmsObject cmsObject;
	private int processCount;
	private int successCount;
	private int errorCount;
	private String errorMessage;
	private InputStream resultStream;
    
	public void init(PageContext context, HttpServletRequest req,HttpServletResponse res){
		CmsJspActionElement cms = new CmsJspActionElement(context,req,res);
		init(cms.getCmsObject());
	}
	public void init(CmsObject cmso){	
		setCmsObject(cmso);
	}
	
	/**
	 * 匯入csv
	 */
	public void importFile(InputStream stream,boolean hasColumn)throws Exception{
		
		beforeImport();
		
		clearLog(); //清除記錄
		char empty = '\0'; 
		
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		CSVWriter csvWriter = new CSVWriter(new OutputStreamWriter(outStream),'\t',empty);
		
		InputStreamReader in =new InputStreamReader(stream,"UTF-8");

		CSVReader reader = new CSVReader(in,'\t');
		String [] nextLine;
		if(hasColumn){//跳過第一筆
			reader.readNext();
			processCount++;
		}
		
		while ((nextLine = reader.readNext()) != null) {	
			ArrayList<String> list = new ArrayList<String>();
		    Collections.addAll(list, nextLine);
		    processCount++;
    		try{
		    	JSONObject jsonObj = convertToJSONObject(nextLine);
		    	System.out.println(jsonObj.toString());
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
	    afterImport();
	}
    
    /**
	 * 將csv一行資料轉成json
	 * @param line
	 * @return
	 * @throws Exception
	 */
	public JSONObject convertToJSONObject(String [] line) throws Exception{		
		String[] columnNames = getColumnNames();
		JSONObject jsonObj = new JSONObject();
    	for(int i=0;i<columnNames.length;i++){
    		if(columnNames[i]!=null)	jsonObj.put(columnNames[i], line[i] );		    		
    	}
    	return jsonObj;
		
	}
	public abstract void beforeImport();
	public abstract void afterImport();
    
    public abstract void importRow(JSONObject jsonObj) throws Exception;
    
    public abstract String[] getColumnNames();
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
	private void clearLog(){
    	processCount=0;
    	successCount=0;
    	errorCount=0;
    	errorMessage="";
    	resultStream = null;
    }
	
	
}
