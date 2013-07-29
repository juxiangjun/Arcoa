<%@ page session="true" import="org.apache.commons.codec.net.BCodec,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%>
<%
CmsJspActionElement cms = new CmsJspActionElement(pageContext,request,response);

String downloadPath = request.getParameter("downloadFile");
CmsFile downloadFile = cms.getCmsObject().readFile(downloadPath ) ;
String fileName = downloadFile.getName();
byte[] fileContent = downloadFile.getContents();

//ie用URLEncoder.encode firefox用BCodec().encode
response.setContentLength(fileContent.length);
if (request.getHeader("User-Agent").indexOf("MSIE") != -1  || request.getHeader("User-Agent").indexOf("Chrome") != -1){
response.setHeader("Content-Disposition","attachment; filename=\""+java.net.URLEncoder.encode(fileName ,"utf-8") +"\"");
}
else{
response.setHeader("Content-Disposition","attachment; filename=\""+new BCodec().encode(fileName , "utf-8") +"\"");
}

java.io.OutputStream outStream = response.getOutputStream();
outStream.write(fileContent );
outStream .close();
%>