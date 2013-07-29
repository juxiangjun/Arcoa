<%@ page trimDirectiveWhitespaces="true" %>
<%@page buffer="none" session="true" import="org.opencms.frontend.templatetwo.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
CmsTemplateMenu menuCms = new CmsTemplateMenu(pageContext, request, response);
pageContext.setAttribute("menuCms",menuCms);
//int navStartLevel = Integer.parseInt(menuCms.property("NavStartLevel", 1);
pageContext.setAttribute("navList", menuCms.getNavigation().getNavigationBreadCrumb(1, true));
%>         
<div id="pieces" class="grey6 font12">
    <a title="前往首頁" href="<cms:link>/index.html</cms:link>"><img alt="前往首頁" src="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/home.png</cms:link>" /></a>
    <a title="前往首頁" href="<cms:link>/index.html</cms:link>">首頁</a>
    <!--麵包屑-->
    <c:forEach items="${navList}" var="elem" >
	<c:set var="fileName" value="${elem.fileName}"/>
	<c:set var="link" value="${elem.resourceName}"/>
	<c:set var="navText"><cms:property name="Title" /></c:set>
	<c:if test="${!empty menuCms.navText[elem]}"><c:set var="navText" value="${menuCms.navText[elem]}"/></c:if>
	<c:if test="${fileName!='product/'}"><span class="grey6 font11 bold">&gt;</span><a href="<cms:link>${link}</cms:link>">${navText}</a></c:if>
    </c:forEach>  
    <c:set var="currentUri"><cms:info property="opencms.request.uri" /></c:set>
    <c:if test="${!fn:endsWith(currentUri,'index.html')}">
    	<c:set var="navText"><cms:property name="NavText" /></c:set>
    	<%/**<c:if test="${fn:length(navText)==0}"><c:set var="navText"><cms:property name="Title" /></c:set></c:if>**/%>
	<c:if test="${!empty navText}"><span class="grey6 font11 bold">&gt;</span><a href="#">${navText}</a></c:if>
    </c:if>
    <!--end 麵包屑-->
            
</div>	
	
	