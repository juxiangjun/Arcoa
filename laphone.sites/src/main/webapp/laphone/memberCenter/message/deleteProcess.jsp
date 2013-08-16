<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="java.util.*,com.thesys.opencms.laphone.msg.*,com.thesys.opencms.laphone.msg.dao.*" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<%
ThesysSystemMsgHandler handler = null;
String[] del = request.getParameterValues("delete");
int msgType = request.getParameter("msgType") == null?0:Integer.parseInt(request.getParameter("msgType"));

if(del.length != 0 && msgType !=0){
	for(int i = 0;i<del.length;i++){
		handler = new ThesysSystemMsgHandler(pageContext,request,response);
		handler.setMessageId(Integer.parseInt(del[i]));
		handler.delete();	
	
	}
}
if(msgType ==1){
	out.print("<script>alert('已刪除');location.href='system/';</script>");
}else{
	out.print("<script>alert('已刪除');location.href='customer/';</script>");
}
return;
%>