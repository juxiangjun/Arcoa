package com.thesys.opencms.laphone.imp;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.opencms.file.CmsObject;
import org.opencms.json.JSONObject;

import com.thesys.opencms.laphone.cvs.dao.ThesysCvsStoreDAO;
import com.thesys.opencms.laphone.cvs.dao.ThesysCvsStoreVO;

public class ThesysCvsStoreImportHandler extends ThesysAbstractXmlImportHandler {
	
	private String CONTENT_TAG = "F01CONTENT"; //有訂單資料的tag
	private final String JSON_STNO = "STNO";//原始店編
	private final String JSON_STNM = "STNM";//取貨店舖名稱
	private final String JSON_STTEL = "STTEL";//店舖電話
	private final String JSON_STCITY = "STCITY";//店舖所在縣、市
	private final String JSON_STCNTRY = "STCNTRY";//店舖所在鄉、鎮、區
	private final String JSON_STADR = "STADR";//店舖所在地址
	private final String JSON_ZIPCD = "ZIPCD";//店舖郵遞區號
	private final String JSON_DCRONO = "DCRONO";//路線路順
	private final String JSON_SDATE = "SDATE";//整修起日期
	private final String JSON_EDATE = "EDATE";//整修迄日期
	
	public ThesysCvsStoreImportHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super.init(context, req, res);		

	}    
    public ThesysCvsStoreImportHandler(CmsObject cmso){
    	super.init(cmso);
    }    
    
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
	
	public static void main(String[] args){
		try{
			ThesysCvsStoreImportHandler handler = new ThesysCvsStoreImportHandler(null);
			handler.importFile(new FileInputStream("E:\\F01ALLCVS20121224.xml"));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	@Override
	public void beforeImport()  throws Exception{
		//清除所有資料
		ThesysCvsStoreDAO.getInstance().clearTable(getSiteId());

	}

	@Override
	public void afterImport()  throws Exception {

	}

	@Override
	public void importRow(JSONObject jsonObj) throws Exception {
		System.out.println(jsonObj.toString());
		String sDate = (String)jsonObj.get(JSON_SDATE);
		if("00000000".equals(sDate)) sDate = null;
		
		String eDate = (String)jsonObj.get(JSON_EDATE);
		if("00000000".equals(eDate)) eDate = null;
		
		String storeNo = jsonObj.opt(JSON_STNO) == null?null:(String)jsonObj.get(JSON_STNO);
		String storeName = jsonObj.opt(JSON_STNM) == null?null:(String)jsonObj.get(JSON_STNM);
		String telphone = jsonObj.opt(JSON_STTEL) == null?null:(String)jsonObj.get(JSON_STTEL);
		String city = jsonObj.opt(JSON_STCITY) == null?null:(String)jsonObj.get(JSON_STCITY);
		String country = jsonObj.opt(JSON_STCNTRY) == null?null:(String)jsonObj.get(JSON_STCNTRY);
		String address = jsonObj.opt(JSON_STADR) == null?null:(String)jsonObj.get(JSON_STADR);
		String zipcode = jsonObj.opt(JSON_ZIPCD) == null?null:(String)jsonObj.get(JSON_ZIPCD);
		String dcroNo = jsonObj.opt(JSON_DCRONO) == null?null:(String)jsonObj.get(JSON_DCRONO);
		Date startDate = sDate == null?null:dateFormat.parse(sDate);
		Date endDate = eDate == null?null:dateFormat.parse(eDate);
		
		ThesysCvsStoreVO vo = new ThesysCvsStoreVO();
		vo.setSiteId(getSiteId());
		vo.setStoreNo(storeNo);
		vo.setStoreType(storeNo.substring(0, 1));
		vo.setStoreName(storeName);
		vo.setTelphone(telphone);
		vo.setCity(city);
		vo.setCountry(country);
		vo.setAddress(address);
		vo.setZipCode(zipcode);
		vo.setDcroNo(dcroNo);
		vo.setStartDate(startDate);
		vo.setEndDate(endDate);
		vo.setCreater(getUserId());
		vo.setCreateDate(new Date());
		if(ThesysCvsStoreDAO.getInstance().insert(vo)!=1){
			throw new Exception("寫入失敗");
		}
		

	}

	@Override
	public String[] getColumnNames() {
		return new String[]{
				JSON_STNO ,//原始店編
				JSON_STNM  ,//取貨店舖名稱
				JSON_STTEL ,//店舖電話
				JSON_STCITY ,//店舖所在縣、市
				JSON_STCNTRY ,//店舖所在鄉、鎮、區
				JSON_STADR ,//店舖所在地址
				JSON_ZIPCD ,//店舖郵遞區號
				JSON_DCRONO ,//路線路順
				JSON_SDATE ,//整修起日期
				JSON_EDATE//整修迄日期
				
		};
	}

	@Override
	public String getRowName() {
		return CONTENT_TAG;
	}

}
