// http://switch2osm.org/using-tiles/getting-started-with-leaflet/

var map;
var ajaxRequest;
var plotlist;
var plotlayers = [];

function initmap() {
	// set up the map
	map = new L.Map('map');
	// create the tile layer with correct attribution
	var osmUrl = '//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
	var osmAttrib = 'Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';
	var osm = new L.TileLayer(osmUrl, {
		minZoom : 1,
		maxZoom : 19,
		attribution : osmAttrib
	});
	// start the map in South-East England
	map.setView(new L.LatLng(0, 0), 2);
	map.addLayer(osm);
	
	// add address search
	L.Control.geocoder({
	    geocoder: L.Control.Geocoder.nominatim({
	    	serviceUrl: "//nominatim.openstreetmap.org/"
	    })
	}).addTo(map);
}
