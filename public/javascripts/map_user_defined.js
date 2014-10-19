var photoArray = [];
var poiArray = [];
var polygon;

function addMarker(google_id, latitude, longitude, marker_type, marker_id,
		task_status, time_stamp) {
	var marker = getMarker(marker_type, marker_id);
	if ((marker && time_stamp && time_stamp > marker.options.time_stamp) || !marker) {
		deleteMarker(marker_type, marker_id);
		var draggable = false;
		var state;
		if (marker_type === 'photo') {
			if (google_id) {
				state = marker_state.photo.opened;
			} else {
				state = marker_state.photo.closed;
			}
		} else if (marker_type === 'poi') {
			if (google_id) {
				if (google_id === my_google_id) {
					draggable = true;
					state = marker_state.poi.opened_by_editor;
				} else {
					state = marker_state.poi.opened_by_other;
				}
			} else {
				if (task_status === 0) {
					state = marker_state.poi.todo;
				} else if (task_status === 1) {
					state = marker_state.poi.done;
				} else if (task_status === 2) {
					state = marker_state.poi.ready_to_submit_to_osm;
				} else if (task_status === 3) {
					state = marker_state.poi.already_submitted_to_osm;
				}
			}
		}
		var customIcon = L.AwesomeMarkers.icon({
			icon : state.icon,
			markerColor : state.color,
			prefix : 'fa',
			spin : (state.spin === 'true')
		});
		var customMarker = L.Marker.extend({
			options : {
				draggable: draggable,
				id : '',
				time_stamp : ''
			}
		});
		marker = new customMarker([ latitude, longitude ], {
			icon : customIcon,
			id : marker_id,
			time_stamp : time_stamp
		});
		marker.on('click', function() {
			if (marker_type === 'poi') {
				showPoi(this);
			} else if (marker_type === 'photo') {
				showPhoto(this);
			}
		});
		if (draggable) {
			marker.on('dragend', function(event) {
				var position = event.target.getLatLng();
				var latitude_steps = $('#poi_latitude').prop('step').length - 2;
				var longitude_steps = $('#poi_longitude').prop('step').length - 2;
				$('#poi_latitude').val(position.lat.toFixed(latitude_steps));
				$('#poi_longitude').val(position.lng.toFixed(longitude_steps));
			});
		}
		if (marker_type === 'poi') {
			poiArray.push(marker);
		} else if (marker_type === 'photo') {
			photoArray.push(marker);
		}
		marker.addTo(map);
	}
}

function deleteMarker(marker_type, marker_id) {
	var marker = getMarker(marker_type, marker_id);
	if (marker) {
		var index;
		if (marker_type === 'poi') {
			index = poiArray.indexOf(marker);
			poiArray.splice(index, 1);
		} else if (marker_type === 'photo') {
			index = photoArray.indexOf(marker);
			photoArray.splice(index, 1);
		}
		map.removeLayer(marker);
	}
}

function getMarker(marker_type, marker_id) {
	var array;
	if (marker_type === 'poi') {
		array = poiArray;
	} else if (marker_type === 'photo') {
		array = photoArray;
	}
	var marker = $.grep(array, function(e) {
		return e.options.id == marker_id;
	});
	if (marker.length === 1) {
		return marker[0];
	}
}
