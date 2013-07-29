<html style="width: 288px; height: 187px;"><head>
<!-- 	
		Internet Explorer:
		In order to set the localized title and the preselected color, create
		a new object, e.g. "colorPicker", and define:
		colorPicker.title = "Your localized title";
		colorPicker.color = "000000"; (The preselected color value) 
		Use this object when calling showModalDialog(URL, colorPicker, options);
		Other browsers:
		Open the popup with window.open("{URL}?{preset color value}"), e.g.
		window.open("index.html?000000"); and be sure to have a setColor(arg)
		function in your opener document.
		
//-->
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="text/javascript">

var isIE = true;

window.resizeTo(295, 190);

var docTitle = "插入難字";
if (window.dialogArguments != null && window.dialogArguments.title != null) {
	docTitle = window.dialogArguments.title;
}
document.write("<title>" + docTitle + "<\/title>");

function _CloseOnEsc() {
  //if (event.keyCode == 27) { window.close(); return; }
}

function Init() {                                                      // run on page load
  //document.body.onkeypress = _CloseOnEsc;
  var color = "";
  if (!document.all) {
  	isIE = false;
  	var theUrl = document.URL;
  	var requestIndex = theUrl.indexOf("?");
  	
  }	           
 }

function Set(string) {                   // select color
   if (isIE) {
    	window.returnValue = string;           // set return value
    } else {
    	window.opener.setCNSJText(string);
    }
        window.close(); 
}


</script>
</head>
<body style="background:ButtonFace; margin:0px; padding:0px" onload="Init();" >

<form method="get" style="margin:0px; padding:0px" onSubmit="Set(document.getElementById('CNSJText').getConvertedText()); return false;">
<table border="0px" cellspacing="0px" cellpadding="4" width="100%">
 <tr>
  <td style="background:buttonface" valign=center><applet id="CNSJText" codeBase="http://www.cns11643.gov.tw/CNS_SwingWS" archive="CNSSwingWSSigned.jar" code="org.cmex.applet.CNSJText.class" name="CNSJText" width="150" height="24">
						    <param NAME="mode" VALUE="ALL">
						    <param NAME="fontcolor" VALUE="#000000">
						    <param NAME="enabled" VALUE="true">
						    <param NAME="maxlength" VALUE="-1">
						    <param NAME="bgcolor" VALUE="#FFFFFF">
						    <param NAME="bordercolor" VALUE="#000000">
						    <param NAME="inittext" VALUE="">
						    <param NAME="borderstyle" VALUE="line">
						    <param NAME="fontimageserver" VALUE="http://www.cns11643.gov.tw/CNS_Swing/fontimageservlet">
						    <param NAME="convertservice" VALUE="http://www.cns11643.gov.tw/CNS_SwingWS/services/CodeConvertPort?wsdl">
						    <param NAME="converttarget" VALUE="B_CNS">
						    <param NAME="voiceserver" VALUE="61.60.106.15">
						    <param NAME="imeservice" VALUE="http://www.cns11643.gov.tw/CNS_SwingWS/services/InputMethodService?wsdl">
						    <param NAME="fontsize" VALUE="22">
						    <param NAME="fonttype" VALUE="1">
						    <param NAME="encodingtype" VALUE="HEX">
						    </applet></td>
  <td style="background:buttonface" width=100%><input type="submit" value="確定插入"/></td>
 </tr>
 <tr>
  <td valign=center colspan="2" style="font-size: 12px;color: #999;">請於空白格上按滑鼠右鍵選取輸入法，拼成並選字後按[Enter]鍵與[確定插入]</td>
 </tr>
</table>
</form>


</body></html>
