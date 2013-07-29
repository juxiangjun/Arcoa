package com.thesys.opencms.laphone.cart;

import java.util.Map;
import java.util.HashMap;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;


import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;




public class ThesysCartHandler extends ThesysLaphoneHandler {
	public static final String SESSION_SHOPPING_CART_KEY = "THESYS_CART_MAP";
	
	
	/** The log object for this class. */
	protected static final Log LOG = CmsLog.getLog(ThesysCartHandler.class);
    
	
	public ThesysCartHandler(){}
	public ThesysCartHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super(context, req, res);

	}	
	/**
	 * 新增或更新購物車
	 * @param itemId
	 * @param quantity
	 */
	public boolean add(String itemId,int quantity){
		try{
			Map<String,Integer> map = getItems();
			map.put(itemId, quantity);
			setItems(map);
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}
		return true;
	}
	
	
	/**
	 * 購物車單項取消
	 * @param itemId
	 */
	public boolean delete(String itemId){
		try{			
			Map<String,Integer> map = getItems();
			map.remove(itemId);
			setItems(map);
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}
		return true;
	}
	
	
	/**
	 * 購物車全部取消
	 */
	public boolean deleteAll(){
		try{
			getJspContext().getSession().removeAttribute(SESSION_SHOPPING_CART_KEY);
		}catch(Exception ex){
			LOG.error(ex, ex.fillInStackTrace());
			return false;
		}

		return true;
	}
	public int getCount(){		
		return getItems().size();
	}
	
	/**
	 * get the shopping cart list
	 * @return
	 */
	public Map<String,Integer> getItems(){
		Map<String,Integer> items  = (Map<String,Integer>)getJspContext().getSession().getAttribute(SESSION_SHOPPING_CART_KEY);
		
		if(items==null) items = new HashMap<String,Integer>();
		setItems(items);
		return items;
	}
	private void setItems(Map<String,Integer> items){
		getJspContext().getSession().setAttribute(SESSION_SHOPPING_CART_KEY,items);
	}
	
	
	
	
}
