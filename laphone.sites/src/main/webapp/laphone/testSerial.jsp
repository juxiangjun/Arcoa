<%@ page trimDirectiveWhitespaces="true" %>
<%
com.thesys.opencms.laphone.system.ThesysSerialHandler handler = new com.thesys.opencms.laphone.system.ThesysSerialHandler(pageContext,request,response);
out.println(handler.getNextSerialNo("MEM"));
out.println(handler.getNextSerialNo("COUPON"));
//out.println(handler.getNextTableSerialNo("LAPHONE_GUEST_MSG"));
%>
