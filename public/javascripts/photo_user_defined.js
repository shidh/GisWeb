function collapsePhoto() {
	if (!row_bottom_collapsed) {
		row_bottom_collapsed = true;
		$('#photo').empty();
		$('#row_bottom').collapse('hide');}}

function expandPhoto() {
	if (row_bottom_collapsed) {
		row_bottom_collapsed = false;
		$('#row_bottom').collapse('show');}}

function showPhoto(photo) {
	var tempPhotoArray = photoArray;
	photoArray = [];
	for (var i = 0; i < tempPhotoArray.length; i++) {
		var currentPhoto = tempPhotoArray[i];
		var spin = false;
		if (photo.options.id === currentPhoto.options.id) {
			spin = true;}
		addMarker(currentPhoto.options.id, currentPhoto._latlng.lat, currentPhoto._latlng.lng, 'photo', spin);
		map.removeLayer(currentPhoto);}
	expandPhoto();
	$('#photo').load(
		routes.getPhoto.url(),
		{
			'gToken': getGoogleIdToken(),
			'photoId': photo.options.id});}
