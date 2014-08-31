function signinCallback(authResult) {
	if (authResult.status.signed_in) {
		// Update the app to reflect a signed in user
		// Hide the sign-in button now that the user is authorized, for example:
		$('#signinButton').hide();
		$('#signoutButton').show();
		$.post(
			routes.registerUser.url(),
			{
				'gToken': authResult.id_token});
		$('#settings').load(
			routes.getSettingsModal.url(),
			{
				'gToken': getGoogleIdToken()});
	} else {
		// Update the app to reflect a signed out user
		// Possible error values:
		//   "user_signed_out" - User is signed-out
		//   "access_denied" - User denied access to your app
		//   "immediate_failed" - Could not automatically log in the user
		$('#signinButton').show();
		$('#signoutButton').hide();
		$('#settings').empty()}}

$(document).on('click', '#signoutButton', function() {
	var access_token = gapi.auth.getToken().access_token;
	if (access_token) {
		var revokeUrl = 'https://accounts.google.com/o/oauth2/revoke?token=' + access_token;
		$.ajax({
			type: 'GET',
			url: revokeUrl,
			async: false,
			contentType: "application/json",
			dataType: 'jsonp',
			success: function(nullResponse) {
				gapi.auth.signOut();}});}});
