<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="org.opencms.main.*, org.opencms.search.*, org.opencms.file.*, org.opencms.jsp.*, java.util.*, java.sql.*, java.net.*" %>
<%@ page import="com.thesys.opencms.laphone.job.order.*,com.thesys.opencms.laphone.system.dao.*" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<%cms.init(pageContext,request,response);%>
</jsp:useBean>
<%
ThesysParamDAO dao = ThesysParamDAO.getInstance();
ThesysParamVO vo = dao.getParam("/sites/laphone", "OPERATIONS_DIVISION_EMAIL");
//String operationsDivisionEmail = dao.getParam("/sites/laphone", "OPERATIONS_DIVISION_EMAIL").getParamVal();
//out.print(operationsDivisionEmail);
//ThesysOrderCancelJob job = new ThesysOrderCancelJob();
//job.launch(cms.getCmsObject(),null);

%>