function joinSocket() {

	// Create a socket
	var WS = window.MozWebSocket ? MozWebSocket : WebSocket;
	var gToken = getGoogleIdToken();
	if (gToken !== '') {
		if (typeof socket !== 'undefined') {
			socket.close();
		}
		socket_opened_with_token = true;
	}
	socket = new WS('wss://www.grid2osm.org/websocket/?gToken=' + gToken);

	// Message received on the socket
	socket.onmessage = function(event) {
		var parts = /^([^:]+):([^:]+):([^:]+):([^:]+):([^:]+):([^:]+):([^:]+)$/
				.exec(event.data);
		var event_type = parts[1];
		var google_id = parts[2];
		var latitude = parts[3];
		var longitude = parts[4];
		var poi_id = parts[5];
		var task_completed = parts[6];
		var time_stamp = parts[7];
		if (event_type === 'null') {
			event_type = null;
		}
		if (google_id === 'null') {
			google_id = null;
		}
		if (latitude === 'null') {
			latitude = null;
		} else {
			latitude = parseFloat(latitude);
		}
		if (longitude === 'null') {
			longitude = null;
		} else {
			longitude = parseFloat(longitude);
		}
		if (poi_id === 'null') {
			poi_id = null;
		} else {
			poi_id = parseInt(poi_id);
		}
		if (task_completed === 'null') {
			task_completed = null;
		} else {
			task_completed = (task_completed === 'true');
		}
		if (time_stamp === 'null') {
			time_stamp = null;
		} else {
			time_stamp = parseInt(time_stamp);
		}
		if (event_type == 'AddMarker') {
			addMarker(google_id, latitude, longitude, 'poi', poi_id,
					task_completed, time_stamp);
		} else if (event_type == 'DeleteMarker') {
			deleteMarker('poi', poi_id);
		}
	};

	socket.onclose = function(event) {
		if (event.code === 1006 || !event.wasClean) {
			joinSocket();
			var googleId = getGoogleIdToken();
			var poiId = $('#poi_id').val();
			if (googleId && poiId) {
				var data = [];
				data.push({
					name : 'gToken',
					value : googleId
				});
				data.push({
					name : 'poiId',
					value : poiId
				});
				$.post(routes.reservePoi.url(), $.param(data)).done(
						$.post(routes.broadcastOpenPoi.url(), $.param(data)));
			}
		}
	};

	socket.onerror = function(event) {
		console.log(event);
	};
}
