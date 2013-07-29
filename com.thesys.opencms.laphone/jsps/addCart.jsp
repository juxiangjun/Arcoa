<%
String itemId = request.getParameter("itemId");
int quantity = request.getParameter("quantity")==null?0:Integer.parseInt(request.getParameter("quantity"));

com.thesys.opencms.laphone.cart.ThesysCartHandler cartHandler = new com.thesys.opencms.laphone.cart.ThesysCartHandler(pageContext,request,response);
if(itemId==null || quantity==0){
	out.print("操作錯誤，請重新操作!");
}else if(cartHandler.add(itemId,quantity )){
	out.print("商品已加入購物車，請至購物車查詢!");
}else{
	out.print("商品無法加入購物車，請洽系統人員!");
}
%>