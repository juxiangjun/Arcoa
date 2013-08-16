<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="org.apache.commons.fileupload.servlet.*,java.util.*,java.io.*,com.thesys.web.*,org.opencms.jsp.*,com.thesys.opencms.laphone.epaper.dao.*,com.thesys.opencms.laphone.epaper.*"%>
<%@ page import="java.text.*,com.thesys.opencms.laphone.util.*" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<cms:include file="/system/modules/com.thesys.opencms.manager/elements/loginCheck.jsp"/>
<jsp:useBean id="cmslogin" class="org.opencms.jsp.CmsJspLoginBean">
	 <% cmslogin.init(pageContext, request, response); %>
</jsp:useBean>
<%
	String cmsuser = cmslogin.getUserName();
	String subject ="";
	String serverFolder = "";
	String strSendDate = "";
	File folder = null;
	String target = application.getRealPath("/")+"epaper\\"; //要儲存的目標  ROOT\epaper\下
	java.util.Date releaseDate = null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	ThesysEpaperHandler handler = new ThesysEpaperHandler(pageContext, request, response);
	boolean succ = false;
	boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	if (isMultipart) { // 如果含有檔案資料
		ThesysFileUploadRequest fileRequest = new ThesysFileUploadRequest(request);
		String sendDate = fileRequest.getParameter("sendDate") == null ? "":(String)fileRequest.getParameter("sendDate");
		subject =  fileRequest.getParameter("subject") == null ? "":(String)fileRequest.getParameter("subject");
		releaseDate = sdf.parse(sendDate);
		String eno = handler.add(subject,cmsuser ,releaseDate ); //回傳此筆資料的ENO 編號
		
		if(eno != null){
			serverFolder =target+eno ;	//組成ENO
			
			folder = new File(serverFolder);
			if (!folder.exists()) {//如果資料夾不存在就做一個;
				folder.mkdirs(); 
			}
			
			InputStream in = (InputStream)fileRequest.getParameter("file");
			ThesysUnZip uzb = new ThesysUnZip(in , serverFolder ); //in也可以是java.io.File 也可以是String 路徑
			succ = uzb.unzip();
			if(!succ){//失敗的話
				handler.deleteRow(eno);
				out.println("<script>alert(\"上傳失敗\");location.href='index.html';</script>");
				return;
			}
		
		}else{
			out.println("<script>alert(\"新增失敗\");location.href='index.html';</script>");
			return;
		}
		
	} else { //如果沒有檔案資料		
		out.println("<script>alert(\"沒有檔案\");location.href='index.html';</script>");
		return;
	}
	
	
	out.println("<script>alert(\"電子報已上傳\");location.href='index.html';</script>");
	return;
	
%>
