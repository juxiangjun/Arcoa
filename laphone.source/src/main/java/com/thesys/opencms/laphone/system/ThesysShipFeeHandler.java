package com.thesys.opencms.laphone.system;

import java.sql.SQLException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.system.dao.ThesysShipFeeVO;
import com.thesys.opencms.laphone.system.dao.ThesysShipFeeDAO;

public class ThesysShipFeeHandler extends ThesysLaphoneHandler {

	/** The log object for this class. */
	private static final Log LOG = CmsLog.getLog(ThesysShipFeeHandler.class);
	private java.util.Date now = new java.util.Date();

	private int feeType;
	private int conditionEnd;
	private int amount;

	public ThesysShipFeeHandler() {
	}

	public ThesysShipFeeHandler(PageContext context, HttpServletRequest req,
			HttpServletResponse res) throws Exception {
		super(context, req, res);
	}

//	@Override
//	public String getSiteId() {
//		return super.getSiteId();
////		return "/sites/laphone";
//	}

	public List<ThesysShipFeeVO> getPageList() {
		List<ThesysShipFeeVO> list = new ArrayList<ThesysShipFeeVO>();
		try {
			list = ThesysShipFeeDAO.getInstance().listByFeeType(getSiteId(),
					feeType);
		} catch (SQLException ex) {
			LOG.error(ex.fillInStackTrace());
		}
		return list;
	}

	public int getCount() {
		int count = 0;
		try {
			count = ThesysShipFeeDAO.getInstance().count(getSiteId(), feeType);
		} catch (SQLException ex) {
			LOG.error(ex.fillInStackTrace());
		}
		return count;
	}

	public ThesysShipFeeVO getRow() {
		return getRow(feeType, conditionEnd);
	}

	public ThesysShipFeeVO getRow(int feeType, int conditionEnd) {
		ThesysShipFeeVO vo = null;
		try {
			vo = ThesysShipFeeDAO.getInstance().getRow(getSiteId(), feeType,
					conditionEnd);
		} catch (Exception ex) {
			LOG.error(ex.fillInStackTrace());
		}
		return vo;
	}
	
	public int add(int feeType, int conditionEnd, int feeAmount, String creater) {
		int res = 0;
		if (getRow(feeType,conditionEnd) == null) {
			ThesysShipFeeVO vo = new ThesysShipFeeVO();
			vo.setSiteId(getSiteId());
			vo.setFeeType(feeType);
			vo.setConditionEnd(conditionEnd);
			vo.setFeeAmount(feeAmount);
			vo.setCreater(creater);
			vo.setCreateDate(now);
			try {
				res = ThesysShipFeeDAO.getInstance().insert(vo);
			} catch (SQLException ex) {
				LOG.error(ex.fillInStackTrace());
			}
		}else{
			res = 99;  //資料庫已有資料
		}
		return res;
	}

	
	public int update(int feeType, int conditionEnd, int feeAmount, String lastUpdater){
		ThesysShipFeeVO vo = getRow(feeType, conditionEnd);
		if(vo != null){
			vo.setFeeAmount(feeAmount);
			vo.setLastUpdater(lastUpdater);
		}else{
			return -1;
		}
		return update(vo);
	}
	
	public int update(ThesysShipFeeVO vo){
		int res = 0;
		if (getRow(vo.getFeeType(),vo.getConditionEnd()) != null) {
			try {
				vo.setLastUpdatedDate(now);
				res = ThesysShipFeeDAO.getInstance().update(vo);
			} catch (SQLException ex) {
				LOG.error(ex.fillInStackTrace());
			}
		}else{
			res = -1;
		}
		return res;
	}
	
	public int delete(){
		return delete(feeType, conditionEnd);
	}
	
	public int delete(int feeType, int conditionEnd){
		int res = 0;
		if (getRow(feeType,conditionEnd) != null) {
			try {
				res = ThesysShipFeeDAO.getInstance().delete(getSiteId(), feeType, conditionEnd);
			} catch (SQLException ex) {
				LOG.error(ex.fillInStackTrace());
			}
		}else{
			res = -1;
		}
		return res;
	}

	public int getShipFee() {
		return getShipFee(feeType, amount);
	}

	public int getShipFee(int feeType, int amount) {
		int result = -1;
		try {
			ThesysShipFeeVO vo = ThesysShipFeeDAO.getInstance()
					.findByCondition(getSiteId(), feeType, amount);
			if (vo != null)
				result = vo.getFeeAmount();
		} catch (Exception ex) {
			LOG.error(ex.fillInStackTrace());
		}
		return result;
	}

	/**
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @return the feeType
	 */
	public int getFeeType() {
		return feeType;
	}

	/**
	 * @param feeType
	 *            the feeType to set
	 */
	public void setFeeType(int feeType) {
		this.feeType = feeType;
	}

	public int getConditionEnd() {
		return conditionEnd;
	}

	public void setConditionEnd(int conditionEnd) {
		this.conditionEnd = conditionEnd;
	}

}
