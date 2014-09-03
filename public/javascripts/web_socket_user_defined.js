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
	socket = new WS('wss://www.grid2osm.org/webSocket/?gToken=' + gToken);

	// Message received on the socket
	socket.onmessage = function(event) {
		var parts = /^([^:]+):([^:]+):([^:]+):([^:]+):([^:]+):([^:]+)$/
				.exec(event.data);
		var event_type = parts[1];
		var google_id = parts[2];
		var latitude = parts[3];
		var longitude = parts[4];
		var poi_id = parts[5];
		var task_completed = parts[6];
		if (event_type === 'null') {
			event_type = null;
		}
		if (google_id === 'null') {
			google_id = null;
		}
		if (latitude === 'null') {
			latitude = null;
		}
		if (longitude === 'null') {
			longitude = null;
		}
		if (poi_id === 'null') {
			poi_id = null;
		}
		if (task_completed === 'null') {
			task_completed = null;
		} else {
			task_completed = (task_completed === 'true');
		}
		if (event_type == 'AddMarker') {
			addMarker(google_id, latitude, longitude, 'poi', poi_id,
					task_completed);
		} else if (event_type == 'DeleteMarker') {
			deleteMarker('poi', poi_id);
		}
	};

	socket.onclose = function(event) {
		console.log(event);
	};

	socket.onerror = function(event) {
		console.log(event);
	}
}