<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="handler" scope="request" class="com.thesys.opencms.laphone.cvs.ThesysCvsStoreHandler">
<%handler.init(pageContext,request,response);%>
</jsp:useBean>
<jsp:setProperty name="handler" property="storeType" param="storeType"/>
<jsp:setProperty name="handler" property="zipCode" param="zipCode"/>
<c:set var="stores" value="${handler.stores}"/>
<option value="">門市</option>
<c:forEach var="store" items="${stores}">
<option value="${store.storeNo}">${store.storeName}</option>
</c:forEach>