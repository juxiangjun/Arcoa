<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<cms:include property="template" element="head" />
<cms:contentload collector="singleFile" param="%(opencms.uri)" editable="false" > 
    <div id="content">
        <div class="main">
            <div class="title">
                <h2><span><cms:contentshow element="Title"/></span></h2>
            </div>
            <div class="privacy">
                <cms:contentloop element="Paragraph">
                    <h3><cms:contentshow element="Title"/></h3>
                    <p><cms:contentshow element="Content"/><p>
                </cms:contentloop>
                
            </div>
        </div>
    </div>    
</cms:contentload>
<cms:include property="template" element="foot" />  
