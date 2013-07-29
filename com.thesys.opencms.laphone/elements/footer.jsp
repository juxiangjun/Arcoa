<%@ page trimDirectiveWhitespaces="true" %>
<%@page buffer="none" session="true" import="org.opencms.frontend.templatetwo.*,java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
	<!-- begin footer -->
	<cms:contentload collector="singleFile" param="/_config_/footer.html" editable="false">
        <div id="footer">
            <div class="center">
                <ul class="grey6 font12 bold">
                    <cms:contentloop element="BottomMenu">
                    <li><a href="<cms:link><cms:contentshow element="MenuLink"/></cms:link>" target="<cms:contentshow element="Target"/>" title="<cms:contentshow element="MenuText"/>"><cms:contentshow element="MenuText"/></a></li>
                    </cms:contentloop>
                </ul>
                <div class="contact grey8 font12">
		全虹企業股份有限公司 客戶服務電話：02-7738-0655<br>
		地址：11466 Taiwan(R.O.C)台北市內湖區民權東路六段18號6樓<br>
		Copyright 2013 All Rights Reserved 全虹企業版權所有
                </div>
                <a class="gotop" href="#"><img src="<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/footer_pic.png</cms:link>" /></a>
            </div>
        </div>
        </cms:contentload>
        <!-- end footer -->