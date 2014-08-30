// Create a socket
var WS = window.MozWebSocket ? MozWebSocket : WebSocket;
var socket = new WS('wss://www.grid2osm.org/webSocket/');

// Message received on the socket
socket.onmessage = function(event) {
	var parts = /^([^:]+):([^:]+):([^:]+):([^:]+)$/.exec(event.data);
	var type = parts[1];
	var poiId = parts[2];
	var latitude = parts[3];
	var longitude = parts[4];
	if (type == 'AddMarker') {
		addMarker(poiId, latitude, longitude, 'poi', false);
	} else if (type == 'DeleteMarker') {
		deleteMarker(poiId, 'poi');}};
