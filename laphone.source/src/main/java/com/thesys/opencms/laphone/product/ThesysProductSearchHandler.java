package com.thesys.opencms.laphone.product;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;


import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.product.dao.ThesysProductSearchDAO;
import com.thesys.opencms.laphone.product.dao.ThesysProductSearchVO;


public class ThesysProductSearchHandler extends ThesysLaphoneHandler {
	
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(ThesysProductSearchHandler.class);
    
    private String[] categoryIds;  
    private String[] colors;  
    private int priceStart;
    private int priceEnd;    
    private int ratingStart;
    private int ratingEnd;
    private String styles;
    private String keyword;
    private int sortType = 0;
	private String itemId ;
    
	
	/**
	 * @return the sortType
	 */
	public int getSortType() {
		return sortType;
	}
	/**
	 * @param sortType the sortType to set
	 */
	public void setSortType(int sortType) {
		this.sortType = sortType;
	}
	public ThesysProductSearchHandler(){}
	public ThesysProductSearchHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super(context, req, res);
	}
	/**
	 * 查詢筆數
	 * @return
	 */
	public int getCount(){
		int result =0;
		try{
			result = ThesysProductSearchDAO.getInstance().count(getSiteId(), getCategoryIds(), getColors(), getPriceStart(), getPriceEnd(), getRatingStart(), getRatingEnd(), getStyles(), getKeyword());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	public List<String> getPageList(){
		List<String> result =new ArrayList<String>();
		try{
			result = ThesysProductSearchDAO.getInstance().listByPage(getSiteId(),getPageSize(),getPageIndex(),getSortType(), getCategoryIds(), getColors(), getPriceStart(), getPriceEnd(), getRatingStart(), getRatingEnd(), getStyles(), getKeyword());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	/**
	 * 增加可搜尋的
	 */
	public void add(ThesysProductSearchVO vo){
		add(getSiteId(), vo.getItemId(),vo.getCategoryId(), vo.getItemName(), vo.getSpecialPrice(), vo.getColors(), vo.getStyles(), vo.getStartDate(), vo.getEndDate(), vo.isOnlineFlag(),vo.isGroupFlag());
	}
	/**
	 * 增加搜尋的關鍵字
	 * @param itemId
	 * @param rating
	 * @return
	 */
	public static boolean add(String siteId,String itemId,String categoryId,String itemName,int specialPrice,String colors,
						String styles,long startDate,long endDate,boolean onlineFlag,boolean groupFlag){
		try{
			ThesysProductSearchDAO.getInstance().insertOrUpdate(siteId, itemId,categoryId, itemName, specialPrice, colors, styles, startDate, endDate, onlineFlag,groupFlag);
			return true;
		}catch(Exception ex){
			LOG.error(ex.fillInStackTrace());
		}
		return false;
		
	}
	
	/**
	 * 更新是否上線的狀態
	 * @param itemId
	 * @param onlineFlag
	 * @return
	 */
//	public boolean updateOnlineStatus(String itemId,boolean onlineFlag){
//		try{
//			ThesysProductSearchDAO.getInstance().updateOnlineStatus(getSiteId(), itemId ,onlineFlag);
//			return true;
//		}catch(Exception ex){
//			LOG.error(ex.fillInStackTrace());
//		}
//		return false;
//	}
	
	/**
	 * 查詢此商品是否上線
	 * @return
	 */
	public boolean isOnline(){
		return isOnline(itemId);
	}
	
	/**
	 * 查詢此商品是否上線
	 * @param itemId
	 * @return
	 */
	public boolean isOnline(String itemId){
		try{
			return ThesysProductSearchDAO.getInstance().isOnline(getSiteId(), itemId );
		}catch(Exception ex){
			LOG.error(ex.fillInStackTrace());
		}
		return false;
	}
		
	/**
	 * @return the categoryIds
	 */
	public String[] getCategoryIds() {
		return categoryIds;
	}
	/**
	 * @param categoryIds the categoryIds to set
	 */
	public void setCategoryIds(String[] categoryIds) {
		this.categoryIds = categoryIds;
	}
	/**
	 * @return the colors
	 */
	public String[] getColors() {
		return colors;
	}
	/**
	 * @param colors the colors to set
	 */
	public void setColors(String[] colors) {
		this.colors = colors;
	}
	/**
	 * @return the priceStart
	 */
	public int getPriceStart() {
		return priceStart;
	}
	/**
	 * @param priceStart the priceStart to set
	 */
	public void setPriceStart(int priceStart) {
		this.priceStart = priceStart;
	}
	/**
	 * @return the priceEnd
	 */
	public int getPriceEnd() {
		return priceEnd;
	}
	/**
	 * @param priceEnd the priceEnd to set
	 */
	public void setPriceEnd(int priceEnd) {
		this.priceEnd = priceEnd;
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
	/**
	 * @return the styles
	 */
	public String getStyles() {
		return styles;
	}
	/**
	 * @param styles the styles to set
	 */
	public void setStyles(String styles) {
		this.styles = styles;
	}
	/**
	 * @return the keyword
	 */
	public String getKeyword() {
		return keyword;
	}
	/**
	 * @param keyword the keyword to set
	 */
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	
	
}
