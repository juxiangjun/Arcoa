<%@ page buffer="none" import="java.net.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%
CmsJspActionElement cms = new CmsJspActionElement(pageContext,request,response);
CmsObject cmso = cms.getCmsObject();
pageContext.setAttribute("AdminFlag",cmso.isManagerOfProject());
%>
<cms:editable mode="true" />
<c:choose>
  <c:when test="${empty ocmsValue}">
    <c:set var="ocmsValue" scope="request">999999</c:set>
  </c:when>
  <c:otherwise>
    <c:set var="ocmsValue" scope="request">${ocmsValue - 1}</c:set>
  </c:otherwise>
</c:choose>
<c:choose>
<c:when test="${AdminFlag}">
  <script type="text/javascript">
  ocms_de_data['ocms_${ocmsValue}']= {
	id: 'ocms_${ocmsValue}',
	deDisabled: false,
	hasEdit: false,
	hasDelete: false,
	hasNew: true,
	resource: '/system/modules/com.thesys.opencms.jcic/schemas/resources/${param.resourcetype}.html',
	editLink: '<cms:link>/system/workplace/editors/editor.jsp</cms:link>',
	language: 'zh',
	element: 'null',
	backlink: '<cms:info property="opencms.request.uri"/>',
	newlink: '<%=URLEncoder.encode(URLEncoder.encode(request.getParameter("collector")+"|"+request.getParameter("filename")+"|"+request.getParameter("resourcetype"),"UTF-8"),"UTF-8")%>',
	closelink: '<cms:link><cms:info property="opencms.request.uri"/></cms:link>',
	deletelink: '<cms:link>/system/workplace/commons/delete.jsp</cms:link>',
	button_edit: '直接編輯',
	button_delete: '刪除',
	button_new: '新增',
	editortitle: '編輯（新資源）'
  };
  </script>
  <div id="ocms_${ocmsValue}" class="ocms_de_norm">
    ${param.content}
  </div>
</c:when>
<c:otherwise>
	${param.content}
</c:otherwise>
</c:choose>