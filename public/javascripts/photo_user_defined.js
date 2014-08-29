function showPhoto(photo) {
	var tempPhotoArray = photoArray;
	photoArray = [];
	for (var i = 0; i < tempPhotoArray.length; i++) {
		var currentPhoto = tempPhotoArray[i];
		var spin = false;
		if (photo.options.id === currentPhoto.options.id) {
			spin = true;
		}
		addMarker(currentPhoto.options.id, currentPhoto._latlng.lat, currentPhoto._latlng.lng, 'photo', spin);
		map.removeLayer(currentPhoto);
	}

	if (row_bottom_collapsed) {
		row_bottom_collapsed = false;
		$('#row_bottom').collapse('show');
	}

	$('#photo').load(getPhoto({photoId: photo.options.id}));
};
