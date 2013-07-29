function convertHardwordToImage(text){
		//var Hardwordtxt = txt.replace("<img style=\"vertical-align: text-bottom;\" src=\"http://www.cns11643.gov.tw/cgi-bin/ttf2png?page=", "<page>");
		//Hardwordtxt = convertHardwordToEmpty(Hardwordtxt);
		//return Hardwordtxt;
		
		while(txt.indexOf("<page>")>=0){
			txt= txt.replace("<page>", "<img style=\"vertical-align: text-bottom;\" src=\"http://www.cns11643.gov.tw/cgi-bin/ttf2png?page=");
			txt= txt.replace("</page><number>", "&number=");
			txt= txt.replace("</number>", "&fontsize=20&bgcolor=ffffff&fgcolor=000000\">");
		}
		return txt;
}
	
function convertHardwordToEmpty(txt){
		//txt = txt.replace(/<page>[a-zA-Z0-9]{1,2}<\/page><number>[a-zA-Z0-9]{4}<\/number>/, "□");
		while(txt.indexOf("<page>")>=0){
			txt=  txt.replace(/<page>[a-zA-Z0-9]{1,2}<\/page><number>[a-zA-Z0-9]{4}<\/number>/, "□");
		}
		return txt;
}

