package com.thesys.opencms.laphone.epaper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import org.apache.commons.logging.Log;
import org.opencms.main.CmsLog;

import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;
import com.thesys.opencms.laphone.ThesysLaphoneHandler;
import com.thesys.opencms.laphone.epaper.dao.ThesysEpaperDAO;
import com.thesys.opencms.laphone.epaper.dao.ThesysEpaperVO;
import com.thesys.opencms.laphone.system.ThesysSerialHandler;

public class ThesysEpaperHandler extends ThesysLaphoneHandler{

	protected static final Log LOG = CmsLog.getLog(ThesysEpaperHandler.class);
	
	private java.util.Date now = new java.util.Date(); //取得現在時間
    private String eno ;
     
	public ThesysEpaperHandler(){}
	public ThesysEpaperHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		super(context, req, res);
	}

	/**
	 * 取得分頁中所有資料的list
	 * @return
	 */
	public List<ThesysEpaperVO> getPageList(){
		List<ThesysEpaperVO> result = new ArrayList<ThesysEpaperVO>();
		try{
			result = ThesysEpaperDAO.getInstance().listByPage(getSiteId(), getPageSize(), getPageIndex());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}
	
	/**
	 * 取得所有資料的筆數	 
	 * @return
	 */
	public int getCount(){
		int result =0;
		try{
			result = ThesysEpaperDAO.getInstance().count(getSiteId());
		} catch (Exception ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return result;
	}	
	
	public ThesysEpaperVO getEpaper(){
		return getEpaper(eno);
	}
	
	/**
	 * 取得此筆資料
	 * @param eno
	 * @return
	 */
	public ThesysEpaperVO getEpaper(String eno) {
		ThesysEpaperVO vo =null;
		try {
			if(eno != null)
				vo = ThesysEpaperDAO.getInstance().getRow(getSiteId(),eno);
		} catch (SQLException ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		return vo;
	}
	
	/**
	 * 新增Epaper
	 * @param subject
	 * @param creater
	 * @param releaseDate
	 * @return
	 */
	public String add(String subject, String creater,java.util.Date releaseDate) {
		ThesysEpaperVO vo = new ThesysEpaperVO();
		int r = 0;
		String serialNo = ThesysSerialHandler.getNextSerialNo(getSiteId(),"EPAPER");
		vo = new ThesysEpaperVO();
		vo.setSiteId(getSiteId());
		vo.setReleaseDate(releaseDate);
		vo.setEno(serialNo);
		vo.setSubject(subject);
		vo.setCreater(creater);
		vo.setCreateDate(now);
		try {
			r = ThesysEpaperDAO.getInstance().add(vo);
		} catch (SQLException ex) {
			LOG.error(ex, ex.fillInStackTrace());
		}
		if(r == 0 )return null;
		return serialNo;
	}
	
	/**
	 * 修改此筆Epaper資料
	 * @param vo
	 * @return	1	成功
	 * 			0	失敗
	 * 			-1	無資料
	 * 			99	新編號與其他電子報重複
	 */
	public int update(ThesysEpaperVO vo , String newEno) {
		int r = 0;
		if (getEpaper(vo.getEno()) == null || newEno == null || newEno.equals("")) {
			r = -1; // 無資料
		}else if((!newEno.equals(vo.getEno())) && getEpaper(newEno) != null){
			r = 99 ;//新編號與其他電子報重複
		}else{
			try {
				vo.setLastUpdateDate(now);
				r = ThesysEpaperDAO.getInstance().update(vo,newEno);
			} catch (SQLException ex) {
				LOG.error(ex, ex.fillInStackTrace());
			}
		}
		return r;
	}

	/**
	 * 刪除epaper
	 * @param eno
	 * @return
	 */
	public int deleteRow(String eno) {
		int r = 0;
		if (getEpaper(eno) != null) {
			try {
				r = ThesysEpaperDAO.getInstance().delete(getSiteId(),eno);
			} catch (SQLException ex) {
				LOG.error(ex, ex.fillInStackTrace());
			}
		} else {
			r = -1; // 無資料
		}
		return r;
	}
	
	public String getEno() {
		return eno;
	}
 
	public void setEno(String eno) {
		this.eno = eno;
	}

}