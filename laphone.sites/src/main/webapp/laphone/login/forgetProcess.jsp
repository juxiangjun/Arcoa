<%@page import="com.thesys.opencms.laphone.member.*,com.thesys.opencms.laphone.member.dao.*,java.util.*"%>
<%@page import="org.opencms.json.*,com.thesys.opencms.laphone.util.*,com.thesys.opencms.laphone.system.*"%>
<%
	
	String accountId = request.getParameter("accountId") ==null?"":(String)request.getParameter("accountId").toLowerCase();
	String idNo = request.getParameter("idNo") ==null?"":(String)request.getParameter("idNo");
	String verifyCode = request.getParameter("verifyCode") ==null?"":(String)request.getParameter("verifyCode").toUpperCase();
	String rand = session.getAttribute("rand") ==null?"":(String)session.getAttribute("rand");
	String randomPwd = "";
	String email = "";
	String cellphone ="";
	boolean isOldMember = false;
	int rus = 0;
	if(!rand.equals(verifyCode)){     
		out.print("<script>alert('驗證碼錯誤');history.go(-1);</script>");
		return;
	}
	
	ThesysMemberHandler handler = new ThesysMemberHandler(pageContext, request, response);
	ThesysMemberVO member = null;
	
	if(accountId.length() ==0 && idNo.length() ==0 ){
		out.print("<script>alert('輸入錯誤');history.go(-1);</script>");
		return;
	}else{
		if(accountId.length()>0){//  一般會員
			member = handler.getMemberByAccountId(accountId);
			if(member == null){
				out.print("<script>alert('查無此帳號');history.go(-1);</script>");
				return;
			}
		}else if(idNo.length()>0){	//舊會員
			isOldMember = true;
			member = handler.getMemberByIdNo(idNo);
			if(member == null){
				out.print("<script>alert('查無此身分證字號');history.go(-1);</script>");
				return;
			}
			if(member.getAccountId() !=null && !member.getAccountId().equals("")){
				out.print("<script>alert('此帳號已完成舊會員設定，請使用一般會員登錄頁查詢密碼');location.href='forget.html';</script>");
				return;
			}
		}
	}
	//檢查發送記錄是否超過5次
	int count = handler.getSendCount(member.getMemberNo(),2);//查詢發送紀錄
	if(count  >= 5){
		out.print("<script>alert('你今日已重寄超過五次，今日已不可再重寄');history.go(-1);</script>");
		return;
	}
	
	if(accountId.length()>0 && !handler.forgetPassword(member )){
		out.print("<script>alert('密碼查詢錯誤');history.go(-1);</script>");
		return;
	}else if(idNo.length()>0 && !handler.forgetOldMemberPassword(member )){
		out.print("<script>alert('密碼查詢錯誤');history.go(-1);</script>");
		return;
	}else{
		if(isOldMember){
			out.print("<script>alert('系統已發送一組新的隨機密碼簡訊至您當初會員資料中所留的手機，請您登入後立即進行密碼變更');location.href='convertLogin.html';</script>");
		}else{
			out.print("<script>alert('系統已發送一組新的隨機密碼簡訊至您當初會員資料中所留的手機，請您登入後立即進行密碼變更');location.href='index.html';</script>");
		}
	}

%>