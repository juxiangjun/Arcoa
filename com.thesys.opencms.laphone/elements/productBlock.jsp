<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="java.net.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
CmsJspActionElement cms = new CmsJspActionElement(pageContext,request,response);
CmsObject cmso = cms.getCmsObject();
%>
<c:if test="${!empty param.productCode}">
	<c:set var="productCode" value="${fn:replace(fn:replace(param.productCode,'+','_'),'-','_')}"/>
	<c:set var="productXml" value="/product/${productCode}.html"/>
</c:if>
<c:if test="${!empty param.productXml}"><c:set var="productXml" value="${param.productXml}"/></c:if>
<c:if test="${!empty productXml}">
<%
String productPath = (String)pageContext.getAttribute("productXml");
if(!cms.getCmsObject().existsResource(productPath)){
	return;
}
%>
<cms:contentload collector="singleFile" param="${productXml}">
<c:set var="imagePath" value="${param.imagePath}"/>
<%--<c:if test="${empty imagePath}"><c:set var="imagePath">/productPhoto/<cms:contentshow element="ProductCode"/>/<cms:contentshow element="ProductCode"/>_1.jpg</c:set></c:if>--%>
<a href="<cms:link>/product.html?eccode=<cms:contentshow element="ProductCode"/></cms:link>" <c:if test="${param.blockIndex==0}">class="search_block"</c:if>>                
	<ul class="block${param.blockIndex} product_block">
		
	    <li class="img">
	    	<img class="pic" src="${imagePath}" alt="<cms:contentshow element="ProductName"/>" />
	    	<c:set var="watermark"><cms:contentshow element="Watermark"/></c:set>
	    	<c:if test="${!empty watermark && watermark!=0}">
	    		<div class="watermark" id="watermark${watermark}"></div>
	    	</c:if>
	    </li>
	    <li class="title title${param.blockIndex}"><cms:contentshow element="ProductName"/></li>
	    <li class="desc desc${param.blockIndex}"><cms:contentshow element="Description"/></li>
	    <li class="rating">
	    <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/ratingBlock.jsp">	    	
            	<cms:param name="itemId"><cms:contentshow element="SapProductCode"/></cms:param>	    	
            	<cms:param name="click">false</cms:param>
	    </cms:include>
	    </li>
	    <li class="price"><sup>NT$</sup>
		    <c:set var="price"><cms:contentshow element="SpecialPrice"/></c:set>
		    <fmt:formatNumber value="${price}" pattern="#,##0.0#" maxFractionDigits="0"/>
	    </li>
	</ul>                
</a>
</cms:contentload>
</c:if>