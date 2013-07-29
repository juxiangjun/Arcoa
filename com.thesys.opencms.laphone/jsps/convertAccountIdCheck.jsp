<%@ page import="java.util.*,com.thesys.opencms.laphone.member.*,com.thesys.opencms.laphone.member.dao.*" %>
<%
	boolean d = true;
	String accountId =  request.getParameter("accountId") == null ? "" : ((String)request.getParameter("accountId"));  
	String rnd =  request.getParameter("rnd") == null ? "" : ((String)request.getParameter("rnd"));  
	ThesysMemberHandler handler = new ThesysMemberHandler(pageContext,request,response);
	
	ThesysMemberVO vo = handler.getMemberByAccId(accountId);
	if(vo != null){
		d = false;   
	}
	out.print(d);
%>
