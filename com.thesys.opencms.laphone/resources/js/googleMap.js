var skipOnce = true;
var dataCount = 0;
var pageSize = 10;
var map;
var geocoder;
var marker;

function loadAddress(storeName,address,phone){
}

function loadAddress(address) {
    loadAddress(address,null);
}
function loadAddress(address, infoWindowContent) {
    if (geocoder) {
        geocoder.geocode({ "address": address }, function (results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                if (map) {
                    if (marker) {
                        marker.setMap(null);
                    }

                    map.setZoom(15);
                    map.setCenter(results[0].geometry.location);
                    marker = new google.maps.Marker({
                        map: map,
                        position: results[0].geometry.location,
                        zoom: 15
                    });
		    if(infoWindowContent != null){
	                    // Creating an InfoWindow object
	                    infoWindow = new google.maps.InfoWindow({
	                        content: infoWindowContent
	                    });
	
	                    google.maps.event.addListener(marker, 'click', function () {
	                        infoWindow.open(map, marker);
	                    });
	            }
                }
            }
        });
    }
}

function openBranchMap(address) {
    //$('#mapDialog .dialogContent p').html(title); 
    $("#btnMapDialog").trigger("click");
    google.maps.event.trigger(map, 'resize');
    loadAddress(address);

    return false;
}
function openMap(title, address) {
    $('#mapDialog .dialogContent p').html(title); 
    $("#btnMapDialog").trigger("click");
    google.maps.event.trigger(map, 'resize');
    loadAddress(address);

    return false;
}

$(function () {
    
    geocoder = new google.maps.Geocoder();
    var latlng = new google.maps.LatLng(24, 120);
    var myOptions = {
        zoom: 5,
        panControl: false,
        zoomControl: true,
        mapTypeControl: false,
        scaleControl: false,
        streetViewControl: true,
        overviewMapControl: false,
        center: latlng,
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };

    map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
});