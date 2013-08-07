package com.thesys.opencms.laphone.job.product;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.opencms.file.CmsFile;
import org.opencms.file.CmsObject;
import org.opencms.file.CmsResourceFilter;
import org.opencms.json.JSONObject;
import org.opencms.main.CmsLog;
import org.opencms.search.CmsSearch;
import org.opencms.search.CmsSearchParameters;
import org.opencms.search.CmsSearchResult;
import org.opencms.xml.content.CmsXmlContent;
import org.opencms.xml.content.CmsXmlContentFactory;

import com.thesys.opencms.laphone.job.ThesysAbstractJob;
import com.thesys.opencms.laphone.system.ThesysParamHandler;
import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;
import com.thesys.opencms.laphone.util.ThesysSendMsgHandler;

/**
 * 商品異常通知
 */
public class ThesysAbnormalNoticeJob extends ThesysAbstractJob {

	protected static final Log LOG = CmsLog.getLog(ThesysAbnormalNoticeJob.class);
	
	
	private int deadline = 7; //超過此天數但未發佈的XML須發mail
	

	@Override
	public String launch(CmsObject cmso, Map parameters) throws Exception {
		setCmsObject(cmso);	
		initDeadline();
		sendOverdueReleaseMail();
		return null;
	}
	
	private void initDeadline(){
		ThesysParamHandler paramhandler = new ThesysParamHandler();
		try {
			deadline = Integer.valueOf(paramhandler.getParamVal("/sites/laphone", "PRODUCT_WARN_THRESHOLD").split(";")[1]);
		} catch (Exception e) {
			LOG.error("Get Deadline failed");
		}
	}
	
	/**
	 * 寄出逾期未發佈的xml
	 */
	public void sendOverdueReleaseMail(){
		try {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -deadline);
			String queryStr = " (laphone.check-date:[1 TO "+cal.getTimeInMillis()+"])";
			System.out.println(queryStr);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Locale locale = cmsObject.getRequestContext().getLocale();
			
			List<CmsSearchResult> searchResult =searchXml("OFFLINE_PRODUCT_INDEX",queryStr);
			
			String msg = "商品逾期未發佈通知<table border='1'><tr><td>商品代號</td><td>商品名稱</td><td>匯入日期</td></tr>";
			if(searchResult.size() > 0){
				for(CmsSearchResult entry:searchResult){
					String xmlPath = entry.getPath().replace(getSiteId(), "");
					boolean isExist = cmsObject.existsResource(xmlPath,CmsResourceFilter.ALL);
					if(isExist){
						CmsFile xmlFile = cmsObject.readFile(xmlPath, CmsResourceFilter.ALL);
						CmsXmlContent xmlContent = CmsXmlContentFactory.unmarshal(cmsObject, xmlFile);
						String itemId = xmlContent.getValue("SapProductCode",locale ).getStringValue(cmsObject);
						String productName = xmlContent.getValue("ProductName",locale ).getStringValue(cmsObject);
						String excelDate = (String)cmsObject.readPropertyObject(xmlPath, "laphone.excel-date", false).getValue();
						if(excelDate != null && !excelDate.equals(""))
							excelDate = sdf.format(Long.parseLong(excelDate));
						msg += "<tr><td>"+itemId+"</td><td>"+productName+"</td><td>"+excelDate+"</td></tr>";
					}
				}
				msg += "</table>";
				String email = ThesysParamDAO.getInstance().getParam(getSiteId(),"PRODUCT_EMAIL").getParamVal();
				JSONObject jsonObj = new JSONObject();
				jsonObj.put("title", "商品逾期未發佈通知");
				jsonObj.put("content",msg);
				sendEmail("orderDataList" ,jsonObj,email);
			}
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
			ex.printStackTrace();
		}
	}
	
	/**
	 * 使用CmsSearch搜尋符合query條件的資料
	 * @param index
	 * @param query
	 * @return
	 * @throws Exception
	 */
	public  List<CmsSearchResult> searchXml(String index,String query) throws Exception {
		CmsSearch search = new CmsSearch();
		search.init(cmsObject );
			search.setIndex(index);
			search.setMatchesPerPage(10000);
			CmsSearchParameters params = search.getParameters();
			params.setQuery(query);
			search.setParameters(params);
		List<CmsSearchResult> searchResult = search.getSearchResult();
		
		return searchResult;
	}

	/**
	 * 發送email
	 * @param msgName
	 * @param jsonObj 須送title及content參數
	 * @param email
	 */
	public void sendEmail(String msgName ,JSONObject jsonObj,String email){
		ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(cmsObject);
		 msgHandler.setMsgName(msgName);
		 msgHandler.setJsonObj(jsonObj);
		 msgHandler.setEmail(email);
		 msgHandler.sendMsg();
	}

}
