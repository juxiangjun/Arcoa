//system/modules/com.thesys.opencms.manager/jsps/publish.jsp

<%@ page buffer="none" import="com.thesys.opencms.xml.*,org.opencms.json.JSONObject,java.util.*,org.opencms.main.*, 
org.opencms.search.CmsSearchManager,
org.opencms.jsp.*,org.opencms.file.*, 
java.lang.String,org.opencms.workplace.*"%>
<%
ThesysCmsXmlHandler xmlHandler = new ThesysCmsXmlHandler(pageContext,request,response);
String xmlPath = request.getParameter("xmlPath");
if(!"true".equals(request.getParameter("fileState"))){ //需更改屬性
	JSONObject properties = new JSONObject();
	properties.put("FileStatus","published"); //增加被修改的屬性，供後台搜尋
	xmlHandler.updateProperties(xmlPath,properties);
}else{ //不需更改屬性，表示為刪除
	xmlHandler.deleteFile(xmlPath);
}

xmlHandler.publishFile(xmlPath );
CmsSearchManager searchManager = OpenCms.getSearchManager();
searchManager.setOfflineUpdateFrequency(1000); //設定後台搜尋頻率為1秒
searchManager.setIndexLockMaxWaitSeconds(5);
try { Thread.sleep(3000); }catch (InterruptedException e) { }
out.println("<script>alert('資料已發佈');location.href='"+request.getParameter("fromUrl")+"';</script>");
%>