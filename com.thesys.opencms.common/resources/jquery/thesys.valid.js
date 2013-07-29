/**
欄位驗證的Form  class ：
	「alertForm」驗證訊息為alert；input需加上title=”中文欄位名稱”
	「fixForm」驗證訊息會顯示於你指定的位置；input後面需加上<span class="error" style=”color:red'></span>
	「不指定」驗證訊息會直接插入input後面
欄位驗證的Input class：(自動檢查sql injection)
	「required」： 必填
	「email」：email格式
	「pid」：身份證
	「length」：檢查長度=maxlength  (maxlength為input的屬性)
	「number」：數字	
        
自動套plug-in Input class：
        「htmlEditor」： html編輯器
        「date」：日期選擇器

 */
 $(document).ready(function(){	
	
	$("form").submit(function(){
		return validateForm($(this));
	});
	$("form").each(function(){
		$(this).find("span.error").hide();
	});
	/*
	$("form :input").blur(function(){
		var column = $(this);
		
		var msgType = getMsgType($(this).parents("form"));
		if(msgType=='normal'){
			column.siblings("span.error").remove();
			//column.next("span.error").remove();
		}else if(msgType=='fix'){
			column.siblings("span.error").html("");
			//column.next("span.error").html("");
		}
		if(msgType=='alert') msgType="alertField";
		
		var checkFlag = true;
		if(!checkSqlInjection(column,msgType )){
			checkFlag = false;
		}else if($(this).hasClass("required") && !checkRequired(column,msgType)){
			checkFlag = false;
		}else if($(this).hasClass("length") && !checkLength(column,msgType)){
			checkFlag = false;
		}else if($(this).hasClass("pid") && !checkPid(column,msgType)){
			checkFlag = false;
		}else if($(this).hasClass("email") && !checkEmail(column,msgType)){
			checkFlag = false;
		}else if($(this).hasClass("password") && !checkPassword(column,msgType)){
			checkFlag = false;
		}else if($(this).hasClass("confirm") && !checkConfirm(column,msgType)){
			checkFlag = false;
		}else if($(this).hasClass("mobile") && !checkMobile(column,msgType)){
			checkFlag = false;
		}else if($(this).hasClass("number") && !checkNumber(column,msgType)){
			checkFlag = false;
		}		
	});
	*/
});

function getMsgType(formSelector){
	if(formSelector.hasClass("alertForm")){
		return 'alert';
	}else if(formSelector.hasClass("fixForm")){
		return 'fix';
	}else{
		return 'normal';
	}
}
var formAlertMsg = "";
function validateForm(formSelector){	
	var msgType = getMsgType(formSelector);
	var flag = true;
	formAlertMsg = "";
	if(msgType=='normal'){
		formSelector.find("span.error").remove();
	}else if(msgType=='fix'){
		formSelector.find("span.error").html("");
		formSelector.find("span.error").hide();
	}
	//必填
	formSelector.find(".required:input").each(function(){
		if(this.tagName  == "INPUT" ||this.tagName  == "SELECT" ||this.tagName  == "TEXTAREA"){		
			if(!checkRequired($(this),msgType)){			
				flag = false;
			}
		}
	});
	//sql injection
	formSelector.find("input[type='text']").each(function(){		
		if(!checkSqlInjection($(this),msgType)){
			flag = false;
		}
	});

	//長度
	formSelector.find(".length").each(function(){		
		if(!checkLength($(this),msgType)){
			flag = false;
		}
	});	
	//身份證
	formSelector.find(".pid").each(function(){		
		if(!checkPid($(this),msgType)){
			flag = false;
		}
	});
	//email
	formSelector.find(".email").each(function(){
		if(!checkEmail($(this),msgType)){
			flag = false;
		}
	});
	//手機
	formSelector.find(".mobile").each(function(){
		if(!checkMobile($(this),msgType)){
			flag = false;
		}
	});
	//密碼
	formSelector.find(".password").each(function(){
		if(!checkPassword($(this),msgType)){
			flag = false;
		}
	});
	//密碼確認
	formSelector.find(".confirm").each(function(){
		if(!checkConfirm($(this),msgType)){
			flag = false;
		}
	});
	//純數字
	formSelector.find(".number").each(function(){
		if(!checkNumber($(this),msgType)){
			flag = false;
		}
	});
	if(!flag){
		if(msgType == 'alert'){
			alert(formAlertMsg );
		}else{
			alert("表單欄位驗證失敗，請檢查資料填寫是否正確");
		}
	}
	return flag;
}
function showErrorMessage(column,msg,msgType){
	if(msgType=='alert'){		
		var title = column.attr("title");
		if(title==undefined) title = '';
		else title = "「"+title+"」";
		
		if(formAlertMsg.length>0) formAlertMsg  = formAlertMsg +"\r\n";
		formAlertMsg = formAlertMsg  +title + msg;
		
	}else if(msgType=='alertField'){
		var title = column.attr("title");
		if(title==undefined) title = '';
		else title = "「"+title+"」";
		alert(title + msg);
		var id = column.attr("id");
		//if(id!=undefined) $("#"+id).focus();
	}else if(msgType=='fix'){
		column.parents(":first").children("span.error").html(msg).show();
	}else{
		if(column.parents(":first").children("span.error").length==0){
			if(column.attr("msg")!=null) msg = column.attr("msg");
			$("<span class=\"error\" style='color:red'>"+msg +"</span>").insertAfter(column);
		}	
	}
}
function checkRequired(column,msgType){
	var hasValue = false;
	if(column.attr('type')=="checkbox" || column.attr('type')=="radio"){
		var objName = column.attr("name");
		if($("input[name="+objName +"]:checked").length>0){
			hasValue = true;
		}
	}else{
		if(column.val()!="" && column.val()!=column.attr("alt")){
			hasValue = true;
		}
	}
	if(!hasValue){
		showErrorMessage(column,"必填欄位",msgType);
		return false;
	}
	return true;
}
function checkLength(column,msgType){
	var txt = column.val();
	var len = column.attr("maxlength");
	if(txt.length!=len){
		showErrorMessage(column,"長度必須等於"+len+"個字元",msgType);	
		return false;
	}
	return true;
}
function checkSqlInjection(column,msgType){
	var txt = column.val();
	if(!isSqlInjection(txt )){
		showErrorMessage(column,"格式輸入錯誤",msgType);	
		column.val("");
		return false;
	}
	return true;
}
function checkEmail(column,msgType){
	var txt = column.val();
	if(!isEmail(txt )){
		showErrorMessage(column,"電子郵件錯誤",msgType);	
		return false;
	}
	return true;
}
function checkMobile(column,msgType){
	var txt = column.val();
	if(!isMobile(txt )){
		showErrorMessage(column,"格式輸入錯誤",msgType);	
		return false;
	}
	return true;
}
function checkNumber(column,msgType){
	var txt = column.val();
	if(!isNumber(txt )){
		showErrorMessage(column,"格式輸入錯誤",msgType);	
		return false;
	}
	return true;
}
function checkPid(column,msgType){
	var txt = column.val();
	if(!isPid(txt )){
		showErrorMessage(column,"格式輸入錯誤",msgType);	
		return false;
	}
	return true;
}
function checkPassword(column,msgType){
	var txt = column.val();
	if(txt == "")return true;
	if(txt.length<6 || !isAlphanumeric(txt )){
		showErrorMessage(column,"密碼僅能使用英文及數字，請勿使用全型、中文、空白及其他符號字元，並至少包含1個英文、1個數字之組合",msgType);	
		return false;
	}
	return true;
}
function checkConfirm(column,msgType){
	var txt = column.val();
	if(txt!=$(".password").val()){
		showErrorMessage(column,"兩次密碼輸入不一致",msgType);	
		return false;
	}
	return true;
}
//只允許輸入09開頭的10位數行動電話
function isMobile(phonenumber){
	if (phonenumber=="") return true;
	rePhonenumber=/^09[0-9]{8}$/
	return rePhonenumber.test(phonenumber);
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
function isSqlInjection(txt){
  	if (txt =="") return true;
	reInjection=/^[^\'|!#|]+$/
	return reInjection.test(txt );
}
function isPid( id ) {
	id = id.toUpperCase();
	 if(id=='') return true;
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
/**純數字或字元**/
function isAlphanumeric(txt){
  if (txt=="") return true;
  pattern=/^[0-9a-zA-Z]+$/
  if(pattern.test(txt)){
  	if(/^[0-9]+$/.test(txt) ||/^[a-zA-z]+$/.test(txt)){ //å…¨æ•¸å­—æˆ–å…¨è‹±æ–‡
  		return false;
  	}else{
  		return true;
  	}
  }else{
  	return false;
  }
}