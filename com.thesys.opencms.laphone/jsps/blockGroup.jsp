<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="org.opencms.main.*, org.opencms.search.*, org.opencms.file.*, org.opencms.jsp.*, java.util.*, java.sql.*, java.net.*" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="currentFolder"><cms:info property="opencms.request.folder"/></c:set>
<cms:include property="template" element="head"/>
<jsp:useBean id="cms" scope="request" class="org.opencms.jsp.CmsJspActionElement">
<%cms.init(pageContext,request,response);%>
</jsp:useBean>
<%
/*
String displayPage = request.getParameter("displayPage");
String currentFolder = cms.info("opencms.request.folder");

String defPath = request.getParameter("defPath");
defPath = (defPath==null || "".equals(defPath))?"/special/":defPath;
String searchPath = defPath;

if(!"new".equals(displayPage)){
	//folder不存在即進入第一層
	if(folder==null || "".equals(folder) || !cms.getCmsObject().existsResource(defPath + folder)){
		List list = cms.getCmsObject().getSubFolders(defPath);
		if(list!=null && list.size()>0){
			CmsResource cr = (CmsResource)list.get(0);
			searchPath = defPath + cr.getName();
		} else {
			//沒有subFolder導向首頁
			out.print("<script>location.href='"+cms.link("/index.html")+"';</script>");
			return;
		}
	} else {
		searchPath = defPath + folder;
	}
}
pageContext.setAttribute("searchPath",searchPath);
*/
%>
<c:set var="minDateLong" value="0"/>
<c:set var="maxDateLong" value="2556028800000"/>
<c:set var="count">0</c:set>

<%
java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy/MM/dd");
java.util.Date now = fmt.parse(fmt.format(new java.util.Date()));
pageContext.setAttribute("nowLong",now.getTime());
%>
			
<jsp:useBean id="search" scope="request" class="org.opencms.search.CmsSearch">
	<jsp:setProperty name="search" property="index" value="LAPHONE_BLOCKGROUP_INDEX"/>
	<jsp:setProperty name="search" property="searchPage" param="1"/>
	<jsp:setProperty name="search" property="displayPages" value="100"/>
	<jsp:setProperty name="search" property="searchRoot" value="${currentFolder}"/>
	<c:set var="query">OnlineDate:[${minDateLong} TO ${nowLong}] AND (OfflineDate:"0" OR OfflineDate:[${nowLong} TO ${maxDateLong}])</c:set>
	<%
	search.init(cms.getCmsObject());
	CmsSearchParameters params = search.getParameters();
	params.setQuery((String)pageContext.getAttribute("query"));
	search.setParameters(params);
	search.setSortOrder(com.thesys.opencms.search.SearchHelper.getIntSort("Sort", false));
	%>
</jsp:useBean>
<c:set var="searchResult" value="${search.searchResult}"/>
<c:if test="${!empty searchResult}">
	<c:forEach var="entry" items="${searchResult}" varStatus="status">
		<c:set var="xmlPath" value="${fn:replace(entry.path,cms.requestContext.siteRoot,'')}"/>
		<cms:contentload collector="thesysSingleFile" param="${xmlPath}" editable="false">
			<c:set var="blockIndex" value="0"/>
	    		<div class="block_group block_group<cms:contentshow element="GroupType"/>">
	    		<cms:contentloop element="Product">
	            		<c:set var="blockIndex" value="${blockIndex+1}"/>
				<cms:include file="/system/modules/com.thesys.opencms.laphone/elements/productBlock.jsp">
					<cms:param name="blockIndex">${blockIndex}</cms:param>
					<cms:param name="productCode"><cms:contentshow element="ProductCode"/></cms:param>
					<cms:param name="imagePath"><cms:link><cms:contentshow element="ImagePath"/></cms:link></cms:param>
				</cms:include>
			</cms:contentloop>
			</div>
			<c:set var="count">${count+1}</c:set>
			<c:if test="${count%2==0}">
			<!-- 廣告 -->
			<cms:include file="/system/modules/com.thesys.opencms.laphone/elements/banner.jsp">
				<cms:param name="PrintPosition">${fn:indexOf(currentFolder,'new')>=0?"AD-new943x100":"AD-sp943x100"}</cms:param>
			</cms:include>
			<!-- 廣告 -->
			</c:if>
		</cms:contentload>
	</c:forEach>
</c:if>
<c:if test="${empty searchResult}"><div style="height: 200px; text-align : center ">此頁建置中...</div></c:if>


<cms:include property="template" element="foot" /> 