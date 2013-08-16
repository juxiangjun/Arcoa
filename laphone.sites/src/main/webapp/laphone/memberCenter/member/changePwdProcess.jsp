<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@page buffer="none" session="true" import="com.thesys.opencms.laphone.member.dao.*,com.thesys.opencms.laphone.member.*,com.thesys.opencms.laphone.system.*" %>
<%@page import="com.thesys.opencms.laphone.util.*,org.opencms.json.*,java.util.*,java.text.*" %>
<%@include file="/system/modules/com.thesys.opencms.laphone/elements/loginCheck.jsp"%>
<%
	int memberNo = (Integer)session.getAttribute("memberNo");
	String oldPwd = request.getParameter("oldPwd") ==null?"":(String)request.getParameter("oldPwd");
	String newPwd = request.getParameter("newPwd") ==null?"":(String)request.getParameter("newPwd");
	String pwdConfirm = request.getParameter("pwdConfirm") ==null?"":(String)request.getParameter("pwdConfirm");
	int pwdErrTimes = session.getAttribute("pwdErrTimes") == null?0:(Integer)session.getAttribute("pwdErrTimes");

	if(!newPwd.equals(pwdConfirm )){
		out.print("<script>alert('兩次密碼不同');history.go(-1);</script>");
		return;
	}	
	if(newPwd.equals(oldPwd)){
		out.print("<script>alert('新舊密碼必須不同');history.go(-1);</script>");
		return;
	}
	
	ThesysMemberHandler handler = new ThesysMemberHandler(pageContext, request, response);
	//member = handler.checkOldPassword(accountId,oldPwd);
	
	if(!handler.checkOldPassword(memberNo ,oldPwd)){
		session.setAttribute("pwdErrTimes",pwdErrTimes+1);
		if(pwdErrTimes >= 2){
			out.print("<script>alert('舊密碼輸入錯誤3次，請重新登入');location.href='"+cms.link("/login/logout.html")+"';</script>");
			return;
		}	
		out.print("<script>alert('舊密碼輸入錯誤');history.go(-1);</script>");
		return;
	}else{
		session.setAttribute("pwdErrTimes",0);
	}
	
	//int rus = handler.modifyPwd(accountId,newPwd);
	
	String accountId =(String)session.getAttribute("memberId");
	String mobile =(String)session.getAttribute("memberMobile");
	String email =(String)session.getAttribute("memberEmail");
	if(!handler.changePassword(memberNo,accountId,mobile,email,newPwd)){
		out.print("<script>alert('密碼修改錯誤');history.go(-1);</script>");
		return;
	}else{
		/*
		String star = "********************";
		newPwd = newPwd.substring(0,1)+star.substring(0,newPwd.length()-2)+newPwd.substring(newPwd.length()-1);
		//SMS與Mail通知...
		
		ThesysParamHandler paramHandler = new ThesysParamHandler(pageContext, request, response);
		String host = paramHandler.getValueByParamKey("WEB_URL");//http://192.168.7.9:8080/shopping/ 
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		String date = sdf.format(new Date());
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("date",date);
		jsonObj.put("host",host);
		jsonObj.put("pwd",newPwd);
		 
		ThesysSendMsgHandler msgHandler = new ThesysSendMsgHandler(pageContext,request,response);
		msgHandler.setMsgName("changePwd");
		msgHandler.setJsonObj(jsonObj);
		msgHandler.setMemberId(accountId );
		msgHandler.setCellphone(member.getCellphone());
		msgHandler.setEmail(member.getEmail());
		msgHandler.sendMsg();
		*/
		out.print("<script>alert('密碼更新成功，請用新密碼重新登入');location.href='"+cms.link("/login/logout.html")+"';</script>");
				
	}
		
%>