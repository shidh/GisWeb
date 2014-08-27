var photoArray = [];
var poiArray = [];
var polygon;

function addMarker(id, latitude, longitude, type, spin) {
	deleteMarker(id, type);
	var icon;
	var markerColor;
	if (type === 'poi') {
		icon = 'dot-circle-o';
		markerColor = 'red';
	} else if (type === 'photo') {
		icon = 'photo';
		markerColor = 'blue';
	}
	var customIcon = L.AwesomeMarkers.icon({
		icon: icon,
		markerColor: markerColor,
		prefix: 'fa',
		spin: spin
	});
	var customMarker = L.Marker.extend({
		options : {
			id : ''
		}
	});
	var marker = new customMarker([ latitude, longitude ], {
		icon : customIcon,
		id : id
	});
	marker.on('click', function(){
		if (type === 'poi') {
			showPoi(this);
		} else if (type === 'photo') {
			showPhoto(this);
		}
	});
	if (type === 'poi') {
		poiArray.push(marker);
	} else if (type === 'photo') {
		photoArray.push(marker);
	}
	marker.addTo(map);
}

function deleteMarker(id, type) {
	var marker = getMarker(id, type);
	if (marker) {
		var index;
		if (type === 'poi') {
			index = poiArray.indexOf(marker);
			poiArray.splice(index, 1);
		} else if (type === 'photo') {
			index = photoArray.indexOf(marker);
			photoArray.splice(index, 1);
		}
		map.removeLayer(marker);
	}
}

function getMarker(id, type) {
	var array;
	if (type === 'poi') {
		array = poiArray;
	} else if (type === 'photo') {
		array = photoArray;
	}
	var marker = $.grep(array, function(e) {
		return e.options.id === id;
	});
	if (marker.length === 1) {
		return marker[0];
	}
}