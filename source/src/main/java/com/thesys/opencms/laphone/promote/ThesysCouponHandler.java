package com.thesys.opencms.laphone.promote;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.promote.dao.ThesysCouponDAO;
import com.thesys.opencms.laphone.promote.dao.ThesysCouponVO;
import com.thesys.opencms.laphone.system.ThesysSerialHandler;

public class ThesysCouponHandler extends ThesysLaphoneHandler {
	protected static final Log LOG = CmsLog.getLog(ThesysCouponHandler.class);
	ThesysSerialHandler handler = null;
	
	//add 用 start
	private String couponCode;
    private Date addstrDate = new Date();  //折價券開始日期預設當天
    private int deadlineDays = 30;	//折建券可使用的天數(預設30天);
    private int hh = 0; //折價券於當天幾點可使用
	private int dd = 0; //折價券於當天幾點幾分可使用
	//add 用 end    
    
	public ThesysCouponHandler(){}
	public ThesysCouponHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super(context, req, res);
		 handler = new com.thesys.opencms.laphone.system.ThesysSerialHandler(context,req,res);
	}
	
	@Override
	public String getSiteId() {
//		return "/sites/laphone";
		return super.getSiteId();
	}
	
	public boolean add(String memberId, int cpAmt,int discountRate,String srcOrdId) {
		ThesysCouponVO vo = new ThesysCouponVO();
		Date now = new Date();
//		String cpCode = "A"+ Integer.parseInt((String.valueOf(now.getTime())).substring(4));
		String cpCode = handler.getNextSerialNo("COUPON");
		vo.setSiteId(getSiteId());
		vo.setCouponCode(cpCode); 
    	vo.setMemberId(memberId); 	
    	vo.setCouponAmount(cpAmt); 	
    	vo.setDiscountRate(discountRate); 
    	vo.setStartDate(getDate(addstrDate)); //取得只有日期的Date
    	vo.setEndDate(getDate(addDate(addstrDate, deadlineDays)));    	 	
    	vo.setSourceOrderId(srcOrdId);
    	vo.setCouponDate(now);
		try {
			 ThesysCouponDAO.getInstance().add(vo);
		} catch (SQLException ex) {
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}
		return true;
	}
	
	public ThesysCouponVO getSelectedCoupon(){
		ThesysCouponVO result = null;
		try{
			result = ThesysCouponDAO.getInstance().read(getSiteId(), getCouponCode());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	
	public List<ThesysCouponVO> getUsableList(){
		List<ThesysCouponVO> result = new ArrayList<ThesysCouponVO>();
		try{
			result = ThesysCouponDAO.getInstance().getUsableList(getSiteId(), getMemberId());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	public int getWeekExpireCount(){
		int result =0;
		try{
			//取得一週後
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.add(Calendar.DATE, 8);
			Date endAfter = cal.getTime();
			result = ThesysCouponDAO.getInstance().getWeekExpireCount(getSiteId(), getMemberId(),endAfter);
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}	
	public int getUsableCount(){
		int result =0;
		try{
			result = ThesysCouponDAO.getInstance().getUsableCount(getSiteId(), getMemberId());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}	
	public List<ThesysCouponVO> getPageList(){
		List<ThesysCouponVO> result = new ArrayList<ThesysCouponVO>();
		try{
			result = ThesysCouponDAO.getInstance().listByPage(getSiteId(), getMemberId(), getPageSize(), getPageIndex());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}	
	public int getCount(){
		int result =0;
		try{
			result = ThesysCouponDAO.getInstance().count(getSiteId(), getMemberId());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	

	
	//取得只有日期的Date
	private Date  getDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH),hh,dd,0);
		return cal.getTime();
	}
	
	// 加天數
	private java.util.Date addDate(java.util.Date now, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
	

	
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}

	public void setAddstrDate(Date addstrDate) {
		this.addstrDate = addstrDate;
	}
    public void setHh(int hh) {
		this.hh = hh;
	}
	public void setDd(int dd) {
		this.dd = dd;
	}
	public void setDeadlineDays(int deadlineDays) {
		this.deadlineDays = deadlineDays;
	}
	
	
	
	
}
