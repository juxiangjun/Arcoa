package com.thesys.opencms.laphone.product;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.product.dao.ThesysCTRDAO;
import com.thesys.opencms.laphone.product.dao.ThesysCTRVO;

public class ThesysCTRHandler extends ThesysLaphoneHandler {

	/** The log object for this class. */
    private static final Log LOG = CmsLog.getLog(ThesysCTRHandler.class);
	
    public ThesysCTRHandler(){}
	public ThesysCTRHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super(context, req, res);
	}
	
	@Override
	public String getSiteId() {
		return super.getSiteId();
//		return "/sites/laphone";
	}
	
	public boolean add(String itemId){
		ThesysCTRVO vo = new ThesysCTRVO();
		vo.setSiteId(getSiteId());
		vo.setItemId(itemId);
		vo.setCreateDate(new Date());
		try{
			ThesysCTRDAO.getInstance().add(vo);
			return true;
		}catch(Exception ex){
			LOG.error(ex.fillInStackTrace());
		}
		return false;
	}

}
