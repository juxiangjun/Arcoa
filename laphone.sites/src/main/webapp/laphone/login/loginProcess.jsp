<%@ page import="java.util.*,com.thesys.opencms.laphone.member.*,com.thesys.opencms.laphone.member.dao.*" %>
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<% cms.init(pageContext,request,response); %>
</jsp:useBean>

<%

int loginErrTimes = session.getAttribute("loginErrTimes")==null?0:(Integer)session.getAttribute("loginErrTimes");
String accountId = request.getParameter("accountId") ==null?"":(String)request.getParameter("accountId").toLowerCase();
String beforePath = (String)request.getParameter("beforePath");
String eccode = "?eccode="+(String)request.getParameter("eccode");
String idNo = request.getParameter("idNo") ==null?"":(String)request.getParameter("idNo").toUpperCase();
String pwd = request.getParameter("pwd") ==null?"":(String)request.getParameter("pwd");
String loginType = request.getParameter("loginType") ==null?"accountId":(String)request.getParameter("loginType");

String verifyCode = request.getParameter("verifyCode") ==null?"":(String)request.getParameter("verifyCode").toUpperCase();
String rand = session.getAttribute("rand") ==null?"":(String)session.getAttribute("rand");
if(loginErrTimes > 1 && !rand.equals(verifyCode)){
	out.print("<script>alert('驗證碼錯誤');location.href='index.html';</script>");
	return;
}
ThesysMemberHandler handler = new ThesysMemberHandler(pageContext, request, response);

if(loginType.equals("idNo")){	//舊會員登入
	ThesysMemberVO oldMember = handler.oldMemberLogin(idNo,pwd);
	if(oldMember == null){
		session.setAttribute("loginErrTimes",loginErrTimes+1);
	  	out.print("<script>alert('帳號或密碼錯誤，請重新登入');location.href='convertLogin.html';</script>");
	  	return;
	}else{
		session.removeAttribute("loginErrTimes");
		if(oldMember.getAccountId() != null && !oldMember.getAccountId().equals("")){
			out.print("<script>location.href='convertAlready.html';</script>");
		  	return;
		}else{
			session.setAttribute("oldMember",oldMember);
			out.println("<script>location.href='convertForm.html';</script>");
		}
	}
}else{ 	//一般會員登入
	ThesysMemberVO member = handler.login(accountId,pwd);
	if(member != null){		
		session.removeAttribute("loginErrTimes");
		session.setAttribute("memberNo",member.getMemberNo());
		session.setAttribute("memberId",member.getAccountId());
		session.setAttribute("cardId",member.getCardId());
		session.setAttribute("memberName",member.getUsername());
		session.setAttribute("memberMobile",member.getCellphone());
		session.setAttribute("memberEmail",member.getEmail());
		session.setAttribute("mailVerifyCode",member.getMailVerifyCode());
		session.setAttribute("smsVerifyCode",member.getSmsVerifyCode());
		session.setAttribute("smsVerifyDeadline",member.getSmsVerifyDeadline());
				
		if(!member.isOverPasswordDate()){//上次改密碼距離現在不到90天,即上次改密碼在今天減90天之後
			if(member.getStatus()== 2){ //未完成簡訊驗證				
				out.println("<script>location.href='verify.html?dt="+new java.util.Date().getTime()+"';</script>");
			}else{		//已完成簡訊驗證
				if(member.getMailStatus() == 0){
					out.println("<script>location.href='"+cms.link("/memberCenter/member/emailVerify.html")+"';</script>");
				}else{
					if(eccode.equals("?eccode="))eccode = "";
					if(beforePath == null || beforePath.equals(""))beforePath = "/index.html";
					out.println("<script>location.href='"+cms.link(beforePath)+eccode +"';</script>");
				}	
			}
		
		}else{       //上次改密碼距離現在超過90天
			out.println("<script>location.href='notice.html';</script>");
		}
	}else{
		session.setAttribute("loginErrTimes",loginErrTimes+1);
		out.print("<script>alert('帳號或密碼錯誤，請重新登入');location.href='index.html';</script>");
	  	return;
	}
}


%>