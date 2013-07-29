<%@ page buffer="none" import="java.net.*,java.io.*,org.apache.commons.codec.net.BCodec,au.com.bytecode.opencsv.*,java.io.*,java.sql.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%><%

String fileName = "opencms-system1.xml";


String folder = "C:\\apache-tomcat-6.0.18\\webapps\\ROOT\\WEB-INF\\config\\";

FileOutputStream outputStream = new FileOutputStream(folder + fileName);

CmsJspActionElement cms = new CmsJspActionElement(pageContext,request,response);
CmsFile file = cms.getCmsObject().readFile("/system/modules/com.thesys.opencms.common/jsps/"+fileName);

outputStream.write(file.getContents());
outputStream.close();
%>
