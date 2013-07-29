package com.thesys.opencms.laphone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;

import com.thesys.opencms.modules.ThesysAbstractHandler;



public abstract class ThesysLaphoneHandler extends ThesysAbstractHandler {
	/** The log object for this class. */
	public static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ThesysLaphoneHandler.class);
	
	private int pageSize = 40;
	private int pageIndex = 1;
	
    public static final String JSON_RESULT_CODE = "code";
    public static final String JSON_RESULT_MESSAGE = "msg";
    public static final String RESULT_CODE_SUCCESS = "S";
    public static final String RESULT_CODE_FAILURE = "F";
    public static final String RESULT_MESSAGE_FAILURE = "系統發生錯誤，請稍後再試，或與系統管理者聯絡";
    
//    private ThesysMemberVO member = null;
    private String memberId = null;
	
	public ThesysLaphoneHandler(){}
	public ThesysLaphoneHandler(PageContext context, HttpServletRequest req,HttpServletResponse res) throws Exception  {
		init(context, req, res);
	}
	public void init(PageContext context, HttpServletRequest req,HttpServletResponse res){
		super.init(context, req, res);
		memberId = (String)req.getSession().getAttribute("memberId");
	}
	public String getMemberId() throws Exception{
//		if(member!=null) return member.getAccountId();
//		else return null;
		return memberId;
	}
//	public ThesysMemberVO getMember(){ 
//		return member;
//	}
	/**
	 * @return the pageSize
	 */
	public int getPageSize() {
		return pageSize;
	}
	/**
	 * @param pageSize the pageSize to set
	 */
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	/**
	 * @return the pageIndex
	 */
	public int getPageIndex() {
		return pageIndex;
	}
	/**
	 * @param pageIndex the pageIndex to set
	 */
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	
	
	
	
}
