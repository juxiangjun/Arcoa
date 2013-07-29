<%@ page buffer="none" import="com.thesys.opencms.xml.*,org.opencms.json.JSONObject,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%>
<%
CmsJspActionElement cms = new CmsJspActionElement(pageContext,request,response);
JSONObject contents = new JSONObject();
JSONObject properties = new JSONObject();
String rootXpath = null;
String tag = null;
int tagId = 0;

java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy/MM/dd");
java.text.SimpleDateFormat bdFmt = new java.text.SimpleDateFormat("MM/dd/yyyy");
for (Enumeration params  = request.getParameterNames() ; params.hasMoreElements() ;) {
	String param = (String)params.nextElement();
	String val = request.getParameter(param);
	if("tagName".equals(param)){
		tag = val;
	}else if("rootXpath".equals(param)){
		rootXpath = val;
	}else if("tagId".equals(param)){
		tagId = Integer.parseInt(val);	
	}else if(!"action".equals(param) &&!"autoPublish".equals(param) && !"xmlPath".equals(param) && !"fromUrl".equals(param)){
		if(param.endsWith("Date")){ //轉形成long
			if( val.length()>0){
				long date = fmt.parse(val).getTime();
				contents.put(param,String.valueOf(date));
			}else{
				contents.put(param,String.valueOf(0));
			}
		
		}else{
			contents.put(param,val);
		}
			
	}
}

ThesysCmsXmlHandler xmlHandler = new ThesysCmsXmlHandler(cms.getCmsObject());
String xmlPath = request.getParameter("xmlPath");
if(rootXpath ==null) rootXpath ="/";


if("add".equals(request.getParameter("action"))){ 		
	xmlHandler.add(xmlPath,rootXpath,tag ,contents,properties);	
}else{		
	xmlHandler.update(xmlPath,rootXpath ,contents,properties);
}

if("true".equals(request.getParameter("autoPublish"))){
	xmlHandler.publishFile(xmlPath);
	out.println("<script>alert('資料已儲存!');location.href='"+request.getParameter("fromUrl")+"';</script>");
}else{
	out.println("<script>alert('資料已儲存，但並未發佈!');location.href='"+request.getParameter("fromUrl")+"';</script>");
}
%>
