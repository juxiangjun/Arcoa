package com.thesys.opencms.laphone.imp;
/**
 * 匯入F11訂單檔回覆檔
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.opencms.file.CmsObject;
import org.opencms.json.JSONObject;

import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;
import com.thesys.opencms.mail.ThesysMailHandler;

public class ThesysCvsConfirmImportHandler extends ThesysAbstractXmlImportHandler {
	
	private String CONTENT_TAG = "ERR_ORDER"; //有錯誤訂單資料的tag
	private final String JSON_ERRCODE = "ERRCODE"; //錯誤代碼
	private final String JSON_ERRDESC = "ERRDESC";  //錯誤說明
	private final String JSON_ODNO = "ODNO";  //EC 訂單號碼
	
	private final String PARAM_ADMIN_EMAIL = "ADMIN_EMAIL"; //發送  email 參數名
	private final String PARAM_CS_EMAIL = "CS_EMAIL";//接收 email 參數名
	
	public ThesysCvsConfirmImportHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super.init(context, req, res);		

	}    
    public ThesysCvsConfirmImportHandler(CmsObject cmso){
    	super.init(cmso);
    }    
    
//	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
//	
//	public static void main(String[] args){
//		try{
//			ThesysCvsStoreImportHandler handler = new ThesysCvsStoreImportHandler(null);
//			handler.importFile(new FileInputStream("E:\\F01ALLCVS20121224.xml"));
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//	}
	@Override
	public void beforeImport()  throws Exception{

	}

	@Override
	public void afterImport()  throws Exception {

	}

	@Override
	public void importRow(JSONObject jsonObj) throws Exception {
		String odno = jsonObj.opt(JSON_ODNO) == null?null:""+ Long.parseLong((String)jsonObj.get(JSON_ODNO));//trim掉前面的0
		int errCode = jsonObj.opt(JSON_ERRCODE) == null?0:Integer.parseInt((String)jsonObj.get(JSON_ERRCODE));
		String errDesc = jsonObj.opt(JSON_ERRDESC) == null?"":(String)jsonObj.get(JSON_ERRDESC);
//		update(odno,errCode,errDesc); //暫時不存DB
		sendMail(odno,errCode,errDesc);
		

	}

	@Override
	public String[] getColumnNames() {
		return new String[]{
				JSON_ERRCODE,//錯誤代碼
				JSON_ERRDESC,//錯誤說明
				JSON_ODNO//EC 訂單號碼
				
		};
	}

	@Override
	public String getRowName() {
		return CONTENT_TAG;
	}
	
	public void sendMail(String odno,int errCode,String errDesc) throws Exception{
		
		String sernderMail = ThesysParamDAO.getInstance().getParam(getSiteId(), PARAM_ADMIN_EMAIL).getParamVal();
		String email = ThesysParamDAO.getInstance().getParam(getSiteId(), PARAM_CS_EMAIL).getParamVal();
		String configSubject = "錯誤訂單回報(訂單編號:"+odno+")";
		String configBody = "錯誤訂單編號:"+odno+"，錯誤原因:"+(errCode == 1?"踢退":"異常")+"，錯誤說明:"+errDesc;

		ThesysMailHandler handler = new ThesysMailHandler(getCmsObject());
		handler.setConfigFromMail(sernderMail);
		handler.setConfigBody(configBody);
		handler.setConfigSubject(configSubject);
		handler.addMailTo(email);
		handler.sendHtmlMail();
	}
	
	
	
}
