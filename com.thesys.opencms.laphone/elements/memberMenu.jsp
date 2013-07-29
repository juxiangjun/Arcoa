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
<ul id="member-menu" class="member-menu">
<%pageContext.setAttribute("navigationList",cms.getNavigation().getNavigationForFolder("/memberCenter/"));%>
    <c:forEach var="menu" items="${navigationList}">
    	<li><span>${menu.navText}</span>
    	<c:if test="${menu.folderLink}"><c:set var="folderPath" value="${menu.resourceName}"/>
    		<ul>
    		<%pageContext.setAttribute("navigationList1",cms.getNavigation().getNavigationForFolder((String)pageContext.getAttribute("folderPath")));%>
    		<c:forEach var="menu1" items="${navigationList1}">
    			<li><a href="<cms:link>${menu1.resourceName}</cms:link>" title="${menu1.navText}" <c:if test="${fn:indexOf(currentPath,menu1.resourceName)>=0}">class="selected"</c:if>>${menu1.navText}</a></li>
    		</c:forEach>
    		</ul>
    	</c:if>
    	</li>
    </c:forEach> 
</ul>