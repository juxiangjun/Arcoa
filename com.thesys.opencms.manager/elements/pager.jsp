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
	<div class="pager">
		<form id="pagerForm" method="post" action="<cms:link><cms:info property="opencms.request.uri"/></cms:link>">
			<c:forEach var="p" items="${param}">
				<c:if test="${p.key!='pageIndex' && p.key!='pageCount'}"><input type="hidden" name="${p.key}" value="${p.value}"/></c:if>
			</c:forEach>
		
		<c:set var="pageIndex" value="${param.pageIndex}"/>
		<c:if test="${empty pageIndex}"><c:set var="pageIndex" value="1"/></c:if>
		共 ${param.pageCount} 頁 | 目前頁次：<select id="pageIndex" name="pageIndex" onchange="this.form.submit()">
		<c:forEach var="i" begin="1" end="${param.pageCount}"> 
			<option value="${i}" <c:if test="${pageIndex==i}">selected</c:if>>第 ${i} 頁</option>
		</c:forEach>
		</select>
		</form>
	<div>