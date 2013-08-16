<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="org.apache.commons.fileupload.servlet.*,java.util.*,java.io.*,com.thesys.web.*,org.opencms.jsp.*,com.thesys.opencms.laphone.cvs.dao.*,com.thesys.opencms.laphone.cvs.*"%>
<%@ page import="com.thesys.opencms.laphone.member.*" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="/system/modules/com.thesys.opencms.manager/elements/loginCheck.jsp"%>
<jsp:useBean id="cms" class="org.opencms.jsp.CmsJspLoginBean">
	 <% cms.init(pageContext, request, response); %>
</jsp:useBean>
<%
String fmt = "UTF-8";
boolean succ = false;
String cmsuser = cms.getUserName();
boolean isMultipart = ServletFileUpload.isMultipartContent(request);
ThesysMemberHandler handler = new ThesysMemberHandler(pageContext, request, response);
InputStream in = null;
int count = 0 ;
if (isMultipart) { // 如果含有檔案資料

	ThesysFileUploadRequest fileRequest = new ThesysFileUploadRequest(request);
	in = (InputStream)fileRequest.getParameter("file");
	count = handler.importBlackList(in,fmt,cmsuser );
	succ = true;
} else { //如果沒有檔案資料		
	out.println("<script>alert(\"沒有檔案\");location.href='index.html';</script>");
	return;
}

if(in != null)in.close();

		
//成功的話
if(succ){
	out.println("<script>alert(\"成功匯入"+count+"筆\");location.href = 'index.html';</script>");
	return;
}else{
	out.println("<script>alert(\"匯入失敗\");location.href='index.html';</script>");
	return;
}	
	

	
%>




