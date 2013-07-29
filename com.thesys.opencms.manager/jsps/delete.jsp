<%@ page buffer="none" import="com.thesys.opencms.xml.*,org.opencms.json.JSONObject,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%>
<%
ThesysCmsXmlHandler xmlHandler = new ThesysCmsXmlHandler(pageContext,request,response);
String xmlPath = request.getParameter("xmlPath");
String fileState = request.getParameter("fileState");
if("new".equals(fileState) || "true".equals(request.getParameter("autoPublish"))){ //新檔案或自動發佈時，直接刪除
	xmlHandler.deleteFile(xmlPath);
	if("true".equals(request.getParameter("autoPublish"))){	
		xmlHandler.publishFile(xmlPath);
	}
	out.println("<script>alert('資料已刪除!');location.href='"+request.getParameter("fromUrl")+"';</script>");
}else{	//加註刪除註記，並沒有直接刪除
	JSONObject properties = new JSONObject();
	properties.put("FileStatus","deleted"); //增加被修改的屬性，供後台搜尋
	xmlHandler.updateProperties(xmlPath,properties);
	out.println("<script>alert('資料已刪除，但並未發佈!');location.href='"+request.getParameter("fromUrl")+"';</script>");
}
OpenCms.getSearchManager().setOfflineUpdateFrequency(1000); //設定後台搜尋頻率為1秒
try { Thread.sleep(3000); }catch (InterruptedException e) { }
%>