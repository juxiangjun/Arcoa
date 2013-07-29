<%@ page trimDirectiveWhitespaces="true" %>
<%@page buffer="none" session="true" import="org.opencms.frontend.templatetwo.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% 
//寫入log變數
org.apache.log4j.MDC.put("RemoteAddress", request.getRemoteAddr() ); //IP
org.apache.log4j.MDC.put("SitePosition","Laphone"); // 前後台
if(session.getAttribute("memberId")!=null){
	org.apache.log4j.MDC.put("UserId",(String)session.getAttribute("memberId") ); // 帳號
}else{
	org.apache.log4j.MDC.put("UserId","GUEST"); // 帳號
}

response.setHeader("Pragma","no-cache"); 
response.setHeader("Cache-Control","no-cache"); 
response.setDateHeader("Expires", 0); %>
<cms:template element="head">
  <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/header.jsp">  		 		
	<cms:param name="title">${param.title}</cms:param> 
	<cms:param name="desc">${param.desc}</cms:param> 
	<cms:param name="image">${param.image}</cms:param> 
	<cms:param name="tt"><%=new java.util.Date().getTime()%></cms:param>
  </cms:include>
  <!-- begin main content -->
        <div id="main-content">
            <div class="art-bg">
                <div class="shadow">
                    <div class="center">
</cms:template>

<cms:template element="body"><cms:include element="body" editable="true"/></cms:template>
<cms:template element="foot">  
                      </div>
                    <div class="clearfix"></div>
                </div>
            </div>
        </div>
        <!-- end main content -->
        <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/footer.jsp"/>
    </div>
</body>
<script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-35621193-1']);
  _gaq.push(['_setDomainName', 'laphonetaiwan.com']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();

</script>
</html>
<c:set var="urlPath"><cms:info property="opencms.request.uri"/></c:set>
<c:forEach var="p" items="${param}" varStatus="st">
	<c:set var="urlPath">${urlPath}${(st.index==0)?'?':'&'}${p.key}=${p.value}</c:set>
</c:forEach>
<%
if(session.getAttribute("memberId")!=null){
	org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("Laphone");
	log.info(pageContext.getAttribute("urlPath"));
}
%>
</cms:template>
