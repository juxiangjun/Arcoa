<%@page import="com.thesys.opencms.laphone.member.*,com.thesys.opencms.laphone.member.dao.*,com.thesys.opencms.laphone.util.*"%>
<%@include file="/system/modules/com.thesys.opencms.laphone/elements/loginCheck.jsp"%>
<%
	
	int smsErrTimes = session.getAttribute("smsErrTimes") == null?0:(Integer)session.getAttribute("smsErrTimes");
	String smsVerifyCode = request.getParameter("validationCode") ==null?"":(String)request.getParameter("validationCode").toLowerCase();
	String verifyCode = request.getParameter("verifyCode") ==null?"":(String)request.getParameter("verifyCode").toUpperCase();
	String rand = session.getAttribute("rand") ==null?"":(String)session.getAttribute("rand");
	java.util.Date now = new java.util.Date();
	if(smsErrTimes >1 && !rand.equals(verifyCode)){
		out.print("<script>alert('驗證碼錯誤');location.href='verify.html?dt="+new java.util.Date().getTime()+"';</script>");
		return;
	}
	ThesysMemberHandler handler = new ThesysMemberHandler(pageContext, request, response);
	int memberNo = (Integer)session.getAttribute("memberNo");
	String sessionSmsCode = (String)session.getAttribute("smsVerifyCode");
	Date sessionSmsDeadline = (Date)session.getAttribute("smsVerifyDeadline");
	if(smsVerifyCode.equals(sessionSmsCode)){
	 	if(sessionSmsDeadline.before(now)){
			out.print("<script>alert('簡訊驗證碼已過期，請重新發送一封簡訊驗證碼');location.href='verify.html?dt="+new java.util.Date().getTime()+"';</script>");
			return;
		}else{
			if(handler.enableMember(memberNo )){ //啟用
				session.removeAttribute("smsErrTimes");
				session.removeAttribute("smsVerifyCode");
				session.removeAttribute("smsVerifyDeadline");
				session.setAttribute("memberStatus",0);
				if(!handler.isEmailVerify(memberNo)){
					out.println("<script>alert('驗證成功，歡迎加入laphone');location.href='"+cms.link("/memberCenter/member/emailVerify.html")+"';</script>");
				}else{
					out.println("<script>alert('驗證成功，歡迎加入laphone');location.href='/index.jsp';</script>");
				}
			}else{
				out.print("<script>alert('驗證失敗，請稍後再試');location.href='verify.html?dt="+new java.util.Date().getTime()+"';</script>");
			}	
		}
	}else{
		session.setAttribute("smsErrTimes",smsErrTimes+1);
		smsErrTimes = (Integer)session.getAttribute("smsErrTimes");
		if(smsErrTimes >= 3){
			int count = handler.getSendCount(memberNo ,1);//查詢發送紀錄
			if(count >= 5){
				out.print("<script>alert('輸入錯誤3次且你今日已重寄超過五次，今日已不可再重寄');location.href='verify.html?dt="+new java.util.Date().getTime()+"';</script>");
				return;
			}else{
				//產生簡訊驗證碼
				String newVerifyCode = ThesysEncryption.getRandomStr(6);
				java.util.Date deadline = handler.sendVerifySms(memberNo,(String)session.getAttribute("memberMobile"),newVerifyCode );
				if(deadline !=null){
					
					session.setAttribute("smsVerifyCode",verifyCode );
					session.setAttribute("smsVerifyDeadline",deadline );					
					session.removeAttribute("smsErrTimes");
					out.print("<script>alert('輸入錯誤3次，系統將重新發送一封簡訊，請輸入新的簡訊驗證碼');location.href='verify.html?dt="+new java.util.Date().getTime()+"';</script>");
				}else{
					out.print("<script>alert('重送簡訊失敗，稍後再試');location.href='verify.html?dt="+new java.util.Date().getTime()+"';</script>");
				}
			}
		}else{
			out.print("<script>alert('驗證失敗，請重新輸入簡訊驗證碼');location.href='verify.html?dt="+new java.util.Date().getTime()+"';</script>");
		}
	}
	
%>