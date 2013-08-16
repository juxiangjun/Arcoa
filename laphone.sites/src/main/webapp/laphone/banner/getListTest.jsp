<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="java.util.*,org.opencms.jsp.*,org.opencms.file.*,org.opencms.xml.content.*,org.opencms.search.*,org.opencms.json.*" %>
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
<c:set var="printPosition" value="${param.PrintPosition}" scope="request"/>
<c:set var="printPosition" value="AD-home214x209" scope="request"/>
<%
	CmsSearchParameters params;

//"AD-home214x209:adbox," +	//首頁右側二小
//"AD-new943x100:banner," +	//新品上市中間橫幅
//"AD-sp943x100:banner," +	//精選商品中間橫幅
//"AD-pd943x100:banner," +	//其他商品列表頁中間橫幅
//"AD-pd214x209:adbox" +	//商品內頁右側二小
%>

<c:if test="${!empty printPosition}">	
	<jsp:useBean id="bannerSearch" scope="request" class="org.opencms.search.CmsSearch">
		<jsp:setProperty name="bannerSearch" property="index" value="LAPHONE_BANNER_INDEX"/>
		<jsp:setProperty name="bannerSearch" property="matchesPerPage" value="1000"/>
		<jsp:setProperty name="bannerSearch" property="displayPages" param="1000"/>
		<c:set var="query">OnlineDate:[${minDateLong} TO ${nowLong}] AND (OfflineDate:"0" OR OfflineDate:[${nowLong} TO ${maxDateLong}])</c:set>
		<c:set var="query">${query} AND (PrintPosition:"${printPosition}")</c:set>
	 	<%
		bannerSearch.init(cms.getCmsObject());
		params = bannerSearch.getParameters();
		params.setQuery((String)pageContext.getAttribute("query"));
		bannerSearch.setParameters(params);
		bannerSearch.setSortOrder(com.thesys.opencms.search.SearchHelper.getStringSort("PublishDate", true));
		%>
	</jsp:useBean>
	<c:set var="searchResult" value="${bannerSearch.searchResult}" scope="request"/>
	<c:if test="${!empty searchResult}">
		<%
		 	List xmlList = (List)request.getAttribute("searchResult");
		 	int c = xmlList.size();
		 	int randomInt = 0 ;
			if(c > 0) randomInt =(int) (Math.random() * c);
			request.setAttribute("randomInt",randomInt);
		%>
		<c:set var="fileName" value="${fn:replace(searchResult[randomInt].path,cms.requestContext.siteRoot,'')}" />
	</c:if>
	<c:remove var="bannerSearch" scope="request"/>
</c:if>
<!-- 右邊廣告 -->
<c:if test='${!empty fileName }'> 
<style>
.adbox1 li img{width:214px;height:209px;border:1px solid #d3d2d3;margin-left:12px;margin-right:12px;margin-top:6px;}    
.adbox2 li img{width:214px;height:98px;border:1px solid #d3d2d3;margin-left:12px;margin-right:12px;margin-top:6px;}    
</style>
    <cms:contentload collector="singleFile" param="${fileName}" editable="false" >
	<c:set var="count" ><cms:contentshow element="Count"/></c:set>
	<c:set var="link"><cms:contentshow element="CarouselBanner[1]/Link"/></c:set>
	<c:set var="openType"><cms:contentshow element="CarouselBanner[1]/OpenType"/></c:set>
	<c:set var="classType" value="adbox" />
	<c:if test='${printPosition == "AD-new943x100" || printPosition == "AD-sp943x100" || printPosition == "AD-pd943x100"}'>
		<c:set var="classType" value="banner" />
	</c:if>
	
	<ul class="${classType}${count}" >
		<c:if test='${link == ""}'><c:set var="link" >#</c:set><c:set var="openType" >_self</c:set></c:if>
		<c:if test='${link != "#" && fn:indexOf(link,"http") == -1}'><c:set var="link" ><cms:link>${link}</cms:link></c:set></c:if>
		<li><a href="${link}" target="${openType}"><img src="<cms:link><cms:contentshow element="CarouselBanner[1]/ImagePath"/></cms:link>" alt="說明文字" /></a></li>
		<c:if test="${count == 2}">
			<c:set var="openType"><cms:contentshow element="CarouselBanner[2]/OpenType"/></c:set> 
			<c:set var="link2" ><cms:contentshow element="CarouselBanner[2]/Link"/></c:set>
			<c:if test='${link2 == ""}'><c:set var="link2" >#</c:set><c:set var="openType" >_self</c:set></c:if>
			<c:if test='${link2 != "#" && fn:indexOf(link2,"http") == -1}'><c:set var="link2" ><cms:link>${link2}</cms:link></c:set></c:if>
			<li><a href="${link2}" target="${openType}"><img src="<cms:link><cms:contentshow element="CarouselBanner[2]/ImagePath"/></cms:link>" alt="說明文字" /></a></li>
		</c:if> 
        </ul> 
    </cms:contentload>
</c:if>  
<!-- 右邊廣告 -->