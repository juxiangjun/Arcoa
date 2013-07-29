package com.thesys.opencms.laphone.imp;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.opencms.file.CmsObject;
import org.opencms.json.JSONObject;
import org.opencms.jsp.CmsJspActionElement;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;




public abstract class ThesysAbstractXmlImportHandler{


	private CmsObject cmsObject;
    
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
	public void importFile(InputStream stream)throws Exception{
		
		beforeImport();
				
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
	    Document doc = builder.parse(stream);
	    
	    //得到XML文檔的根節點
//        Element root=doc.getDocumentElement();
        
        //取得Rows
        NodeList list = doc.getElementsByTagName(getRowName());
        if(list!=null){
			int length = list.getLength();
			for(int i=0;i<length;i++){
				Element node = (Element)list.item(i);
				//讀取每一筆資料					
				JSONObject jsonObj = convertToJSONObject(node);
				importRow(jsonObj);
			}
			
		}
	    stream.close();
	    afterImport();
	}
	
    
    /**
	 * 將csv一行資料轉成json
	 * @param line
	 * @return
	 * @throws Exception
	 */
	public JSONObject convertToJSONObject(Element row) throws Exception{	
		String[] columnNames = getColumnNames();
		JSONObject jsonObj = new JSONObject();
		
    	for(int i=0;i<columnNames.length;i++){
    		if(columnNames[i]!=null){
    			String tag = columnNames[i];
    			NodeList list = row.getElementsByTagName(tag);
    			if(list!=null && list.getLength()>0){
    				String value = list.item(0).getTextContent();
    				jsonObj.put(tag, value );	
    			}
    		}
    	}
    	return jsonObj;
		
	}
	public abstract void beforeImport() throws Exception;
	public abstract void afterImport() throws Exception;
    
    public abstract void importRow(JSONObject jsonObj) throws Exception;
    
    /**
     * 取得欄位名稱
     * @return
     */
    public abstract String[] getColumnNames();
    
    /**
     * 取得Row Tag Name
     * @return
     */
    public abstract String getRowName();
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
	
	
	
}
