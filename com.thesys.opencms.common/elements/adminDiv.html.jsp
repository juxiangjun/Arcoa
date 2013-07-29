<%@page buffer="none" session="false" import="org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%	
	CmsSystemInfo sysInfo = OpenCms.getSystemInfo() ;	
	//取得登入者資料
	CmsJspLoginBean cms = new CmsJspLoginBean(pageContext, request, response);
%>

<%if(cms.isLoggedIn() && !cms.getUser().isWebuser() ){%>
<link href="<cms:link>/system/modules/com.thesys.opencms.common/resources/css/admin.css</cms:link>" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<cms:link>/system/modules/com.thesys.opencms.common/resources/Js/admin.js</cms:link>"></script>
<script type="text/javascript" src="<cms:link>/system/workplace/resources/components/widgets/downloadgallery.js</cms:link>"></script>
<script>
// the OpenCms context path
var contextPath = "<%=sysInfo.getServletPath()%>";
var editedResource = "<%=cms.info("opencms.request.uri")%>";
// the OpenCms workplace path
var workplacePath="<cms:link>/system/workplace/</cms:link>";
var treewin;
var startupFolder402294174 = "/css/";
var startupType402294174 = "gallery";
downloadGalleryPath = "<%= org.opencms.workplace.galleries.A_CmsAjaxGallery.PATH_GALLERIES + org.opencms.workplace.galleries.CmsAjaxDownloadGallery.OPEN_URI_SUFFIX + "?" %>"

function openDownloadWidget(p1,p2,p3){
	$("#"+p2).val("");
	openDownloadGallery(p1,  p2,  p3);
	$(treewin).bind("beforeunload",function(){
		downloadCss();
		$(this).close();
	});

}
function downloadCss(){
	if($("#cssfile").val()!=""){
			$("#downloadFile").val($("#cssfile").val());
			$("#downloadForm").submit();
		
	}
}
function openUploadWidget(p1,p2,p3){
	$("#"+p2).val("");
	openDownloadGallery(p1,  p2,  p3);
	return false;
}
</script>
<form method="POST" id="downloadForm" target="_blank" action="<cms:link>/system/modules/com.thesys.opencms.common/pages/downloadFile.jsp</cms:link>">
	<input type="hidden" name="downloadFile" id="downloadFile">	
</form>
<div id="div_admin_menu" style="width:180px">
  <table width="100%" border="0" cellpadding="0" cellspacing="0" class="adm_border_01">
    <tr>
      <td><table width="100%" border="0" cellpadding="0" cellspacing="0" class="adm_text_01">
        <tr>
          <td>帳號：<cms:user property="name"/></td>
          </tr>
        <tr>
          <td align="left">
 		<!--客製選單-->
 		<table width="100%" border="0" cellspacing="0" cellpadding="0">
	            <tr>
	              <td class="adm_line_top_dot"><table border="0" cellspacing="0" cellpadding="0">
	                <tr>
	                  <td><img src="<cms:link>/system/modules/com.thesys.opencms.common/resources/images/icon_arrow.gif</cms:link>" width="12" height="12" /></td>
	                  <td> <a href="#" onclick="javascript:openDownloadWidget('widget',  'cssfile',  '402294174');return false;"><input type="hidden" id="cssfile" /><span class="adm_a">樣式表下載</span>&nbsp;</a></td>
	                </tr>
	              </table></td>
	            </tr>
	             <tr>
	              <td class="adm_line_top_dot"><table border="0" cellspacing="0" cellpadding="0">
	                <tr>
	                  <td><img src="<cms:link>/system/modules/com.thesys.opencms.common/resources/images/icon_arrow.gif</cms:link>" width="12" height="12" /></td>
	                  <td> <a href="#" onclick="javascript:openUploadWidget('widget',  'tmpfile',  '402294174');return false;"><input type="hidden" id="tmpfile"/><span class="adm_a">樣式表上傳</span>&nbsp;</a></td>
	                </tr>
	              </table></td>
	            </tr>
	             <tr>
	              <td class="adm_line_top_dot"><table border="0" cellspacing="0" cellpadding="0">
	                <tr>
	                  <td><img src="<cms:link>/system/modules/com.thesys.opencms.common/resources/images/icon_arrow.gif</cms:link>" width="12" height="12" /></td>
	                  <td><cms:contentload collector="singleFile" param="/index.html" editable="true"><span class="adm_a">購物首頁編輯&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;</cms:contentload></td>
	                </tr>
	              </table></td>
	            </tr>
	            <tr>
	              <td class="adm_line_top_dot"><table border="0" cellspacing="0" cellpadding="0">
	                <tr>
	                  <td><img src="<cms:link>/system/modules/com.thesys.opencms.common/resources/images/icon_arrow.gif</cms:link>" width="12" height="12" /></td>
	                  <td><cms:contentload collector="singleFile" param="/brand.html" editable="true"><span class="adm_a">形象頁編輯&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>&nbsp;</cms:contentload></td>
	                </tr>
	              </table></td>
	            </tr>
	          </table>
 		<!--客製選單-->         
          
		  </td>
          </tr>
          <tr>
          <td align="left">
 		<table width="100%" border="0" cellspacing="0" cellpadding="0">

            <tr>
              <td class="adm_line_top_dot"><table border="0" cellspacing="0" cellpadding="0">
                <tr>
                  <td><img src="<cms:link>/system/modules/com.thesys.opencms.common/resources/images/icon_arrow.gif</cms:link>" width="12" height="12" /></td>
                  <td> <a href="#" onclick="MM_openBrWindow('<cms:link>/system/modules/com.thesys.opencms.common/pages/password.html</cms:link>','','width=400,height=400')" ><span class="adm_a">變更密碼</span></a></td>
                </tr>
              </table></td>
            </tr>
			 </table>     
          
		  </td>
          </tr>
        <tr>
          <td align="center" class="adm_line_top_dot"><div class="adm_top_5"><a href="<cms:link>/system/modules/com.thesys.opencms.common/pages/logout.html</cms:link>"><span class="adm_a">[ 登出 ]</span></a></div></td>
        </tr>
      </table></td>
    </tr>
  </table>
</div>
<script type="text/javascript">
<!--
flevInitPersistentLayer('div_admin_menu',0,'0','','','0','','',10);
//-->
</script>
<%}%>