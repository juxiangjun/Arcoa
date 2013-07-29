<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="org.opencms.search.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="ratingHandler" scope="request" class="com.thesys.opencms.laphone.product.ThesysRatingHandler">
<%ratingHandler.init(pageContext,request,response);%>
</jsp:useBean>
<c:set var="bannerIndex">8</c:set><%--每隔幾格商品出現一則廣告限為4的倍數--%>
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<%cms.init(pageContext,request,response);%>
</jsp:useBean>
<jsp:useBean id="now" scope="request" class="java.util.Date"/>	
<cms:contentload collector="singleFile" param="%(opencms.uri)" editable="false" > 
	<c:set var="categoryId"><cms:contentshow element="CategoryId"/></c:set>	
  	<jsp:useBean id="search" scope="request" class="org.opencms.search.CmsSearch">
		<jsp:setProperty name="search" property="index" value="LAPHONE_PRODUCT_INDEX"/>
		<jsp:setProperty name="search" property="matchesPerPage" value="40"/>
		<jsp:setProperty name="search" property="searchPage" param="searchPage"/>
		<c:set var="query">(OnlineFlag:"true") AND (OnlineDate:[0 TO ${now.time}]) AND (OfflineDate:[${now.time} TO 2556028800000])</c:set>
		<c:if test="${categoryId!='search'}">
			<cms:contentloop element="SapCategoryCode">
				<c:if test="${!empty categoryQuery}"><c:set var="categoryQuery">${categoryQuery} OR </c:set></c:if>
				<c:set var="categoryQuery">${categoryQuery} (SapCategoryCode:"SAP[<cms:contentshow/>")</c:set>
			</cms:contentloop>			
	                <c:if test="${!empty categoryQuery}">	                
				<c:if test="${!empty query}"><c:set var="query">${query} AND </c:set></c:if>
				<c:set var="query">${query} (${categoryQuery})</c:set>
			</c:if>
		</c:if>
		<c:if test="${!empty param.keyword}">
			<c:set var="query">(Content:"${param.keyword}")</c:set>
		</c:if>
		<c:if test="${!empty param.searchColor && param.searchColor!='all'}">
			<cms:contentload collector="singleFile" param="/_config_/colorSetting.html" editable="false">
	                	<cms:contentloop element="ColorGroup">
	                		<c:set var="colorGroupId"><cms:contentshow element="ColorGroupId"/></c:set>
	                		<c:if test="${colorGroupId==param.searchColor}">
	                			<cms:contentloop element="Color">
	                			<c:if test="${!empty colorQuery}"><c:set var="colorQuery">${colorQuery} OR </c:set></c:if>
						<c:set var="colorQuery">${colorQuery} (ColorCode:"<cms:contentshow element="ColorCode"/>")</c:set>
	                			</cms:contentloop>
	                		</c:if>
	                	</cms:contentloop>
	                </cms:contentload>
	                <c:if test="${!empty colorQuery}">	                
				<c:if test="${!empty query}"><c:set var="query">${query} AND </c:set></c:if>
				<c:set var="query">${query} (${colorQuery})</c:set>
			</c:if>
		</c:if>		
		<c:if test="${!empty param.searchRating && param.searchRating!='all'}">
			<c:choose>
			<c:when test="${param.searchRating==1}">
				<jsp:setProperty name="ratingHandler" property="ratingStart" value="1"/>
				<jsp:setProperty name="ratingHandler" property="ratingEnd" value="99"/>
			</c:when>
			<c:when test="${param.searchRating==2}">			
				<jsp:setProperty name="ratingHandler" property="ratingStart" value="100"/>
				<jsp:setProperty name="ratingHandler" property="ratingEnd" value="199"/>
			</c:when>
			<c:when test="${param.searchRating==3}">
				<jsp:setProperty name="ratingHandler" property="ratingStart" value="200"/>
				<jsp:setProperty name="ratingHandler" property="ratingEnd" value="299"/>
			</c:when>
			<c:when test="${param.searchRating==4}">
				<jsp:setProperty name="ratingHandler" property="ratingStart" value="300"/>
				<jsp:setProperty name="ratingHandler" property="ratingEnd" value="399"/>
			</c:when>
			<c:when test="${param.searchRating==5}">
				<jsp:setProperty name="ratingHandler" property="ratingStart" value="400"/>
				<jsp:setProperty name="ratingHandler" property="ratingEnd" value="99999999"/>
			</c:when>
			</c:choose>
			<c:set var="ratingItems" value="${ratingHandler.ratingItems}"/>
			<c:forEach var="itemId" items="${ratingItems}">
				<c:if test="${!empty ratingQuery}"><c:set var="ratingQuery">${ratingQuery} OR </c:set></c:if>
				<c:set var="ratingQuery">${ratingQuery} (SapProductCode:"${itemId}")</c:set>
			</c:forEach>
			<c:choose>
			<c:when test="${!empty ratingQuery}">	                
				<c:if test="${!empty query}"><c:set var="query">${query} AND </c:set></c:if>
				<c:set var="query">${query} (${ratingQuery})</c:set>
			</c:when>
			<c:otherwise>
				<c:if test="${!empty query}"><c:set var="query">${query} AND </c:set></c:if>
				<c:set var="query">${query} (SapProductCode:"查無資料")</c:set>
			</c:otherwise>
			</c:choose>
			
		</c:if>	
		<c:if test="${!empty param.searchPrice && param.searchPrice!='all'}">
			<c:if test="${!empty query}"><c:set var="query">${query} AND </c:set></c:if>
			<c:choose>
			<c:when test="${param.searchPrice==1}"><c:set var="query">${query} (SpecialPrice:[000000 TO 000299])</c:set></c:when>
			<c:when test="${param.searchPrice==2}"><c:set var="query">${query} (SpecialPrice:[000300 TO 000799])</c:set></c:when>
			<c:when test="${param.searchPrice==3}"><c:set var="query">${query} (SpecialPrice:[000800 TO 001499])</c:set></c:when>
			<c:when test="${param.searchPrice==4}"><c:set var="query">${query} (SpecialPrice:[001500 TO 002999])</c:set></c:when>
			<c:when test="${param.searchPrice==5}"><c:set var="query">${query} (SpecialPrice:[003000 TO 005999])</c:set></c:when>
			<c:when test="${param.searchPrice==6}"><c:set var="query">${query} (SpecialPrice:[006000 TO 009999])</c:set></c:when>
			<c:when test="${param.searchPrice==7}"><c:set var="query">${query} (SpecialPrice:[010000 TO 999999])</c:set></c:when>
			</c:choose>
		</c:if>		
		<c:if test="${!empty param.searchStyle && param.searchStyle!='all'}">
			<c:if test="${!empty query}"><c:set var="query">${query} AND </c:set></c:if>
			<c:set var="query">${query} (Style:"${param.searchStyle}")</c:set>
		</c:if>
	<%
	String sortId = request.getParameter("sortId");
	search.init(cms.getCmsObject());
	CmsSearchParameters params = search.getParameters();
	params.setQuery((String)pageContext.getAttribute("query"));
	search.setParameters(params);
	if("2".equals(sortId))
		search.setSortOrder(com.thesys.opencms.search.SearchHelper.getIntSort("SpecialPrice",false));
	else if("5".equals(sortId))
		search.setSortOrder(com.thesys.opencms.search.SearchHelper.getStringSort("OnlineDate",true));
	else
		search.setSortOrder(com.thesys.opencms.search.SearchHelper.getIntSort("SpecialPrice",true));
	%>
 </jsp:useBean>		
	<c:set var="searchResult" value="${search.searchResult}"/>	
	<c:set var="resultCount" value="${search.searchResultCount}"/>		
	<fmt:parseNumber  parseLocale="#" integerOnly="true" value="${resultCount/search.displayPages}" var="pageCount" />
	<c:if test="${resultCount%search.displayPages>0}"><c:set var="pageCount" value="${pageCount+1}"/></c:if>
	
<cms:include property="template" element="head">
	<cms:param name="resultCount" value="${resultCount}"/>
	<cms:param name="pageCount" value="${pageCount}"/>
</cms:include>
<c:set var="sortId">${param.sortId}</c:set>
<c:if test="${empty sortId || sortId=='0' || sortId=='5'}"><c:set var="sortId">1</c:set></c:if>
<script type="text/javascript">
function setSort(sortId) {
	$("#sorting-select").val(sortId);
	$("#sorting-select").msDropDown({ mainCSS: 'dd' }).data("dd");
}
$(function () {
	$("#sorting-select").change(function(){
		var sortId = $("#sorting-select").val();
		if(sortId == '0') sortId = 1;
		setSort(sortId);
		location.href = "?searchColor=${param.searchColor}&searchPrice=${param.searchPrice}&searchRating=${param.searchRating}&searchStyle=${param.searchStyle}&sortId="+sortId;
	});
	setSort(${sortId});
});
</script>
   <div id="content">     
	<c:choose><c:when test="${!empty searchResult}">
	
	<c:forEach var="entry" items="${searchResult}" varStatus="status">
	     <c:set var="index" value="${status.index}" />
		<c:set var="productXml" value="${fn:replace(entry.path,cms.requestContext.siteRoot,'')}"/>
		<cms:include file="/system/modules/com.thesys.opencms.laphone/elements/productBlock.jsp">
	        <cms:param name="blockIndex">0</cms:param>
		<cms:param name="productXml">${productXml}</cms:param>
	        </cms:include>
	     <c:if test="${(index+1) == bannerIndex || (index+1) == bannerIndex*2 }">
		    <!-- 水平廣告 -->
	            <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/banner.jsp">
	            	<cms:param name="PrintPosition">PRODUCTLIST</cms:param>
	            </cms:include>
		    <!-- 水平廣告 -->
	     </c:if>   
	</c:forEach>
	     <c:if test="${(index+1) < bannerIndex}">
		    <!-- 水平廣告 -->
	            <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/banner.jsp">
	            	<cms:param name="PrintPosition">PRODUCTLIST</cms:param>
	            </cms:include>
		    <!-- 水平廣告 -->
	     </c:if>
	</c:when>
	<c:otherwise>	
		<div style="height:300px;font-size:13px">&nbsp;<br/>&nbsp;無符合查詢條件之結果</div>
	</c:otherwise>
	</c:choose>
	<c:if test="${!empty search.pageLinks}">
	     <c:set var="searchParam" value="searchColor=${param.searchColor}&searchPrice=${param.searchPrice}&searchRating=${param.searchRating}&searchStyle=${param.searchStype}"/>
	     <ul class="pager">
		<c:choose>
			<c:when test="${!empty search.previousUrl}"><a class="prev" href="<cms:link><cms:info property="opencms.request.uri"/></cms:link>?${searchParam}&searchPage=${param.searchPage-1}">上一頁</a></c:when>
			<c:otherwise><span class="current prev">上一頁</span></c:otherwise>
		</c:choose>
		<c:forEach var="map" items="${search.pageLinks}">
			<c:set var="page" value="${map.key}"/>
			<c:choose>
				<c:when test="${page==param.searchPage || (empty param.searchPage &&page==1)}"><span class="current">${page}</span></c:when>
				<c:otherwise><a href="<cms:link><cms:info property="opencms.request.uri"/></cms:link>?${searchParam}&searchPage=${page}">${page}</a></c:otherwise>
			</c:choose>
		</c:forEach>
		<c:choose>
			<c:when test="${empty search.nextUrl}"><span class="current next">下一頁</span></c:when>
			<c:when test="${empty param.searchPage}"><a class="next" href="<cms:link><cms:info property="opencms.request.uri"/></cms:link>?${searchParam}&searchPage=2">下一頁</a></c:when>
			<c:otherwise><a class="next" href="<cms:link><cms:info property="opencms.request.uri"/></cms:link>?${searchParam}&searchPage=${param.searchPage+1}">下一頁</a></c:otherwise>
		</c:choose>
	     </ul>
	 </c:if>    
     </div>
     <cms:include property="template" element="foot" />      
</cms:contentload>

