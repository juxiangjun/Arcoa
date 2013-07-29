<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
/**
<script type="text/javascript" src="/resources/components/widgets/downloadgallery.js"></script>
<script type="text/javascript" src="/resources/components/widgets/imagegallery.js"></script>
<script type="text/javascript" src="/resources/components/widgets/vfsimage.js"></script>
 **/
<c:set var="contextPath"><cms:link>/</cms:link></c:set>
<c:set var="contextPath">${fn:substring(contextPath,0,fn:length(contextPath)-1)}</c:set>

var opencmsBase = "${contextPath}"; 

var workplacePath="/shopping/system/workplace/";
var imageGalleryPath = "/system/workplace/galleries/imagegallery/index.jsp?";
//var imageGalleryPath = '/components/imagegallery/index.jsp?';
var downloadGalleryPath = '/system/workplace/galleries/downloadgallery/index.jsp?';

var contextPath = "${contextPath}";
var startupFolder1040506356 = "/images/";
var startupType1040506356 = "gallery";
var useFmts1040506356 = false;
var imgFmts1040506356 = null;
var imgFmtNames1040506356 = null;
//downloadgallery
var startupFolder5104846933 = "/files/";
var startupType5104846933 = "gallery";

$(function(){
	$(".imageGallery").each(function(){
		var id = $(this).attr("id");
		if(id==undefined){alert("id is undefined");}
		else{
			var html = "<a href=\"#\" class=\"imageButton\" onClick=\"clickImageButton('"+id+"');\" title=\"圖片庫\"><span><img src\=\"/resources/buttons/imagegallery.png\" alt=\"圖片庫\"></span></a>";
			$(html).insertAfter($(this));
		}
		
	});
	$(".fileGallery").each(function(){
		var id = $(this).attr("id");
		if(id==undefined){alert("id is undefined");}
		else{
			var html = "<a href=\"#\" class=\"fileButton\" onClick=\"clickFileButton('"+id+"');\" title=\"下載庫\"><span><img src\=\"/resources/buttons/downloadgallery.png\" alt=\"下載庫\"></span></a>";
			$(html).insertAfter($(this));
		}
		
	});
});
function clickImageButton(id){
	openImageGallery('widget',  id,  '1040506356');
	return false;
}
function clickFileButton(id){
	openDownloadGallery('widget',  id,  '5104846933');
	return false;
}
