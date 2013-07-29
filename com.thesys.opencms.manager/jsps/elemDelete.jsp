<%@ page buffer="none" import="com.thesys.opencms.xml.*,org.opencms.json.JSONObject,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
CmsJspActionElement cms = new CmsJspActionElement(pageContext,request,response);

ThesysCmsXmlHandler xmlHandler = new ThesysCmsXmlHandler(cms.getCmsObject());
String xmlPath = request.getParameter("xmlPath");
String rootXpath =request.getParameter("rootXpath");
String tagName = request.getParameter("tagName");
int tagId = Integer.parseInt(request.getParameter("tagId"));	
xmlHandler.remove(xmlPath,rootXpath + tagName ,tagId);

if("true".equals(request.getParameter("autoPublish"))){
	xmlHandler.publishFile(xmlPath);
	out.println("<script>alert('資料已刪除!');location.href='"+request.getParameter("fromUrl")+"';</script>");
}else{
	out.println("<script>alert('資料已刪除，但並未發佈!');location.href='"+request.getParameter("fromUrl")+"';</script>");
}
%>
