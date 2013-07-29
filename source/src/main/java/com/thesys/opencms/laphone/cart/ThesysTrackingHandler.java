package com.thesys.opencms.laphone.cart;


import java.util.ArrayList;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;


import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.cart.dao.ThesysTrackingDAO;
import com.thesys.opencms.laphone.cart.dao.ThesysTrackingVO;


public class ThesysTrackingHandler extends ThesysLaphoneHandler {
	protected static final Log LOG = CmsLog.getLog(ThesysTrackingHandler.class);
	
	
    
	public ThesysTrackingHandler(){}
	public ThesysTrackingHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super(context, req, res);
	}
	public List<ThesysTrackingVO> getPageList(){
		List<ThesysTrackingVO> result = new ArrayList<ThesysTrackingVO>();
		try{
			result = ThesysTrackingDAO.getInstance().listByPage(getSiteId(), getMemberId(), getPageSize(), getPageIndex());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}	
	public int getCount(){
		int result =0;
		try{
			result = ThesysTrackingDAO.getInstance().count(getSiteId(), getMemberId());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}	
	
	public boolean add(String itemId){
		try{
			ThesysTrackingVO vo = ThesysTrackingVO.getInstance(getSiteId(), getMemberId(), itemId);
			ThesysTrackingDAO.getInstance().insert(vo);
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}
		return true;
	}
	public boolean delete(String itemId){
		try{
			ThesysTrackingDAO.getInstance().delete(getSiteId(), getMemberId(), itemId);
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}
		return true;
	}
	
	
	public boolean deleteAll(){
		try{
			ThesysTrackingDAO.getInstance().deleteAll(getSiteId(), getMemberId());
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}
		return true;
	}
	
	
	
	
	
}
