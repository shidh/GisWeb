function collapsePoi() {
	if (!row_top_collapsed) {
		row_top_collapsed = true;
		$('#poi').empty();
		$('#row_top').collapse('hide');
	}
}

function expandPoi() {
	if (row_top_collapsed) {
		row_top_collapsed = false;
		$('#row_top').collapse('show');
	}
}

function removePoiLayers() {
	map.removeLayer(polygon);
	for ( var i = 0; i < photoArray.length; i++) {
		map.removeLayer(photoArray[i]);
	}
	photoArray = [];
}

function showPoi(poi) {

	if (row_top_collapsed || $('#poi_id').val() == poi.options.id) {
		var data = [];
		data.push({
			name : 'gToken',
			value : getGoogleIdToken()
		});
		data.push({
			name : 'poiId',
			value : poi.options.id
		});
		$
				.post(routes.reservePoi.url(), $.param(data))
				.done(
						$
								.post(routes.broadcastOpenPoi.url(),
										$.param(data))
								.done(

										function() {
											expandPoi();
											var poiId = poi.options.id;
											$('#poi')
													.load(
															routes.renderPoi
																	.url(),
															{
																'gToken' : getGoogleIdToken(),
																'poiId' : poiId
															},
															function() {
																
																$('#poi_status').load(routes.renderPoiStatus.url(), {
																	'gToken' : getGoogleIdToken(),
																	'poiId' : poiId
																},
																function() {
																
																$(
																		'#poi_power_tag_details')
																		.load(
																				routes.renderPoiPowerTag
																						.url(),
																				{
																					'gToken' : getGoogleIdToken(),
																					'poiId' : poiId,
																					'powerTag' : $(
																							'#poi_power_tag')
																							.val()
																				},
																				function() {
																					if ($(
																							'#poi_power_tag')
																							.val() === 'Generator'
																							&& $(
																									'#source')
																									.val() !== 'null') {
																						$(
																								'#generator_method')
																								.load(
																										routes.renderPoiPowerTagGeneratorMethod
																												.url(),
																										{
																											'gToken' : getGoogleIdToken(),
																											'poiId' : $(
																													'#poi_id')
																													.val(),
																											'source' : $(
																													'#source')
																													.val()
																										},
																										function() {
																											$(
																													'#generator_type')
																													.load(
																															routes.renderPoiPowerTagGeneratorType
																																	.url(),
																															{
																																'gToken' : getGoogleIdToken(),
																																'poiId' : $(
																																		'#poi_id')
																																		.val(),
																																'sourceMethod' : $(
																																		'#source')
																																		.val()
																																		+ '_'
																																		+ $(
																																				'#method')
																																				.val()
																															});
																										});
																					}
																				});
																});
															});
										}

								)

				);
	}
}

$(document).on('change', '#poi_power_tag', function() {
	if ($('#poi_power_tag').val() === 'null') {
		$('#poi_power_tag_details').empty();
	} else {
		$('#poi_power_tag_details').load(routes.renderPoiPowerTag.url(), {
			'gToken' : getGoogleIdToken(),
			'poiId' : $('#poi_id').val(),
			'powerTag' : $('#poi_power_tag').val()
		});
	}
});

$(document)
		.on(
				'change',
				'#source',
				function() {
					if ($('#source').val() === 'null') {
						$('#generator_method').empty();
						$('#generator_type').empty();
					} else {
						$('#generator_method')
								.load(
										routes.renderPoiPowerTagGeneratorMethod
												.url(),
										{
											'gToken' : getGoogleIdToken(),
											'poiId' : $('#poi_id').val(),
											'source' : $('#source').val()
										},
										function() {
											$('#generator_type')
													.load(
															routes.renderPoiPowerTagGeneratorType
																	.url(),
															{
																'gToken' : getGoogleIdToken(),
																'poiId' : $(
																		'#poi_id')
																		.val(),
																'sourceMethod' : $(
																		'#source')
																		.val()
																		+ '_'
																		+ $(
																				'#method')
																				.val()
															});
										});
					}
				});

$(document).on('change', '#method', function() {
	$('#generator_type').load(routes.renderPoiPowerTagGeneratorType.url(), {
		'gToken' : getGoogleIdToken(),
		'poiId' : $('#poi_id').val(),
		'sourceMethod' : $('#source').val() + '_' + $('#method').val()
	});
});

$(document).on('click', '#poi_close', function() {
	var data = [];
	data.push({
		name : 'gToken',
		value : getGoogleIdToken()
	});
	data.push({
		name : 'poiId',
		value : $('#poi_id').val()
	});
	$.post(routes.broadcastClosePoi.url(), $.param(data)).done(function() {
		collapsePhoto();
		collapsePoi();
		removePoiLayers();
	});
});

$(document).on('click', '#poi_delete', function() {
	var data = [];
	data.push({
		name : 'gToken',
		value : getGoogleIdToken()
	});
	data.push({
		name : 'poiId',
		value : $('#poi_id').val()
	});
	$.post(routes.deletePoi.url(), $.param(data)).done(function() {
		$('#poi_delete').removeClass('btn-warning');
		$('#poi_delete').addClass('btn-success');
		$('#delete_poi_button').modal('hide');
		$('body').removeClass('modal-open');
		$('.modal-backdrop').remove();
		collapsePoi();
		removePoiLayers();
	}).fail(function() {
		$('#poi_delete').removeClass('btn-success');
		$('#poi_delete').addClass('btn-warning');
	});
});

$(document).on('click', '#poi_submit', function() {
	var data = [];
	data.push({
		name : 'gToken',
		value : getGoogleIdToken()
	});
	data.push({
		name : 'poiId',
		value : $('#poi_id').val()
	});
	$.post(routes.broadcastOpenPoi.url(), $.param(data)).done(function() {
		var data = $('#poi_form').serializeArray();
		data.push({
			name : 'gToken',
			value : getGoogleIdToken()
		});
		$.post(routes.updatePoi.url(), $.param(data)).done(function() {
			$('#poi_submit').removeClass('btn-warning');
			$('#poi_submit').addClass('btn-success');
			var poi = getMarker('poi', $('#poi_id').val());
			showPoi(poi);
		}).fail(function() {
			$('#poi_submit').removeClass('btn-success');
			$('#poi_submit').addClass('btn-warning');
		});
	});
});

$(document).on('submit', '#poi_form', function() {
	return false;
});

$(document).on('click', '#poi_zoom', function() {
	var marker = getMarker('poi', $('#poi_id').val());
	if (photoArray.length !== 0) {
		var markerArray = photoArray.concat(new Array(marker));
		var group = new L.featureGroup(markerArray);
		map.fitBounds(group.getBounds());
	} else {
		map.setView(marker._latlng, 12);
	}
});

$(document).on('click', '#poi_photos_modal_show', function() {
	var photo_id = parseInt($('#carousel-example-generic .active').find('img').attr('alt'));
	var photo = getMarker('photo', photo_id);
	$('#poi_photos_modal').modal('hide');
	showPhoto(photo);
});
