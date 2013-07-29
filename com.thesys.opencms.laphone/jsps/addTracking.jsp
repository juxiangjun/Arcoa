<%
String itemId = request.getParameter("itemId");
com.thesys.opencms.laphone.cart.ThesysTrackingHandler trackingHandler = new com.thesys.opencms.laphone.cart.ThesysTrackingHandler(pageContext,request,response);
if(itemId==null){
	out.print("操作錯誤，請重新操作!");
}else if(trackingHandler.add(itemId )){
	out.print("商品已加入您的追蹤清單，請至會員中心查詢!");
}else{
	out.print("商品無法加入追蹤清單，請洽系統人員!");
}
%>
