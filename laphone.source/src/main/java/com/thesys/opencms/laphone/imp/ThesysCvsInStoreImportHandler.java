package com.thesys.opencms.laphone.imp;
/**
 * 商品已到門市處理
 */
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.opencms.file.CmsObject;
import org.opencms.json.JSONObject;
import com.thesys.opencms.laphone.order.dao.ThesysOrderDAO;
import com.thesys.opencms.laphone.order.dao.ThesysOrderVO;
import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;
import com.thesys.opencms.laphone.util.ThesysSendMsgHandler;

public class ThesysCvsInStoreImportHandler extends ThesysAbstractXmlImportHandler {
	private String CONTENT_TAG = "F04CONTENT"; //有訂單資料的tag
	private final String JSON_STNO = "STNO";//取貨門市編號
	private final String JSON_ODNO = "ODNO";//EC 訂單號碼
	private final String JSON_DCSTDT = "DCSTDT";//實際進店日期
	private SimpleDateFormat srcDateFormat = new SimpleDateFormat("yyyyMMdd"); // 資料中日期的格式
	private SimpleDateFormat mailDateFormat = new SimpleDateFormat("yyyy/MM/dd"); // 信件日期的格式
	private String host = "";
	private ThesysSendMsgHandler msgHandler;
	
	public ThesysCvsInStoreImportHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super.init(context, req, res);		

	}    
    public ThesysCvsInStoreImportHandler(CmsObject cmso){
    	super.init(cmso);
    }
//    
//    public static void main(String[] args){
//		try{
//			ThesysCvsIntoStoreImportHandler handler = new ThesysCvsIntoStoreImportHandler(null);
//			handler.updateData(new FileInputStream("E:\\F04173CVS20121225.xml"));
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
//	}
    public void updateData(InputStream stream) throws Exception{
    	this.importFile(stream);
    }
	@Override
	public void beforeImport() throws Exception {
		msgHandler = new ThesysSendMsgHandler(getCmsObject());
		host = ThesysParamDAO.getInstance().getParam(getSiteId(),"WEB_URL").getParamVal();
	}
	@Override
	public void afterImport() throws Exception {
		
	}
	@Override
	public void importRow(JSONObject jsonObj) throws Exception {
		String orderId = jsonObj.opt(JSON_ODNO) == null?null:""+ Long.parseLong((String)jsonObj.get(JSON_ODNO));//trim掉前面的0
		Date arrivalDate = jsonObj.opt(JSON_DCSTDT) == null?null:srcDateFormat.parse((String)jsonObj.get(JSON_DCSTDT));
		
		if(ThesysOrderDAO.getInstance().updateInCvsStore(getSiteId(), orderId, arrivalDate) == 1){
			ThesysOrderVO orderVO = ThesysOrderDAO.getInstance().read(getSiteId(), orderId);
			//發送貨到超商通知   				
			JSONObject json = new JSONObject();
			json.put("host",host);
			json.put("orderId", orderId);
			String storeType = "";
			if("L".equals(orderVO.getShipType())) storeType = "萊爾富超商";
			if("K".equals(orderVO.getShipType())) storeType = "OK超商";
			if("F".equals(orderVO.getShipType())) storeType = "全家超商";
			json.put("storeType", storeType); //超商名稱
			json.put("sapShipNo", orderVO.getSapShipNo()); //取貨單號
			json.put("storeName", orderVO.getCvsStoreName()); //門市名稱
			json.put("storeAddress", orderVO.getCvsStoreAddress()); //門市地址
			json.put("startDate",mailDateFormat.format(arrivalDate));
			//便利商店可取貨時間為商品到店時間+6個日曆天
			Calendar cal = Calendar.getInstance();
			cal.setTime(arrivalDate);
			cal.add(Calendar.DATE, 6);
			json.put("endDate",mailDateFormat.format(cal.getTime()));
			json.put("receiver",orderVO.getMaskReceiver());
			
			msgHandler.setMsgName("orderInStore");
			msgHandler.setJsonObj(jsonObj);		
	 		msgHandler.setMemberId(orderVO.getMemberId());
			msgHandler.setCellphone(orderVO.getMemberMobile()); 
			msgHandler.setEmail(orderVO.getMemberEmail()); 
			msgHandler.sendMsg(); 
		}else{			
			throw new Exception("更新失敗");
		}
	}
	@Override
	public String[] getColumnNames() {
		return new String[]{
				JSON_STNO ,//原始店編
				JSON_ODNO ,//EC 訂單號碼
				JSON_DCSTDT //實際進店日期
		};
	}
	@Override
	public String getRowName() {
		return CONTENT_TAG;
	}
    
    
}
