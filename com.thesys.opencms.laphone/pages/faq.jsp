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
            <div class="page">
                <h3><cms:contentshow element="Question"/></h3>
                <p><cms:contentshow element="LongAnswer"/></p>
            </div>
            <div class="ft"></div>
        </div>
    </div>    
</cms:contentload>
<cms:include property="template" element="foot" />  
