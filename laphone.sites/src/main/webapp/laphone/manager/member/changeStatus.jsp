<%@ page buffer="none" import="org.opencms.jsp.*, java.util.*, java.sql.*,com.thesys.opencms.laphone.member.dao.*,com.thesys.opencms.laphone.member.*" %>
<%@ include file="/system/modules/com.thesys.opencms.manager/elements/loginCheck.jsp"%>
<jsp:useBean id="cms" class="org.opencms.jsp.CmsJspLoginBean">
<% cms.init(pageContext, request, response); %>
</jsp:useBean>
<%
	String accountId = request.getParameter("accountId") == null?"":(String)request.getParameter("accountId");
	int oldStatus = request.getParameter("oldStatus") == null?0:Integer.parseInt(request.getParameter("oldStatus"));
 	int status = request.getParameter("status") == null?0:Integer.parseInt(request.getParameter("status"));
	
 	String user = cms.getUserName();
 	ThesysMemberHandler handler = new ThesysMemberHandler(pageContext,request,response);


 	if(oldStatus == status){
 		out.print("<script>alert('狀態相同');history.go(-1);</script>");
		return;
	}
 	int rus = handler.updateMemberStatus(accountId,status ,user);
	if(rus == -1){//查無此帳號
		out.print("<script>alert('帳號錯誤');history.go(-1);</script>");
		return;	
	}else if(rus != 1){
		out.print("<script>alert('修改失敗');history.go(-1);</script>");
		return;   
	}else{
		out.println("<script>alert('修改成功');location.href='index.html';</script>");
	}
	
 %>