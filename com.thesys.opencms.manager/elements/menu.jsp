<%@ page trimDirectiveWhitespaces="true" %>
<%@page buffer="none" session="true" import="org.opencms.frontend.templatetwo.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.thesys.opencms.laphone.member.*"%>
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<% cms.init(pageContext,request,response); %>
</jsp:useBean>
<c:set var="currentPath"><cms:info property="opencms.request.uri"/></c:set>
<div class="menubox">
	<div class="menu_title">
		<span class="t_02-1"></span><span class="t_02-2"></span>
		<span class="t_02">管理功能</span></div>
	<div class="menu">
<%pageContext.setAttribute("navigationList",cms.getNavigation().getNavigationForFolder("/manager/"));%>
    <c:forEach var="menu" items="${navigationList}">
    	<a href="<cms:link>${menu.resourceName}</cms:link>" title="${menu.navText}" <c:if test="${fn:indexOf(currentPath,menu.resourceName)>=0}">class="selected"</c:if>>${menu.navText}</a>
    	
    </c:forEach> 
	</div>
	<div class="b">
		<span class="b_02-1"></span><span class="b_02-2"></span>
		<span class="b_02"></span></div>
</div>