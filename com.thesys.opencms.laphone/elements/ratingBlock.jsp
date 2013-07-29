<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="handler" scope="request" class="com.thesys.opencms.laphone.product.ThesysRatingHandler">
<%handler.init(pageContext,request,response);%>
</jsp:useBean>
<jsp:setProperty name="handler" property="itemId" value="${param.itemId}"/>
<c:set var="rating" value="${handler.rating}"/>
<c:choose>
<c:when test="${rating==0}"><c:set var="star" value="r0"/></c:when>
<c:when test="${rating<=99}"><c:set var="star" value="r1"/></c:when>
<c:when test="${rating<=199}"><c:set var="star" value="r2"/></c:when>
<c:when test="${rating<=299}"><c:set var="star" value="r3"/></c:when>
<c:when test="${rating<=399}"><c:set var="star" value="r4"/></c:when>
<c:otherwise><c:set var="star" value="r5"/></c:otherwise>
</c:choose>
<div class="${star}">
	<c:choose><c:when test="${param.click==true}">
		<a href="${param.itemId}" ref="1" class="btnRating star1">★</a>
		<a href="${param.itemId}" ref="2" class="btnRating star2">★</a>
		<a href="${param.itemId}" ref="3" class="btnRating star3">★</a>
		<a href="${param.itemId}" ref="4" class="btnRating star4">★</a>
		<a href="${param.itemId}" ref="5" class="btnRating star5">★</a>
	</c:when><c:otherwise>
		<span class="star1">★</span>
		<span class="star2">★</span>
		<span class="star3">★</span>
		<span class="star4">★</span>
		<span class="star5">★</span>
	</c:otherwise>
	</c:choose>
</div>
