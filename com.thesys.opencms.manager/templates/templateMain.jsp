<%@ page trimDirectiveWhitespaces="true" %>
<%@page buffer="none" session="true" import="org.opencms.frontend.templatetwo.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<% cms.init(pageContext,request,response); %>
</jsp:useBean>
<% 
//寫入log變數
org.apache.log4j.MDC.put("RemoteAddress", request.getRemoteAddr() ); //IP
org.apache.log4j.MDC.put("SitePosition","Backend"); // 前後台
org.apache.log4j.MDC.put("UserId",cms.getCmsObject().getRequestContext().currentUser().getName() ); // 帳號

response.setHeader("Pragma","no-cache"); 
response.setHeader(" Cache-Control","no-cache"); 
response.setDateHeader("Expires", 0); 


%>

<%
String folderPath = cms.info("opencms.request.folder");
boolean accessFlag = false;

boolean superGroup = Boolean.valueOf(String.valueOf(session.getAttribute("SuperGroup")));
if("/manager/".equals(folderPath) || Boolean.valueOf(String.valueOf(session.getAttribute("SuperGroup")))){ //根目錄下或超級管理群組，不檢查
	accessFlag = true; 
}else if(folderPath.endsWith("/check/")){ //訂單審核
	if(Boolean.valueOf(String.valueOf(session.getAttribute("OrderGroup")))){
		accessFlag = true; 
	}
}else if(folderPath.endsWith("/order/")  || //訂單管理
	folderPath.endsWith("/member/")  || //會員管理
	folderPath.endsWith("/service/")){ //問與答管理
	if(Boolean.valueOf(String.valueOf(session.getAttribute("MemberGroup")))){
		accessFlag = true; 
	}
}else if(folderPath.endsWith("/product/") || //商品管理
	folderPath.endsWith("/category/")  //商品管理
	){ 
	if(Boolean.valueOf(String.valueOf(session.getAttribute("ProductGroup")))){
		accessFlag = true; 
	}
}else if(folderPath.endsWith("/epaper/")  ||  //電子報管理功能 
	 folderPath.endsWith("/news/")  || //最新消息管理功能
	 folderPath.endsWith("/event/")  || //緊急公告功能
	 folderPath.endsWith("/color/") ||   //顏色參數設定	
	 folderPath.endsWith("/note/") ||   //滿額免運費等說明設定功能	
	 folderPath.endsWith("/fee/") ||   //運費管理功能	
	 folderPath.endsWith("/sitemap/") ||   //sitemap XML下載功能
	 folderPath.endsWith("/banner/")   ||//廣告管理
	 folderPath.endsWith("/faq/") ||  //購物FAQ管理   	
	 folderPath.endsWith("/blockGroup/") || //田字版型管理
	 folderPath.endsWith("/homeFixBlock/") //首頁固定版位
	  ){
	if(Boolean.valueOf(String.valueOf(session.getAttribute("SiteGroup")))){
		accessFlag = true; 
	}
}else if(folderPath.endsWith("/receiptReport/")  ||  //每日對帳報表
	 folderPath.endsWith("/arrivalReport/")  || //每日到貨未取異常表
	 folderPath.endsWith("/shippedReport/")  || //有效訂單報表
	 folderPath.endsWith("/productReport/") ||   //產品排名報表	
	 folderPath.endsWith("/searchReport/")    //關鍵字搜尋報表
	 ){
	if(Boolean.valueOf(String.valueOf(session.getAttribute("ReportGroup")))){
		accessFlag = true; 
	}
}else if(folderPath.endsWith("/param/") || //系統參數管理
	folderPath.endsWith("/faq/")   //後台操作時間統計報表
	){
	if(Boolean.valueOf(String.valueOf(session.getAttribute("SystemGroup")))){
		accessFlag = true; 
	}
	
}


if(!accessFlag){
%>
<script>
alert("您無此功能權限");
location.href="<cms:link>/manager/index.html</cms:link>";
</script>
<%
return;
}
%>
<cms:template element="head">
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">

<head>
<title>laphone 後台管理系統</title>

<link href="<cms:link>/system/modules/com.thesys.opencms.manager/resources/css/marscss-b.css</cms:link>" rel="stylesheet" type="text/css" />
<link href="<cms:link>/system/modules/com.thesys.opencms.manager/resources/css/thesys.css</cms:link>" rel="stylesheet" type="text/css" />

<link type="text/css" href="<cms:link>/system/modules/com.thesys.opencms.common/resources/jquery/jquery.datepicker/jquery.ui.core.css</cms:link>" rel="stylesheet" />
<link type="text/css" href="<cms:link>/system/modules/com.thesys.opencms.common/resources/jquery/jquery.datepicker/jquery.ui.datepicker.css</cms:link>" rel="stylesheet" />
<link type="text/css" href="<cms:link>/system/modules/com.thesys.opencms.common/resources/jquery/jquery.datepicker/jquery.ui.theme.css</cms:link>" rel="stylesheet" />


<script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.manager/resources/js/jquery-1.7.1.min.js</cms:link>" ></script>
<script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.common/resources/jquery/jquery.datepicker/js/jquery.ui.core.js</cms:link>"></script>
<script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.common/resources/jquery/jquery.datepicker/js/jquery.ui.widget.js</cms:link>"></script>
<script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.common/resources/jquery/jquery.datepicker/js/jquery.ui.datepicker.js</cms:link>"></script>	
<script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.common/resources/jquery/jquery.datepicker/js/jquery.ui.datepicker-zh-TW.js</cms:link>"></script>

<script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.manager/resources/ckeditor/ckeditor.js</cms:link>" ></script>
<script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.common/resources/jquery/thesys.valid.js</cms:link>" ></script>
<!--<script type="text/javascript" src="/resources/editors/fckeditor/fckeditor.js"></script>
<script type="text/javascript" src="/resources/components/widgets/fckeditor.js"></script>-->
<script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.manager/resources/js/thesys.js</cms:link>" ></script>
<script type="text/javascript" src="/resources/components/widgets/downloadgallery.js"></script>
<script type="text/javascript" src="/resources/components/widgets/imagegallery.js"></script>
<script type="text/javascript" src="/resources/components/widgets/vfsimage.js"></script>
<script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.manager/resources/js/gallery.js</cms:link>" ></script>
</head>

<body>
<div class="logo">
</div>
<div class="w">
	<span><cms:user property="lastname"/> Welcome!</span><br />
</div>
<div class="main">
		<!--左選單 -->
		<div class="left">
		<%/**	<cms:include file="/system/modules/com.thesys.opencms.manager/elements/menu.jsp"/>*/%>
		
		<c:set var="currentPath"><cms:info property="opencms.request.uri"/></c:set>
		<div class="menubox">
			<div class="menu_title">
				<span class="t_02-1"></span><span class="t_02-2"></span>
				<span class="t_02">管理功能</span></div>
			<div class="menu">
			<c:if test="${sessionScope.SuperGroup || sessionScope.OrderGroup}">
		    	<a href="<cms:link>/manager/check/index.html</cms:link>" title="訂單審核" >訂單審核</a>
		    	</c:if>
		    	<c:if test="${sessionScope.SuperGroup || sessionScope.MemberGroup}">
		    	<a href="<cms:link>/manager/order/index.html</cms:link>" title="訂單管理" >訂單管理</a>
		    	<a href="<cms:link>/manager/member/index.html</cms:link>" title="會員管理" >會員管理</a>
		    	<a href="<cms:link>/manager/service/index.html</cms:link>" title="問與答管理" >問與答管理</a>
		    	</c:if>
		    	<c:if test="${sessionScope.SuperGroup || sessionScope.ProductGroup}">
		    	<a href="<cms:link>/manager/product/index.html?clearSession=true</cms:link>" title="商品管理" >商品管理</a>
		    	<a href="<cms:link>/manager/category/index.html</cms:link>" title="商品分類管理" >商品分類管理</a>
		    	<a href="<cms:link>/manager/homeFixBlock/index.html</cms:link>" title="首頁固定版位管理" >首頁固定版位管理</a>
		    	<a href="<cms:link>/manager/blockGroup/index.html</cms:link>" title="田字版型管理" >田字版型管理</a>
		    	</c:if>
		    	<c:if test="${sessionScope.SuperGroup || sessionScope.SiteGroup}">
		    	<a href="<cms:link>/manager/epaper/index.html</cms:link>" title="電子報管理" >電子報管理</a>
		    	<a href="<cms:link>/manager/news/index.html</cms:link>" title="最新消息管理" >最新消息管理</a>
		    	<a href="<cms:link>/manager/event/index.html</cms:link>" title="緊急公告" >緊急公告管理</a>		    	
		    	<a href="<cms:link>/manager/color/index.html</cms:link>" title="顏色參數設定" >顏色參數設定</a>
		    	<a href="<cms:link>/manager/note/index.html</cms:link>" title="滿額免運費等說明" >滿額免運費等說明</a>
		    	<a href="<cms:link>/manager/fee/index.html</cms:link>" title="運費管理" >運費管理</a>
		    	<a href="<cms:link>/manager/banner/index.html</cms:link>" title="廣告管理" >廣告管理</a>
		    	<a href="<cms:link>/manager/sitemap/index.html</cms:link>" title="SiteMap XML下載" >SiteMap XML下載</a>	
		    	<a href="<cms:link>/manager/faq/index.html</cms:link>" title="購物FAQ管理" >購物FAQ管理</a>    	
		    	</c:if>
		    	<c:if test="${sessionScope.SuperGroup || sessionScope.SystemGroup}">
		    	<a href="<cms:link>/manager/param/index.html</cms:link>" title="系統參數管理" >系統參數管理</a>
		    	<a href="<cms:link>/manager/loginReport/index.html</cms:link>" title="後台操作時間統計報表" >後台操作時間統計報表</a>
		    	</c:if>
		    	<c:if test="${sessionScope.SuperGroup || sessionScope.ReportGroup}">		    	
		    	<a href="<cms:link>/manager/receiptReport/index.html</cms:link>" title="每日對帳報表" >每日對帳報表</a>
		    	<a href="<cms:link>/manager/arrivalReport/index.html</cms:link>" title="每日到貨未取異常表" >每日到貨未取異常表</a>
		    	<a href="<cms:link>/manager/shippedReport/index.html</cms:link>" title="有效訂單報表" >有效訂單報表</a>
		    	<a href="<cms:link>/manager/productReport/index.html</cms:link>" title="產品排名報表" >產品排名報表</a>
		    	<a href="<cms:link>/manager/searchReport/index.html</cms:link>" title="關鍵字搜尋報表" >關鍵字搜尋報表</a>
		    	</c:if>
		    	<c:if test="${sessionScope.SuperGroup}">
		    	</c:if>
		    	<a href="<cms:link>/manager/password.html</cms:link>" title="變更密碼">變更密碼</a>
		    	<a href="<cms:link>/manager/logout/</cms:link>" title="登出" >登出</a>
			</div>
			<div class="b">
				<span class="b_02-1"></span><span class="b_02-2"></span>
				<span class="b_02"></span></div>
		</div>
		</div>
		<!--左選單 end -->
		<!--內容 -->
		<div class="right">
</cms:template>
<cms:template element="body"><cms:include element="body" editable="true"/></cms:template>
<cms:template element="foot"> 
		</div>
</div>

</body>

</html>
<c:set var="urlPath"><cms:info property="opencms.request.uri"/></c:set>
<c:forEach var="p" items="${param}" varStatus="st">
	<c:set var="urlPath">${urlPath}${(st.index==0)?'?':'&'}${p.key}=${p.value}</c:set>
</c:forEach>
<%
org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger("Backend");
log.info(pageContext.getAttribute("urlPath"));
%>
</cms:template>
