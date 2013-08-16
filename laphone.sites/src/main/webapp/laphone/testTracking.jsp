<%@ page trimDirectiveWhitespaces="true" %>
<%
com.thesys.opencms.laphone.stock.ThesysStockHandler handler = new com.thesys.opencms.laphone.stock.ThesysStockHandler(pageContext,request,response);
handler.setItemId("78034-22");
out.println(handler.getStockQuantity());


com.thesys.opencms.laphone.cart.ThesysTrackingHandler trackingHandler = new com.thesys.opencms.laphone.cart.ThesysTrackingHandler(pageContext,request,response);
trackingHandler.add("78034+23");
%>
