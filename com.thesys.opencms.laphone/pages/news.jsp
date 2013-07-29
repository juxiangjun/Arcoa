<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<cms:include property="template" element="head" /> 
<cms:contentload collector="singleFile" param="%(opencms.uri)" editable="false" > 
<c:set var="currentFolder"><cms:info property="opencms.request.folder"/></c:set>
<div id="content">
  <div class="main">
        <div class="title">
            <h2><span><cms:property name="Title" file="${currentFolder}"/></span></h2>
        </div> 
        <cms:include file="/system/modules/com.thesys.opencms.laphone/elements/newsMenu.jsp"/>
        <div class="news-content">
        	<div class="news-title">
                        <c:set var="publishDate"><cms:contentshow element="PublishDate"/></c:set>
                    	<jsp:useBean id="dateValue" class="java.util.Date" /> 
			<jsp:setProperty name="dateValue" property="time" value="${publishDate}" /> 
			<fmt:formatDate value="${dateValue}" pattern="yyyy/MM/dd" /><br/>
			<h3><cms:contentshow element="Title"/></h3>
		</div>
                 <div class="news-msg">
                 	<cms:contentshow element="Content"/>
                 </div>
        </div>
  </div>
</div>    

</cms:contentload>
<cms:include property="template" element="foot" />  
