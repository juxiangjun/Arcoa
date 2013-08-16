<%@page import="com.thesys.opencms.laphone.util.*,com.thesys.opencms.laphone.member.*,com.thesys.opencms.laphone.member.dao.*,com.thesys.opencms.laphone.system.*,org.opencms.json.*"%>
<%@include file="/system/modules/com.thesys.opencms.laphone/elements/loginCheck.jsp"%>
<%
	ThesysMemberHandler handler = new ThesysMemberHandler(pageContext,request,response);
	/*ThesysMemberVO vo = (ThesysMemberVO)session.getAttribute("member");
	
	ThesysParamHandler paramHandler = new ThesysParamHandler(pageContext, request, response);
	String host = paramHandler.getValueByParamKey("WEB_URL");	
	 */
	int memberNo = (Integer)session.getAttribute("memberNo");
	String memberEmail = (String)session.getAttribute("memberEmail");
	String verifyCode = (String)session.getAttribute("mailVerifyCode");
	if(!handler.sendVerifyMail(memberNo,memberEmail,verifyCode)){
		out.print("<script>alert('錯誤');history.go(-1);</script>");
		return; 
	}else{
		/*
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DATE, 3);
		java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
		String date = sdf.format(ca.getTime());
		String href = host + "account_email_verify.jsp?id=" +vo.getAccountId() + "&mailv=" +vo.getMailVerifiCode() +"&dl="+date;
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("href",href);
		jsonObj.put("host",host);
		ThesysSendMsgHandler msghandler = new ThesysSendMsgHandler(pageContext,request,response);
		msghandler.setMsgName("sendVerify");
		msghandler.setJsonObj(jsonObj);
		msghandler.setEmail(vo.getEmail());
		msghandler.sendMsg();
		*/
		out.print("<script>alert('信已寄出，請查看信件開通');location.href='emailVerify.html';</script>");
		
	}
%>