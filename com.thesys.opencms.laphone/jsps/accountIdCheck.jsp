<%@ page import="java.util.*,com.thesys.opencms.laphone.member.*,com.thesys.opencms.laphone.member.dao.*" %>
<%
String accountId =  request.getParameter("accountId") == null ? "" : ((String)request.getParameter("accountId")).toLowerCase();
ThesysMemberHandler handler = new ThesysMemberHandler(pageContext,request,response);
%>
﻿{
	"result": <%=!handler.isAccountIdExist(accountId )%>
}