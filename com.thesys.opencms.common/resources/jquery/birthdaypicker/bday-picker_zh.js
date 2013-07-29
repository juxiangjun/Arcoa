/*!
 * jQuery Birthday Picker: v1.4 - 10/16/2011
 * http://abecoffman.com/stuff/birthdaypicker
 *
 * Copyright (c) 2010 Abe Coffman
 * Dual licensed under the MIT and GPL licenses.
 *
 */

(function( $ ){

  // plugin variables
  var months = {
    "short": ["1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"],
    "long": ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"] },
      todayDate = new Date(),
      todayYear = todayDate.getFullYear(),
      todayMonth = todayDate.getMonth() + 1,
      todayDay = todayDate.getDate();


  $.fn.birthdaypicker = function( options ) {

    var settings = {
      "maxAge"        : 120,
      "futureDates"   : false,
      "maxYear"       : todayYear,
      "dateFormat"    : "middleEndian",
      "monthFormat"   : "short",
      "placeholder"   : true,
      "legend"        : "",
      "defaultDate"   : false,
      "fieldName"     : "birthdate",
      "fieldId"       : "birthdate",
      "hiddenDate"    : true,
      "onChange"      : null,
      "tabindex"      : null
    };

    return this.each(function() {

      if (options) { $.extend(settings, options); }

      // Create the html picker skeleton
      var $fieldset = $("<fieldset class='birthday-picker'></fieldset>"),
          $year = $("<select class='birth-year' name='birth[year]'></select>"),
          $month = $("<select class='birth-month' name='birth[month]'></select>"),
          $day = $("<select class='birth-day' name='birth[day]'></select>");

      if (settings["legend"]) { $("<legend>" + settings["legend"] + "</legend>").appendTo($fieldset); }

      var tabindex = settings["tabindex"];

      // Deal with the various Date Formats
        $fieldset.append($year).append($month).append($day);
        if (tabindex != null) {
          $year.attr('tabindex', tabindex);
          $month.attr('tabindex', tabindex++);
          $day.attr('tabindex', tabindex++);
        }

      // Add the option placeholders if specified
      if (settings["placeholder"]) {
        $("<option value='0'>請選擇年份</option>").appendTo($year);
        $("<option value='0'>請選擇月份</option>").appendTo($month);
        $("<option value='0'>請選擇日期</option>").appendTo($day);
      }

      var hiddenDate;
      if (settings["defaultDate"]) {
        var defDate = new Date(settings["defaultDate"]);
        defYear = defDate.getFullYear();
        defMonth = defDate.getMonth() + 1;
        defDay = defDate.getDate();
        if(defMonth <10) hiddenDate = "0"+defMonth + "/";
        else hiddenDate = defMonth + "/";
        if(defDay <10)hiddenDate = hiddenDate + "0";
        hiddenDate = hiddenDate + defDay + "/" +defYear ;
      }

      // Create the hidden date markup
      if (settings["hiddenDate"]) {
        $("<input type='hidden' name='" + settings["fieldName"] + "' id='" + settings["fieldName"] + "'/>")
            .attr("id", settings["fieldId"])
            .val(hiddenDate)
            .appendTo($fieldset);
      }

      // Build the initial option sets
      var startYear = todayYear;
      var endYear = todayYear - settings["maxAge"];
      if (settings["futureDates"] && settings["maxYear"] != todayYear) {
        if (settings["maxYear"] > 1000) { startYear = settings["maxYear"]; }
        else { startYear = todayYear + settings["maxYear"]; }
      }
      while (startYear >= endYear) { $("<option></option>").attr("value", startYear).text(startYear+"年 (民國"+(startYear-1911)+"年)").appendTo($year); startYear--; }
      for (var j=0; j<12; j++) { $("<option></option>").attr("value", j+1).text(months[settings["monthFormat"]][j]).appendTo($month); }
      for (var k=1; k<32; k++) { $("<option></option>").attr("value", k).text(k+"日").appendTo($day); }
      $(this).append($fieldset);

      
      // Update the option sets according to options and user selections
      $fieldset.change(function() {
            // todays date values
        var todayDate = new Date(),
            todayYear = todayDate.getFullYear(),
            todayMonth = todayDate.getMonth() + 1,
            todayDay = todayDate.getDate(),
            // currently selected values
            selectedYear = $year.val(),
            selectedMonth = $month.val(),
            selectedDay = $day.val(),
            // number of days in currently selected year/month
            actMaxDay = (new Date(selectedYear, selectedMonth, 0)).getDate(),
            // max values currently in the markup
            curMaxMonth = parseInt($month.children(":last").val()),
            curMaxDay = parseInt($day.children(":last").val());
	
        // Dealing with the number of days in a month
        // http://bugs.jquery.com/ticket/3041
        //修正ie不正常問題
        var bday = document.getElementsByName("birth[day]")[0];
        if (curMaxDay > actMaxDay) {          
          while (curMaxDay > actMaxDay) {
            bday.remove(curMaxDay );
            //$day.children("option:last").remove();
            curMaxDay--;
          }
        } else if (curMaxDay < actMaxDay) {
          while (curMaxDay < actMaxDay) {
            curMaxDay++;
            bday.options.add(new Option(curMaxDay+"日",curMaxDay));
            //$day.append("<option value=" + curMaxDay + ">" + curMaxDay + "日</option>");
          }
        }

        // Dealing with future months/days in current year
        if (!settings["futureDates"] && selectedYear == todayYear) {
          if (curMaxMonth > todayMonth) {
            while (curMaxMonth > todayMonth) {
              $month.children(":last").remove();
              curMaxMonth--;
            }
            // reset the day selection
            $day.children(":first").attr("selected", "selected");
          }
        }

        // Adding months back that may have been removed
        // http://bugs.jquery.com/ticket/3041
        if (selectedYear != todayYear && curMaxMonth != 12) {
          while (curMaxMonth < 12) {
            $month.append("<option value=" + (curMaxMonth+1) + ">" + months[settings["monthFormat"]][curMaxMonth] + "</option>");
            curMaxMonth++;
          }
        }

        // update the hidden date
        if ((selectedYear * selectedMonth * selectedDay) != 0) {
          if(selectedMonth<10) hiddenDate = "0"+selectedMonth+ "/";
          else hiddenDate = selectedMonth+ "/";
          if(selectedDay<10)hiddenDate = hiddenDate + "0";
          hiddenDate = hiddenDate + selectedDay+ "/" +selectedYear  ;
          $(this).find('#'+settings["fieldId"]).val(hiddenDate);
          if (settings["onChange"] != null) {
            settings["onChange"](hiddenDate);
          }
        }
      });
      
      // Set the default date if given
      if (settings["defaultDate"]) {     
        var date = new Date(settings["defaultDate"]);
        $year.val(date.getFullYear());
        $month.val(date.getMonth() + 1);
        $day.val(date.getDate());
        $fieldset.change();
      }
    });
  };
})( jQuery );