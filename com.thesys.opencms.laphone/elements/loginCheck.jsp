<%@page buffer="none" session="true" import="com.thesys.opencms.laphone.member.dao.*,org.opencms.frontend.templatetwo.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<% cms.init(pageContext,request,response); %>
</jsp:useBean>
<%
	if(session.getAttribute("memberNo") == null){
		out.print("<script>location.href='"+cms.link("/login/index.html")+"';</script>");
		return;
	}
%>