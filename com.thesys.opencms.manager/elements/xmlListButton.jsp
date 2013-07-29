<%@ page trimDirectiveWhitespaces="true" %>
<%@page buffer="none" session="true" import="org.opencms.frontend.templatetwo.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<% cms.init(pageContext,request,response); %>
</jsp:useBean>
<c:set var="fileState"><cms:property name="FileStatus" file="${param.xmlPath}"/></c:set>
<form id="form_${param.rowId}" method="post">
<input type="hidden" name="fromUrl" value="${param.fromUrl}"/>
<input type="hidden" name="xmlPath" value="${param.xmlPath}"/>
<input type="hidden" name="autoPublish" value="${param.autoPublish}"/>
<input type="hidden" name="fileState" value="${fileState}"/>
</form>
<%
CmsFile file = cms.getCmsObject().readFile((String)request.getParameter("xmlPath"),CmsResourceFilter.ALL);
pageContext.setAttribute("file",file);
%>
<c:if test="${param.showState!='false'}">
<c:choose>
<c:when test="${fileState=='deleted'}"><span style="color:red">刪除待發佈</span></c:when>
<c:when test="${fileState=='new'}"><span style="color:red">新增待發佈</span></c:when>
<c:when test="${fileState=='changed'}"><span style="color:red">修改待發佈</span></c:when>
<c:otherwise><span>無異動</span></c:otherwise>
</c:choose><br/>
</c:if>
<c:if test="${param.editButton!='false'}">
	<c:if test="${fileState!='deleted'}">[<a href="#form_${param.rowId}" ref="${param.editUrl}" class="btnEdit">編輯</a>]</c:if> 
</c:if>
<c:if test="${param.deleteButton!='false'}">
	<c:if test="${fileState!='deleted' && fileState!='changed'}">[<a href="#form_${param.rowId}" ref="<cms:link>/system/modules/com.thesys.opencms.manager/jsps/delete.jsp</cms:link>" class="btnDelete">刪除</a>]</c:if>
</c:if> 
<c:if test="${sessionScope.PublishGroup || sessionScope.SuperGroup}"><c:if test="${!param.autoPublish && fileState!='published'}">[<a href="#form_${param.rowId}" ref="${param.publishUrl}" class="btnPublish">發佈</a>]</c:if></c:if>
					