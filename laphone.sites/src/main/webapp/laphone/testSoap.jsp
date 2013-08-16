<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="java.net.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%>
<%

String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><ORDER_DOC><ORDER><EDCNO><![CDATA[D05]]></EDCNO><AMT>0</AMT><TRADETYPE>1</TRADETYPE><REALAMT>2277</REALAMT><STNO><![CDATA[L00F782]]></STNO><SERCODE>963</SERCODE><CUTKNM><![CDATA[林令傑3]]></CUTKNM><PRODNM>0</PRODNM><CUTKTL><![CDATA[0953815198]]></CUTKTL><ECWEB><![CDATA[www.laphonetaiwan.com]]></ECWEB><ECNO>195</ECNO><ODNO><![CDATA[00112180001]]></ODNO><ECSERTEL><![CDATA[0800-032-123]]></ECSERTEL></ORDER><ORDERCOUNT><TOTALS>1</TOTALS></ORDERCOUNT></ORDER_DOC>";

String endpointURL = "https://cvsweb.cvs.com.tw/webservice/service.asmx";  
  String namespaceURI = "http://tempuri.org/" ;//命名空间
  String soapactionURI = "http://tempuri.org/ORDERS_ADD"; //soapactionURI
  String remotemethod = "ORDERS_ADD";//方法名
  
  
  
  org.apache.axis.client.Service  service = new org.apache.axis.client.Service();
  
  org.apache.axis.client.Call call=(org.apache.axis.client.Call) service.createCall();
  
  call.addParameter(new javax.xml.namespace.QName(namespaceURI,"xmlStr"),org.apache.axis.encoding.XMLType.XSD_STRING ,javax.xml.rpc.ParameterMode.IN);
  call.setReturnType(org.apache.axis.encoding.XMLType.XSD_SCHEMA);
 
  
  call.setUseSOAPAction(true); 
  call.setSOAPActionURI(soapactionURI);   
  call.setTargetEndpointAddress(new java.net.URL(endpointURL).toString() );
  javax.xml.namespace.QName  qname = new javax.xml.namespace.QName(namespaceURI, remotemethod);
  call.setOperationName(qname);
     
  org.apache.axis.types.Schema schema = (org.apache.axis.types.Schema) call.invoke(new Object[]{xmlStr });
   
  //Object obj = call.invoke(new Object[]{xmlStr });
  out.println(schema.get_any().length);
  String res = schema.get_any()[0].getAsString();
  out.println(res);
%>