<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
ul,li{list-style-type:none;margin:0px;padding:0px;}
dl,dt,dd,ul,h1,h2,h3,h4,h5,h6,li,img,p{padding: 0px; margin: 0px; }

.error{background:url(../images/validate_error.png) no-repeat;padding-left:15px;	}
/**form element**/
form{margin:0px;padding:0px;}
input,select,button{vertical-align:middle;border:1px solid #ccc;margin:3px;}
input[type='radio']{vertical-align:top;height:16px;border:0px;}
input[type='checkbox']{vertical-align:middle;height:16px;border:0px;}
button.ui-datepicker-trigger{background:none;border:none;}
input.w80{width:80px;}
input.w160{width:160px;}
input.w240{width:240px;}

#history-select{margin:0px;}

/** notice-box **/
.notice-box{background: none repeat scroll 0 0 #FBDFCC;border: 1px solid #F4D1D1;padding: 14px 30px;line-height: 26px;}
.notice-box a{text-decoration: underline;color: #c71818;}
.notice-box a.sky{color:skyblue;}
.notice-box .header{ color: #c71818;font-weight:bold;}

ul.sq,ul.disc{margin-left:18px;font-size:12px;color:#666;}
ul.sq li{list-style:square outside none;line-height:22px;}
ul.disc li{list-style:disc outside none;line-height:22px;}
ul.sq li.header,ul.disc li.header{color: #c71818;font-weight:bold;list-style:none;margin-left:-18px;font-size:13px;}
ul.sq li.txt,ul.disc li.txt{list-style:none;margin-left:-18px;}

/** address **/
input.addr-zipcode{width:30px;border:none;border-bottom:1px solid #ccc;padding:0;background:none;}
/** error msg **/
span#validateError{display:inline-block;width:18px;height:15px;margin:0px;padding:0px;background:url(../images/validate_error.png) no-repeat 0 0;}
span#validateOk{display: inline-block;width:18px;height:15px;margin:0px;padding:0px;background:url(../images/validate_ok.png) no-repeat 0 0;}

div.dotted-line{border-bottom: 1px dotted #cdcdcd;margin: 18px 0;}
div.space-line{height: 20px;}

.floatRight{float:right;}

.nodata-line{width:100%;font-size:14px;color: #c71818;font-weight:bold;text-align:center;background: #efefef;border: 1px solid #cccccc;height: 40px;margin:0px;padding-top:20px;}


div.btn-line-right{text-align:right;padding:10px}
div.btn-line-center{text-align:center;padding:10px}
div.btn-line-left{text-align:left;padding:10px}
a.button{
	cursor:pointer;	
	background:url(../images/shoppingCart_button_center.gif) repeat-x -1px -2px;	
	height:22px;line-height:22px;border:solid 1px #959595;padding-top:2px;
	padding-left:7px;padding-right:7px;padding-top:2px;padding-bottom:0px;margin-left:2px;margin-right:2px;
	-moz-border-radius: 3px; /* Firefox */
  	-webkit-border-radius: 3px; /* Safari and Chrome */
  	border-radius: 3px; /* Opera 10.5+, future browsers, and now also Internet Explorer 6+ using IE-CSS3 */
  	behavior: url(<cms:link>ie-css3.htc</cms:link>); /* This lets IE know to call the script on all elements which get the 'box' class */
  	position:relative;
  	z-index:99;
	
		
}
a.button:hover{color:#cd2929;}

div.msgbar-gray{background: #a0a0a0;color: #fff;text-align: left;padding: 12px 20px;}
div.msgbar-lightgray{height:120px;margin:0 auto;padding:35px 230px;background: #eee;text-align:center;line-height:30px;border:1px solid #ddd;}
div.msgbar-darkgray{background: #a0a0a0;color: #fff;text-align: left;padding: 12px 20px;}
div.msgbar-darkgray a{color: #fff;text-decoration:underline;}
div.msgbar-darkgray a:hover{color: #ccc}

div.msgbar-peach{min-height: 50px;border: 1px solid #f5d4d4;background: #fbdfcc;font-size: 14px;padding: 0 16px 5px 16px;margin:0;}
div.msgbar-peach h2{color:#c71718;font-size:14px;margin: 10px 0;}
div.msgbar-peach p{margin:0;line-height: 24px;font-size: 12px;}
/** customer servier **/
.service-form div{overflow : auto;}
.service-form .member-radio{float:left;margin: 70px 0 0 67px;width:182px;padding: 0;color: #3f3f3f;width: 310px;}
.service-form .member-radio li{float:left;width: 182px;height: 30px;padding-left: 10px;line-height: 30px;border: 1px solid #d3d3d3;margin-top:-1px;}
.service-form .customer-info{border: 1px solid #d3d3d3;width: 420px;float: right;margin: 20px 0 0 0;padding: 20px 0 15px 20px;color: #3f3f3f;}
.service-form .customer-info li {width: 380px;height: 30px;line-height: 30px;margin: 0 0 5px 0;padding: 0 0 0 8px;background: #eee;position: relative;text-align: left;} 
.service-form .service-msg li{margin: 9px 0 9px 30px;}
.service-form .service-msg textarea{width: 800px;height: 142px;border: 1px solid #d4d4d4;}   
.service-form .service-msg li.btn-line{text-align:center;}
/** epaper **/
.epaper-form {width: 376px;height: 156px;border: 1px solid #cdcdcd;margin: 10px auto 0 auto;color: #3f3f3f;padding:17px 22px;}
.epaper-form li{font-size: 11px;float:right;height: 26px;margin:0 0 5px 0;line-height: 26px;width:376px;text-align:right;}
.epaper-form .epaper-radio{width: 185px;height: 30px;line-height: 30px;float: left;border: 1px solid #cdcdcd;font-size:12px;text-indent: 9px;text-align:left;}
.epaper-form .epaper-email{font-size:12px;width: 375px;float:left;background: #eee;height: 26px;line-height: 26px;text-indent: 8px;margin: 15px 0 5px 0px;padding:3px 0;text-align:left;}  
.epaper-form .epaper-email input{width:200px;} 
.epaper-history {width: 100%;}
.epaper-history th, .epaper-history td {height: 30px;line-height: 30px;border: 1px solid #d4d4d4;text-align: center;text-indent: 25px;color: #666;}
.epaper-history th {background: #efefef;font-size: 15px;}
.epaper-history td:first-child{text-align: left;text-indent: 25px;width:70%;}
/** news **/
.news-menu{width: 150px;float: left;margin:25px 0 0 0;}
.news-menu h3{font-size: 17px;font-weight:normal;color;#c71818;}
.news-menu ul{margin: 0 0 15px 0;padding: 0 0 15px 20px;}
.news-menu ul li{font-size: 14px;line-height: 28px;margin: 0;font-weight: 100;cursor:pointer;}
.news-menu ul li.selected{font-weight: 600;}
.news-menu ul li.selected ul{display: block;}
.news-menu ul li ul{margin: 0;padding: 0;font-size: 15px;display: none;}
.news-menu ul li ul li{margin-left: 12px;}
.news-menu ul li.selected ul li.selected a{color: #c71818;font-weight:normal;}
.news-menu a:hover{color: #c71818;}
.news-menu .rss{float:left;width:150px;font-size: 17px;height: 55px;line-height:55px;border-top: 1px dotted #d4d4d4;border-bottom: 1px dotted #d4d4d4;}
.news-list,.news-content{float:left;width: 670px;min-height: 300px;margin:25px 0 0 0;border: 1px solid #d4d4d4;}
.news-list ul{padding: 12px 28px;line-height: 22px;margin:0px;}
.news-list li{float:left;margin:0px;display: block;padding-bottom:10px;}
.news-list .news-date{float:left;width: 100px;}
.news-list .news-title{float:left;width: 510px;}
.news-list .news-title a:hover{text-decoration: underline;}

.news-content .news-title{font-size: 12px;background: #c71818;color: #fff;padding: 10px 0 0 30px;height: 48px;}
.news-content .news-title h3 {text-indent: 0;font-weight: 100;font-size: 17px;}
.news-content .news-msg{margin: 30px;padding: 0 0 30px 0;}


/** Search Page **/
a.search_block:hover ul{ border: 1px solid #bc0101}
a.search_block ul.product_block {width: 238px;height: 238px;}
a.search_block .pic{width:238px;height:160px;}
#content{width:960px;float:left;}
ul.pager{clear:both;min-width: 20%;padding-top:16px;padding-bottom:16px;margin:0 auto;}
ul.pager span, .pager a{margin-right:4px;}
ul.pager span{float:left;}
ul.pager span.current{float:left;display:block;width:21px;height:20px;float:left;background:url(../images/pagenation_selected_index.gif) 0 0 no-repeat;overflow:hidden;text-align:center;line-height:20px;color:#fff;}
ul.pager span.prev, .pager a.prev{float:left;width:64px;height:20px;background:url(../images/pagenation_prev_button.gif) 0 0 no-repeat;display:block;text-indent:-9999px;}
ul.pager span.next, .pager a.next{float:left;width:64px;height:20px;background:url(../images/pagenation_next_button.gif) 0 0 no-repeat;display:block;text-indent:-9999px;}
ul.pager a{float:left;width:20px;height:20px;display:block;background:url(../images/pagenation_index.gif) 0 0 no-repeat;overflow:hidden;text-align:center;line-height:20px;}
ul.pager a.button{
	cursor:pointer;	
	background:url(../images/shoppingCart_button_center.gif) repeat-x -1px -2px;	
	height:17px;line-height:15px;border:solid 1px #959595;padding-top:2px;
	padding-left:7px;padding-right:7px;padding-top:2px;padding-bottom:0px;margin-left:2px;margin-right:2px;
	-moz-border-radius: 3px; /* Firefox */
  	-webkit-border-radius: 3px; /* Safari and Chrome */
  	border-radius: 3px; /* Opera 10.5+, future browsers, and now also Internet Explorer 6+ using IE-CSS3 */
  	behavior: url(<cms:link>ie-css3.htc</cms:link>); /* This lets IE know to call the script on all elements which get the 'box' class */
  	position:relative;
  	z-index:99;
	width:auto;
}





/** index right ad block**/
ul.ad_block{width:240px;height:240px;float:right;border:0px;}
ul.ad_block li{margin-left:10px;margin-top: 13px;margin-bottom: 2px;}
ul.ad_block li img{border: 1px solid #d3d2d3;width:214px;height:98px;}
/** index new block **/
.news_block{width: 200px;height: 480px;padding: 0 20px;float:right;}
.news_block .news-title{padding: 0 2px;margin-top: 20px;height: 48px;border-bottom: 2px dotted #b8b8b7;}
.news_block .more{padding-top: 5px;float: right;}
.news_block .news-box{height: 100px;border-bottom: 2px dotted #b8b8b7;}
/** index top banner **/
.indexkv .buttons{padding-right:3px;text-align: right;cursor:pointer;}
.indexkv.active .open, .indexkv .close{display: none;}
.indexkv .open, .indexkv.active .close{display: inline;}
/** index slideshow **/
.slideshow{width: 460px;height: 460px;border: 10px solid #d3d2d3;position: relative;overflow: hidden;}
.slideshow:hover{border-color: #bc0101;}
.slideshow img{width:460px;height:460px;}
/** slide **/
#slide_nav{ z-index: 999; position: absolute; top: 10px; left: 10px }
#slide_nav a { 
	background:url(../images/slideshow_listStyle.png) 0 0 no-repeat;
	display:block;
	width:20px;height:14px;float:left;font-size:1px;color:#fff;line-height:14px;text-align:center;
}
#slide_nav a.activeSlide { background:url(../images/slideshow_listStyle_focus.png) 0 0 no-repeat;color:#FCF4F4;}
#slide_nav a:focus { outline: none; }
/** banner **/
ul.banner1,ul.banner2{	list-style-type: none;width: 960px;float: left;margin: 0px;padding:0px;}
ul.banner1 li{width: 960px;float: left;margin-top:8px;margin-bottom:8px;margin-left:8px;}
ul.banner2 li{width: 476px;float: left;margin-top:8px;margin-bottom:8px;margin-left:4px;}
ul.banner2 li img{width: 472px;height: 100px;}


.textRed{color:#c71818;}
.text01{color:#bebebe;font-size:12px;line-height:20px;}
.text02{font-size:12px;}

/** rating **/
.rating span{color: #949494;font-size:16px;margin-right:2px;display:block;width:14px;height:16px;float:left}
.rating a{color: #949494;font-size:20px;margin-right:2px;display:block;width:16px;height:16px;float:left}
.r5 .star1,.r5 .star2,.r5 .star3,.r5 .star4,.r5 .star5,.r4 .star1,.r4 .star2,.r4 .star3,.r4 .star4,.r3 .star1,.r3 .star2,.r3 .star3,.r2 .star1,.r2 .star2,.r1 .star1{ color: #d03733}

.main {padding: 30px 60px;}
.main h2 {color: #c71818;font-size: 17px;margin: 0;}
.main h2.black{margin: 0;width:100%;font-size: 16px;color: #333;height:28px;line-height:28px;}

.main .title{height: 45px;background: url(../images/uni-sf.png) repeat-x 0 -90px;}
.main .title h2 {;height: 45px;background: url(../images/uni-sf.png) no-repeat  right -45px;}
.main .title h2 span {display: block;width: 100%;padding: 12px 0;background-position: 0 0;height: 45px;background: url(../images/uni-sf.png) no-repeat 0 0;}

.main .page h3{color: #3f3f3f;text-indent: 30px;font-size: 17px;margin: 20px 0 20px 0;}
.main .page p{margin: 0 0 0 30px;line-height: 30px;}


/** tos **/
.tos{margin: 0 0 30px 0;line-height: 24px;color: #3f3f3f;}
.tos h4{color: #c71818;font-size: 14px;margin: 12px 0 12px 0;font-weight:normal;}
.tos ol {margin: 0;padding: 0 0 0 20px;}
.tos li{margin:0px;padding:0px;list-style-type:decimal;}
.tos p{margin:0px;padding:0px;}

.tos .arrow {
    list-style: none;
    margin: 0;
    padding: 0 0 0 30px;
}
.tos .arrow li {
    list-style: none;
    line-height: 30px;
    background: url(../images/uni-sf.png) no-repeat left -239px;
    padding: 0 0 0 12px;
}



/** privacy **/
.privacy {
    margin: 0 0 240px 0;
}
.privacy h3 {
    margin: 20px 0;
}
.privacy p {
    margin: 0 0 0 30px;
    line-height: 30px;
}
.privacy .arrow {
    list-style: none;
    margin: 0;
    padding: 0 0 0 30px;
}
.privacy .arrow li {
    line-height: 30px;
    background: url(../images/uni-sf.png) no-repeat left -239px;
    padding: 0 0 0 12px;
}
.privacy .num {
list-style:decimal;
    margin:0;
}
.privacy .num li {
list-style:decimal;
    line-height: 30px;
}
.privacy .indent1 {
    margin-left: 37px;
}
.privacy .indent2 {
    margin-left: 46px;
}
.privacy .indent3 {
    margin-left: 56px;
}
.privacy .indent4 {
    margin-left: 73px;
}
.privacy .indent5 {
    margin-left: 92px;
}
/** sitemap **/
.site-map ul {color: #3f3f3f;list-style: none;margin: 40px 0 0 0;padding: 0;}
.site-map li {border-bottom: 1px dotted #cdcdcd;}
.site-map li h3{font-size: 15px;height: 42px;line-height: 42px;margin: 0;text-indent: 12px;background: url(../images/uni-sf.png) no-repeat left -233px;}
.site-map li h3 a{text-decoration: underline;font-weight:bold;}
.site-map li ul {margin: 0;}
.site-map li ul li {border: 0;width: 125px;height: 26px;margin: 0 0 0 55px;float: left;text-indent: 12px;background: url(../images/uni-sf.png) no-repeat left -246px;}
.site-map li ul li a:hover {text-decoration: underline;}

/** faq **/
#faq-tab-box{margin-top:25px;border-bottom:2px solid #c71818;*height:29px;}
#faq-tab-box .faq-tabs{height:29px;margin:0;padding:0;border-right:1px solid #ccc;float:left;}
#faq-tab-box .faq-tabs li{ float:left;line-height:27px;height:27px;padding:0 8px;border-width:1px 0 1px 1px;border-color:#ccc;border-style:solid;background:url(../images/faq_tab_bg.jpg) repeat-x 0 0;}
#faq-tab-box .faq-tabs li.selected{border-color:#b41d1d;background:url(../images/faq_tab_bg_active.jpg) repeat-x 0 0;}
#faq-tab-box .faq-tabs li.selected a{color:#fff;}
#faq-list-box{margin-top:15px;}
#faq-list-box ul{margin-left:18px;display:none}
#faq-list-box ul li{list-style-image:url(../images/faq_list_icon.jpg);}
#faq-list-box li:hover{list-style-image:url(../images/faq_list_icon_active.jpg);}
#faq-list-box li a{letter-spacing:1px;line-height:24px;color:#474747;font-size:13px;}
#faq-list-box li a:hover{color:#ef850b;}
#faq-list-box li .answer{padding:0 15px 30px;display:none;}
#faq-list-box li .answer h3{font-size:14px;color:#c71818;background:#e8e9ee;font-weight:normal;margin:15px auto;padding:3px;}
#faq-list-box li .answer p{font-size:13px;}
#faq-list-box li .answer a{ text-decoration:underline;}

/** about us **/
.storefront-wrap h4 {
    font-size: 17px;
    text-indent: 30px;
    margin: 0 0 30px 0;
}
.storefront-wrap p{
    color: #3f3f3f;
    line-height: 30px;
    padding: 0 0 30px 30px;
}
.storefront-wrap {
    border-bottom: 1px dotted #cdcdcd;
}
.storefront {
    float: right;
    margin: 0 0 0 20px;
}
.company-info {
    color: #3f3f3f;
    list-style: none;
    margin: 0 0 0 60px;
    padding: 0;
}
.company-info li {
    margin: 0;
    padding: 0;
    width: 330px;
    height: 33px;
    line-height: 33px;
    float: left;
}
/** branch map **/
#branch_wrapper h4{margin:0;padding:14px 0 8px 0;}
#branch_wrapper .branch_main{margin:0 auto;width:760px;font-size:12px;}
#branch_wrapper .branch_main h3{line-height:50px;}
#branch_wrapper .branch_main p{line-height:30px;}
.blank10px{height:10px;width:100%;display:block;}
.blank55px{margin:0 auto;height:55px;width:790px;display:block;border-bottom:1px dotted #c3c3c3}
#branch_wrapper .list{margin:0 auto;padding:0 13px 0 7px;width:790px;}
#branch_wrapper .list .left{border:1px solid #dcdcdc;float:left}
#branch_wrapper .list .small_wrapper{margin:0 auto;width:790px;}
#branch_wrapper .list .small_wrapper .blank20px{width:790px;height:20px;margin:0 auto;display:block;border-bottom:1px dotted #c3c3c3}
#branch_wrapper .list .branch{margin:0 auto;width:780px;}
#branch_wrapper .list .right{width:460px;height:240px;border:1px solid #dcdcdc;float:right}
#branch_wrapper .list .right div{line-height:27px;padding:15px 20px;margin: 10px;background:#eeeeee;}
#branch_wrapper .list .right .grey35px{height:30px;display:block;}
#branch_wrapper .list .right div p{padding:0;margin: 0;}
#branch_wrapper .list .right div a img{display: block;float: left;margin: 2px 0 0 20px;}
#branch_wrapper .list .right div a span{display: block;float: left;}

/** shop guide **/
.ordered dt {
    float: left;
    color: #c71818;
    margin: 0 0 0 70px;
    width: 145px;
    font-size: 15px;
}
.ordered dd {
    margin: 0 0 50px 0;
    padding: 0 0 50px 215px;
    border-bottom: 1px dotted #cdcdcd;
}
.ordered dd.lst {
    border: 0;
}
.ordered dd .q-login-type {
    border: 0;
    padding: 0;
}
.guide {
    border-bottom:1px dotted #CDCDCD;
    margin:0 0 50px;
    padding:0 0 50px;
}
.guide_text {
    font-size:14px;
    line-height:25px;
    margin:25px 36px;
}
/** light box **/
#branchMapDialog{width:680px;}
#branchMapDialog #map_canvas{
    width:660px;
    height:588px;
}
#mapDialog{width:450px;}
#mapDialog p{width:390px;display:block;text-align:center;}
#mapDialog #map_canvas{
    width:390px;
    height:380px;
    margin:15px 0 15px 22px;
    *margin:15px 0 15px 22px;
}
#eventDialog{width:650px;}
#passwordDialog{width:200px;}
#couponDialog{width:582px;}
#couponDialog .dialogContent{height:300px;overflow:auto;width:98%;}
#orderDialog{width:300px;}
.laphoneDialog .dialogContent{margin: 10px 0px 5px 10px;float:left;}
.laphoneDialog h4{font-size:14px;font-weight:bold;color:#c71818;height: 32px;background: #dfdfdf;border-bottom: 1px solid #d2d2d2;line-height: 32px;float:left;margin:0px;width:100%;text-align:center;}
.laphoneDialog a.btnDialogClose{width: 13px;height: 12px;background: url(../images/couponDialogCloseButton.gif) 0 0 no-repeat;display: inline-block;float: right;cursor:pointer;position:absolute;top:10px;right:14px;}
