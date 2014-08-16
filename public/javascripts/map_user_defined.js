var markerArray = [];

function addMarker(poiId, latitude, longitude) {
	var customIcon = L.icon({
		iconUrl : '/public/images/leaflet/marker-icon.png',
		shadowUrl : '/public/images/leaflet/marker-shadow.png'
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

function updateMarker(poiId, latitude, longitude) {
	deleteMarker(poiId);
	addMarker(poiId, latitude, longitude);
}