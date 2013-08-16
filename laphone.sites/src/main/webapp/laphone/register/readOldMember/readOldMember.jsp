<%@ page import="com.thesys.opencms.laphone.member.*,com.thesys.opencms.laphone.epaper.*,com.thesys.opencms.laphone.member.dao.*,java.util.*" %>

<%
		ThesysMemberHandler mhandler = new ThesysMemberHandler(pageContext, request, response);
		ThesysSubscribeHandler shandler = new ThesysSubscribeHandler(pageContext, request, response);
		ThesysReadOldMember rf = new ThesysReadOldMember();
		ArrayList altxt = rf.readFileToArrayList("C://oldMemberData//laphone.txt","UTF-8");
		rf.arrayListToSql(mhandler ,shandler ,altxt);
		
%>		