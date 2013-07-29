<%@ page trimDirectiveWhitespaces="true" %>
<%@ page buffer="none" import="java.util.*,org.opencms.main.*, org.opencms.jsp.*,org.opencms.file.*, java.lang.String,org.opencms.workplace.*"%>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
<cms:include property="template" element="head" />
<script type="text/javascript">
$(function () {
	$("#btnSubmit").click(function(){
		if ($("#agree").is(":checked")) {
			$("#agreeForm").submit();
			return false;
		}else {
			alert("很抱歉，您必須同意此服務條款方能註冊成為會員。\r\n同意請勾選 \" 我已詳細閱讀，並同意接受以上內容 \" 並按「我同意」進行會員註冊，不同意請按「我不同意」");
			return false;
		}
	
	});
});
</script>
    
<div id="content">
  <div class="main">
  	<div class="register-steps"><div class="step1"></div></div>
        <h2 class="register-title">註冊前請先詳閱，以維護您的權益!</h2>
        <div class="agreement">
            <cms:contentload collector="singleFile" param="%(opencms.uri)" editable="false" > 
        	<cms:contentloop element="Paragraph">
                <h3><cms:contentshow element="Title"/></h3>
                <p><cms:contentshow element="Content"/></p>
                </cms:contentloop>
            </cms:contentload>
        </div>
              <div class="agreement-checkbox"><form method="post" action="<cms:link>/register/step2.html</cms:link>" id="agreeForm"><input type="checkbox" id="agree" name="agree" />&nbsp;我已詳細閱讀，並同意接受以上內容</form></div>
               <div class="btn-line-right">
               		 <a href="<cms:link>/index.html</cms:link>" class="button">我不同意</a>
                         <a href="#" class="button" id="btnSubmit">我同意</a>
               </div>
    </div>
</div>



<cms:include property="template" element="foot" />  
