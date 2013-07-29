<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*,org.opencms.search.*"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<%cms.init(pageContext,request,response);%>
</jsp:useBean>

<jsp:useBean id="now" scope="request" class="java.util.Date"/>
<c:set var="nowLong" value="${now.time}"/>
<c:set var="minDateLong" value="0"/>
<c:set var="maxDateLong" value="2556028800000"/>
<jsp:useBean id="search" scope="request" class="org.opencms.search.CmsSearch">
	<jsp:setProperty name="search" property="index" value="LAPHONE_EVENT_INDEX"/>
	<jsp:setProperty name="search" property="searchPage" param="1"/>
	<jsp:setProperty name="search" property="displayPages" param="1000"/>
	<c:set var="query">(OnlineFlag:"true") AND OnlineDate:[${minDateLong} TO ${nowLong}] AND (OfflineDate:"0" OR OfflineDate:[${nowLong} TO ${maxDateLong}])</c:set>
	<% 
	search.init(cms.getCmsObject());
	CmsSearchParameters params = search.getParameters();
	params.setQuery((String)pageContext.getAttribute("query"));
	search.setParameters(params);
	search.setSortOrder(com.thesys.opencms.search.SearchHelper.getStringSort("EventDate", true));
	%>
</jsp:useBean>
<c:set var="searchResult" value="${search.searchResult}"/>

<c:choose>
	<c:when test="${param.displayPage=='index' and empty event}">
		<c:set var="event" value="true"  scope="session"/><!--記入session 每個session 只跑一次-->
		<div style="display:none">
			<c:if test="${!empty searchResult}">
			<script>
			$(function(){	$("#btnEventDialog").click();});
			</script>
			<a href="#eventDialog" id="btnEventDialog" class="btnLightbox"></a>         
			<div id="eventDialog" class="laphoneDialog">
				<h4>緊急公告<a class="btnDialogClose"></a></h4>
				<div class="dialogContent">
					<c:forEach var="entry" items="${searchResult}" varStatus="status">
						<c:set var="xmlPath" value="${fn:replace(entry.path,cms.requestContext.siteRoot,'')}"/>
						<cms:contentload collector="thesysSingleFile" param="${xmlPath}" editable="false">
							<c:set var="LinkFlag"><cms:contentshow element="LinkFlag"/></c:set>
							<c:set var="Link"><cms:contentshow element="Link"/></c:set>
							<c:choose>
								<c:when test="${LinkFlag=='true' && not empty Link}">
								■<a href="<cms:contentshow element="Link"/>"><cms:contentshow element="EventTitle"/><a><br>
								</c:when>
								<c:otherwise>
									■<cms:contentshow element="EventTitle"/><br>
								</c:otherwise>
							</c:choose>
						</cms:contentload>
					</c:forEach>
				   <div class="btn-line-center"><a href="#" class="button btnDialogSubmit">確定</a></div>
				</div>
			</div>
			</c:if> 
		</div>
	</c:when>
	<c:when test="${param.displayPage=='memberCenter'}">
		<c:choose>
			<c:when test="${!empty searchResult}">
				<jsp:useBean id="dateValue" class="java.util.Date" /> 	
				<c:forEach var="entry" items="${searchResult}" varStatus="status">
					<c:set var="xmlPath" value="${fn:replace(entry.path,cms.requestContext.siteRoot,'')}"/>
					<cms:contentload collector="thesysSingleFile" param="${xmlPath}" editable="false">
						<c:set var="EventTitle"><cms:contentshow element="EventTitle"/></c:set>
						<c:set var="EventDate"><cms:contentshow element="EventDate"/></c:set>
						<c:set var="Link"><cms:contentshow element="Link"/></c:set>
						<c:set var="LinkFlag"><cms:contentshow element="LinkFlag"/></c:set>
						<c:set var="EventDateStr"></c:set>
						<c:if test="${not empty EventDate && EventDate!='0'}">
							<jsp:setProperty name="dateValue" property="time" value="${EventDate}" />
							<c:set var="EventDateStr"><fmt:formatDate value="${dateValue}" pattern="yyyy/MM/dd" />&nbsp;</c:set>
						</c:if>
						<p>
						<c:choose>
							<c:when test="${LinkFlag=='true' && not empty Link}">
								<a href="<cms:contentshow element="Link"/>">${EventDateStr}${EventTitle}<a>
							</c:when>
							<c:otherwise>
								${EventDateStr}${EventTitle}
							</c:otherwise>
						</c:choose>	
						</p>
					</cms:contentload>
				</c:forEach>
			</c:when>
			<c:otherwise>
				目前尚無公告
			</c:otherwise>
		</c:choose>
	</c:when>
</c:choose>