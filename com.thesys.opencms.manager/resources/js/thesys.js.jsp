<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
$(function() { 
    $(".iconBtnAdd").each(function(){
	if($(this).parent(":first").children(":input").length==3) $(this).hide();
    });
    $(".iconBtnDelete").click(function(){					
	$(this).prev().remove();
	$(this).parent(":first").children(".iconBtnAdd").show();
	$(this).remove();
	return false;
    });
    $(".iconBtnAdd").click(function(){
	var spanId = $(this).attr("href");
	
	$($(spanId).children().clone(true)).insertBefore($(this));		
	
	
	
	if($(this).parent(":first").children(":input").length==3) $(this).hide();
	return false;
    });
    $(".date").each(function(){
    	var val = $(this).val();
    	$(this).datepicker({
        	showOn: "both",
	        buttonImage: "<cms:link>/system/modules/com.thesys.opencms.manager/resources/images/calendar.jpg</cms:link>",
	        dateFormat: 'yy/mm/dd',
	        minDate: new Date('2012/1/1'),
	       /* maxDate: new Date,*/
	        changeMonth: true,
	        changeYear: true,
	        showAnim: 'fadeIn',
	        /*yearRange: '1920:' + new Date().getFullYear(),*/
	        showMonthAfterYear: true,
		defaultDate: new Date(new Date * 1 - 24 * 60 * 60 * 1000),
	        autoSize: true,
	        monthNamesShort: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
    	});
    });  
    $("#ui-datepicker-div").hide();//如不隱藏 ,google chrome 會出現一條白色的div
    
    $("#validateError").hide();
    $("#validateOk").hide();
    
    $("#pageIndex").change(function(){
    	$("#pagerForm").submit();
    });
    $(".btnSubmit").click(function(){
    	var formId = $(this).attr("href");
    	var url =  $(this).attr("ref");
    	if(url!='') $(formId ).attr("action",url);
    	$(".htmlEditor").each(function(){
    		var id = $(this).attr("id");
    		$(this).val(CKEDITOR.instances[id ].getData());
    	});
    	$(formId).submit();
	return false;	
    });
    
    $(".btnReset").click(function(){
    	var formId = $(this).attr("href");
    	var fid = $(formId).attr("id");
    	//$("form[id='"+fid+"']")[0].reset();
    	$("form[id='"+fid+"']").find("input[type=text]").each(function(){
    		$(this).attr("value","");
    	})
    	$("form[id='"+fid+"']").find("select").each(function(){
    		$(this)[0].selectedIndex = 0; 
    	})
	return false;	
    });
    $(".btnEdit").click(function(){
    	var formId = $(this).attr("href");
    	var url =  $(this).attr("ref");
    	if(url!='') $(formId ).attr("action",url);
    	$(formId).submit();
	return false;	
    });
    
    $(".btnPublish").click(function(){
    	var formId = $(this).attr("href");
    	var url =  $(this).attr("ref");
    	if(url!='') $(formId ).attr("action",url);
    	$(formId).submit();
	return false;	
    });
    $(".btnDelete").click(function(){
    	if(confirm('確定要刪除此筆資料?')){
	    	var formId = $(this).attr("href");
	    	var url =  $(this).attr("ref");
	    	if(url!='') $(formId ).attr("action",url);
	    	$(formId).submit();
    	}
	return false;	
    });    
    $(".btnUndoChange").click(function(){
    	var formId = $(this).attr("href");
    	var url =  $(this).attr("ref");
    	if(url!='') $(formId ).attr("action",url);
    	$(formId).submit();
	return false;	
    });
    $(".btnUndelete").click(function(){
    	var formId = $(this).attr("href");
    	var url =  $(this).attr("ref");
    	if(url!='') $(formId ).attr("action",url);
    	$(formId).submit();
	return false;	
    });
   
    
    $(".htmlEditor").each(function(){
    	var id = $(this).attr("id");
    	CKEDITOR.replace( id,{uiColor:'#F1F6FD',skin : 'kama',
    		toolbar :    		
		[
			{ name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
			{ name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote',
			'-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },
			{ name: 'links', items : [ 'Link','Unlink','Anchor' ] },
			'/',
			{ name: 'insert', items : [ 'Image','Table','SpecialChar' ] },
			{ name: 'styles', items : [ 'Styles','Format','Font','FontSize' ] },
			{ name: 'colors', items : [ 'TextColor','BGColor' ] },
			{ name: 'tools', items : [ 'Maximize', 'ShowBlocks','-','About' ] }
		]
    	
    	});
    	
    	
    });
    
	
    var contextPath = "<cms:link>/</cms:link>";
    $(".imageUrl").focus(function(){
		var val = $(this).val();
		if(val!=''){
			if(val.substring(0,contextPath.length)!=contextPath) val = contextPath+val.substring(1);
			$(this).val(val);
		}
    });
    
    $(".btnSave").click(function(){
	var formId = $(this).attr("href");
	$(formId).submit();
    });

    $(".btnCancel").click(function(){		
	history.go(-1);
	return false;
    });	
    $(".btnRefresh").click(function(){	//驗證碼更新
	var id = $(this).attr("href");	
	$(id).attr('src',"<cms:link>/newImage.html?tt="+Math.random()+"</cms:link>");
	return false;	
    });
});

