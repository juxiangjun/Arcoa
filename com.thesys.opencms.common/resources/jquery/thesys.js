//重新定位cms
function reposition(){
	if(typeof func_ocms_de_reposition =='function'){
		$("div[id^='ocms_']").each(func_ocms_de_reposition);
	}
}