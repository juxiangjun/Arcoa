<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="org.opencms.main.*, org.opencms.search.*, org.opencms.file.*, org.opencms.jsp.*, java.util.*, java.sql.*, java.net.*" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
response.setHeader("pragma", "no-cache");
response.setHeader("Cache-control", "no-cache");
response.setHeader("Cache-control", "no-store");
response.setHeader("Expires", "Tues, 01 Jan 1980 00:00:00 GMT");
%>
<jsp:useBean id="creditVO" scope="request" class="com.thesys.opencms.laphone.order.dao.ThesysOrderCreditVO">
</jsp:useBean>
<jsp:setProperty name="creditVO" property="*"/>
<jsp:useBean id="orderHandler" scope="request" class="com.thesys.opencms.laphone.order.ThesysOrderHandler">
<%orderHandler.init(pageContext,request,response);%>
</jsp:useBean>
<jsp:setProperty name="orderHandler" property="orderId" param="od_sob"/>
<c:set var="orderVO" value="${orderHandler.selectedOrder}"/>
<c:choose><c:when test="${orderVO.orderStatus==-1}">
<%
orderHandler.authCreditCard(creditVO);
%>
<body onload="document.getElementById('fm').submit();">
<form id="fm" method="post" action="step5.html">
	<input type="hidden" name="orderId" value="${creditVO.od_sob}">
</form>
</body>
</c:when>
<c:otherwise>
<script>
alert("網頁已過期，將為您導回首頁");
location.href="<cms:link>/index.html</cms:link>";
</script>
</c:otherwise>
</c:choose>