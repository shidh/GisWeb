var map;
var ajaxRequest;
var plotlist;
var plotlayers=[];

function initmap() {
	// set up the map
	map = new L.Map('map');

	// create the tile layer with correct attribution
	var osmUrl='http://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png';
	var osmAttrib='Map data © <a href="http://openstreetmap.org">OpenStreetMap</a> contributors';
	var osm = new L.TileLayer(osmUrl, {minZoom: 4, maxZoom: 12, attribution: osmAttrib});		

	// start the map in South-East England
	map.setView(new L.LatLng(51.163375, 10.447683),8);
	map.addLayer(osm);

	#{list items:models.Poi.findAll(), as:'poi'}
		var marker = L.marker([${poi.latitude}, ${poi.longitude}]).addTo(map);
		marker.bindPopup("<img width='200px' src='@{Application.getPicture(poi.id, 0)}'/><img width='200px' src='@{Application.getPicture(poi.id, 1)}'/><br />Altitude: " + ${poi.altitude} + "<br />Latitude: " + ${poi.latitude} + "<br />" + "Longitude: " + ${poi.longitude});
	#{/list}
}