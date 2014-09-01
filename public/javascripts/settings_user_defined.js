$(document).on('hidden.bs.modal', '#myModal', function (e) {
	$('#user_administration').empty();});

$(document).on('shown.bs.modal', '#myModal', function (e) {
	$('#user_administration').load(
		routes.getSettingsUserAdministration.url(),
		{
			'gToken': getGoogleIdToken()});});
