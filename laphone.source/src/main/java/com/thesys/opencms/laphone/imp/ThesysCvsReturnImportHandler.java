package com.thesys.opencms.laphone.imp;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.opencms.file.CmsObject;
import org.opencms.json.JSONObject;

import com.thesys.opencms.laphone.order.dao.ThesysOrderDAO;
/**
 * 便利達康-大物流驗退檔(F07)
 */
public class ThesysCvsReturnImportHandler extends ThesysAbstractXmlImportHandler {
	
	private String CONTENT_TAG = "F07CONTENT"; //有訂單資料的tag
	private final String JSON_RET_M = "RET_M";//退貨原因
	private final String JSON_ODNO = "ODNO";//EC 訂單號碼
	private final String JSON_RTDCDT = "RTDCDT";//大物流實際驗退日
	private final String JSON_FRTDCDT = "FRTDCDT";//結帳基準日
	
	public ThesysCvsReturnImportHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super.init(context, req, res);		

	}    
    public ThesysCvsReturnImportHandler(CmsObject cmso){
    	super.init(cmso);
    }    
    
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	
	@Override
	public void beforeImport()  throws Exception{}

	@Override
	public void afterImport()  throws Exception {

	}

	@Override
	public void importRow(JSONObject jsonObj) throws Exception {
		String returnCode = jsonObj.opt(JSON_RET_M) == null?"":(String)jsonObj.get(JSON_RET_M);
		String orderId = jsonObj.opt(JSON_ODNO) == null?null:""+ Long.parseLong((String)jsonObj.get(JSON_ODNO));//trim掉前面的0
		Date returnDate = jsonObj.opt(JSON_RTDCDT) == null?null:dateFormat.parse((String)jsonObj.get(JSON_RTDCDT));
		Date basicDate = jsonObj.opt(JSON_FRTDCDT) == null?null:dateFormat.parse((String)jsonObj.get(JSON_FRTDCDT));
		ThesysOrderDAO.getInstance().updateCvsReturn(getSiteId(),orderId,returnCode,returnDate,basicDate);
	}

	@Override
	public String[] getColumnNames() {
		return new String[]{
				JSON_RET_M ,//退貨原因
				JSON_ODNO  ,//EC 訂單號碼
				JSON_RTDCDT ,//大物流實際驗退日
				JSON_FRTDCDT ,//結帳基準日
		};
	}

	@Override
	public String getRowName() {
		return CONTENT_TAG;
	}

}
