function showPoi(poi) {
	if (row_top_collapsed) {
		row_top_collapsed = false;
		$('#row_top').collapse('show');
		var poiId = poi.options.id;
		$('#poi').load(
			routes.getPoi.url(),
			{
				'gToken': getGoogleIdToken(),
				'poiId': poiId
			},
			function() {
				$('#poi_power_tag_details').load(
					routes.getPoiPowerTag.url(),
					{
						'gToken': getGoogleIdToken(),
						'poiId': poiId,
						'powerTag': $('#poi_power_tag').val()
					},
					function() {
						if ($('#poi_power_tag').val() === 'Generator' && $('#source').val() !== 'null') {
							$('#generator_method').load(
								routes.getPoiPowerTagGeneratorMethod.url(),
								{
									'gToken': getGoogleIdToken(),
									'poiId': $('#poi_id').val(),
									'source': $('#source').val()
								},
								function() {
									$('#generator_type').load(
										routes.getPoiPowerTagGeneratorType.url(),
										{
											'gToken': getGoogleIdToken(),
											'poiId': $('#poi_id').val(),
											'sourceMethod': $('#source').val() + '_' + $('#method').val()});});}});});}}

$(document).on('change', '#poi_power_tag', function() {
	if ($('#poi_power_tag').val() === 'null') {
		$('#poi_power_tag_details').empty();
	} else {
		$('#poi_power_tag_details').load(
			routes.getPoiPowerTag.url(),
			{
				'gToken': getGoogleIdToken(),
				'poiId': $('#poi_id').val(),
				'powerTag': $('#poi_power_tag').val()});}});

$(document).on('change', '#source', function() {
	if ($('#source').val() === 'null') {
		$('#generator_method').empty();
		$('#generator_type').empty();
	} else {
		$('#generator_method').load(
			routes.getPoiPowerTagGeneratorMethod.url(),
			{
				'gToken': getGoogleIdToken(),
				'poiId': $('#poi_id').val(),
				'source': $('#source').val()
			},
			function() {
				$('#generator_type').load(
					routes.getPoiPowerTagGeneratorType.url(),
					{
						'gToken': getGoogleIdToken(),
						'poiId': $('#poi_id').val(),
						'sourceMethod': $('#source').val() + '_' + $('#method').val()});});}});

$(document).on('change', '#method', function() {
	$('#generator_type').load(
		routes.getPoiPowerTagGeneratorType.url(),
		{
			'gToken': getGoogleIdToken(),
			'poiId': $('#poi_id').val(),
			'sourceMethod': $('#source').val() + '_' + $('#method').val()});});

$(document).on('click', '#poi_cancel', function() {
	$('#row_bottom').collapse('hide');
	$('#row_top').collapse('hide');
	$('#photo').empty();
	$('#poi').empty();
	row_bottom_collapsed = true;
	row_top_collapsed = true;

	map.removeLayer(polygon);
	for (var i = 0; i < photoArray.length; i++) {
		map.removeLayer(photoArray[i]);}
	photoArray = [];});

$(document).on('click', '#poi_submit', function() {
	$.post(
		routes.updatePoi.url(),
		$('#poi_form').serialize())
		.done(function() {
			$('#poi_submit').removeClass('btn-warning');
			$('#poi_submit').addClass('btn-success');})
		.fail(function() {
			$('#poi_submit').removeClass('btn-success');
			$('#poi_submit').addClass('btn-warning');});
	return false;});

$(document).on('click', '#poi_zoom', function() {
	var marker = getMarker($('#poi_id').val(), 'poi');
	if (photoArray.length !== 0) {
		var markerArray = photoArray.concat(new Array(marker));
		var group = new L.featureGroup(markerArray);
		map.fitBounds(group.getBounds());
	} else {
		map.setView(marker._latlng, 12);}});
