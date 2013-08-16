<%@ page trimDirectiveWhitespaces="true" %>
<%
com.thesys.opencms.mail.ThesysMailHandler handler = new com.thesys.opencms.mail.ThesysMailHandler(pageContext,request,response);
handler.setMailConfiguration("/_config_/email/mailTest.html");
handler.addMacro("name","李紫微");
handler.addMailTo("maggie0219@hotmail.com");
handler.sendHtmlMail();

%>
