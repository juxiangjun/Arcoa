<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="org.apache.commons.codec.net.BCodec,org.opencms.main.*, org.opencms.search.*, org.opencms.file.*, org.opencms.jsp.*, java.util.*, java.sql.*, java.net.*,com.thesys.opencms.laphone.epaper.*,com.thesys.opencms.laphone.epaper.dao.*" %>
<%@ page buffer="none" import="java.io.*,org.apache.commons.net.ftp.*"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
String selectFolder =  request.getParameter("selectFolder") == null ? "" : (String)request.getParameter("selectFolder");
String zh = "";
if(selectFolder.equals("")) zh ="全部";
if(selectFolder.equals("Y")) zh ="訂閱";
if(selectFolder.equals("N")) zh ="取消訂閱";
String fileName = "Epaper"+zh+"名單.csv";

//ie用URLEncoder.encode firefox用BCodec().encode
if (request.getHeader("User-Agent").indexOf("MSIE") != -1  || request.getHeader("User-Agent").indexOf("Chrome") != -1){
	response.setHeader("Content-Disposition","attachment; filename=\""+java.net.URLEncoder.encode(fileName ,"utf-8") +"\"");
}else{
	response.setHeader("Content-Disposition","attachment; filename=\""+new BCodec().encode(fileName , "utf-8") +"\"");
}

ThesysSubscribeHandler handler = new ThesysSubscribeHandler(pageContext,request,response);
java.io.OutputStream outStream = response.getOutputStream();
handler.export(outStream ,selectFolder);
outStream .close();
%> 
