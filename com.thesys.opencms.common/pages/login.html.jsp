<%@ page session="false" import="org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*,org.opencms.db.CmsUserSettings" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<% 
CmsJspActionElement cms = new CmsJspActionElement(pageContext,request,response);
 
CmsLogin loginBean = new CmsLogin(pageContext, request, response);
String user = request.getParameter("user");
String password = request.getParameter("password");
String ocOuFqn = "/"+(request.getParameter("ocOuFqn")==null?"":request.getParameter("ocOuFqn"));
String url = request.getParameter("url");  
if(url==null || url.length()==0 || url.equalsIgnoreCase("null")) url = "";
 
boolean loginFailed = false;
 
//form was submitted => try to log in and redirect to given URl
if((user != null) && (user.length() != 0)) {
		CmsObject cmsObject = cms.getCmsObject();
		loginBean.login(ocOuFqn+user, password);
		if (loginBean.getLoginException() == null) {
                    CmsUserSettings settings = new CmsUserSettings(cmsObject);
					try {
                        CmsProject project = cmsObject.readProject(settings.getStartProject());
                        if (OpenCms.getOrgUnitManager().getAllAccessibleProjects(cmsObject, project.getOuFqn(), false).contains(
                            project)) {
                            // user has access to the project, set this as current project
                            cms.getRequestContext().setCurrentProject(project);
                        }
                        //判斷登入的單位是否正確
			cmsObject.getRequestContext().setSiteRoot(settings.getStartSite());  
			response.sendRedirect(cms.link("/"));
                    } catch (CmsException e) {
                        out.println(e.getMessage());
                    }
		}else{
			user = null;
	%>
<script>
alert("登入失敗，請重新登入");
</script>
<%
	}	
}
%>
 
<%
//no user submitted or login failed => show login form
if(user==null || user.length()==0 || loginFailed) {
        %>	
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>後台管理登入</title>
<link href="<cms:link>/system/modules/com.thesys.opencms.common/resources/css/css.css</cms:link>" rel="stylesheet" type="text/css" />
<script type="text/javascript">
<!--
function MM_preloadImages() { //v3.0
  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
}
function MM_swapImgRestore() { //v3.0
  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
}
function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_swapImage() { //v3.0
  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
}
//-->
</script>
<script>
function formSubmit(){
	document.getElementById("fm").submit();
}
</script>

</head>
<body onload="MM_preloadImages('<cms:link>/system/modules/com.thesys.opencms.common/resources/images/login_01_o.jpg</cms:link>')">
<form id="fm" method="post" action="<cms:link><cms:info property="opencms.request.uri"/></cms:link>">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td class="login_bg_01-1">&nbsp;</td>
    <td height="700" class="login_bg_01">&nbsp;</td>
    <td align="center" valign="top" class="login_bg_02"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td align="center" valign="top" class="login_01">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="left" class="h_40">帳 號：&nbsp;</td>
            <td align="left"><input name="user" type="text" class="input_login" id="user" value="" /></td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td align="center"><table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="left" class="h_40">密 碼：&nbsp;</td>
            <td align="left"><input name="password" type="password" class="input_login" id="password" value="" /></td>
          </tr>
        </table></td>
      </tr>      
      <tr>
        <td align="center"><table width="100%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td align="left" class="h_40">單 位：&nbsp;</td>
            <td >
            <%if(cms.getCmsObject().getRequestContext().getSiteRoot().length()>0){%>
            	<select style='width: 100%;' size='1'  name="ocOuFqn" id="ocOuFqn" >
            	<%String ouName = cms.getCmsObject().getRequestContext().getSiteRoot().replaceAll("/sites/","");%>
		<option value='<%=ouName%>/'><%=ouName%> (/<%=ouName%>/)</option>
		</select>
	    <%}else{%>
            <%=loginBean.buildOrgUnitSelector()%>            
            <%}%>
            </td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><a href="#" onclick="formSubmit();" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image1','','<cms:link>/system/modules/com.thesys.opencms.common/resources/images/login_01_o.jpg</cms:link>',1)"><img src="<cms:link>/system/modules/com.thesys.opencms.common/resources/images/login_01.jpg</cms:link>" alt="登入" name="Image1" width="250" height="50" border="0" id="Image1" /></a></td>
      </tr>
    </table></td>
    <td class="login_bg_03">&nbsp;</td>
    <td class="login_bg_03-1">&nbsp;</td>
  </tr>
</table>
</form>
<script>
document.getElementById("ocOuFqn").className="select_login";
document.getElementById("ocOuFqn").style.width="250px";
</script>
</body>
</html>	
	
<%
}
%>