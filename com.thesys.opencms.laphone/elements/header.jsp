<%@ page trimDirectiveWhitespaces="true" %>
<%@page buffer="none" session="true" import="org.opencms.frontend.templatetwo.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="com.thesys.opencms.laphone.member.*"%>
<%@ page import="com.thesys.opencms.laphone.system.*"%>
<%
	ThesysParamHandler paramHandler = new ThesysParamHandler(pageContext, request, response);
	String webIp = paramHandler.getValueByParamKey("WEB_IP");
	//pageContext.setAttribute("webIp",webIp);
	pageContext.setAttribute("webBaseUrl","http://"+webIp);

%>
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<% cms.init(pageContext,request,response); %>
</jsp:useBean>
<jsp:useBean id="cart" scope="request" class="com.thesys.opencms.laphone.cart.ThesysCartHandler">
<% cart.init(pageContext,request,response); %>
</jsp:useBean>
<%--系統+客服訊息--%>
<c:if test="${!empty sessionScope.memberNo}">
<jsp:useBean id="sysMsg" class="com.thesys.opencms.laphone.msg.ThesysSystemMsgHandler">
	<%sysMsg.init(pageContext,request,response);%>
	<jsp:setProperty name="sysMsg" property="memberId" value="${sessionScope.memberId}"/>
</jsp:useBean>	
<c:set var="allcount" value="${sysMsg.allNotReadCount}" />
</c:if>
 <%
String currentPath = cms.info("opencms.request.uri");
int typeId =cms.getCmsObject().readFile(currentPath ).getTypeId();
if(typeId ==905){ //ThesysLaphoneCategory                    
pageContext.setAttribute("inSearchPage",true);              
pageContext.setAttribute("searchPath",currentPath);
}else{                    	
pageContext.setAttribute("inSearchPage",false);        
pageContext.setAttribute("searchPath","/search.html");
}
request.setAttribute("currentPath",currentPath);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:og="http://ogp.me/ns#" xmlns:fb="http://www.facebook.com/2008/fbml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link type="image/x-icon" href="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/favi.ico</cms:link>" rel="shortcut icon" />
    
    <!-- Facebook Meta Setting -->
    <c:set var="title" value="${param.title}"/>
    <c:if test="${empty title}"><c:set var="title"><cms:property name="Title"/></c:set></c:if>
    <title>laphone手機．平板．配件專賣店<c:if test="${!emptytitle}">-${title}</c:if></title>   	
    <meta content="laphone手機．平板．配件專賣店<c:if test="${!emptytitle}">-${title}</c:if>" property="og:title" />    
    <c:if test="${!empty param.desc}">    
   	<meta name="description" content="${param.desc}" />
    	<meta property="og:description" content="${param.desc}"/>
    </c:if>
    <meta property="og:type" content="article"/>
    <meta name="URL" content="${webBaseUrl}<cms:link><cms:info property="opencms.request.uri"/></cms:link><c:if test="${!empty param.eccode}">?eccode=${param.eccode}</c:if>" />
    <meta property="og:url" content="${webBaseUrl}<cms:link><cms:info property="opencms.request.uri"/></cms:link><c:if test="${!empty param.eccode}">?eccode=${param.eccode}</c:if>"/>
    <c:choose>
    <c:when test="${!empty param.image}">
	    <c:if test="${fn:startsWith(param.image,'http')}">
	    <meta property="og:image" content="${param.image}" />
	    <link rel="image_src" type="image/jpeg" href="${param.image}">
	    </c:if>
	    <c:if test="${!fn:startsWith(param.image,'http')}">
	    <meta property="og:image" content="${webBaseUrl}${param.image}" />
	    <link rel="image_src" type="image/jpeg" href="${webBaseUrl}${param.image}">
	    </c:if>
    </c:when>
    <c:otherwise>
    	    <meta property="og:image" content="${webBaseUrl}<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/logo.png</cms:link>" />
	    <link rel="image_src" type="image/jpeg" href="${webBaseUrl}<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/logo.png</cms:link>">
    </c:otherwise>
    </c:choose>
    <meta property="og:site_name" content="laphone手機．平板．配件專賣店" />
    <meta property="og:updated_time" content="<%=new java.util.Date().getTime()%>" />
  
    <!--共用的css-->
    <link rel="stylesheet" type="text/css" href="<cms:link>/system/modules/com.thesys.opencms.common/resources/jquery/fancybox/jquery.fancybox-1.3.4.css</cms:link>" media="screen" /> 
    <link rel="stylesheet" type="text/css" href="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/css/base.css</cms:link>" />
    <link rel="stylesheet" type="text/css" href="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/css/block.css</cms:link>" />
    <link rel="stylesheet" type="text/css" href="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/css/thesys.css</cms:link>" />
    <link rel="stylesheet" type="text/css" href="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/css/product.css</cms:link>" />
    <link rel="stylesheet" type="text/css" href="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/css/shoppingcart.css</cms:link>" />
    <link rel="stylesheet" type="text/css" href="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/css/member.css</cms:link>" />    
    <link rel="stylesheet" type="text/css" href="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/css/login.css</cms:link>" />   
    <link rel="stylesheet" type="text/css" href="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/css/register.css</cms:link>" />
    <script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/js/jquery-1.7.1.min.js</cms:link>" ></script>
    <script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/js/jquery-ui-1.8.9.js</cms:link>" ></script>
    <script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.common/resources/jquery/jquery.cycle/jquery.cycle.all.min.js</cms:link>"></script>
    <script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.common/resources/jquery/fancybox/jquery.mousewheel-3.0.4.pack.js</cms:link>"></script>
    <script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.common/resources/jquery/fancybox/jquery.fancybox-1.3.4.pack.js</cms:link>"></script>
    <script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/js/twzipcode-1.3.1.js</cms:link>"></script>
    <script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/js/superfish.js</cms:link>" ></script>
    <script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/js/jquery.dd.js</cms:link>" ></script>   
    <script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.common/resources/jquery/thesys.valid.js</cms:link>" ></script>
    <script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/js/thesys.js</cms:link>" ></script>
    
</head>

<body>
<div id="fb-root"></div>
<script>(function(d, s, id) {
  var js, fjs = d.getElementsByTagName(s)[0];
  if (d.getElementById(id)) return;
  js = d.createElement(s); js.id = id;
  js.src = "//connect.facebook.net/zh_TW/all.js#xfbml=1";
  fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));</script>


    <div id="wrap">
        <!-- begin header -->
        <div id="header">
            <div class="center">
                <a class="logo" href="<cms:link>/index.html</cms:link>" title="回 laphone 首頁">
                    <img src="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/logo.png</cms:link>" alt="回 laphone 首頁" /></a>
                <ul id="nav" class="white font13">
                    <%pageContext.setAttribute("navigationList",cms.getNavigation().getNavigationForFolder("/"));%>
                    <c:forEach var="menu" items="${navigationList}">
                    	<c:if test="${menu.navPosition >=1 && menu.navPosition<=99}">
                    	<c:choose>
                    	<c:when test="${menu.folderLink && fn:indexOf(menu.resourceName,'faq')==-1  && fn:indexOf(menu.resourceName,'new')==-1}"><c:set var="folderPath" value="${menu.resourceName}"/>
                    		<li><a href="<cms:link>${menu.resourceName}</cms:link>" title="${menu.navText}">${menu.navText}<img alt="" src="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/menu_shadow.png</cms:link>"></a>
                    		<ul class="sub">
                    		<%pageContext.setAttribute("navigationList1",cms.getNavigation().getNavigationForFolder((String)pageContext.getAttribute("folderPath")));%>
                    		<c:forEach var="menu1" items="${navigationList1}">
                    			<li><a href="<cms:link>${menu1.resourceName}</cms:link>" title="${menu1.navText}">${menu1.navText}</a></li>
                    		</c:forEach>
                    		</ul>
                    	</c:when>
                    	<c:otherwise>
                    		<li><a href="<cms:link>${menu.resourceName}</cms:link>" title="${menu.navText}">${menu.navText}<img alt="" src="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/menu_shadow.png</cms:link>"></a>
                    	</c:otherwise>
                    	</c:choose>
                    	</li>
                    	</c:if>
                    </c:forEach>                   
                    <li><a class="shopping-cart" href="<cms:link>/shoppingcart/</cms:link>" title="前往結帳">購物車<c:if test="${!empty cart.items}"><span class="count font11"><c:out value="${cart.count}"/></span></c:if>
                        <img alt="" src="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/menu_shadow.png</cms:link>"></a>
                        <c:if test="${!empty cart.items}">
                        <ul class="sub">
                        	<c:forEach var="item" items="${cart.items}">                        		
	               			<c:set var="productCode" value="${fn:replace(fn:replace(item.key,'-','_'),'+','_')}"/>
                        		<c:set var="xmlPath" value="/product/${productCode}.html"/>
                        		<cms:contentload collector="singleFile" param="${xmlPath}" editable="false">
                        		<li><a href="<cms:link>/product.html?eccode=<cms:contentshow element="ProductCode"/></cms:link>"><cms:contentshow element="ProductName"/></a></li>
                        		</cms:contentload>
                        	</c:forEach>
                        </ul>
                        </c:if>
                    </li>
                    <li>
                        <form id="formSearch" method="get" action="<cms:link>${searchPath}</cms:link>" class="alertForm">
                        	<input id="serachText" type="text" name="keyword" value="${param.keyword}"/>
	                    	<input type="hidden" name="searchColor" id="searchColor" value="${param.searchColor}">
	                    	<input type="hidden" name="searchPrice" id="searchPrice" value="${param.searchPrice}">
	                    	<input type="hidden" name="searchRating" id="searchRating" value="${param.searchRating}">
	                    	<input type="hidden" name="searchStyle" id="searchStyle" value="${param.searchStyle}">
	                    	<input type="hidden" name="sortType" id="sortType" value="${param.sortType}"> 
	                    	<a id="btnSearch" href="#" onclick="$('#formSearch').submit()"></a>                   
                    	</form> 
                    </li>
                </ul>
                <div id="login" class="white font13">
	        	<form id="loginHiddenForm" method="post" action="<cms:link>/login/</cms:link>">
	                    	<input type="hidden" name="beforePath" value="${currentPath}" />  
	                    	<input type="hidden" name="eccode" value="${param.eccode}" />     
	            	</form> 
                    <div class="register-logon-logoff">
                        <%-- 登入後不顯示註冊及登入--%>
			<c:if test="${empty sessionScope.memberNo}">
                        <a href="<cms:link>/register/</cms:link>">註冊</a>&nbsp;&nbsp;&nbsp;<a href="<cms:link>/login/</cms:link>">登入</a>
                        <%--<a class="btnSubmit" href="#loginHiddenForm">登入</a>--%>
                        </c:if>
                        <c:if test="${!empty sessionScope.memberNo}">
                            <span>${sessionScope.memberName}您好，<a href="<cms:link>/login/logout.html?dt=<%=new java.util.Date().getTime()%></cms:link>">登出</a></span>
                        </c:if>              
                    </div>
                    <div class="alert" style="white-space: nowrap;">
                        <a href="<cms:link>/memberCenter/index.html</cms:link>">
				會員中心<c:if test="${not empty sessionScope.memberNo && allcount != 0 }"><span class="count font11">${allcount}</span></c:if>
                        </a>
                    </div>
                    <div class="service">
                        <a href="<cms:link>/service/index.html</cms:link>">客服中心</a>
                    </div>
                </div>
            </div>
        </div>
        <!-- end header -->
        <!-- begin search tab -->
        <div id="search-tabs">
            <div class="center">  
                <div class="tab-box">   
            	    <ul class="tabs grey6">
                        <li class="b1 active">顏色</li>
                        <li class="b2">價格</li>
                        <li class="b3">人氣</li>
                        <li class="b4 last">風格</li>
                    </ul>
                   
                    
                    <ul class="tab-views">
                        <li class="view active">
                            <div id="color-slider">                            	
                                <c:set var="colorWidth" value="60"/>  
                                <c:set var="colorCount" value="1"/>     
		                <ul class="colors clearfix">
			            <li ref="all" style="width:${colorWidth}px;">All</li>
			            <cms:contentload collector="singleFile" param="/_config_/colorSetting.html" editable="false">
	                            <cms:contentloop element="ColorGroup">                         
                                    <c:set var="colorCount" value="${colorCount+1}"/>   	                      
                                    <c:set var="colorGroupId"><cms:contentshow element="ColorGroupId"/></c:set> 
                                    <c:set var="htmlColorCode"><cms:contentshow element="HtmlColorCode"/></c:set>   
                                    <c:set var="htmlColorImage"><cms:contentshow element="HtmlColorImage"/></c:set>   
                                    <c:if test="${colorGroupId==param.searchColor}">
                                    	<c:set var="colorSelect" value="${colorCount-1}"/>
                                    	<c:set var="colorSelectTxt"><cms:contentshow element="ColorGroupName"/></c:set>
                                    </c:if> 
                                    <c:if test="${!empty htmlColorCode}">                                   
	                            <li ref="${colorGroupId}" title="<cms:contentshow element="ColorGroupName"/>" style="background:${htmlColorCode};width:${colorWidth}px;"></li>
	                            </c:if>
	                            <c:if test="${!empty htmlColorImage}">                                 
	                            <li ref="${colorGroupId}" style="background:url(<cms:link>${htmlColorImage}</cms:link>);width:${colorWidth}px;"></li>
	                           </c:if>
	                             </cms:contentloop>
                            	    </cms:contentload>
		                 </ul>
	                    </div>
                        </li>
                        <script>
                        $(function () {                
			    $("#color-slider").slider({
			        value: ${colorSelect*colorWidth+colorWidth/2},
			        min: 0,
			        max: ${colorWidth*colorCount},
			        step: 1,
			        change: function (event, ui) {
			        	var val = Math.ceil(ui.value / ${colorWidth});
			        	if(val==0) val = 1;
			        	$("#searchColor").val($('#color-slider li:eq('+(val-1)+')').attr("ref"));
			        	$("#formSearch").submit();
			        }
			    });
                        });
                        </script>
                        <li class="view">
                            <div id="price-slider">
                                <ul class="clearfix">
                                    <c:set var="priceWidth" value="96"/>  
                                    <c:set var="priceCount" value="${1}"/>
                                    <li ref="all" style="width:${priceWidth}px;">All</li>
                                    <c:forTokens var="price" items="299以下,300~799,800~1499,1500~2999,3000~5999,6000~9999,10000以上" delims="," varStatus="status">
					<c:set var="priceCount" value="${priceCount+1}"/>
					<c:if test="${status.count==param.searchPrice}">
						<c:set var="priceSelect" value="${priceCount-1}"/>
						<c:set var="priceSelectTxt" value="${price}"/>
					</c:if>  	
                                    	<li ref="${status.count}" style="width:${priceWidth}px;">${price}</li>
                                    </c:forTokens>
                                </ul>
                            </div>
                        </li>
                        <script> 
                        $(function () {                
			    $("#price-slider").slider({
			        value: ${priceSelect*priceWidth+priceWidth/2},
			        min: 0,
			        max: ${priceWidth*priceCount},
			        step: 1,
			        change: function (event, ui) {
			        	var val = Math.ceil(ui.value / ${priceWidth});
			        	if(val==0) val = 1;
			        	$("#searchPrice").val($('#price-slider li:eq('+(val-1)+')').attr("ref"));
			        	$("#formSearch").submit();
			        }
			    });
                        });
                        </script>
                        <li class="view">
                            <div id="rating-slider">
                                <ul class="clearfix">
                                    <c:set var="ratingWidth" value="130"/>  
                                    <c:set var="ratingCount" value="1"/>    
                                    <li ref="all" style="width:${ratingWidth}px;">All</li>
                                    <c:forEach var="i" begin="1" end="5">                                     
                                    <c:set var="ratingCount" value="${ratingCount+1}"/>  
                                    <c:if test="${i==param.searchRating}">
                                    	<c:set var="ratingSelect" value="${ratingCount-1}"/>                                    	
					<c:set var="ratingSelectTxt">
					<span class="rating r${ratingSelect}">
                                            <span class="star1">★</span> <span class="star2">★</span> <span class="star3">★</span> <span class="star4">★</span> <span class="star5">★</span>
                                        </span>
                                        </c:set>
                                    </c:if>  
                                    <li ref="${i}" style="width:${ratingWidth}px;">
                                        <div class="rating r${i}">
                                            <span class="star1">★</span> <span class="star2">★</span> <span class="star3">★</span> <span class="star4">★</span> <span class="star5">★</span>
                                        </div>
                                    </li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </li>
                        <script> 
                        $(function () {                
			    $("#rating-slider").slider({
			        value: ${ratingSelect*ratingWidth+ratingWidth/2},
			        min: 0,
			        max: ${ratingWidth*ratingCount},
			        step: 1,
			        change: function (event, ui) {
			        	var val = Math.ceil(ui.value / ${ratingWidth});
			        	if(val==0) val = 1;
			        	$("#searchRating").val($('#rating-slider li:eq('+(val-1)+')').attr("ref"));
			        	$("#formSearch").submit();
			        }
			    });
                        });
                        </script>
                        <li class="view">
                            <div id="style-slider">
                                <c:set var="styleWidth" value="84"/>  
                                <c:set var="styleCount" value="1"/>   
                                <ul class="clearfix">
                                    <li ref="all" style="width:${styleWidth}px;">All</li>                       
                                    <cms:contentload collector="singleFile" param="/_config_/styleSetting.html" editable="false">                                    	
                                    	<cms:contentloop element="Parameter">
                                    	<c:set var="styleCount" value="${styleCount+1}"/>		                      
                                        <c:set var="key"><cms:contentshow element="Key"/></c:set> 
                                        <c:set var="value"><cms:contentshow element="Value"/></c:set>   
                                        <c:if test="${key==param.searchStyle}">
                                        	<c:set var="styleSelect" value="${styleCount-1}"/>
                                        	<c:set var="styleSelectTxt" value="${value}"/>
                                        </c:if>                                
	                                <li ref="${key}"  style="width:${styleWidth}px;">${value}</li>
	                                </cms:contentloop>
                                    </cms:contentload>
                                </ul>
                            </div>
                        </li>
                        <script> 
                        $(function () {                
			    $("#style-slider").slider({
			        value: ${styleSelect*styleWidth+styleWidth/2},
			        min: 0,
			        max: ${styleWidth*styleCount},
			        step: 1,
			        change: function (event, ui) {
			        	var val = Math.ceil(ui.value / ${styleWidth});
			        	if(val==0) val = 1;
			        	$("#searchStyle").val($('#style-slider li:eq('+(val-1)+')').attr("ref"));
			        	$("#formSearch").submit();
			        }
			    });
                        });
                        </script>
                    </ul>          		 
                </div><!--end of tab-box -->                 
                <div class="nav-history">
                    <select name="history-select" id="history-select" style="width:131px;" tabindex="1" onchange="location.href=this.value;">
                        <option value="1" selected="selected">瀏覽過商品</option>
                        <c:forEach var="productCode" items="${sessionScope.viewList}">
                        	<c:set var="productPath" value="/product/${productCode}.html"/>
                        	<cms:contentload collector="singleFile" param="${productPath}">
                        	<option value="<cms:link>/product.html?eccode=<cms:contentshow element="ProductCode"/></cms:link>"><cms:contentshow element="ProductName"/></option>
                        	</cms:contentload>
                        </c:forEach>
                    </select>
                </div> 
               <c:if test="${inSearchPage}">
                <div id="search-patern"> 
                   <c:if test="${!empty colorSelectTxt || !empty priceSelectTxt || !empty styleSelectTxt || !empty ratingSelectTxt || !empty param.keyword}">
                   <div>搜尋條件：
                   	 <c:if test="${!empty colorSelectTxt}"><c:set var="selectTxt" value="${colorSelectTxt}色"/></c:if>
                   	 <c:if test="${!empty priceSelectTxt}">
                   	 	<c:if test="${!empty selectTxt}"><c:set var="selectTxt" value="${selectTxt} + "/></c:if>
                   	 	<c:set var="selectTxt" value="${selectTxt} ${priceSelectTxt} "/>
                   	 </c:if>
                   	 <c:if test="${!empty styleSelectTxt}">
                   	 	<c:if test="${!empty selectTxt}"><c:set var="selectTxt" value="${selectTxt} + "/></c:if>
                   	 	<c:set var="selectTxt" value="${selectTxt} ${styleSelectTxt} "/>
                   	 </c:if>                   	 
                   	 <c:if test="${!empty ratingSelectTxt}">
                   	 	<c:if test="${!empty selectTxt}"><c:set var="selectTxt" value="${selectTxt} + "/></c:if>
                   	 	<c:set var="selectTxt" value="${selectTxt} ${ratingSelectTxt} "/>
                   	 </c:if>                   	                    	 
                   	 <c:if test="${!empty  param.keyword}">
                   	 	<c:if test="${!empty selectTxt}"><c:set var="selectTxt" value="${selectTxt} + "/></c:if>
                   	 	<c:set var="selectTxt" value="${selectTxt} ${ param.keyword} "/>
                   	 </c:if>
                   	 ${selectTxt}
                   	 <a href="<cms:link>${searchPath}</cms:link>">(清除條件)</a>                   
                   </div>
                   </c:if>
                   <div>搜尋結果：共${param.resultCount}筆，${param.pageCount}頁</div>
                </div>
                <script type="text/javascript">
		$(function () {
			$("#sort-select").msDropDown();
			$("#sort-select").change(function(){
				$("#sortType").val($(this).val());
			        $("#formSearch").submit();
			});
			$("#btnDefaultSort").click(function(){
				if($(this).hasClass("active")){
					return false;
				}else{
					$("#sortType").val(0);
			        	$("#formSearch").submit();
				}
			});
		});		
		</script>
              	<div class="sorting">
              		<div class="sorting-dropdown grey6" style="float:right;margin-left:2px">
		            <select name="sortType" id="sort-select" style="width:116px;" tabindex="1">
		                <option value="1" <c:if test="${param.sortType==1}">selected</c:if> title="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/sort_arrow.png</cms:link>">價格由高到低</option>
		                <option value="2" <c:if test="${param.sortType==2}">selected</c:if> title="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/sort_arrow_2.png</cms:link>">價格由低到高</option>
		                <option value="3" <c:if test="${param.sortType==3}">selected</c:if> title="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/sort_arrow.png</cms:link>">銷量由高到低</option>
		                <option value="4" <c:if test="${param.sortType==4}">selected</c:if> title="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/sort_arrow_2.png</cms:link>">銷量由低到高</option>
		                <option value="0" <c:if test="${empty param.sortType || param.sortType==0}">selected</c:if> title="">預設排序</option>
		            </select>
		        </div>
		         <a class="newerfirst <c:if test="${empty param.sortType || param.sortType==0}"> active</c:if>" id="btnDefaultSort" href="#"><img src="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/blank.gif</cms:link>"/></a>
		        <label >商品排序按:</label>		        
		</div>
		
	        <div style="clear:both"></div>
		</c:if>            
	        <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/breadCrumb.jsp"/>
	        <div style="clear:both"></div>
            </div>    
              <!-- end center -->  
        </div>
        <!-- end search tab -->