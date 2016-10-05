var map;
var markers = [];

function initialize() {

	var mapOptions = {
		center : new google.maps.LatLng(51.5, -0.2),
		zoom : 10,
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        zoomControl: true,
        scaleControl: true,        
        disableDefaultUI: true		
	};
	
	map = new google.maps.Map(document.getElementById("map-canvas"), mapOptions);
	
	fetchPlaces();
}

// when page is ready, initialize the map!
google.maps.event.addDomListener(window, 'load', initialize);

var fetchPlaces = function() {
	var infowindow =  new google.maps.InfoWindow({
	    content: ''
	});
	jQuery.ajax({
		type: 'GET',
		contentType : 'application/json',
		url : '/api/places/',
		dataType : 'json',
		success : function(data) {
			for (i = 0; i < data.length; i++) {
				//create gmap latlng obj
				tmpLatLng = new google.maps.LatLng( data[i].latitude, data[i].longitude);
				
				// make and place map maker.
				var marker = new google.maps.Marker({
				    map: map,
				    id: data[i].id,
				    position: tmpLatLng,
				    title : data[i].name
				});

			    google.maps.event.addListener(marker,'click', function() {
			    	requestPlaceInfo(this.id);
					sidebar.open('info', null);
				});				
				
				bindInfoWindow(marker, map, infowindow, data[i].name);
				// not currently used but good to keep track of markers
				markers.push(marker);
			}
		}
	})
};

//binds a map marker and infoWindow together on click
var bindInfoWindow = function(marker, map, infowindow, html) {
    google.maps.event.addListener(marker, 'click', function() {
        infowindow.setContent(html);
        infowindow.open(map, marker);
    });
};

function requestPlaceInfo(placeId) {
	
	console.log("requesting info for place id="+placeId);

	var search = {}
	
	search["placeId"] = placeId; 
	
	$.ajax({
		type : "GET",
		beforeSend: function (request)
        {
            request.setRequestHeader("X-CSRF-TOKEN", "${_csrf.token}");
        },
		contentType : 'application/json',
		url : '/api/places/'+placeId,
		dataType : 'json',
		timeout : 100000,
		success : function(data) {
			console.log("SUCCESS: ", data);
			displayPlaceInfo(data);
		},
		error : function(e) {
			console.log("ERROR: ", e);
		},
		done : function(e) {
			console.log("DONE");
		}
	});

}


function displayPlaceInfo(data) {

	var content = "<h3><a href='"+data.id+"'>"+data.name+"</a></h3>";
	
	$("#placeInfoContent").html ( content );
}
