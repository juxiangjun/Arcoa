<%@ page buffer="none" import="java.util.* ,com.thesys.opencms.laphone.member.*,com.thesys.opencms.laphone.member.dao.*, com.thesys.opencms.laphone.msg.*,com.thesys.opencms.laphone.system.*,org.opencms.json.*,com.thesys.opencms.laphone.util.*,com.thesys.opencms.mail.*"%>
<%


	String accountId = session.getAttribute("memberId")==null?null:(String)session.getAttribute("memberId");
//	ThesysMemberVO vo = (ThesysMemberVO)session.getAttribute("member");
	
	boolean isMember = false ;
	String username = request.getParameter("username") == null?"":(String)request.getParameter("username");
	String phone = request.getParameter("phone") == null?"":(String)request.getParameter("phone");
	String email = request.getParameter("email") == null?"":(String)request.getParameter("email");
	int msgType = request.getParameter("msgType") == null?0:Integer.parseInt(request.getParameter("msgType"));
	String msgContent = request.getParameter("msgContent") == null?"":(String)request.getParameter("msgContent");
	String memberId = "";
	
//	if(vo != null){
	if(accountId != null){
		isMember = true;
		username = (String)session.getAttribute("memberName");//vo.getMemberName();
		phone = (String)session.getAttribute("memberMobile");//vo.getCellphone();
		email = (String)session.getAttribute("memberEmail");//vo.getEmail();
		memberId = accountId ;//vo.getAccountId() ;
	}
	
	ThesysGuestMsgHandler handler = new  ThesysGuestMsgHandler(pageContext,request,response);
	
	int msgId = handler.add(isMember , memberId , username ,phone ,email ,msgType ,msgContent );
	
	
	//發E-mail
	ThesysParamHandler paramHandler = new ThesysParamHandler(pageContext, request, response);
	String host = paramHandler.getValueByParamKey("WEB_URL");//http://192.168.7.9:8080/shopping/ 
	
	JSONObject jsonObj = new JSONObject();
	jsonObj.put("msgId",msgId );
	jsonObj.put("host",host);
	
	ThesysSendMsgHandler msghandler = new ThesysSendMsgHandler(pageContext,request,response);
	msghandler.setMsgName("serviceQuestion");
	msghandler.setJsonObj(jsonObj);
	msghandler.setEmail(email);
	msghandler.sendMsg();	
	
	
	String msg = "";
	String subject ="[我的提問]";
	
	if(isMember){	
		switch (msgType) {
			case 1:subject  +="商品內容不正確";break;
			case 2:subject  +="商品瑕疵";break;
			case 3:subject  +="退換貨";break;
			case 4:subject  +="取消廠商出貨訂單";break;
			case 5:subject  +="購物步驟";break;
			case 6:subject  +="付款方式";break;
			case 7:subject  +="發票";break;
			case 8:subject  +="維修／保固";break;
			case 9:subject  +="超商取貨";break;
			case 10:subject +="商品資訊";break;
			case 11:subject +="電子報";break;
			case 12:subject +="帳號／密碼／認證";break;
			default:break;
			}
		ThesysSystemMsgHandler syshandler = new ThesysSystemMsgHandler(pageContext, request,response);
		syshandler.add(memberId,2,msgId,subject,null,null);
	}
		
	out.println("<script>alert(\"謝謝你提出的問題，我們會盡速以E-mail回覆給您\");location.href='index.html';</script>");

%>