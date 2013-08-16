<%@ page buffer="none" import="com.thesys.opencms.laphone.system.*,com.thesys.opencms.laphone.system.dao.*" %>
<%@ include file="/system/modules/com.thesys.opencms.manager/elements/loginCheck.jsp"%>
 <jsp:useBean id="cms" class="org.opencms.jsp.CmsJspLoginBean">
 <% cms.init(pageContext, request, response); %>
 </jsp:useBean>
<%
	String cmsuser = cms.getUserName();
	int feeType = request.getParameter("feeType") == null?0:Integer.parseInt(request.getParameter("feeType"));
	int conditionEnd = request.getParameter("conditionEnd") == null?0:Integer.parseInt(request.getParameter("conditionEnd"));
	String num = request.getParameter("num") == null?"":(String)request.getParameter("num");
	int amount = request.getParameter("feeAmount") == null?0:Integer.parseInt(request.getParameter("feeAmount"));
	ThesysShipFeeHandler handler = new ThesysShipFeeHandler(pageContext,request,response);
	
	int res = handler.update(feeType,conditionEnd,amount ,cmsuser);
	
	if(res == 0){
		out.println("<script>alert(\"更新錯誤\");history.go(-1);</script>");
	}else if(res == -1){
	 	out.println("<script>alert(\"輸入錯誤，請重新輸入\");history.go(-1);</script>");
	}else{
		out.println("<script>alert(\"更新成功\");location.href='index.html';</script>");
	}
	
%>