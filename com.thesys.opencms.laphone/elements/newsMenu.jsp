<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="java.net.*,java.util.*,org.apache.lucene.search.*,org.opencms.main.*,org.opencms.search.*,org.opencms.jsp.*,org.opencms.file.*,org.opencms.workplace.*"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%!
Calendar calendar = Calendar.getInstance();
public long [] getYearRange(java.util.Date date){
	long [] result = new long[2];
	calendar.setTime(date);
	calendar.set(Calendar.MONTH, 0);
	calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
	result[0] = calendar.getTimeInMillis();
	
	calendar.set(Calendar.MONTH, 11);
	calendar.set(Calendar.DAY_OF_MONTH, 31);
	calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        result[1] = calendar.getTimeInMillis();
        
        return result;
}
%>
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<%cms.init(pageContext,request,response);%>
</jsp:useBean>
<jsp:useBean id="now" scope="request" class="java.util.Date"/>
<c:set var="nowLong" value="${now.time}"/>
<c:set var="minDateLong" value="0"/>
<c:set var="maxDateLong" value="2556028800000"/>
<%
String y = request.getParameter("y");
String m = request.getParameter("m");

String [] monthName = {"January","February","March","April","May","June","July","August","September","October","November","December"};
String [] dateRange = new String[2];
int count = 0;

CmsSearchParameters params;
%>
<c:forEach var="x" begin="0" end="1" step="1">
	<jsp:useBean id="search" scope="request" class="org.opencms.search.CmsSearch">
		<jsp:setProperty name="search" property="index" value="LAPHONE_NEWS_INDEX"/>
		<jsp:setProperty name="search" property="matchesPerPage" value="1"/>
		<jsp:setProperty name="search" property="searchPage" param="pageIndex"/>
		<jsp:setProperty name="search" property="displayPages" param="1000"/>
		<c:set var="query">OnlineFlag:"true" AND OnlineDate:[${minDateLong} TO ${nowLong}] AND (OfflineDate:"0" OR OfflineDate:[${nowLong} TO ${maxDateLong}])</c:set>
		<%
		search.init(cms.getCmsObject());
		params = search.getParameters();
		params.setQuery((String)pageContext.getAttribute("query"));
		search.setParameters(params);
		boolean flag = false;
		if(count!=0) flag = true;
		search.setSortOrder(com.thesys.opencms.search.SearchHelper.getStringSort("PublishDate", flag));
		%>
	</jsp:useBean>
	<c:set var="searchResult" value="${search.searchResult}"/>
	<c:if test="${!empty searchResult}">
		<c:forEach var="entry" items="${searchResult}" varStatus="status">
			<c:set var="xmlPath" value="${fn:replace(entry.path,cms.requestContext.siteRoot,'')}"/>
			<cms:contentload collector="thesysSingleFile" param="${xmlPath}" editable="false">
				<c:set var="OnlineDate" scope="request"><cms:contentshow element="PublishDate"/></c:set>
				<%
				dateRange[count] = (String)request.getAttribute("OnlineDate");
				%>
			</cms:contentload>
		</c:forEach>
	</c:if>
	<c:remove var="search" scope="request"/>
	<%
	count++;
	%>
</c:forEach>
<script>
$(function() {
	$("li[ref='year']").click(function(){
		var mark = $(this).attr("mark");
		if($(this).attr("class")=='selected') {
			$(this).attr("class","year");
			$("#mark_"+mark).html("+");
		} else {
			$(this).attr("class","selected");
			$("#mark_"+mark).html("-");
		}
	});
});
</script>
<div class="news-menu"> 
	<h3>ARCHIVE</h3>          
        <ul >
  	<%
	if(dateRange[0]!=null && dateRange[1]!=null){
		long dateRangeLong = Long.parseLong(dateRange[0]);
		Date startDate = new Date(dateRangeLong);
		dateRangeLong = Long.parseLong(dateRange[1]);
		Date endDate = new Date(dateRangeLong);
		String outStr = "";
		
		if(y==null || m==null){
			calendar.setTime(endDate);
  			y = String.valueOf(calendar.get(Calendar.YEAR));
  			m = String.valueOf(calendar.get(Calendar.MONTH)+1);
  		}
		//while(endDate.compareTo(startDate)>=0){  //假如最新一年的月份筆最舊一年的月份還小的話，最舊的一年會搜不出來
		while(endDate.getYear() >= startDate.getYear()){	 //所以直接比年的大小
			long[] temp = getYearRange(endDate);
			request.setAttribute("start",temp[0]);
			request.setAttribute("end",temp[1]);
			request.setAttribute("endDate",endDate);
			
			calendar.setTime(endDate);
			String yearStr = String.valueOf(calendar.get(Calendar.YEAR));
	%>
	<jsp:useBean id="search2" scope="request" class="org.opencms.search.CmsSearch">
		<jsp:setProperty name="search2" property="index" value="LAPHONE_NEWS_INDEX"/>
		<jsp:setProperty name="search2" property="matchesPerPage" value="1"/>
		<jsp:setProperty name="search2" property="searchPage" param="pageIndex"/>
		<jsp:setProperty name="search2" property="displayPages" param="1000"/>
		<c:set var="query">OnlineFlag:"true" AND OnlineDate:[${minDateLong} TO ${nowLong}] AND (OfflineDate:"0" OR OfflineDate:[${nowLong} TO ${maxDateLong}]) AND PublishDate:[${start} TO ${end}]</c:set>
		<%
		search2.init(cms.getCmsObject());
		params = search2.getParameters();
		params.setQuery((String)pageContext.getAttribute("query"));
		search2.setParameters(params);
		search2.setSortOrder(com.thesys.opencms.search.SearchHelper.getStringSort("PublishDate", false));
		%>
	</jsp:useBean>
	<c:set var="searchResult2" value="${search2.searchResult}"/>
	<c:if test="${!empty searchResult2}">
		<li class="<%=(yearStr.equals(y))?"selected":"year"%>" ref="year" mark="<%=yearStr%>">
			<a class="year"><span id="mark_<%=yearStr%>"><%=(yearStr.equals(y))?"-":"+"%></span><%=yearStr%></a>
  			<ul>
  				<%
  				for(int i=11;i>=0;i--){
	  				calendar.set(Calendar.MONTH, i);
	  				calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	  				request.setAttribute("endMonth",String.valueOf(calendar.getTimeInMillis()));
	  				
	  				calendar.set(Calendar.DAY_OF_MONTH, 1);
	  				calendar.set(Calendar.HOUR_OF_DAY, 0);
				        calendar.set(Calendar.MINUTE, 0);
				        calendar.set(Calendar.SECOND, 0);
	  				request.setAttribute("startMonth",String.valueOf(calendar.getTimeInMillis()));
  				%>
  				<jsp:useBean id="search3" scope="request" class="org.opencms.search.CmsSearch">
					<jsp:setProperty name="search3" property="index" value="LAPHONE_NEWS_INDEX"/>
					<jsp:setProperty name="search3" property="matchesPerPage" value="1"/>
					<jsp:setProperty name="search3" property="searchPage" param="pageIndex"/>
					<jsp:setProperty name="search3" property="displayPages" param="1000"/>
					<c:set var="query">OnlineFlag:"true" AND OnlineDate:[${minDateLong} TO ${nowLong}] AND (OfflineDate:"0" OR OfflineDate:[${nowLong} TO ${maxDateLong}]) AND PublishDate:[${startMonth} TO ${endMonth}]</c:set>
					<%
					search3.init(cms.getCmsObject());
					params = search3.getParameters();
					params.setQuery((String)pageContext.getAttribute("query"));
					search3.setParameters(params);
					search3.setSortOrder(com.thesys.opencms.search.SearchHelper.getStringSort("PublishDate", false));
					%>
				</jsp:useBean>
				<c:set var="searchResult3" value="${search3.searchResult}"/>
				<c:if test="${!empty searchResult3}">
					<li <%=(yearStr.equals(y) && String.valueOf(i+1).equals(m)?"class=\"selected\"":"")%>><a href="<cms:link>/news</cms:link>?y=<%=yearStr%>&m=<%=(i+1)%>"><%=monthName[i]%></a></li>
				</c:if>
				<c:remove var="search3" scope="request"/>
  				<%
  				}
  				%>
  			</ul>
  		</li>
	</c:if>
	<c:remove var="search2" scope="request"/>
	<%
		calendar.setTime(endDate);
		calendar.add(Calendar.YEAR, -1);
		endDate = calendar.getTime();
		}
	}
	%>
        </ul>
        <a class="rss">RSS FEED</a>
</div>