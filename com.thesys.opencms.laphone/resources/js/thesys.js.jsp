<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
$(function() {
	$("img").each(function(index){
	        $(this).error(function() {
				$(this).attr("src","<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/blank_productPhoto.jpg</cms:link>"); 
	        });
	        $(this).attr("src", $(this).attr("src"));
	});    
	/** address **/    
	$(".zipcodeSelect").each(function(){
		var id = $(this).attr("id");    
		var requiredClass = "";
		if($(this).hasClass("required")){
			requiredClass = " required";
		}	
		$(this).twzipcode({
			css:['addr-county'+requiredClass ,'addr-area'+requiredClass ,'addr-zipcode'+requiredClass ],
			countyName: id+'_county',
			areaName: id+'_area',
			zipName: id+'_zipcode',
			countySel:$('#'+id+'_county_def').val(),
			areaSel: $('#'+id+'_area_def').val(),
			zipSel: $('#'+id+'_zipcode_def').val(),
			zipReadonly:true
		}); 
	});
	
	/**birthday**/
	$(".birthday").datepicker({
		showOn: "both",
		buttonImage: "<cms:link>/system/modules/com.thesys.opencms.laphone/resources/images/calendar.jpg</cms:link>",
		dateFormat: 'yy/mm/dd',
		minDate: new Date('1920/1/1'),
		maxDate: new Date,
		changeMonth: true,
		changeYear: true,
		showAnim: 'fadeIn',
		yearRange: '1920:' + new Date().getFullYear(),
		showMonthAfterYear: true,
		//defaultDate: new Date(new Date * 1 - 24 * 60 * 60 * 1000),
		defaultDate: new Date(new Date(new Date*1 - 25 * 365 * 24 * 60 * 60 * 1000).getFullYear()+'/1/1'),//預設25年前1月1日
		autoSize: true,
		monthNamesShort: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
	});
	 
	//shoppingcart
	$(".couponCheckBox").click(function(){
		if($(this).attr("checked")=="checked"){
			$(".couponCheckBox").attr("checked",false);
			$(this).attr("checked",true);
		}
	});
	//lightbox
	$(".btnLightbox").fancybox({showCloseButton:false,modal:true});
	$(".btnDialogClose").click(function(){
		$.fancybox.close();
		return false;
	});	
	$(".btnDialogSubmit").click(function(){
		$.fancybox.close();
		return false;
	});	
	$(".btnDialogCancel").click(function(){
		$.fancybox.close();
		return false;
	});
	
	$(".cartQuantity").change(function(){
		var formId = $(this).attr("ref");
		$(formId+" input[name='action']").val("add");
		$(formId).submit();
	});
	$(".btnCartDelete").click(function(){	                		
		var formId = $(this).attr("href");
		$(formId+" input[name='action']").val("delete");
		$(formId).submit();
		return false;
	});
	
	$(".btnCartMove").click(function(){	                		
		var formId = $(this).attr("href");
		$(formId+" input[name='action']").val("move");
		$(formId).submit();
		return false;
	});
	$(".btnTrackingDelete").click(function(){	                		
		var formId = $(this).attr("href");
		$(formId+" input[name='action']").val("trackingDelete");
		$(formId).submit();
		return false;
	});
	
	$(".btnTrackingMove").click(function(){	                		
		var formId = $(this).attr("href");
		$(formId+" input[name='action']").val("trackingMove");
		$(formId).submit();
		return false;
	});
	$(".pager a").click(function(){	//分頁
		var formId = $(this).attr("href");	
		var pageno = $(this).attr("ref");	
		$("#pageIndex").val(pageno);
		$(formId).submit();
		return false;	
	});
	$(".btnRating").click(function(){ //加入評分
		var itemId = $(this).attr("href");    	
		var rating = $(this).attr("ref");    	
		$.ajax({
		    url: '<cms:link>/system/modules/com.thesys.opencms.laphone/jsps/addRating.jsp</cms:link>',
		    type: "POST",
		    data: { itemId: itemId,rating:rating },
		    success: function (msg) {
		        alert(msg);
		    }
		});
		return false;
	});
	$(".btnTracking").click(function(){ //加入追蹤清單
		var itemId = $(this).attr("ref");    	
		$.ajax({
		    url: '<cms:link>/system/modules/com.thesys.opencms.laphone/jsps/addTracking.jsp</cms:link>',
		    type: "POST",
		    data: { itemId: itemId },
		    success: function (msg) {
		        alert(msg);
		    }
		});
		return false;
	});
     
	$("#validateError").hide();
	$("#validateOk").hide();
	$(".btnRefresh").click(function(){	//驗證碼更新
		var id = $(this).attr("href");	
		$(id).attr('src',"<cms:link>/newImage.html?tt="+Math.random()+"</cms:link>");
		return false;	
	});
	$(".btnSubmit").click(function(){	//form submit
		var id = $(this).attr("href");
		$(id).submit();
		return false;	
	});
	$(".btnBack").click(function(){	//上一頁
	history.go(-1);
	return false;	
	});
	
	$(".question").click(function(){
	$(this).next().is(':hidden')?$(this).next().slideDown():$(this).next().slideUp();void(0)
	});
	$(".faq-tab").click(function(){
	refId = $(this).attr("ref");
	$(".faq-tab").removeClass("selected");
	$(this).addClass("selected");
	$(".faq-box").hide();
	$('#'+refId).show();
	
	});
	$(".faq-tab:first").click();
	$(".cart-tab").click(function(){
		var link = $(this).children("a").attr("href");		
		if(link!="#"){
			alert("請先登入!");
			return true;
		}else{
			refId = $(this).attr("ref");		
			$(".cart-tab").removeClass("selected");
			$(".tab-content").removeClass("selected");
			$(this).addClass("selected");
			$('#'+refId).addClass("selected");
			return false;
		}
	
	});
	
	
	$('#nav').superfish();
	
	$(".tabs li").click(function () {
		$(".tabs li").not($(".tabs li").eq($(this).index())).removeClass('active');
		$(".tabs li").eq($(this).index()).addClass('active');
		$(".tab-views .view").not($(".tab-views .view").eq($(this).index())).removeClass('active');
		$(".tab-views .view").eq($(this).index()).addClass('active');        
		return false;
	});
	
	$('.slideshow').cycle({
			fx:     'fade', 
			speed:  'slow', 
			timeout: 8000, 
			pager:  '#slide_nav',
			slideExpr: 'img'//,
			//after:function(){changeScrollSize();}
	});
	setTimeout(function () {
		closeAd();
	}, 5 * 1000); //頁面載入後廣告讀完此秒數後收合
	
	$(".indexkv .open").click(function(){
		openAd();
		return false;
	});
	$(".indexkv .close").click(function(){
		closeAd();
		return false;
	});
	//產品頁
	$("#productImage-tab li").click(function(){
		var refId = $(this).attr("ref");
		$("#productImage-tab li").removeClass("selected");
		$("#main-image-view div").removeClass("selected");
		$(this).addClass("selected");
		$(refId).addClass("selected");	
		return false;
	});
	$("#productImage-tab li:first").click();
	$("#productDetail-tab li").click(function(){
		var refId = $(this).attr("ref");
		$("#productDetail-tab li").removeClass("selected");
		$("#productDetailContent div").removeClass("selected");
		$(this).addClass("selected");
		$(refId).addClass("selected");	
		return false;
	});
	$("#productDetail-tab li:first").click();

});

function openAd() {
    	$('.indexkv').addClass('active').find('.content').slideDown(0.25 * 1000);//展開歷時幾秒
}
function closeAd() {
    	$('.indexkv').removeClass('active').find('.content').slideUp(0.13 * 1000);//收合歷時幾秒
}
