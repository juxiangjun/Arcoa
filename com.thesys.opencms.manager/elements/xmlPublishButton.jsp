<%@ page trimDirectiveWhitespaces="true" %>
<%@page buffer="none" session="true" import="org.opencms.frontend.templatetwo.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<% cms.init(pageContext,request,response); %>
</jsp:useBean>
<%pageContext.setAttribute("file",cms.getCmsObject().readFile((String)request.getParameter("xmlPath"),CmsResourceFilter.ALL));%>
<c:set var="fileState"><cms:property name="FileStatus" file="${param.xmlPath}"/></c:set>
<form id="publishForm" method="post" class="hiddenForm">
<input type="hidden" name="fromUrl" value="${param.fromUrl}"/>
<input type="hidden" name="xmlPath" value="${param.xmlPath}"/>
<input type="hidden" name="fileState" value="${fileState=='deleted'}"/>
<input type="hidden" name="searchIndex" value="${param.searchIndex}"/>
</form>
<c:choose>				
<c:when test="${fileState=='deleted'}">
	<c:if test="${sessionScope.PublishGroup || sessionScope.SuperGroup}"><a href="#publishForm" ref="<cms:link>/system/modules/com.thesys.opencms.manager/jsps/publish.jsp</cms:link>" class="button btnPublish">發佈刪除的資料</a></c:if>
	<a href="#publishForm" ref="<cms:link>/system/modules/com.thesys.opencms.manager/jsps/undelete.jsp</cms:link>" class="button btnUndelete">取消刪除</a>
</c:when>
<c:when test="${fileState=='new'}">
	<c:if test="${sessionScope.PublishGroup || sessionScope.SuperGroup}"><a href="#publishForm" ref="<cms:link>/system/modules/com.thesys.opencms.manager/jsps/publish.jsp</cms:link>" class="button btnPublish">發佈新增的資料</a></c:if>
</c:when>					
<c:when test="${fileState=='changed'}">					
	<c:if test="${sessionScope.PublishGroup || sessionScope.SuperGroup}"><a href="#publishForm" ref="<cms:link>/system/modules/com.thesys.opencms.manager/jsps/publish.jsp</cms:link>" class="button btnPublish">發佈</a></c:if>
	<a href="#publishForm" ref="<cms:link>/system/modules/com.thesys.opencms.manager/jsps/undoChange.jsp</cms:link>" class="button btnUndoChange">取消修改</a>
</c:when>
</c:choose>