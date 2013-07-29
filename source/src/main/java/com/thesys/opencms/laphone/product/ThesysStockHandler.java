package com.thesys.opencms.laphone.product;




import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.order.dao.ThesysOrderDAO;
import com.thesys.opencms.laphone.product.dao.ThesysStockDAO;
import com.thesys.opencms.laphone.product.dao.ThesysStockVO;
import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;


public class ThesysStockHandler extends ThesysLaphoneHandler {
	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(ThesysStockHandler.class);
    
	private String itemId;
	
	
	public ThesysStockHandler(){}
	public ThesysStockHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super(context, req, res);
	}	
	
	public int getStockQuantity(){
		return getStockQuantity(getItemId());
	}
	
	
	/**
	 * 查詢組合商品庫存數量
	 * @param map<String,Integer> 料號,所需該料號數量
	 * @return
	 */
	public int getGroupStockQuantity(Map<String,Integer> map){
		int minQty = 0;
		int i = 0;
		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			int qty = getStockQuantity(entry.getKey())/entry.getValue();
			if(i == 0 || qty < minQty)minQty = qty ;
		    i++;
		}
		return minQty ;
	}
	
	/**
	 * 可銷售數量
	 * @param itemId 料號
	 * @return
	 */
	public int getStockQuantity(String itemId) {
		int result = 0;
		int safetyStock =0;
		String PARAM_ITEM_SAFETY_STOCK ="ITEM_SAFETY_STOCK";//安全庫存量參數名
		try{
			//取得商品餘資料庫中的庫存
			ThesysStockVO vo = ThesysStockDAO.getInstance().read(getSiteId(),itemId);
			if(vo!=null) result = vo.getQuantity();
			//取得未核單特定商品的數量
			int count = ThesysOrderDAO.getInstance().getCountFormUncheckedItem(getSiteId(), itemId);
			//取得系統參數中商品安全庫存數量
			String strSafetyStock = ThesysParamDAO.getInstance().getParam(getSiteId(),PARAM_ITEM_SAFETY_STOCK).getParamVal();
			if(strSafetyStock != null && strSafetyStock.length() != 0)safetyStock = Integer.parseInt(strSafetyStock);
			//可銷售數量 = 資料庫中的庫存 - 未核單特定商品的數量 - 商品安全庫存數量
			result = result - count - safetyStock;
			if(result < 0 ) result = 0 ;
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
	
}
