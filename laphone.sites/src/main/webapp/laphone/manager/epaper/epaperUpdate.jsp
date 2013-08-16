<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="org.apache.commons.fileupload.servlet.*,java.util.*,java.io.*,com.thesys.web.*,org.opencms.jsp.*,com.thesys.opencms.laphone.epaper.dao.*,com.thesys.opencms.laphone.epaper.*"%>
<%@ page import="java.text.*,com.thesys.opencms.laphone.util.*" %>
<%@ page import="org.apache.commons.fileupload.servlet.*,org.apache.commons.fileupload.disk.*,org.apache.commons.fileupload.*,java.util.*,java.text.*"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="cmslogin" class="org.opencms.jsp.CmsJspLoginBean">
	 <% cmslogin.init(pageContext, request, response); %>
</jsp:useBean>
<%
	String cmsuser = cmslogin.getUserName();
	String eno = "";
	String subject ="";
	String serverFolder = "";
	File folder = null;
	String target = application.getRealPath("/")+"epaper\\"; //要儲存的目標  ROOT\epaper\下
	java.util.Date releaseDate = null;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	boolean succ = true;
	boolean isMultipart = ServletFileUpload.isMultipartContent(request);
	if (isMultipart) { // 如果含有檔案資料
		ThesysFileUploadRequest fileRequest = new ThesysFileUploadRequest(request);
		eno = fileRequest.getParameter("eno") == null ? "":(String)fileRequest.getParameter("eno");
		String relDateStr = fileRequest.getParameter("sendDate") == null ? "":(String)fileRequest.getParameter("sendDate");
		releaseDate = sdf.parse(relDateStr);
		subject =  fileRequest.getParameter("subject") == null ? "":(String)fileRequest.getParameter("subject");
		
		serverFolder =target+eno ;
		folder = new File(serverFolder);
		
		InputStream in = (InputStream)fileRequest.getParameter("file");
		if(in != null && in.available() != 0){
			if (folder.exists()) {//如果資料夾存在就清空;
				DeleteFolder.delAllFile(serverFolder);
			}else{
				succ = false;
				out.println("<script>alert(\"電子報代號錯誤，查無資料夾\");history.go(-1);</script>");
				return;
			}
			ThesysUnZip uzb = new ThesysUnZip(in , serverFolder ); //in也可以是java.io.File 也可以是String 路徑
			uzb.unzip();
		}
	} else { //如果沒有檔案資料		
		out.println("<script>alert(\"沒有檔案\");location.href='index.html';</script>");
		return;
	}
	
	ThesysEpaperHandler handler = new ThesysEpaperHandler(pageContext, request, response);
	ThesysEpaperVO vo = handler.getEpaper(eno);
	int r = 0;
	if(vo != null){
		vo.setSubject(subject);
		vo.setReleaseDate(releaseDate);
		vo.setLastUpdater(cmsuser);
		r = handler.update(vo);
	}else{
		out.println("<script>alert(\"電子報代號錯誤\");history.go(-1);</script>");
		return;
	}
	if (r == 1) {
		out.println("<script>alert(\"修改成功\");location.href='index.html';</script>");
		return;
	}else{
		out.println("<script>alert(\"錯誤\");history.go(-1);</script>");
		return;
	}
	
%>
