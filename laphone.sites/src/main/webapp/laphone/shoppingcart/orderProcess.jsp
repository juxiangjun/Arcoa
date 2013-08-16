<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="org.opencms.main.*, org.opencms.search.*, org.opencms.file.*, org.opencms.jsp.*, java.util.*, java.sql.*, java.net.*" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@include file="/system/modules/com.thesys.opencms.laphone/elements/loginCheck.jsp"%>
<%
response.setHeader("pragma", "no-cache");
response.setHeader("Cache-control", "no-cache");
response.setHeader("Cache-control", "no-store");
response.setHeader("Expires", "Tues, 01 Jan 1980 00:00:00 GMT");
%>
<c:choose>
<c:when test="${empty sessionScope.orderVO}">
<script>
alert("網頁已過期，將為您導回首頁");
location.href="<cms:link>/index.html</cms:link>";
</script>
</c:when>
<c:otherwise>


	<jsp:useBean id="orderVO" scope="session" class="com.thesys.opencms.laphone.order.dao.ThesysOrderVO">
	</jsp:useBean>
	<jsp:useBean id="orderHandler" scope="request" class="com.thesys.opencms.laphone.order.ThesysOrderHandler">
	<%orderHandler.init(pageContext,request,response);%>
	</jsp:useBean>
	<jsp:useBean id="paramHandler" scope="request" class="com.thesys.opencms.laphone.system.ThesysParamHandler">
	<%paramHandler.init(pageContext,request,response);%>
	</jsp:useBean>
	<c:set var="webUrl"><%=paramHandler.getValueByParamKey("WEB_URL")%></c:set>
	<c:set var="webUrl" value="${fn:replace(webUrl,'http:','https:')}"/>
	<c:set var="payUrl"><%=paramHandler.getValueByParamKey("CREDIT_PAY_URL")%></c:set>
	<c:set var="installUrl"><%=paramHandler.getValueByParamKey("CREDIT_INSTALLMENT_URL")%></c:set>
	<c:set var="installAccount"><%=paramHandler.getValueByParamKey("CREDIT_INSTALLMENT_ACCOUNT")%></c:set>
	<c:set var="storeAccount"><%=paramHandler.getValueByParamKey("CREDIT_STORE_ACCOUNT")%></c:set>
	<%
	boolean flag = orderHandler.createOrder(orderVO);
	if(flag){
		if(orderVO.getPayType()==1){ //1：信用卡付款；
		%>
		<body onload="document.getElementById('fm').submit();">
			<form id="fm" method="post" action="${payUrl}">
			<input type="hidden" name="act" value="auth">
			<input type="hidden" name="client" value="${storeAccount}">
			<input type="hidden" name="amount" value="${orderVO.orderAmount}">
			<input type="hidden" name="od_sob" value="${orderVO.orderId}">
			<input type="hidden" name="roturl" value="${webUrl}shoppingcart/creditProcess.jsp">
			</form>
		</body>
		<%
		}else if(orderVO.getPayType()==2){ //2：信用卡分期
		%>
		<body onload="document.getElementById('fm').submit();">
			<form id="fm" method="post" action="${installUrl}">
			<input type="hidden" name="act" value="auth">
			<input type="hidden" name="client" value="${installAccount}">
			<input type="hidden" name="stage" value="${orderVO.installment}">
			<input type="hidden" name="amount" value="${orderVO.orderAmount}">
			<input type="hidden" name="od_sob" value="${orderVO.orderId}">
			<input type="hidden" name="roturl" value="${webUrl}shoppingcart/creditProcess.jsp">
			</form>
		</body>
		<%
		}else{
		%>
		<body onload="document.getElementById('fm').submit();">
		<form id="fm" method="post" action="step5.html">
			<input type="hidden" name="orderId" value="${orderVO.orderId}">
		</form>
		</body>
		<%
			
		}
	}else{
	%>
		<script>
		alert("系統發生錯誤，將為您導回首頁");
		location.href="<cms:link>/index.html</cms:link>";
		</script>
	<%
	}
	%>
</c:otherwise>
</c:choose>
<%
session.removeAttribute("orderVO");
%>