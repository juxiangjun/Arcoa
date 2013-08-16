package com.thesys.opencms.laphone.system;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;


import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.system.dao.ThesysSerialDAO;
import com.thesys.opencms.laphone.system.dao.ThesysSerialFormatVO;
import com.thesys.opencms.laphone.system.dao.ThesysSerialNoVO;
import com.thesys.opencms.modules.*;


public class ThesysSerialHandler extends ThesysAbstractHandler {
	/** The log object for this class. */
	protected static final Log LOG = CmsLog.getLog(ThesysSerialHandler.class);
    
	public ThesysSerialHandler(){}
	public ThesysSerialHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super(context, req, res);
	}
	public String getNextReturnNo(){
		return getNextSerialNo(ThesysSerialFormatVO.SERIAL_TYPE_RETURN);
	}
	
	public String getNextOrderNo(){
		return getNextSerialNo(ThesysSerialFormatVO.SERIAL_TYPE_ORDER);
	}
	/**
	 * 取得表格流水號
	 * @param tableName
	 * @return
	 */
	public int getNextTableSerialNo(String tableName){
		return ThesysSerialHandler.getNextTableSerialNo(getSiteId(), tableName);
	}
	public static int getNextTableSerialNo(String siteId,String tableName){
		int result = 1;
		try {
			
			//2) 取得目前編號
			ThesysSerialNoVO vo = ThesysSerialDAO.getInstance().readSerialNo(siteId, tableName, "-", "-");
			
			if(vo==null){
				vo = new ThesysSerialNoVO();
				vo.setSiteId(siteId);
				vo.setSerialType(tableName);
				vo.setPrefixText("-");
				vo.setDateText("-");
				vo.setSerialNumber(1);
			}else{
				vo.setSerialNumber(vo.getSerialNumber()+1);
			}		
			//3)儲存編號
			ThesysSerialDAO.getInstance().write(vo);
			//4)格式化編號字串
			result = vo.getSerialNumber();
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	public static void main(String[] args){
		String pattern = "YYMMdd";
		String dateText = "";
        if(!"N".equals(pattern)){
        	
        	java.util.Date now = new java.util.Date();
        	if(pattern.indexOf("Y")>=0){ //民國年
        		String yearPattern =  pattern.substring(0,pattern.lastIndexOf("Y")+1);
        		Calendar cal = Calendar.getInstance();
        		cal.setTime(now);
        		int twYear=cal.get(Calendar.YEAR)-1911;
        		dateText = String.valueOf(twYear).substring(3-yearPattern.length());
        		
        		//get month and day
        		String datePattern = pattern.substring(pattern.lastIndexOf("Y")+1);
        		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
	        	dateText += dateFormat.format(now);
        	}else{
	        	SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
	        	dateText = dateFormat.format(new java.util.Date());
        	}
        }
        System.out.println(dateText);
		
	}
	public String getNextSerialNo(String serialType){
		return ThesysSerialHandler.getNextSerialNo(getSiteId(), serialType);
	}
	public static String getNextSerialNo(String siteId,String serialType){
		String result = null;
		try {
			//1) 取得流水號格式設定
			ThesysSerialFormatVO format = ThesysSerialDAO.getInstance().readSerialFormat(siteId, serialType);
			//取得預設的 format
			if(format==null) format = ThesysSerialDAO.getInstance().readSerialFormat(ThesysSerialFormatVO.SITE_ID_DEFAULT, serialType);
			
			//2) 檢查是否使用日期
			String dateText = "";
			String pattern = format.getDateFormat();
	        if(!"N".equals(pattern)){
	        	
	        	java.util.Date now = new java.util.Date();
	        	if(pattern.indexOf("Y")>=0){ //民國年
	        		String yearPattern =  pattern.substring(0,pattern.lastIndexOf("Y")+1);
	        		Calendar cal = Calendar.getInstance();
	        		cal.setTime(now);
	        		int twYear=cal.get(Calendar.YEAR)-1911;
	        		dateText = String.valueOf(twYear).substring(3-yearPattern.length());
	        		
	        		//get month and day
	        		String datePattern = pattern.substring(pattern.lastIndexOf("Y")+1);
	        		SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
		        	dateText += dateFormat.format(now);
	        	}else{
		        	SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		        	dateText = dateFormat.format(new java.util.Date());
	        	}
	        }
			//2) 取得目前編號
			ThesysSerialNoVO vo = ThesysSerialDAO.getInstance().readSerialNo(siteId, format.getSerialType(), format.getPrefixText(), dateText);
			
			if(vo==null){
				vo = new ThesysSerialNoVO();
				vo.setSiteId(siteId);
				vo.setSerialType(format.getSerialType());
				vo.setPrefixText(format.getPrefixText());
				vo.setDateText(dateText);
				vo.setSerialNumber(1);
			}else{
				vo.setSerialNumber(vo.getSerialNumber()+1);
			}		
			//3)儲存編號
			ThesysSerialDAO.getInstance().write(vo);
			//4)格式化編號字串
			result = formatSerialNo(format,vo);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	
	private static String formatSerialNo(ThesysSerialFormatVO format,ThesysSerialNoVO vo){
		String result = format.getPrefixText()+vo.getDateText();
		
		String numFormatText = "";
		for(int i=1;i<=format.getNumberLength();i++){
			numFormatText += "0"; 
		}
		DecimalFormat dc = new DecimalFormat(numFormatText);
		result += dc.format(vo.getSerialNumber());
		return result;
	}
	
	
	
	
	
}
