package com.thesys.opencms.laphone.imp;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;



import org.apache.commons.logging.Log;
import org.opencms.file.CmsObject;
import org.opencms.json.JSONObject;
import org.opencms.main.CmsLog;


import com.thesys.opencms.laphone.product.dao.ThesysStockDAO;
import com.thesys.opencms.laphone.product.dao.ThesysStockVO;

public class ThesysSapStockImportHandler  extends ThesysAbstractCsvImportHandler{
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(ThesysSapStockImportHandler.class);
    
    public ThesysSapStockImportHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super.init(context, req, res);		

	}    
    public ThesysSapStockImportHandler(CmsObject cmso){
    	super.init(cmso);
    }    
   	/**
	 * 匯入csv
	 */
//	public void importCsvFile(InputStream stream)throws Exception{
//		InputStreamReader in =new InputStreamReader(stream,"UTF-8");
//
//		CSVReader reader = new CSVReader(in,'\t');
//		String [] nextLine;
//
//		while ((nextLine = reader.readNext()) != null) {
//	        if(true){//if(rowId>=1){ //跳過第一筆
//	    		try{
//			    	JSONObject jsonObj = convertToJSONObject(nextLine);
//			    	System.out.println(jsonObj.toString());
//			    	importRow(jsonObj);
//			    	
//	    		}catch(Exception ex){
//	    			LOG.error(ex, ex.fillInStackTrace());
//	    		}
//	    	}
//	    }
//	    reader.close();  
//		
//		
//	    in.close();
//	}	
	
	/* (non-Javadoc)
	 * @see com.thesys.opencms.laphone.ThesysCvsImportHandler#beforeImport()
	 */
	@Override
	public void beforeImport() {
		// 
		
	}
	/* (non-Javadoc)
	 * @see com.thesys.opencms.laphone.ThesysCvsImportHandler#afterImport()
	 */
	@Override
	public void afterImport() {
		// 
		
	}
	/* (non-Javadoc)
	 * @see com.thesys.opencms.laphone.ThesysCvsImportHandler#getColumnNames()
	 */
	@Override
	public String[] getColumnNames() {
		String[] columnNames = {ThesysStockVO.JSON_ITEM_ID, //料號
				null,//物料名稱
				null,//工廠
				null,//儲存位置
				null,//批次
				null,//供應商
				null,//STOCK TYPE
				ThesysStockVO.JSON_QUANTITY //庫存數量
				};
		return columnNames;
	}
	/**
	 * 匯入一行資料
	 * @param jsonObj
	 * @throws Exception
	 */
	public void importRow(JSONObject jsonObj) throws Exception{		

    	LOG.debug(jsonObj.toString());			
		int quantity = jsonObj.getInt(ThesysStockVO.JSON_QUANTITY);
    	String itemId = jsonObj.getString(ThesysStockVO.JSON_ITEM_ID);
    	
    	ThesysStockVO vo = new ThesysStockVO();
    	vo.setSiteId(getSiteId());
    	vo.setItemId(itemId);
    	vo.setQuantity(quantity);
    	vo.setCreater(getUserId());
    	vo.setCreateDate(new java.util.Date());
    	vo.setLastUpdater(getUserId());
    	vo.setLastUpdateDate(new java.util.Date());		
		ThesysStockDAO.getInstance().insertOrUpdate(vo);
		
	}
	
	
}
