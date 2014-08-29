function showPoi(poi) {
	if (row_top_collapsed) {
		row_top_collapsed = false;
		$('#row_top').collapse('show');
				
		var poiId = poi.options.id;
		$('#poi').load(getPoi({poiId: poiId}), function() {
				
			var powerTag = encodeURIComponent($('#poi_power_tag').val());
			$('#poi_power_tag_details').load(getPoiPowerTag({poiId: poiId, powerTag: powerTag}), function() {
					
				if ($('#poi_power_tag').val() === 'Generator') {
					if ($('#source').val()) {
							
						var source = encodeURIComponent($('#source').val());
						$('#generator_method').load(getPoiPowerTagGeneratorMethod({poiId: $('#poi_id').val(), source: source}), function() {
								
							if ($('#method').val()) {
								var source = encodeURIComponent($('#source').val());
								var method = encodeURIComponent($('#method').val());
								var sourceMethod;
								if (!source) {
									source = '';
								}
								if (!method) {
									method = '';
								}
								if (source === '' || method === ''){
									sourceMethod = source + method;
								} else {
									sourceMethod = source + '_' + method;
								}
								if (sourceMethod) {
									$('#generator_type').load(getPoiPowerTagGeneratorType({poiId: $('#poi_id').val(), sourceMethod: sourceMethod}));
								}
							}
						});
					}
				}
			});
		});
	}
};
$(document).on('change', '#poi_power_tag', function() {
	if ($('#poi_power_tag').val() === 'null') {
		$('#poi_power_tag_details').empty();
	} else {
		var powerTag = encodeURIComponent($('#poi_power_tag').val());
		$('#poi_power_tag_details').load(getPoiPowerTag({poiId: $('#poi_id').val(), powerTag: powerTag}));
	};
});
$(document).on('change', '#source', function() {
	if ($('#source').val() === 'null') {
		$('#generator_method').empty();
		$('#generator_type').empty();
	} else {
		var source = encodeURIComponent($('#source').val());
		$('#generator_method').load(getPoiPowerTagGeneratorMethod({poiId: $('#poi_id').val(), source: source}), function() {
				
			if ($('#method').val() === 'null') {
				$('#generator_type').empty();
			} else {
				var source = encodeURIComponent($('#source').val());
				var method = encodeURIComponent($('#method').val());
				var sourceMethod;
				if (!source) {
					source = '';
				}
				if (!method) {
					method = '';
				}
				if (source === '' || method === ''){
					sourceMethod = source + method;
				} else {
					sourceMethod = source + '_' + method;
				}
				if (sourceMethod) {
					$('#generator_type').load(getPoiPowerTagGeneratorType({poiId: $('#poi_id').val(), sourceMethod: sourceMethod}));
				}
			}
		});
	}
});
$(document).on('change', '#method', function() {
	if ($('#method').val() === 'null') {
		$('#generator_type').empty();
	} else {
		var source = encodeURIComponent($('#source').val());
		var method = encodeURIComponent($('#method').val());
		var sourceMethod;
		if (!source) {
			source = '';
		}
		if (!method) {
			method = '';
		}
		if (source === '' || method === ''){
			sourceMethod = source + method;
		} else {
			sourceMethod = source + '_' + method;
		}
		if (sourceMethod) {
			$('#generator_type').load(getPoiPowerTagGeneratorType({poiId: $('#poi_id').val(), sourceMethod: sourceMethod}));
		}
	}
});
$(document).on('click', '#poi_cancel', function() {
	$('#row_bottom').collapse('hide');
	$('#row_top').collapse('hide');
	$('#photo').empty();
	$('#poi').empty();
	row_bottom_collapsed = true;
	row_top_collapsed = true;

	map.removeLayer(polygon);
	for (var i = 0; i < photoArray.length; i++) {
		map.removeLayer(photoArray[i]);
	}
	photoArray = [];
});
$(document).on('click', '#poi_submit', function() {
	$.post(updatePoi.url(),
		$('#poi_form').serialize())
		.done(function() {
			$('#poi_submit').removeClass('btn-warning');
			$('#poi_submit').addClass('btn-success');
		})
		.fail(function() {
			$('#poi_submit').removeClass('btn-success');
			$('#poi_submit').addClass('btn-warning');
		});
	return false;
});
$(document).on('click', '#poi_zoom', function() {
	var marker = getMarker($('#poi_id').val(), 'poi');
	if (photoArray.length !== 0) {
		var markerArray = photoArray.concat(new Array(marker));
		var group = new L.featureGroup(markerArray);
		map.fitBounds(group.getBounds());
	} else {
		map.setView(marker._latlng, 12);
	}
});
