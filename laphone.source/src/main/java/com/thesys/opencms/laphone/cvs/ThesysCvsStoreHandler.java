package com.thesys.opencms.laphone.cvs;




import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.cvs.dao.ThesysCvsStoreDAO;
import com.thesys.opencms.laphone.cvs.dao.ThesysCvsStoreVO;

public class ThesysCvsStoreHandler extends ThesysLaphoneHandler {
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(ThesysCvsStoreHandler.class);
    
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    
    private String storeType;
    private String zipCode;
	private String storeNo;
	
	
	@Override
	public String getSiteId() {
		return super.getSiteId();
//		return "/sites/laphone";
	}
	
	public ThesysCvsStoreHandler(){}
	public ThesysCvsStoreHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super(context, req, res);
	}	
	
	public List<ThesysCvsStoreVO> getStores(){
	
		return getStores(getStoreType(),getZipCode());
		
	}
	public List<ThesysCvsStoreVO> getStores(String storeType,String zipCode){
		
		try{
			return ThesysCvsStoreDAO.getInstance().list(getSiteId(),storeType,zipCode);
			
		}catch(Exception ex){
			LOG.error(ex.fillInStackTrace());
		}
		return null;
	}
	
	public List<ThesysCvsStoreVO> getPageList(){
		List<ThesysCvsStoreVO> result = new ArrayList<ThesysCvsStoreVO>();
		try{
			result = ThesysCvsStoreDAO.getInstance().listByPage(getSiteId(), getPageSize(), getPageIndex());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	
	public int getCount(){
		int result =0;
		try{
			result = ThesysCvsStoreDAO.getInstance().count(getSiteId());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}	
	
	public ThesysCvsStoreVO getSelectedStore(){
		return getSelectedStore(storeNo);
	}
	
	public ThesysCvsStoreVO getSelectedStore(String storeNo){
		try{
			return ThesysCvsStoreDAO.getInstance().read(getSiteId(),storeNo);
		}catch(Exception ex){
			LOG.error(ex.fillInStackTrace());
		}
		return null;
	}
	
//	public  int add(String storeNo,String storeName,String telphone
//			, String city,String country,String address,String zipcode,String dcroNo,String strStartDate
//			, String strEndDate,String creater){
//		int flag = 0;
//		Date startDate = null;
//		Date endDate = null;
//		try {
//			if (!strStartDate.equals("00000000"))
//				startDate = sdf.parse(strStartDate);
//			if (!strEndDate.equals("00000000"))
//				endDate = sdf.parse(strEndDate);
//			ThesysCvsStoreVO vo = new ThesysCvsStoreVO();
//			vo.setSiteId(getSiteId());
//			vo.setStoreNo(storeNo);
//			vo.setStoreType(storeNo.substring(0, 1));
//			vo.setStoreName(storeName);
//			vo.setTelphone(telphone);
//			vo.setCity(city);
//			vo.setCountry(country);
//			vo.setAddress(address);
//			vo.setZipCode(zipcode);
//			vo.setDcroNo(dcroNo);
//			vo.setStartDate(startDate);
//			vo.setEndDate(endDate);
//			vo.setCreater(creater);
//			vo.setCreateDate(new Date());
//			flag = ThesysCvsStoreDAO.getInstance().insert(vo);
//		} catch (Exception ex) {
//			LOG.error(ex.fillInStackTrace());
//		} 
//
//		return flag;
//	}
//	
//	public int clearData(){
//		int flag = 0;
//		try {
//			flag = ThesysCvsStoreDAO.getInstance().clearTable(getSiteId());
//		} catch (SQLException ex) {
//			ex.printStackTrace();
//			LOG.error(ex.fillInStackTrace());
//		}
//		return flag;
//	}

	
	/**
	 * @return the storeType
	 */
	public String getStoreType() {
		return storeType;
	}
	/**
	 * @param storeType the storeType to set
	 */
	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}
	/**
	 * @return the zipCode
	 */
	public String getZipCode() {
		return zipCode;
	}
	/**
	 * @param zipCode the zipCode to set
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	/**
	 * @return the storeCode
	 */
	public String getStoreNo() {
		return storeNo;
	}
	/**
	 * @param storeCode the storeCode to set
	 */
	public void setStoreNo(String storeNo) {
		this.storeNo = storeNo;
	}
	
	
}
