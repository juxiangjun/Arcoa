<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
﻿﻿$(function () {
    $("#validateOk").hide();
    $("#validateError").hide();
    $("#accountId").blur(function () {
        $("#validateOk").hide();
        $("#validateError").hide();
       $("#accountId").parents(":first").find("span.error").html("");
        var flag = true;
        
        if($(this).val().length<6 || $(this).val().length>20){			
           showErrorMessage($("#accountId"),"帳號僅能使用英文及數字，請勿使用全型、中文、空白及其他符號字元","fix");	
            flag=false;
	}else if(!isNorD($(this).val())){
	   showErrorMessage($("#accountId"),"帳號僅能使用英文及數字，請勿使用全型、中文、空白及其他符號字元","fix");	
           flag=false;
	}
	/*		
        var myUrl = "<cms:link>/system/modules/com.thesys.opencms.laphone/jsps/convertAccountIdCheck.jsp</cms:link>?accountId=" + $("#accountId").val() + "&rnd=" + Math.random();
       $.ajax({
            url: myUrl,
            data:{accountId:$("#accountId").val()},
            type: "POST",
            success: 	function (jdata) {
               if (jdata.trim() =='true' && flag) {
                    //$("#validateOk").show();
                    //$("#validateError").hide();
                    $("#accountId").parents(":first").find("span.error").html("");
                    $("#accountId").parents(":first").find("span.error").hide();
                }
                else {
                    //$("#validateError").show();
	            if(jdata.trim() != 'true' && flag)
	           	 showErrorMessage($("#accountId"),"帳號重複。","fix")
                }
            }
        });
        */
        var myUrl = "<cms:link>/system/modules/com.thesys.opencms.laphone/jsps/accountIdCheck.jsp</cms:link>?accountId=" + $("#accountId").val() + "&rnd=" + Math.random();

        $.ajax({
            url: myUrl,
            type: "GET",
            dataType: "json",
            success: function (jdata) {
                if (jdata && jdata.result && flag) {
                    //$("#validateOk").show();
                    //$("#validateError").hide();
                    $("#accountId").parents(":first").find("span.error").html("");
                    $("#accountId").parents(":first").find("span.error").hide();
                }
                else {	
                    //$("#validateOk").hide();
                    //$("#validateError").show();
                	if(!(jdata && jdata.result) && flag == true)
                		 showErrorMessage($("#accountId"),"帳號重複。","fix");	
                }
            }
        });
    });
     
});
function isNorD(num){
  if (num=="") return true;
  reNorD=/^[A-Za-z0-9]+$/
  return reNorD.test(num);
}