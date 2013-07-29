<%@ page session="false" import="org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*,org.opencms.db.CmsUserSettings" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<% 
 
CmsLogin cms= new CmsLogin(pageContext, request, response);
String newPwd= request.getParameter("new_pwd");
String cfmPwd= request.getParameter("cfm_pwd");
boolean flag = false;
if(newPwd!=null){
	if(newPwd.equals(cfmPwd)){
		CmsUser loginUser = cms.getUser();
		out.println(loginUser.getName());
		try{
			CmsObject cmso = cms.getCmsObject();
			cmso.setPassword(loginUser.getName(),newPwd); //變更密碼
			out.println("<script>alert('您的密碼已變更，下次請使用新密碼登入');window.close();</script>");
			return;
			
		}catch(Exception ex){
			flag = false;
			out.println("<script>alert('變更密碼操作失敗，請重新操作');</script>");
		}
	}else{
		flag = false;
		out.println("<script>alert('新密碼與再一次輸入的密碼不符合，請重新操作');</script>");
	}
}
%>
 

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>變更密碼</title>
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
<body onload="MM_preloadImages('<cms:link>/system/modules/com.thesys.opencms.common/resources/images/save_password_o.jpg</cms:link>')">
<form id="fm" method="post" action="<%=cms.info("opencms.url")%>" onsubmit="return checkForm()">
<!--form id="fm" method="post" action="http://journal.nmtl.gov.tw:8080<cms:info property="opencms.uri"/>"-->
<table width="400" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td height="400" align="center" valign="top" class="bg_password"><table border="0" cellpadding="0" cellspacing="0">
      <tr>
        <td align="center" valign="top" class="h_p-1">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><table width="100%" border="0" cellspacing="0" cellpadding="0">
          <!--<tr>
            <td class="h_35">輸入舊密碼：</td>
            <td><input name="textfield" type="password" class="input_password" id="textfield" value="******" /></td>
          </tr>-->
          <tr>
            <td class="h_40" valign="top">輸入新密碼：</td>
            <td valign="top"><input name="new_pwd" type="password" maxlength="12" class="input_password" id="new_pwd" value="" onblur="checkNewPwd(this.value);"/><div id="new_pwd_msg" style="color:red">&nbsp;</div></td>
          </tr>
          <tr>
            <td class="h_40" valign="top">確認新密碼：</td>
            <td valign="top"><input name="cfm_pwd" type="password" maxlength="12" class="input_password" id="cfm_pwd" value=""  onblur="checkConfirmPwd(this.value);"/><div id="cfm_pwd_msg" style="color:red">&nbsp;</div></td>
          </tr>
        </table></td>
      </tr>
      <tr>
        <td align="center">&nbsp;</td>
      </tr>
      <tr>
        <td align="center"><input type="image" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image1','','<cms:link>/system/modules/com.thesys.opencms.common/resources/images/save_password_o.jpg</cms:link>',1)" src="<cms:link>/system/modules/com.thesys.opencms.common/resources/images/save_password.jpg</cms:link>" name="Image1" width="250" height="50" border="0" id="Image1" /></td>
      </tr>
    </table></td>
  </tr>
</table>
</form>
<script>
function checkNewPwd(pwd){
	var divObj = document.getElementById("new_pwd_msg");
	var msg = checkPwd(pwd,"新密碼");
	if(msg!=null){
		divObj.innerHTML = msg;
		return false;
	}else{
		divObj.innerHTML = "";
		return true;
	}
}
function checkConfirmPwd(pwd){
	var divObj = document.getElementById("cfm_pwd_msg");
	var msg = checkPwd(pwd,"確認新密碼");
	if(msg!=null){
		divObj.innerHTML = msg;
		return false;
	}else if(document.getElementById("new_pwd").value!=pwd){
		divObj.innerHTML = "與新密碼不相符";
		return false;	
	}else{
		divObj.innerHTML = "";
		return true;
	}
}
function checkPwd(pwd,show_name){
	if(pwd.length==0){
		return "請輸入"+show_name;
	}else if(pwd.length<6){
		return "長度必須大於等於6個字元";
	}else if(pwd.length>10){
		return "長度必須小於等於10個字元";
	}else{
		return null;
	}
}
function checkForm(){
	
	if(checkNewPwd(document.getElementById("new_pwd").value) && checkConfirmPwd(document.getElementById("cfm_pwd").value)){
		return true;
	}else{
		return false;
	}
}
</script>
</body>
</html>	
