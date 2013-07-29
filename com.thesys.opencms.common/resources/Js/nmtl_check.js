function processKeyDown() {
   var isValidKey = true;

   var key = event.keyCode;
   
   if (key == 13) isValidKey = false; //13 enter
   if (key == 33) isValidKey = false; // !
   if (key == 39) isValidKey = false; // '
   if (key == 35) isValidKey = false; // #
   if (key == 59) isValidKey = false; // ;
   
      //依照需求自行增刪合法的 key code
   window.event.returnValue = isValidKey;

   
}

function searchCheck(){
       // var div_query1_value = document.getElementsById("div_query1").innerHTML;
	//var query1_value = document.getElementsById("query1").value;

	//alert(query1_value );
	
	return true;
}