<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="org.opencms.search.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="ratingHandler" scope="request" class="com.thesys.opencms.laphone.product.ThesysRatingHandler">
<%ratingHandler.init(pageContext,request,response);%>
</jsp:useBean>
<%   
com.thesys.opencms.laphone.system.ThesysParamHandler paramHandler = new com.thesys.opencms.laphone.system.ThesysParamHandler(pageContext, request, response);
String photoUrl = paramHandler.getValueByParamKey("PRODUCT_PHOTO_URL");
if(photoUrl.endsWith("/")) photoUrl = photoUrl.substring(0,photoUrl.length()-1);
pageContext.setAttribute("productPhotoUrl",photoUrl);

%>
<c:set var="bannerIndex">8</c:set><%--每隔幾格商品出現一則廣告限為4的倍數--%>

<jsp:useBean id="search" scope="request" class="com.thesys.opencms.laphone.product.ThesysProductSearchHandler">
<%search.init(pageContext,request,response);%>
</jsp:useBean>
<jsp:setProperty name="search" property="pageSize" value="40"/>
<jsp:setProperty name="search" property="pageIndex" param="pageIndex"/>
		
<cms:contentload collector="singleFile" param="%(opencms.uri)" editable="false" > 
	<c:set var="categoryId"><cms:contentshow element="CategoryId"/></c:set>	  	
	<c:if test="${categoryId!='search'}">
		<cms:contentloop element="SapCategoryCode">
			<c:if test="${!empty categoryQuery}"><c:set var="categoryQuery" value="${categoryQuery};"/></c:if>
			<c:set var="categoryQuery">${categoryQuery}<cms:contentshow/></c:set>
		</cms:contentloop>			
                <c:if test="${!empty categoryQuery}">	                
			<c:set var="categoryIds" value="${fn:split(categoryQuery,';')}"/>
			<jsp:setProperty name="search" property="categoryIds" value="${categoryIds}"/>
		</c:if>
	</c:if>
</cms:contentload>		
<c:if test="${!empty param.keyword}">
	<jsp:setProperty name="search" property="keyword" param="keyword"/>
</c:if>
<c:if test="${!empty param.searchColor && param.searchColor!='all'}">
	<cms:contentload collector="singleFile" param="/_config_/colorSetting.html" editable="false">
        	<cms:contentloop element="ColorGroup">
        		<c:set var="colorGroupId"><cms:contentshow element="ColorGroupId"/></c:set>
        		<c:if test="${colorGroupId==param.searchColor}">
        			<cms:contentloop element="Color">
        			<c:if test="${!empty colorQuery}"><c:set var="colorQuery" value="${colorQuery};"/></c:if>
				<c:set var="colorQuery">${colorQuery}<cms:contentshow element="ColorCode"/></c:set>
        			</cms:contentloop>
        		</c:if>
        	</cms:contentloop>
        </cms:contentload>
        <c:if test="${!empty colorQuery}">
        	<c:set var="colors" value="${fn:split(colorQuery,';')}"/>
		<jsp:setProperty name="search" property="colors" value="${colors}"/>	  
	</c:if>
</c:if>		
<c:if test="${!empty param.searchRating && param.searchRating!='all'}">
	<c:choose>
	<c:when test="${param.searchRating==1}">
		<jsp:setProperty name="search" property="ratingStart" value="1"/>
		<jsp:setProperty name="search" property="ratingEnd" value="99"/>
	</c:when>
	<c:when test="${param.searchRating==2}">			
		<jsp:setProperty name="search" property="ratingStart" value="100"/>
		<jsp:setProperty name="search" property="ratingEnd" value="199"/>
	</c:when>
	<c:when test="${param.searchRating==3}">
		<jsp:setProperty name="search" property="ratingStart" value="200"/>
		<jsp:setProperty name="search" property="ratingEnd" value="299"/>
	</c:when>
	<c:when test="${param.searchRating==4}">
		<jsp:setProperty name="search" property="ratingStart" value="300"/>
		<jsp:setProperty name="search" property="ratingEnd" value="399"/>
	</c:when>
	<c:when test="${param.searchRating==5}">
		<jsp:setProperty name="search" property="ratingStart" value="400"/>
		<jsp:setProperty name="search" property="ratingEnd" value="99999999"/>
	</c:when>
	</c:choose>			
</c:if>	
<c:if test="${!empty param.searchPrice && param.searchPrice!='all'}">
	<c:choose>
	<c:when test="${param.searchPrice==1}">
		<jsp:setProperty name="search" property="priceStart" value="0"/>
		<jsp:setProperty name="search" property="priceEnd" value="299"/>			
	</c:when>
	<c:when test="${param.searchPrice==2}">
		<jsp:setProperty name="search" property="priceStart" value="300"/>
		<jsp:setProperty name="search" property="priceEnd" value="799"/>
	</c:when>
	<c:when test="${param.searchPrice==3}">
		<jsp:setProperty name="search" property="priceStart" value="800"/>
		<jsp:setProperty name="search" property="priceEnd" value="1499"/>
	</c:when>
	<c:when test="${param.searchPrice==4}">
		<jsp:setProperty name="search" property="priceStart" value="1500"/>
		<jsp:setProperty name="search" property="priceEnd" value="2999"/>
	</c:when>
	<c:when test="${param.searchPrice==5}">
		<jsp:setProperty name="search" property="priceStart" value="3000"/>
		<jsp:setProperty name="search" property="priceEnd" value="5999"/>
	</c:when>
	<c:when test="${param.searchPrice==6}">
		<jsp:setProperty name="search" property="priceStart" value="6000"/>
		<jsp:setProperty name="search" property="priceEnd" value="9999"/>
	</c:when>
	<c:when test="${param.searchPrice==7}">
		<jsp:setProperty name="search" property="priceStart" value="10000"/>
		<jsp:setProperty name="search" property="priceEnd" value="999999"/>
	</c:when>
	</c:choose>
</c:if>		
<c:if test="${!empty param.searchStyle && param.searchStyle!='all'}">
	<jsp:setProperty name="search" property="styles" param="searchStyle"/>
</c:if>
<c:if test="${!empty param.sortType}">
	<jsp:setProperty name="search" property="sortType" param="sortType"/>
</c:if>
			
<c:set var="searchResult" value="${search.pageList}"/>	
<c:set var="resultCount" value="${search.count}"/>	
<c:set var="pageCount" value="${resultCount/search.pageSize}"/>
<%pageContext.setAttribute("pageCount",((Double)pageContext.getAttribute("pageCount")).intValue());%>
<c:if test="${(resultCount % search.pageSize) >0}"><c:set var="pageCount" value="${pageCount+1}"/></c:if>		
<cms:include property="template" element="head">	
	<cms:param name="keyword" value="${keyword}"/>
	<cms:param name="resultCount" value="${resultCount}"/>
	<cms:param name="pageCount" value="${pageCount}"/>	
</cms:include>

   <div id="content">     
	<c:choose><c:when test="${!empty searchResult}">
	
	<c:forEach var="itemId" items="${searchResult}" varStatus="status">
	     	<c:set var="index" value="${status.index}" />
	     	<c:set var="productCode" value="${fn:replace(fn:replace(itemId,'-','_'),'+','_')}"/>
		<cms:include file="/system/modules/com.thesys.opencms.laphone/elements/productBlock.jsp">
	        <cms:param name="blockIndex">0</cms:param>	
		<cms:param name="productCode">${itemId}</cms:param>				
		<cms:param name="imagePath">${productPhotoUrl}/${productCode}/${productCode}_1.jpg</cms:param>
		</cms:include>
	     <c:if test="${(index+1) == bannerIndex || (index+1) == bannerIndex*2 }">
		    <!-- 水平廣告 -->
	            <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/banner.jsp">
	            	<cms:param name="PrintPosition">AD-pd943x100</cms:param>
	            </cms:include>
		    <!-- 水平廣告 -->
	     </c:if>   
	</c:forEach>
	     <c:if test="${(index+1) < bannerIndex}">
		    <!-- 水平廣告 -->
	            <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/banner.jsp">
	            	<cms:param name="PrintPosition">AD-pd943x100</cms:param>
	            </cms:include>
		    <!-- 水平廣告 -->
	     </c:if>
	</c:when>
	<c:otherwise>	
		<div style="height:300px;font-size:13px">&nbsp;<br/>&nbsp;無符合查詢條件之結果</div>
	</c:otherwise>
	</c:choose>
	<c:if test="${pageCount>0}">
	     <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/pager.jsp">	     		
			<jsp:setProperty name="search" property="keyword" value="${keyword}"/>
			<cms:param name="pageCount">${pageCount}</cms:param>
	     </cms:include>
	 </c:if>    
     </div>
<cms:include property="template" element="foot" />