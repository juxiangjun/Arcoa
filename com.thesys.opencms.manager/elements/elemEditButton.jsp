<%/**
  *<cms:include file="/system/modules/com.thesys.opencms.manager/elements/elemListButton.jsp">
  *	<cms:param name="rowId">${totalRowId}</cms:param>
  *	<cms:param name="fromUrl"><cms:info property="opencms.uri"/></cms:param>
  *	<cms:param name="xmlPath">${xmlPath}</cms:param>
  *	<cms:param name="editUrl"><cms:link>editForm.html</cms:link></cms:param>
  *	<cms:param name="rootXpath">ColorGroup[${groupRowId}]/</cms:param>				
  *	<cms:param name="tagName">Color</cms:param>				
  *	<cms:param name="tagId">${colorRowId}</cms:param>
  *</cms:include>
**/%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@page buffer="none" session="true" import="org.opencms.frontend.templatetwo.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<% cms.init(pageContext,request,response); %>
</jsp:useBean>
<form id="form_${param.rowId}" method="post">
<input type="hidden" name="fromUrl" value="${param.fromUrl}"/>
<input type="hidden" name="xmlPath" value="${param.xmlPath}"/>
<input type="hidden" name="rootXpath" value="${param.rootXpath}"/>
<input type="hidden" name="tagName" value="${param.tagName}"/>
<input type="hidden" name="tagId" value="${param.tagId}"/>
<input type="hidden" name="autoPublish" value="${param.autoPublish}"/>
<c:set var="deleteUrl"><cms:link>/system/modules/com.thesys.opencms.manager/jsps/elemDelete.jsp</cms:link></c:set>
<c:if test="${!empty param.deleteUrl}">
<c:set var="deleteUrl" value="param.deleteUrl"/></c:if>
</form>
<c:if test="${param.editButton!='false'}">[<a href="#form_${param.rowId}" ref="${param.editUrl}" class="btnEdit">編輯</a>]</c:if>
<c:if test="${param.deleteButton!='false'}">[<a href="#form_${param.rowId}" ref="${deleteUrl}" class="btnDelete">刪除</a>] </c:if>
					