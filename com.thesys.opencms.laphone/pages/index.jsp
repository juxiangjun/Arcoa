<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" session="true" import="java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*,org.opencms.xml.content.*, java.lang.String,org.opencms.workplace.*,org.opencms.search.*"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<%cms.init(pageContext,request,response);%>
</jsp:useBean>
<cms:include property="template" element="head"/>
     <cms:contentload collector="singleFile" param="%(opencms.uri)" editable="false" > 
    <div class="indexkv active">
    	<%--banner所需參數--%>
 	<% CmsSearchParameters params;%>
 	<%
 	java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy/MM/dd");
 	java.util.Date now = fmt.parse(fmt.format(new java.util.Date()));
 	pageContext.setAttribute("nowLong",now.getTime());
 	%>
	<c:set var="minDateLong" value="0"/>
	<c:set var="maxDateLong" value="2556028800000"/>
	<%--banner所需參數--%>
	<!--banner首頁覇刊頭-->
    	<jsp:useBean id="bannerSearch" scope="request" class="org.opencms.search.CmsSearch">
		<jsp:setProperty name="bannerSearch" property="index" value="LAPHONE_BANNER_INDEX"/>
		<jsp:setProperty name="bannerSearch" property="matchesPerPage" value="1000"/>
		<jsp:setProperty name="bannerSearch" property="displayPages" param="1000"/>
		<c:set var="query">OnlineDate:[${minDateLong} TO ${nowLong}] AND (OfflineDate:"0" OR OfflineDate:[${nowLong} TO ${maxDateLong}])</c:set>
		<c:set var="query">${query} AND (PrintPosition:"AD-960bar")</c:set>
	 	<%
		bannerSearch.init(cms.getCmsObject());
		params = bannerSearch.getParameters();
		params.setQuery((String)pageContext.getAttribute("query"));
		bannerSearch.setParameters(params);
		bannerSearch.setSortOrder(com.thesys.opencms.search.SearchHelper.getStringSort("PublishDate", true));
		%>
	</jsp:useBean>
    	<c:set var="searchResult" value="${bannerSearch.searchResult}" scope="request"/>
    	<c:choose>
		<c:when test="${!empty searchResult}">
			<%
			 	List xmlList = (List)request.getAttribute("searchResult");
			 	int c = xmlList.size();
			 	int randomInt = 0 ;
				if(c > 0) randomInt =(int) (Math.random() * c);
				request.setAttribute("randomInt",randomInt);
			%>
			<c:set var="fileName" value="${fn:replace(searchResult[randomInt].path,cms.requestContext.siteRoot,'')}" />
			<c:remove var="bannerSearch" scope="request"/>
		    	<cms:contentload collector="singleFile" param="${fileName}" editable="false" >
		    		<c:set var="barOpenType"><cms:contentshow element="CarouselBanner[1]/OpenType"/></c:set>
			    	<c:set var="barlink"><cms:contentshow element="CarouselBanner[1]/Link"/></c:set>
			    	<c:if test='${barlink == ""}'><c:set var="barlink" >#</c:set><c:set var="barOpenType" >_self</c:set></c:if>
				<c:if test='${barlink != "#" && fn:indexOf(barlink,"http") == -1}'><c:set var="barlink" ><cms:link>${barlink}</cms:link></c:set></c:if>
			        <div class="content"><a href="${barlink}"  target="${barOpenType}"><img width="960" height="330"  src='<cms:link><cms:contentshow element="CarouselBanner[1]/ImagePath"/></cms:link>' /></a></div>
			</cms:contentload>
        	</c:when>
		<c:otherwise>	
			<div class="content"><a href="#">沒有圖片</a></div>
		</c:otherwise>
	</c:choose>
    	
        <div class="buttons font14 grey">
            <a class="open"><img src="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/openad.jpg</cms:link>"/>&nbsp;展開觀看</a>
            <a class="close"><img src="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/closead.jpg</cms:link>" />&nbsp;關閉廣告</a>
        </div>
        <!--banner首頁覇刊頭-->
    </div>
    	<div class="block_group">
    	        <!--banner begin 左邊slideshow 首頁大輪播-->
    	        <jsp:useBean id="bannerSearch2" scope="request" class="org.opencms.search.CmsSearch">
		<jsp:setProperty name="bannerSearch2" property="index" value="LAPHONE_BANNER_INDEX"/>
		<jsp:setProperty name="bannerSearch2" property="matchesPerPage" value="1000"/>
		<jsp:setProperty name="bannerSearch2" property="displayPages" param="1000"/>
		<c:set var="query">OnlineDate:[${minDateLong} TO ${nowLong}] AND (OfflineDate:"0" OR OfflineDate:[${nowLong} TO ${maxDateLong}])</c:set>
		<c:set var="query">${query} AND (PrintPosition:"AD-kv")</c:set>
	 	<%
		bannerSearch2.init(cms.getCmsObject());
		params = bannerSearch2.getParameters();
		params.setQuery((String)pageContext.getAttribute("query"));
		bannerSearch2.setParameters(params);
		bannerSearch2.setSortOrder(com.thesys.opencms.search.SearchHelper.getStringSort("PublishDate", true));
		%>
		</jsp:useBean>
	    	<c:set var="searchResult" value="${bannerSearch2.searchResult}" scope="request"/>
	    	<div class="slideshow"><div id="slide_nav"></div>
	    		<c:choose>
				<c:when test="${!empty searchResult}">
					<c:forEach var="entry" items="${searchResult}" varStatus="status">
						<c:set var="xmlPath" value="${fn:replace(entry.path,cms.requestContext.siteRoot,'')}"/>
						<cms:contentload collector="thesysSingleFile" param="${xmlPath}" editable="false">
							<c:set var="kvOpenType"><cms:contentshow element="CarouselBanner[1]/OpenType"/></c:set>
							<c:set var="kvlink"><cms:contentshow element="CarouselBanner[1]/Link"/></c:set>
						    	<c:if test='${kvlink == ""}'><c:set var="kvlink" >#</c:set><c:set var="kvOpenType" >_self</c:set></c:if>
							<c:if test='${kvlink != "#" && fn:indexOf(kvlink,"http") == -1}'><c:set var="kvlink" ><cms:link>${kvlink}</cms:link></c:set></c:if>
						        <a href="${kvlink}"  target="${kvOpenType}"><img width="960" src='<cms:link><cms:contentshow element="CarouselBanner[1]/ImagePath"/></cms:link>' alt="圖片" width="460" /></a>
						</cms:contentload>
					</c:forEach>
		        	</c:when>
				<c:otherwise>	
					<a href="#">沒有圖片</a>
				</c:otherwise>
			</c:choose>	
	        </div>	        
        	<!--banner end 左邊slideshow 首頁大輪播-->
        </div>
        <!-- begin 固定版型TOP -->
        <div class="block_group block_group6">            
            <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/productBlock.jsp">
            	<cms:param name="blockIndex">1</cms:param>
	        <cms:param name="productCode"><cms:contentshow element="Product[1]/ProductCode"/></cms:param>
	        <cms:param name="imagePath"><cms:link><cms:contentshow element="Product[1]/ImagePath"/></cms:link></cms:param>
            </cms:include>
            <div class="news_block">
                <div class="news-title font16">
                    最新消息<a href="<cms:link>/news</cms:link>" class="more font12 grey">更多 </a>
                </div>
                <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/newsBlock.jsp"></cms:include>
            </div>
            <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/productBlock.jsp">
            	<cms:param name="blockIndex">3</cms:param>
	        <cms:param name="productCode"><cms:contentshow element="Product[2]/ProductCode"/></cms:param>
	        <cms:param name="imagePath"><cms:link><cms:contentshow element="Product[2]/ImagePath"/></cms:link></cms:param>
            </cms:include>
        </div>
        <!-- end 固定版型TOP -->
        
        <!-- begin 固定版型middle--> 
        <div class="block_group block_group4" style="float:right">
	    <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/productBlock.jsp">
            	<cms:param name="blockIndex">1</cms:param>
            	<cms:param name="productCode"><cms:contentshow element="Product[3]/ProductCode"/></cms:param>
            	<cms:param name="imagePath"><cms:link><cms:contentshow element="Product[3]/ImagePath"/></cms:link></cms:param>
            </cms:include>            
            <!-- 右邊廣告 start-->
            <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/banner.jsp">
            	<cms:param name="PrintPosition">AD-home214x209</cms:param>
            </cms:include>
	    <!-- 右邊廣告 end-->
            <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/productBlock.jsp">
            	<cms:param name="blockIndex">3</cms:param>
            	<cms:param name="productCode"><cms:contentshow element="Product[4]/ProductCode"/></cms:param>
            	<cms:param name="imagePath"><cms:link><cms:contentshow element="Product[4]/ImagePath"/></cms:link></cms:param>
            </cms:include>
        </div> 
        <!-- end  固定版型middle- -->

     </cms:contentload>
     <!-- begin 田字格 -->
     <c:set var="minDateLong" value="0"/>
	<c:set var="maxDateLong" value="2556028800000"/>
	<c:set var="currentUri"><cms:info property="opencms.request.uri"/></c:set>
	<c:set var="count">0</c:set>
	
	<jsp:useBean id="homeSearch" scope="request" class="org.opencms.search.CmsSearch">
		<jsp:setProperty name="homeSearch" property="index" value="LAPHONE_BLOCKGROUP_INDEX"/>
		<jsp:setProperty name="homeSearch" property="searchPage" param="1"/>
		<jsp:setProperty name="homeSearch" property="displayPages" value="100"/>
		<jsp:setProperty name="homeSearch" property="searchRoot" value="/blockGroup/"/>
		<c:set var="query">OnlineDate:[${minDateLong} TO ${nowLong}] AND (OfflineDate:"0" OR OfflineDate:[${nowLong} TO ${maxDateLong}])</c:set>
		<%
		homeSearch.init(cms.getCmsObject());
		CmsSearchParameters params = homeSearch.getParameters();
		params.setQuery((String)pageContext.getAttribute("query"));
		homeSearch.setParameters(params);
		homeSearch.setSortOrder(com.thesys.opencms.search.SearchHelper.getIntSort("Sort", false));
		%>
	</jsp:useBean>
	<c:set var="searchResult" value="${homeSearch.searchResult}"/>
	<c:if test="${!empty searchResult}">
		<c:forEach var="entry" items="${searchResult}" varStatus="status">
			<c:set var="xmlPath" value="${fn:replace(entry.path,cms.requestContext.siteRoot,'')}"/>
			<cms:contentload collector="thesysSingleFile" param="${xmlPath}" editable="false">
				<c:set var="blockIndex" value="0"/>
		    		<div class="block_group block_group<cms:contentshow element="GroupType"/>">
		    		<cms:contentloop element="Product">
	            		<c:set var="blockIndex" value="${blockIndex+1}"/>
				<cms:include file="/system/modules/com.thesys.opencms.laphone/elements/productBlock.jsp">
					<cms:param name="blockIndex">${blockIndex}</cms:param>
					<cms:param name="productCode"><cms:contentshow element="ProductCode"/></cms:param>
					<cms:param name="imagePath"><cms:link><cms:contentshow element="ImagePath"/></cms:link></cms:param>
				</cms:include>
				</cms:contentloop>
				</div>
			</cms:contentload>
		</c:forEach>
	</c:if>
     <!-- end 田字格 -->
     <cms:include file="/system/modules/com.thesys.opencms.laphone/jsps/event.jsp">
     	<cms:param name="displayPage">index</cms:param>
     </cms:include>
<cms:include property="template" element="foot" />