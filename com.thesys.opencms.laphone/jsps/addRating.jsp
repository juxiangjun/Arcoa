<%
String itemId = request.getParameter("itemId");
String rating = request.getParameter("rating");
com.thesys.opencms.laphone.product.ThesysRatingHandler handler = new com.thesys.opencms.laphone.product.ThesysRatingHandler(pageContext,request,response);
if(itemId==null || rating==null){
	out.print("操作錯誤，請重新操作!");
	
}else if(handler.add(itemId ,Integer.parseInt(rating))){
	out.print("謝謝您的評分!");
}else{
	out.print("無法加入評分，請洽系統人員!");
}
%>
