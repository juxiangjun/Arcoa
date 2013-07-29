<%@page buffer="none" session="true" import="com.thesys.opencms.laphone.member.dao.*,java.util.*" %>

<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<% cms.init(pageContext,request,response); %>
</jsp:useBean>
<jsp:useBean id="memberhandler" class="com.thesys.opencms.laphone.member.ThesysMemberHandler">
	<%memberhandler.init(pageContext,request,response);%>
	<jsp:setProperty name="memberhandler" property="memberNo" value="${memberNo}"/>
</jsp:useBean>
<%
	int memberStatus = memberhandler.getSelectedMemberStatus();
	if(memberStatus == 2){
	   out.print("<script>alert('手機尚未驗證請先驗證');location.href='"+cms.link("/login/'verify.html")+"';</script>");
	   return;		
	}else if(memberStatus == 1){
	  //out.print("<script>alert('黑名單');location.href='"+cms.link("/index.html")+"';</script>");
	  //return;
	}else if(memberStatus == -1){
	  out.print("<script>alert('輸入錯誤');location.href='"+cms.link("/index.html")+"';</script>");
	  return;
	}	  
		
%>