<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="java.io.*,com.thesys.opencms.util.*,com.thesys.opencms.laphone.epaper.dao.*,com.thesys.opencms.laphone.epaper.*" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>										    						
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
	String eno = request.getParameter("eno") ==null ? "" :(String)request.getParameter("eno");
	boolean b = false;
	String target = application.getRealPath("/")+"epaper\\"+eno; //要刪除的目標  ROOT\epaper\下
	File folder = new File(target);
	
	if (folder.exists()) {//如果資料夾存在就清空;
		DeleteFolder.delFolder(target );
		b = true;
	}
	
	if(b = true){
		ThesysEpaperHandler handler = new ThesysEpaperHandler(pageContext, request, response);
		int res = handler.deleteRow(eno);
		if(res == 1){
			out.print("<script>alert('已刪除');location.href='index.html';</script>");
		}else{
			out.print("<script>alert('刪除錯誤');location.href='index.html';</script>");
		}
	}else{
		out.println("<script>alert(\"錯誤\");history.go(-1);</script>");
		return;
	}
%>