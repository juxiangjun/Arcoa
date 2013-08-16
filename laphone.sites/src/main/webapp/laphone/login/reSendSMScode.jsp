
<%@page import="com.thesys.opencms.laphone.util.*,com.thesys.opencms.laphone.member.*,com.thesys.opencms.laphone.member.dao.*"%>
<%@include file="/system/modules/com.thesys.opencms.laphone/elements/loginCheck.jsp"%>
<%
	ThesysMemberHandler handler = new ThesysMemberHandler(pageContext, request, response);
	int memberNo = (Integer)session.getAttribute("memberNo");
	
	int count = handler.getSendCount(memberNo ,1);
	if(count >= 5){
		out.print("<script>alert('你今日已重寄超過五次，今日已不可再重寄');history.go(-1);</script>");
		return;
	}
	
	
	String verifyCode = ThesysEncryption.getRandomStr(6);
	/*vo = handler.addSMSDeadline(vo);
	vo.setSmsVerifiCode(randomstr);	
	int r = handler.modifyForSystem(vo);
	*/
	java.util.Date deadline = handler.sendVerifySms(memberNo,(String)session.getAttribute("memberMobile"),verifyCode);
	if(deadline ==null){
		out.print("<script>alert('錯誤');history.go(-1);</script>");
		return;
	}else{
		/*
		handler.addSendRacord(vo.getSno(),1);
		//傳簡訊...
		com.thesys.opencms.laphone.util.ThesysSMSHandler smshandler = new com.thesys.opencms.laphone.util.ThesysSMSHandler(pageContext,request,response);
		smshandler.setSMSConfiguration("/_config_/sms/sendVerify.html");
		smshandler.addMacro("name",vo.getUsername());
		smshandler.addMacro("randomstr",randomstr);
		smshandler.addMoblieTo(vo.getCellphone());
		smshandler.sendSMS();
		*/
		session.setAttribute("smsVerifyCode",verifyCode );
		session.setAttribute("smsVerifyDeadline",deadline );
		out.print("<script>alert('驗證碼已發送，請查看手機並輸入');location.href='verify.html';</script>");
	}
%>