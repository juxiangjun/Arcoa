<%@page import="com.thesys.opencms.laphone.member.*,com.thesys.opencms.laphone.member.dao.*,java.util.*,java.text.*"%>
<%
	ThesysMemberHandler handler = new ThesysMemberHandler(pageContext, request, response);
	
	String code = request.getParameter("code");
	int memberNo = 0;
	String verifyCode = "";
	long deadline = 0;
	
	if(code==null){
		out.print("<script>alert('請至會員中心重新發送一封驗證信');location.href='login/index.html';</script>");
		return;
	}
	
	try{
		 memberNo = Integer.parseInt(code.substring(0,6));
		 verifyCode = code.substring(6,12);
		 deadline = Long.parseLong(code.substring(12));
	} catch (Exception e) {
		out.print("<script>alert('驗證錯誤，請至會員中心重新發送一封驗證信');location.href='login/index.html';</script>");
		return;
	}
	
	
	
	
	if(deadline < new Date().getTime()){
		out.print("<script>alert('驗證信過期，請至會員中心重新發送一封');location.href='login/index.html';</script>");
		return;
	}else if(handler.verifyEmail(memberNo,verifyCode)){
		session.setAttribute("mailStatus",1);			
		out.print("<script>alert('此信箱已驗證成功');location.href='index.html';</script>");
	}else{
		out.print("<script>alert('驗證錯誤');location.href='login/index.html';</script>");
	}
%>