<%@ page buffer="none" import="java.net.*,java.io.*,org.apache.commons.codec.net.BCodec,au.com.bytecode.opencsv.*,java.io.*,java.sql.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%><%

String fileName = "opencms-system.xml";
//ie用URLEncoder.encode firefox用BCodec().encode
if (request.getHeader("User-Agent").indexOf("MSIE") != -1  || request.getHeader("User-Agent").indexOf("Chrome") != -1){
	response.setHeader("Content-Disposition","attachment; filename=\""+java.net.URLEncoder.encode(fileName ,"utf-8") +"\"");
}
else{
	response.setHeader("Content-Disposition","attachment; filename=\""+new BCodec().encode(fileName , "utf-8") +"\"");
}

String folder = "C:\\apache-tomcat-6.0.18\\webapps\\ROOT\\WEB-INF\\config\\";

FileReader reader = new FileReader(folder + fileName);
java.io.OutputStream outStream = response.getOutputStream();

while(true){
	int b = reader.read();
	if(b==-1) break;
	outStream.write(b);
}
outStream .close();
%>
