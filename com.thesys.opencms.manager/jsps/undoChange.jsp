<%@ page buffer="none" import="com.thesys.opencms.xml.*,org.opencms.json.JSONObject,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%>
<%
ThesysCmsXmlHandler xmlHandler = new ThesysCmsXmlHandler(pageContext,request,response);
xmlHandler.unchangeFile(request.getParameter("xmlPath"));
OpenCms.getSearchManager().setOfflineUpdateFrequency(1000); //設定後台搜尋頻率為1秒
try { Thread.sleep(3000); }catch (InterruptedException e) { }
response.sendRedirect(request.getParameter("fromUrl"));
%>