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
		var google_id = '';
		if (photo.options.id === currentPhoto.options.id) {
			google_id = my_google_id;
		}
		addMarker(google_id, currentPhoto._latlng.lat,
				currentPhoto._latlng.lng, 'photo', currentPhoto.options.id, '');
		map.removeLayer(currentPhoto);
	}
	expandPhoto();
	$('#photo').load(routes.renderPhoto.url(), {
		'gToken' : getGoogleIdToken(),
		'photoId' : photo.options.id
	});
}
