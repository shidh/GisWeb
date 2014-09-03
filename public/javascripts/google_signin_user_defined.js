function getGoogleId() {
	gapi.client.load('plus', 'v1', function() {
		gapi.client.plus.people.get({
			'userId' : 'me'
		}).execute(function(resp) {
			my_google_id = resp.id;
		});
	});
}

function getGoogleIdToken() {
	var token = gapi.auth.getToken();
	if (token) {
		return token.id_token;
	} else {
		return '';
	}
}
