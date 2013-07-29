<%@page buffer="none" session="false" import="org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*" %>
<%

	//登出
	CmsJspLoginBean cms = new CmsJspLoginBean(pageContext, request, response);
	//out.println(cms.link("/index.jsp"));
	//response.sendRedirect(cms.link("/index.jsp"));
	if(cms.isLoggedIn()){
		cms.logout();
	}else{
		//String siteId = cms.getRequestContext().getSiteRoot();
		//out.println(cms.link("/index.jsp"));
		response.sendRedirect("http://"+request.getServerName()+":"+request.getServerPort()+"/admin/index.jsp?rdm="+(new java.util.Date()).getTime());
	}
%>
