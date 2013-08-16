<%@ page import="java.util.*,com.thesys.opencms.laphone.epaper.*,com.thesys.opencms.laphone.epaper.dao.*" %>
<%@ page import="com.thesys.opencms.laphone.member.*,com.thesys.opencms.laphone.member.dao.*" %>
<%
ThesysSubscribeHandler handler = new ThesysSubscribeHandler(pageContext, request, response);
ThesysMemberHandler memberhandler = new ThesysMemberHandler(pageContext, request, response);
String email = request.getParameter("epaper-email") ==null?"":(String)request.getParameter("epaper-email");
String subscribe = request.getParameter("epaper-subscribe") ==null?"N":(String)request.getParameter("epaper-subscribe");
boolean wantSubscribe = subscribe.equals("Y");
boolean rs = false;

if(email.equals("") || email.trim().length() == 0 ){
	out.print("<script>alert('沒有輸入電子郵件');history.go(-1);</script>");
	return;
}
String applySrc = "EPAPER";
handler.setEmail(email);
ThesysSubscribeVO vo = handler.getRow();

if(wantSubscribe && vo != null && vo.isSubscribeFlag() ){   //如果要訂閱 已被訂閱的email
	out.print("<script>alert('此電子郵件已被訂閱過，請勿重複訂閱');history.go(-1);</script>");
	return;
}else if((!wantSubscribe && vo == null) || (!wantSubscribe && vo !=null && !vo.isSubscribeFlag())){  //如果要停止訂閱 未被訂閱的email
	out.print("<script>alert('查無此電子郵件');history.go(-1);</script>");
	return;
}else{
	ThesysMemberVO membervo = memberhandler.getMemberByEmail(email);
        int memberEdm = (membervo ==null)?0:membervo.getEdm();
	if(vo != null ){		  //如果要變更已有資料email的狀態
		if(membervo != null && membervo.isEmailFlag() != wantSubscribe){
			membervo.setEmailFlag(wantSubscribe);
			int newEdm = 0;
			if( memberEdm == 1 && !wantSubscribe )
				newEdm = 3;
			else if( memberEdm == 2 && !wantSubscribe )
				newEdm = 4;
			else if( memberEdm == 3 && wantSubscribe ) 
				newEdm = 1;
			else if( memberEdm == 4 && wantSubscribe ) 
				newEdm = 2;
			memberhandler.updateMemberEdm(membervo.getMemberNo(), newEdm );
			membervo.setEdm(newEdm);
		}
		vo.setApplySrc(applySrc);
		vo.setSubscribeFlag(wantSubscribe);
		rs = handler.chengStatus(vo);
	}else if(vo == null){			  //訂閱電子報
		rs = handler.subscribe(email,applySrc);
		if(membervo != null && !membervo.isEmailFlag()){
			membervo.setEmailFlag(true);
			int newEdm = 0;
			if( memberEdm == 3  ) 
				newEdm = 1;
			else if( memberEdm == 4  ) 
				newEdm = 2;
			memberhandler.updateMemberEdm(membervo.getMemberNo(), newEdm );
			membervo.setEdm(newEdm);
		}
		
	}
}
if(rs == true){
	if(wantSubscribe){
			out.println("<script>alert('訂閱成功');history.go(-1);</script>");
	}else{
			out.println("<script>alert('已取消訂閱');history.go(-1);</script>");
	}
}else{
	out.print("<script>alert('錯誤');history.go(-1);</script>");
}
%>