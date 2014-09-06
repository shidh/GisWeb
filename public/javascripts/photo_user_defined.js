function collapsePhoto() {
	if (!row_bottom_collapsed) {
		row_bottom_collapsed = true;
		$('#photo').empty();
		$('#row_bottom').collapse('hide');
	}
}

function expandPhoto() {
	if (row_bottom_collapsed) {
		row_bottom_collapsed = false;
		$('#row_bottom').collapse('show');
	}
}

function showPhoto(photo) {
	var tempPhotoArray = photoArray;
	photoArray = [];
	for ( var i = 0; i < tempPhotoArray.length; i++) {
		var currentPhoto = tempPhotoArray[i];
		var google_id = null;
		if (photo.options.id === currentPhoto.options.id) {
			google_id = my_google_id;
		}
		addMarker(google_id, currentPhoto._latlng.lat,
				currentPhoto._latlng.lng, 'photo', currentPhoto.options.id, null);
		map.removeLayer(currentPhoto);
	}
	expandPhoto();
	$('#photo').load(routes.renderPhoto.url(), {
		'gToken' : getGoogleIdToken(),
		'photoId' : photo.options.id
	});
}

$(document).on('click', '#photo_close', function() {
	var tempPhotoArray = photoArray;
	photoArray = [];
	for ( var i = 0; i < tempPhotoArray.length; i++) {
		var currentPhoto = tempPhotoArray[i];
		addMarker(null, currentPhoto._latlng.lat,
				currentPhoto._latlng.lng, 'photo', currentPhoto.options.id, null);
		map.removeLayer(currentPhoto);
	}
	collapsePhoto();
});

$(document).on('click', '#photo_zoom', function() {
	var photo_id = parseInt($('#photo_id').val());
	var marker = getMarker('photo', photo_id);
	map.setView(marker._latlng);
});
