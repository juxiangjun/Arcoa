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
	<fmt:parseNumber var="pageCount" type="number" value="${param.pageCount}" />
	<c:if test="${pageCount>0}">
	<ul class="pager">
		<form id="pagerForm" method="post" action="<cms:link><cms:info property="opencms.request.uri"/></cms:link>">
			<input type="hidden" id="pageIndex" name="pageIndex" value=""/>
			<c:forEach var="p" items="${param}">
				<c:if test="${p.key!='pageIndex' && p.key!='pageCount'}"><input type="hidden" name="${p.key}" value="${p.value}"/></c:if>
			</c:forEach>
		</form>
		<fmt:parseNumber var="pageIndex" type="number" value="${param.pageIndex}" />
		<c:if test="${empty pageIndex || pageIndex <1}"><c:set var="pageIndex" value="1"/></c:if>
		<c:if test="${pageIndex>pageCount}"><c:set var="pageIndex" value="${pageCount}"/></c:if>
		<c:choose><c:when test="${pageIndex>1}"><a class="prev" href="#pagerForm" ref="${pageIndex-1}">上一頁</a></c:when>
		<c:otherwise><span class="prev current">上一頁</span></c:otherwise></c:choose>
		<c:forEach var="i" begin="1" end="${pageCount}"> 
			<c:choose> 
			<c:when test="${pageIndex==i}"><span class="current"><c:out value="${i}"/></span></c:when> 
			<c:otherwise><a href="#pagerForm" ref="${i}" ><c:out value="${i}"/></a></c:otherwise> 
			</c:choose> 
		</c:forEach>
		<c:choose><c:when test="${pageIndex!=pageCount}"><a class="next" href="#pagerForm" ref="${pageIndex+1}">下一頁</a></c:when>
		<c:otherwise><span class="next current">上一頁</span></c:otherwise></c:choose>
	<ul>
	</c:if>