package com.thesys.opencms.laphone.product;




import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;


import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.product.dao.ThesysRatingDAO;
import com.thesys.opencms.laphone.product.dao.ThesysRatingVO;


public class ThesysRatingHandler extends ThesysLaphoneHandler {
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(ThesysRatingHandler.class);
    
	private String itemId;
	private int ratingStart;
	private int ratingEnd;
	
	public ThesysRatingHandler(){}
	public ThesysRatingHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super(context, req, res);
	}	
	
	public int getRating(){
		return getRating(getItemId());
	}
	public List<String> getRatingItems(){
		return getRatingItems(getRatingStart(),getRatingEnd());
	}
	public List<String> getRatingItems(int start,int end){
		try{
			return ThesysRatingDAO.getInstance().between(getSiteId(), start, end);
			
		}catch(Exception ex){
			LOG.error(ex.fillInStackTrace());
		}
		return null;
	}
	/**
	 * 增加評價
	 * @param itemId
	 * @param rating
	 * @return
	 */
	public boolean add(String itemId,int rating){
		return add(getSiteId(),getUserId(),itemId,rating);
		
	}
	/**
	 * 增加評價
	 * @param siteId
	 * @param userId
	 * @param itemId
	 * @param rating
	 * @return
	 */
	public static boolean add(String siteId,String userId,String itemId,int rating){
		ThesysRatingVO vo = new ThesysRatingVO();
		vo.setSiteId(siteId);
		vo.setItemId(itemId);
		vo.setRating(rating);
		vo.setCreater(userId);
		vo.setCreateDate(new java.util.Date());
		vo.setLastUpdater(userId);
		vo.setLastUpdateDate(new java.util.Date());
		try{
			ThesysRatingDAO.getInstance().insertOrUpdate(vo);
			return true;
		}catch(Exception ex){
			LOG.error(ex.fillInStackTrace());
		}
		return false;
	}
	
	public int getRating(String itemId) {
		int result = 0;
		try{
			ThesysRatingVO vo = ThesysRatingDAO.getInstance().read(getSiteId(),itemId);
			if(vo!=null) result = vo.getRating();
			
		}catch(Exception ex){
			LOG.error(ex.fillInStackTrace());
		}
		return result;
		
	}
	
	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}
	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	/**
	 * @return the ratingStart
	 */
	public int getRatingStart() {
		return ratingStart;
	}
	/**
	 * @param ratingStart the ratingStart to set
	 */
	public void setRatingStart(int ratingStart) {
		this.ratingStart = ratingStart;
	}
	/**
	 * @return the ratingEnd
	 */
	public int getRatingEnd() {
		return ratingEnd;
	}
	/**
	 * @param ratingEnd the ratingEnd to set
	 */
	public void setRatingEnd(int ratingEnd) {
		this.ratingEnd = ratingEnd;
	}
}
