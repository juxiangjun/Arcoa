package com.thesys.opencms.laphone.imp;
/**
 * 便利達康-取貨完成檔(F05)
 */
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.opencms.file.CmsObject;
import org.opencms.json.JSONObject;

import com.thesys.opencms.laphone.order.dao.ThesysOrderDAO;

public class ThesysCvsReceivedImportHandler extends ThesysAbstractXmlImportHandler {
	
	private String CONTENT_TAG = "F05CONTENT"; //有訂單資料的tag
	private final String JSON_BC1 = "BC1";//第一段條碼
	private final String JSON_BC2 = "BC2";//第二段條碼
//	private final String JSON_STNO = "STNO";//取貨門市編號
	private final String JSON_RTDT = "RTDT";//實際取貨繳費日期
	private final String JSON_TKDT = "TKDT";//結帳基準日期
	
	public ThesysCvsReceivedImportHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super.init(context, req, res);		

	}    
    public ThesysCvsReceivedImportHandler(CmsObject cmso){
    	super.init(cmso);
    }    
    
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	
	@Override
	public void beforeImport()  throws Exception{}

	@Override
	public void afterImport()  throws Exception {}

	@Override
	public void importRow(JSONObject jsonObj) throws Exception {
		String bc1 = jsonObj.opt(JSON_BC1) == null?"000000000":(String)jsonObj.get(JSON_BC1);
		String bc2 = jsonObj.opt(JSON_BC2) == null?"0000000000000000":(String)jsonObj.get(JSON_BC2);
		Date receivedDate = jsonObj.opt(JSON_RTDT) == null?null:dateFormat.parse((String)jsonObj.get(JSON_RTDT));
		Date basicDate = jsonObj.opt(JSON_TKDT) == null?null:dateFormat.parse((String)jsonObj.get(JSON_TKDT));
		String orderId = Integer.parseInt(bc1.substring(3,6))+bc2.substring(0,8);//trim掉前面的0
		ThesysOrderDAO.getInstance().updateCvsReceived(getSiteId(),orderId,receivedDate,basicDate);
	}


	@Override
	public String[] getColumnNames() {
		return new String[]{
				JSON_BC1 ,//第一段條碼
				JSON_BC2  ,//第二段條碼
//				JSON_STNO ,//取貨門市編號
				JSON_RTDT ,//實際取貨繳費日期
				JSON_TKDT ,//結帳基準日期
		};
	}

	@Override
	public String getRowName() {
		return CONTENT_TAG;
	}

}
