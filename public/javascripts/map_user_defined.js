var markerArray = [];

function addMarker(poiId, latitude, longitude) {
	deleteMarker(poiId);
	var customIcon = L.AwesomeMarkers.icon({
		icon: 'dot-circle-o',
		markerColor: 'red',
		prefix: 'fa'
	});
	customMarker = L.Marker.extend({
		options : {
			poiId : ''
		}
	});
	var marker = new customMarker([ latitude, longitude ], {
		icon : customIcon,
		poiId : poiId
	});
	marker.on('click', function(){
		showPoi(this.options.poiId);
	});
	markerArray.push(marker);
	marker.addTo(map);
}

function deleteMarker(poiId) {
	var marker = getMarker(poiId);
	if (marker !== undefined && marker !== null) {
		var index = markerArray.indexOf(marker);
		markerArray.splice(index, 1);
		map.removeLayer(marker);
	}
}

function getMarker(poiId) {
	var marker = $.grep(markerArray, function(e) {
		return e.options.poiId == poiId;
	});
	if (marker.length == 1) {
		return marker[0];
	}
}