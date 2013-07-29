$(document).ready(function(){
	$("#registerForm").submit(function(){		
		if(validateForm("#registerForm")){
			return true;
		}	
		return false;
	});
	$("#verifyForm").submit(function(){
		var flag = true;
		if($(".validationCode").val() =="" || $(".validationCode").val().trim().length ==0){
			alert("請輸入簡訊驗證碼");
			return false;
		}else if($(".verifyCode").val() =="" ||  $(".verifyCode").val().trim().length ==0){
			alert("請輸入驗證碼");
			return false;
		}
		return true;
	});
	$("#loginForm").submit(function(){
		 if($("#accountId").val() == "" || $("#accountId").val().trim().length ==0){
		 	alert("請輸入帳號");
		 	$("#accountId").foucs();
		 	return false;
		 }else if($("#pwd").val() =="" || $("#pwd").val().trim().length ==0){
		 	alert("請輸入密碼");
		 	$("#pwd").foucs();
		 	return false;
		 }else if($("#verifyCode").val() =="" || $("#verifyCode").val().trim().length !=6){
		 	alert("請輸入完整驗證碼");
		 	$("#verifyCode").foucs();
		 	return false;
		 }
		 return true;
	
	});	
	$("#convertForm").submit(function(){
		 if($("#accountId").val() == "" || $("#accountId").val().trim().length ==0){
		 	alert("請輸入帳號");
		 	return false;
		 }else if($("#accountId").val().length<6 || $("#accountId").val().length>20){			
                      alert("帳號需為6至20個字元");
	 		return false;
		}else if(!isNumless($("#accountId").val())){			
	             alert("帳號限為數字或英文");
	 		return false;
		}else if($("#validateOk").is(":hidden")){
			alert("帳號輸入錯誤");
 			return false;
		}else{
			return true;
		}
	
	});
	$("#converForgetForm").submit(function(){
		if($(".IdNo").val() =="" || $(".IdNo").val().trim().length ==0){
			alert("請輸入身份證字號");
			return false;
		}else if($(".verifyCode").val() =="" ||  $(".verifyCode").val().trim().length ==0){
			alert("請輸入驗證碼");
			
			return false;
		}
		return true;
	});	
	$("#forgetForm").submit(function(){
		var flag = true;
		if($(".accountId").val() =="" || $(".accountId").val().trim().length ==0){
			alert("請輸入帳號");
			return false;
		}else if($(".verifyCode").val() =="" ||  $(".verifyCode").val().trim().length ==0){
			alert("請輸入驗證碼");
			return false;
		}
		return true;
	});
	
	$("#modifyFormSubmit").click(function(){	
		if(validateForm("#modifyForm")){
			$("#modifyForm").submit();
		}	
		return false;
	});
	$("#changePwdForm").submit(function(){	
		return validateChangePwdForm("#changePwdForm");
	});
	$("#serviceSubmit").click(function(){
		if(validateForm("#serviceForm")){
			$("#serviceForm").submit();
		}
		return false;	
	});

});
function validateChangePwdForm(formSelector){	
	if($(".oldPwd").val() == "" || $(".oldPwd").val().trim().length == 0){
	 	alert("請輸入原密碼");
	 	return false;
	}
	if($(".newPwd").val() == "" || $(".newPwd").val().trim().length == 0){
	 	alert("請輸入新密碼");
	 	return false;
	}else if($(".newPwd").val().length<6 || $(".newPwd").val().length>20){			
	      alert("新密碼需為6至20個字元");
	      return false;
	}else if(!isNumless($(".newPwd").val())){			
              alert("限為數字與英文");
              return false;
	}else if(!isPassword($(".newPwd").val())){
	      alert("須為英文與數字之組合");
              return false;
	}	
	if($(".pwdConfirm").val() == "" || $(".pwdConfirm").val().trim().length == 0){
	 	alert("請再輸入一次新密碼");
	 	return false;
	}
	if($(".pwdConfirm").val() != $(".newPwd").val()){
	         alert("兩次新密碼輸入不同");
	         return false;
	}
	if($(".oldPwd").val() == $(".newPwd").val()){
	         alert("新舊密碼不可相同");
	         return false;
	}
	return true;
}
function validateForm(formSelector){	
     $(formSelector+" .error").remove();
  	
	var flag = true;

     $(formSelector+" .required").each(function(){
     	  if($(this).attr("type") == "text" || $(this).attr("type") == "password" ){
             var hasValue = false;
	     if($(this).val()!="" && $(this).val().trim().length !=0){
	         	hasValue = true;
	       }
             if(!hasValue){
                var msg = "此為必填欄位,且不得有空白。";
                flag=showErr($(this),msg); 
	     }
	     if($(this).attr("class")=="accountId")
	     		$(" .accountId").focus().blur();
     	  }
     	  if($(this)[0].tagName == "SELECT" && $(this).val() == 0){
     	  	var msg = "請選擇";
                flag=showErr($(this),msg);
     	  }
     	  if($(this)[0].tagName == "TEXTAREA" &&($(this).val() == "" || $(this).val().trim().length ==0)){
     	  	var msg = "請輸入";
                flag=showErr($(this),msg);
     	  }
     })
     $(formSelector+" .text").each(function(){
             if(!checkSqlInjection($(this).val())){ 
                if(checkSqlInjectionZh($(this).val())){
                      var msg = "請勿填寫中文";
                }else{
		      var msg = "請勿填寫特殊字元,包括空白";
                }   
		      flag=showErr($(this),msg);
             }
     })

     $(formSelector+" .zh").each(function(){ 
             if(!checkSqlInjectionZh($(this).val())){
		var msg = "請勿填寫特殊字元,包括空白";
		flag=showErr($(this),msg);
             }
     });
    
  
      $(formSelector+" .email").each(function(){ 
             if(!isEmail($(this).val())){
		var msg = "格式錯誤";
		flag=showErr($(this),msg);
             }
     });
     $(formSelector+" .cellphone").each(function(){ 
             if(!isCellphone($(this).val())){ 
		var msg = "格式錯誤";
		flag=showErr($(this),msg);
             }
     });
     $(formSelector+" .isnumber").each(function(){ 
             if(!isNumber($(this).val())){ 
		var msg = "格式錯誤";
		flag=showErr($(this),msg);
             }
     });
     var s = $('input[name=idType]:checked').val()  
     if(s=="1"){
	     $(formSelector+" .idNo").each(function(){
	             if(!isIdNo($(this).val().toUpperCase())){ 
			var msg = "格式錯誤";
			flag=showErr($(this),msg);
	             }
	     });
     }else if(s=="2"){
     	//護照格式
     
     
     
     }
     if($('input:[name=gender]').size() != 0){
	     var check_gender =$('input:checked[name=gender]').size();
	     if(check_gender == 0){
		 var msg = "請選擇性別";
	         flag=showErr($(".gender"),msg);
	     }
     }
      $(formSelector+" .accountId").each(function(){
              $(" .accountId").focus().blur();
     });
     $(formSelector+" .password").each(function(){
             if($(this).val().length!=0){
		if($(this).val().length<6 || $(this).val().length>20){			
                      var msg = "需為6至20個字元"; 
                      flag=showErr($(this),msg);
		}else if(!isNumless($(this).val())){			
	              var msg = "限為數字或英文"; 
                      flag=showErr($(this),msg);
		}else if(!isPassword($(this).val())){
		      var msg = "須為英文與數字之組合"; 
                      flag=showErr($(this),msg);
		}			
	     }
     });
    
     $(formSelector+" .pwdVerify").each(function(){
             if($(this).val().length!=0){
		if($(this).val().length<6 || $(this).val().length>20){	 
                      var msg = "需為6至20個字元,限為數字或英文";
                      flag=showErr($(this),msg);
		}else if(!isNumless($(this).val())){			
	              var msg = "需為6至20個字元,限為數字或英文";
                      flag=showErr($(this),msg);
		}			
	     }
	     if($(this).val() != $(".password").val()){
	         var msg = "兩次密碼輸入錯誤";
                 flag=showErr($(this),msg);
	     }
     });
     $(formSelector+" .addr-county").each(function(){
     	var d= $(this).val();   
     	if($("#fullAddress").val() != "" && checkSqlInjectionZh($("#fullAddress").val())){  
            if(d == ""){
            	var msg = "請選擇縣市";
	         flag=showErr($("#fullAddress"),msg);
            }
        } 
     })
     $(formSelector+" .edm").each(function(){ 
   	 var d= $("#edm").val();
	    if(d == 0){
	    	 var msg = "請選擇是否願意接受廣告";
	         flag=showErr($(this),msg);
	    }
    })
    
    
   
    return flag;
 }  





function checkSqlInjectionZh(str){
  if (str=="") return true;
  reInjection=/^[\w*_\u4E00-\u9fa5.-]+$/   //只可為英文 數字 底線 也允許使用中文
  return reInjection.test(str);
}
function checkSqlInjection(str){
  if (str=="") return true;
   reInjection=/^[\w*_.]+$/              //只可為英文 數字 底線 點
 // reInjection=/^[A-Za-z0-9_]+$/       //只可為英文 數字 底線
 // reInjection=/^[^\'|!#|;]+$/
  return reInjection.test(str);
}

function isNumless(str){
  if (str=="") return true;
   rePassword=/^[\w*]+$/              //只可為英文 數字 
  return rePassword.test(str);
}
String.prototype.trim=function(){      
    return this.replace(/(^\s*)|(\s*$)/g, '');   
}
      

function showErr(formSelector ,str){
  if(formSelector.parents("td").children("span.error").length==0){
        var msg = str;
	if(formSelector.attr("msg")!=null) msg = $(this).attr("msg");
		formSelector.parents("td:first").find("span[name=errmsg]").append("<span class=\"error\" style='color:red;white-space: nowrap;'>"+msg +"</span>");
		formSelector.parents("li:first").find("span[name=errmsg]").append("<span class=\"error\" style='color:red;white-space: nowrap;'>"+msg +"</span>");	
		
  }
  		return false;
}
function isEmail(email){
  if (email=="") return true;
  reEmail=/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/
  return reEmail.test(email);
}
function isNumber(num){
  if (num=="") return true;
  reNumber=/^[0-9]+$/
  return reNumber.test(num);
}
function isIdNo(id) {
	if(id=="") return true;
	  tab = "ABCDEFGHJKLMNPQRSTUVXYWZIO"                     
	  A1 = new Array (1,1,1,1,1,1,1,1,1,1,2,2,2,2,2,2,2,2,2,2,3,3,3,3,3,3 );
	  A2 = new Array (0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5,6,7,8,9,0,1,2,3,4,5 );
	  Mx = new Array (9,8,7,6,5,4,3,2,1,1);

	  if ( id.length != 10 ) return false;
	  i = tab.indexOf( id.charAt(0) );
	  if ( i == -1 ) return false;
	  sum = A1[i] + A2[i]*9;

	  for ( i=1; i<10; i++ ) {
		v = parseInt( id.charAt(i) );
		if ( isNaN(v) ) return false;
		sum = sum + v * Mx[i];
	  }
	  if ( sum % 10 != 0 ) return false;
	  return true;
}
//只允許輸入09開頭的10位數行動電話
function isCellphone(phonenumber){
	if (phonenumber=="") return true;
	rePhonenumber=/^09[0-9]{8}$/
	return rePhonenumber.test(phonenumber);
}
//判斷radio是否為空值
function emptyRadioField(Obj)
{
        for(var i=0 ;i<Obj.length;i++){
                if(Obj[i].checked){
                        return true;alert("123");
                }
        }
        return false;
}
function isPassword(pwd){
  if (pwd=="") return true;
  rePassword=/^[0-9a-zA-Z]+$/
  if(rePassword.test(pwd)){
  	if(/^[0-9]+$/.test(pwd) ||/^[a-zA-z]+$/.test(pwd)){ //全數字或全英文
  		return false;
  	}else{
  		return true;
  	}
  }else{
  	return false;
  }
}