<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="com.thesys.opencms.web.*,org.opencms.main.*,org.opencms.jsp.*,javax.servlet.http.*,javax.servlet.*"%>
<% 
    org.opencms.jsp.CmsJspActionElement cms = new CmsJspActionElement(pageContext, request, response);
    String siteId = cms.getRequestContext().getAdjustedSiteRoot(cms.getRequestContext().getUri());
    
    //判斷是否已登入過 沒有的話，就將瀏覽人次加1
    if (session.getAttribute("SessionViewCount")==null ||session.getAttribute("SessionViewCount").equals("")) {
      SiteVisitController.addSiteVisitCount(cms);
      session.setAttribute("SessionViewCount", "View");
    }
    
    //讀取瀏覽人數
    String visitCount = SiteVisitController.getSiteVisitCount(cms,7);
    out.println(visitCount );
%>