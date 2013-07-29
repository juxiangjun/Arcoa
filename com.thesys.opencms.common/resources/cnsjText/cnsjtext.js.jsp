<%@page buffer="none" session="true" import="java.util.*" %>
<%@ taglib prefix="cms" uri="http://www.opencms.org/taglib/cms" %>
var cnsjtext= new Object();

cnsjtext.title = "插入難字";
cnsjtext.url = "<cms:link>cnsjText.html</cms:link>";

var currFieldName;

function openCNSJTextDialog(fieldName) {
	var theField = document.getElementById(fieldName);
	currFieldName= fieldName;
	var fieldValue = theField.value;
	if (document.all) {	
		//window.open(cnsjtext.url , "cnsjtext",
		//		      "toolbar=no,menubar=no,personalbar=no,width=10,height=10," +
		//		      "scrollbars=no,resizable=yes"); 
	//
		setCNSJText(showModalDialog(cnsjtext.url, cnsjtext, "resizable: yes; help: no; status: no; scroll: no;"));
	} else {
		// Mozilla based or other browser, use standards compliant method to open popup
		window.open(cnsjtext.url , "cnsjtext",
				      "toolbar=no,menubar=no,personalbar=no,width=10,height=10," +
				      "scrollbars=no,resizable=yes"); 
	}
}

function setCNSJText(value) {
	if (currFieldName!= null) {
		var theField = document.getElementById(currFieldName);
		if(typeof(value) != 'undefined' ){
			theField.value = theField.value + value;
			setImageCNSJText(currFieldName);
		}
	}
}
function setImageCNSJText(fieldName){
	var theDivField = document.getElementsByName("div_"+fieldName)[0];
	var theField = document.getElementById(fieldName);
	var text = theField.value;
	//難字圖片url -> http://www.cns11643.gov.tw/cgi-bin/ttf2png?page=3&number=424e&fontsize=10
	if(text.indexOf("<page>")>=0){
		text= replaceAll(text,"<page>", "<img src=\"http://www.cns11643.gov.tw/cgi-bin/ttf2png?page=");
		text= replaceAll(text,"</page><number>", "&number=");
		text= replaceAll(text,"</number>", "&fontsize=20&bgcolor=ffffff&fgcolor=000000\">");
	}
	theDivField.innerHTML= text;
	
}
function setStringCNSJText(fieldName){
	var theDivField = document.getElementsByName("div_"+fieldName)[0];
	var theField = document.getElementById(fieldName);
	var text= theDivField.innerHTML;
	//去html tag
	text = text.replace(/<(?!img).*?>/ig,"");
	//難字圖片url -> http://www.cns11643.gov.tw/cgi-bin/ttf2png?page=3&number=424e&fontsize=10
	if(text.indexOf("<img")>=0 || text.indexOf("<IMG")>=0){
		text= replaceAll(text, "<IMG src=\"http://www.cns11643.gov.tw/cgi-bin/ttf2png?page=","<page>");
		text= replaceAll(text, "<img src=\"http://www.cns11643.gov.tw/cgi-bin/ttf2png?page=","<page>");
		text= replaceAll(text, "&amp;number=","</page><number>");
		text= replaceAll(text, "&amp;fontsize=20&amp;bgcolor=ffffff&amp;fgcolor=000000\">","</number>");
		
	}

	theField.value = text;
	
}
function replaceAll(strOrg,strFind,strReplace){
 var index = 0;
 while(strOrg.indexOf(strFind,index) != -1){
  strOrg = strOrg.replace(strFind,strReplace);
  index = strOrg.indexOf(strFind,index);
 }
 return strOrg;
} 
function processKeyDown(e) {
   var isValidKey = true;

   var key = document.all? e.keyCode : e.which;
   if (key == 13) isValidKey = false; //13 enter
   if (key == 33) isValidKey = false; // !
   if (key == 39) isValidKey = false; // '
   if (key == 35) isValidKey = false; // #
   if (key == 59) isValidKey = false; // ;  
  
      //依照需求自行增刪合法的 key code
   if(document.all){
   	e.returnValue = isValidKey;
   }else{
   	return isValidKey;
   }

   
}




