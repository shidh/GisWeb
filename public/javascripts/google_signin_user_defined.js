function getGoogleIdToken() {
	var token = gapi.auth.getToken();
	if (token) {
		return token.id_token;
	} else {
		return '';}}
