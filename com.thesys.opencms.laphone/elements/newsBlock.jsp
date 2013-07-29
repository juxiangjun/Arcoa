<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="org.apache.lucene.search.*,org.opencms.search.*"%>
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
<jsp:useBean id="search" scope="page" class="org.opencms.search.CmsSearch">
	<jsp:setProperty name="search" property="index" value="LAPHONE_NEWS_INDEX"/>
	<jsp:setProperty name="search" property="matchesPerPage" value="4"/>
	<jsp:setProperty name="search" property="searchPage" value="1"/>
	<jsp:setProperty name="search" property="displayPages" param="1000"/>
	<c:set var="query">OnlineFlag:"true" AND OnlineDate:[${minDateLong} TO ${nowLong}] AND (OfflineDate:"0" OR OfflineDate:[${nowLong} TO ${maxDateLong}])</c:set>
	<%
	search.init(cms.getCmsObject());
	CmsSearchParameters params = search.getParameters();
	params.setQuery((String)pageContext.getAttribute("query"));
	search.setParameters(params);
	search.setSortOrder(com.thesys.opencms.search.SearchHelper.getStringSort("PublishDate", true));
	%>
</jsp:useBean>
<c:set var="searchResult" value="${pageScope.search.searchResult}"/>
<c:if test="${!empty searchResult}">
	<c:forEach var="entry" items="${searchResult}" varStatus="status">
		<c:set var="xmlPath" value="${fn:replace(entry.path,cms.requestContext.siteRoot,'')}"/>
		<cms:contentload collector="thesysSingleFile" param="${xmlPath}" editable="false">
			<c:set var="OnlineDate" scope="request"><cms:contentshow element="PublishDate"/></c:set>
			<div class="news-box">                    
				<div class="news-box-text1">
	                    		<c:set var="publishDate"><cms:contentshow element="PublishDate"/></c:set>
		                    	<jsp:useBean id="dateValue" class="java.util.Date" /> 
					<jsp:setProperty name="dateValue" property="time" value="${publishDate}" /> 
					<fmt:formatDate value="${dateValue}" pattern="yyyy/MM/dd" />
				</div>
				<div class="news-box-text2 font16 red">
					<a href="<cms:link><cms:contentshow element="%(opencms.filename)"/></cms:link>?y=<fmt:formatDate value="${dateValue}" pattern="yyyy" />&m=<fmt:formatDate value="${dateValue}" pattern="MM" />" title="<cms:contentshow element="Title"/>">
	                        		<cms:contentshow element="Title"/>
	                        	</a>
				</div>
	                </div>
		</cms:contentload>
	</c:forEach>
</c:if>
<c:remove var="search" scope="page"/>