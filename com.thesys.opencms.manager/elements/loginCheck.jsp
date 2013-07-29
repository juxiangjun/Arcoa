<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="cmslogin" class="org.opencms.jsp.CmsJspLoginBean">
	<% cmslogin.init(pageContext, request, response); %>
</jsp:useBean>
<%if(!cmslogin.isLoggedIn()){%>	
	<script type="text/javascript">
		//alert("尚未登入，請先登錄");
		location.href='<cms:link>/manager/login.html</cms:link>';
	</script>
<%
	
	return;
}%>