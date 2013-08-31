package com.fol.opencms.laphone.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.jsp.CmsJspActionElement;
import org.opencms.jsp.CmsJspXmlContentBean;
import org.opencms.jsp.I_CmsXmlContentContainer;
import org.opencms.main.CmsLog;
import org.opencms.search.CmsSearch;
import org.opencms.search.CmsSearchParameters;
import org.opencms.search.CmsSearchResult;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.order.dao.ThesysOrderDAO;
import com.thesys.opencms.laphone.product.dao.ThesysStockDAO;
import com.thesys.opencms.laphone.product.dao.ThesysStockVO;
import com.thesys.opencms.laphone.system.dao.ThesysParamDAO;


public class FolStockHandler extends ThesysLaphoneHandler {
    /** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(FolStockHandler.class);
    
    private String itemId;
    private static CmsSearch cmsSearch;
    
    public FolStockHandler(){}
    
    public FolStockHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
        super(context, req, res);
    }
    
    
    private static CmsSearch getCmsSearch(CmsJspActionElement cms ) {
        LOG.debug("get cms search...");
        if (cmsSearch == null) {
            LOG.debug("ready to build cms search....");
            cmsSearch = new CmsSearch();
            cmsSearch.setIndex("OFFLINE_PRODUCT_INDEX");
            cmsSearch.setMatchesPerPage(10000);
            cmsSearch.setSearchPage(1);
            
        }
        cmsSearch.init(cms.getCmsObject());
        return cmsSearch;
    }
    
    private List<CmsSearchResult> getOfflineProudctSearchResult(CmsJspActionElement cms, String query) {
        
        CmsSearch search = getCmsSearch(cms);
        CmsSearchParameters params = search.getParameters();
        params.setQuery(query);
        search.setParameters(params);
        List<CmsSearchResult> list = search.getSearchResult();
        return list;
    }
    
    
    private List<String> getGroupedCodesBySelf(CmsJspActionElement cms) throws JspException {
    	List<String> result = new ArrayList<String>();
    	String query = "(GroupFlag:true) AND (SapProductCode:"+itemId+")";
    	List<CmsSearchResult> list = this.getOfflineProudctSearchResult(cms, query);
    	if (list != null && list.size()==1) {
    		result.add("1");
    		CmsSearchResult record = list.get(0);
    		String xmlPath = record.getPath().replace(cms.getRequestContext().getSiteRoot(), "");
            LOG.debug(xmlPath); 
            //获取JSP文件
            CmsJspXmlContentBean xmlContent = new CmsJspXmlContentBean();
            xmlContent.init(this.getJspContext(), this.getRequest(), this.getResponse());
            I_CmsXmlContentContainer container =  xmlContent.contentload("thesysSingleFile", xmlPath, false);
            
            if (container == null) {
                LOG.debug("Container is null,["+itemId+"]");
            } else {
            	while (container.hasMoreContent()) {
            		I_CmsXmlContentContainer elementContainer = xmlContent.contentloop(container, "GroupItem");
            		int i=1;
            		while (elementContainer.hasMoreContent()) {
            			String productCode = xmlContent.contentshow(elementContainer, "GroupSapProductCode");
            			LOG.debug("Group["+i + "]ProductCode:"+productCode);
            			String quantity = xmlContent.contentshow(elementContainer, "GroupQuantity");
            			LOG.debug("Group["+i + "]Quantity:"+quantity);
            			String item = productCode + "," + quantity;
            			result.add(item);
            			i++;
            		}
            	}
            }
    	} else {
    		LOG.debug("Search Result is Null.["+itemId+"]...self");
    	}
    	return result;
    }
    
    
    private List<String> getGroupedCodesByOthers(CmsJspActionElement cms) throws JspException {
    	List<String> result = new ArrayList<String>();
    	String query = "(GroupFlag:true)";
    	List<CmsSearchResult> list = this.getOfflineProudctSearchResult(cms, query);
    	if (list != null && list.size()>0) {
    		result.add("2");
    		for (CmsSearchResult record: list) {
	    		String xmlPath = record.getPath().replace(cms.getRequestContext().getSiteRoot(), "");
	            LOG.debug(xmlPath); 
	            //获取JSP文件
	            CmsJspXmlContentBean xmlContent = new CmsJspXmlContentBean();
	            xmlContent.init(this.getJspContext(), this.getRequest(), this.getResponse());
	            I_CmsXmlContentContainer container =  xmlContent.contentload("thesysSingleFile", xmlPath, false);
	            if (container == null) {
	                LOG.debug("Container is null,["+itemId+"]");
	            } else {
	            	while (container.hasMoreContent()) {
	            		I_CmsXmlContentContainer elementContainer = xmlContent.contentloop(container, "GroupItem");
	            		String parent = xmlContent.contentshow(elementContainer, "SapProductCode");
	            		while (elementContainer.hasMoreContent()) {
	            			String productCode = xmlContent.contentshow(elementContainer, "GroupSapProductCode");
	            			if (productCode.trim().equals(itemId.trim())){
	            				String quantity = xmlContent.contentshow(elementContainer, "GroupQuantity");
		            			String item = parent + "," + quantity;
		            			result.add(item);
	            			}
	            		}
	            	}
	            }
    		}
    	} else {
    		LOG.debug("Search Result is Null.["+itemId+"]...others");
    	}
    	return result;
    }
    
    public List<String> getGroupedProductCodes()  {
        CmsJspActionElement cms = new CmsJspActionElement();
        cms.init(this.getJspContext(), this.getRequest(), this.getResponse());
        LOG.debug(itemId);
        List<String> result = new ArrayList<String>();
        try {
        	result = this.getGroupedCodesBySelf(cms);
	        if (result.size()==0) {
	        	result = this.getGroupedCodesByOthers(cms);
	        }
        } catch(Exception e) {
        	LOG.error(e.toString());
        }
        return result;
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
        List<String> groupList = this.getGroupedProductCodes();
        
        
        String PARAM_ITEM_SAFETY_STOCK ="ITEM_SAFETY_STOCK";//安全庫存量參數名
        try{
            //取得商品餘資料庫中的庫存
        	result = this.getQuantityInStock(groupList);
        	LOG.debug("In Stock:" + result);
            //取得未核單特定商品的數量
            int count = this.getQuantityInOrder(groupList);
            LOG.debug("In Order:"+ count);
            //取得系統參數中商品安全庫存數量
            String strSafetyStock = ThesysParamDAO.getInstance().getParam(getSiteId(),PARAM_ITEM_SAFETY_STOCK).getParamVal();
            if(strSafetyStock != null && strSafetyStock.length() != 0)safetyStock = Integer.parseInt(strSafetyStock);
            LOG.debug("Safe Stock:" + safetyStock);
            //可銷售數量 = 資料庫中的庫存 - 未核單特定商品的數量 - 商品安全庫存數量
            result = result - count - safetyStock;
            if(result < 0 ) result = 0 ;
        }catch(Exception ex){
            
        }
        return result;
    }
    
    private int getQuantityInStock(List<String> groupList) {
    	int count = 0;
    	try {
	        if (groupList.size()>0) {
	        	String type = groupList.get(0);
	        	LOG.debug("type:"+type);
	        	groupList.remove(0);
	        	if (type.equals("1")) {
	        		count = this.getQuantityGroupedBySelfInStock(groupList);
	        	} else {
	        		count = this.getQuantityGroupedByOthersInStock(groupList);
	        	}
	        } else {
	        	ThesysStockVO vo = ThesysStockDAO.getInstance().read(getSiteId(),itemId);
	            if(vo!=null) count = vo.getQuantity();
	        }
    	} catch (Exception e) {
    		LOG.error(e.fillInStackTrace());
    	}
    	return count;
    }
    
    
    
    /**
     * @类型: 自身即为组合商品.
     * @算法:<br>
     * 1. 获取此商品当前库存量，若不存在，则为0;<br>
     * 2. 获致此商品所属子项的库存量，并除以组合权数，取最小值，以得到组合库存量; <br>
     * 3. 返回值为库存量＋组合库存量
     * @param groupList
     * @return
     */
    private int getQuantityGroupedBySelfInStock(List<String> groupList) {
    	int count = 0;
    	try {
    		int qtyInStockOfSelf = 0;
    		ThesysStockVO vo = ThesysStockDAO.getInstance().read(getSiteId(),itemId);
    		if (vo!=null) qtyInStockOfSelf = vo.getQuantity();
    		int minQty = 0;
    		int i=0;
    		for (String item : groupList) {
    			String[] tmp = item.split(",");
    			vo = ThesysStockDAO.getInstance().read(getSiteId(),tmp[0]);
        		if (vo!=null) {
        			int qty = vo.getQuantity()/Integer.valueOf(tmp[1]);
        			if(i == 0 || qty < minQty ) minQty = qty ;
        		}
        		i++;
    		}
    		LOG.debug("MinQty:"+minQty);
    		LOG.debug("qtyInStockOfSelf:"+qtyInStockOfSelf);
    		count = qtyInStockOfSelf + minQty;
    	} catch (Exception e) {
    		LOG.error(e.fillInStackTrace());
    	}
		return count;
	}

    /**
     * @算法:<br>
     * 1. 取得自身当前库存量 <br>
     * 2. 循环取得所属组合商品的库存量，并乘以权数，得出包含于组合商品中的累计库存量<br>
     * 3. 以上两者相加
     * @param groupList
     * @return
     */
	private int getQuantityGroupedByOthersInStock(List<String> groupList) {
		int count = 0;
    	try {
    		ThesysStockVO vo = ThesysStockDAO.getInstance().read(getSiteId(),itemId);
    		if (vo!=null) count = vo.getQuantity();
    		for (String item : groupList) {
    			String[] tmp = item.split(",");
    			vo = ThesysStockDAO.getInstance().read(getSiteId(),tmp[0]);
        		if (vo!=null) {
        			count = count + vo.getQuantity()  * Integer.valueOf(tmp[1]).intValue();
        		}
    		}
    	} catch (Exception e) {
    		LOG.error(e.fillInStackTrace());
    	}
    	return count;
	}
    
    

	/**
     * 获取自身存在于组合商品中的数量。
     * @param groupList
     * @return
     */
    private int getQuantityInOrder(List<String> groupList) {
    	int count = 0;
    	try {
	        if (groupList.size()>0) {
	        	String type = groupList.get(0);
	        	groupList.remove(0);
	        	if (type.equals("1")) {
	        		count = this.getQuantityByGroupedSelfInOrder(groupList);
	        	} else {
	        		count = this.getQuantityByGroupedOthersInOrder(groupList);
	        	}
	        } else {
	        	count = ThesysOrderDAO.getInstance().getCountFormUncheckedItem(getSiteId(), itemId);
	        }
    	} catch (Exception e) {
    		LOG.error(e.fillInStackTrace());
    	}
    	return count;
    }
    
    /**
     * 获取自身为组合商品的数量
     * @param groupList
     * @return
     */
    private int getQuantityByGroupedSelfInOrder(List<String> groupList) {
    	int count = 0;
    	try {
    		//自身在订单中的数量
    		int itemQty = ThesysOrderDAO.getInstance().getCountFormUncheckedItem(getSiteId(), itemId);
    		//所属项目在订单中的数量.
    		int maxQty = 0;
    		int i =0; 
	    	for (String item : groupList) {
	    		String[] tmp = item.split(",");
	    		int qtyInOrder = ThesysOrderDAO.getInstance().getCountFormUncheckedItem(getSiteId(), tmp[0]);
	    		int qty = qtyInOrder/Integer.valueOf(tmp[1]).intValue();
	    		//取最大组合数
	            if(i == 0 || qty > maxQty ) maxQty = qty ;
	    		i++;
	    	}
	    	count = itemQty + maxQty;
    	} catch (Exception e) {
    		LOG.error(e.fillInStackTrace());
    	}
    	return count;
    }
    
    private int getQuantityByGroupedOthersInOrder(List<String> groupList) {
    	int count = 0;
    	try {
    		count = ThesysOrderDAO.getInstance().getCountFormUncheckedItem(getSiteId(), itemId);
    		for (String item : groupList) {
    			String[] tmp = item.split(",");
    			int qtyInOrder = ThesysOrderDAO.getInstance().getCountFormUncheckedItem(getSiteId(), tmp[0]);
    			count = count + qtyInOrder * Integer.valueOf(tmp[1]);
    		}
    	} catch (Exception e) {
    		LOG.error(e.fillInStackTrace());
    	}
    	return count;
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

