<%@page import="com.thesys.opencms.web.*,org.opencms.main.*,org.opencms.jsp.*,org.opencms.file.*,javax.servlet.http.*,javax.servlet.*"%><% 
org.opencms.jsp.CmsJspActionElement cms = new CmsJspActionElement(pageContext, request, response);
java.util.List<CmsResource> list = cms.getCmsObject().getFilesInFolder("/css/");
java.util.Iterator<CmsResource> it = list.iterator();
while(it.hasNext()){
	String rootPath = it.next().getRootPath();
	//列出樣式表
	if(rootPath.endsWith(".css")){ %><link rel="stylesheet" type="text/css" href="<%=cms.link(rootPath)%>">
<%
	}		
}
%>