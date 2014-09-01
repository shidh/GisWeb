$(document).on('click', '#settings_submit', function() {
	var data = $('#settings_form').serializeArray();
	data.push({name: 'gToken', value: getGoogleIdToken()});
	$.post(
		routes.updateSettings.url(),
		$.param(data))
		.done(function() {
			$('#settings_submit').removeClass('btn-primary');
			$('#settings_submit').removeClass('btn-warning');
			$('#settings_submit').addClass('btn-success');})
		.fail(function() {
			$('#settings_submit').removeClass('btn-primary');
			$('#settings_submit').removeClass('btn-success');
			$('#settings_submit').addClass('btn-warning');});
	return false;});

$(document).on('hidden.bs.modal', '#myModal', function (e) {
	$('#user_administration').empty();
	$('#settings_submit').addClass('btn-primary');
	$('#settings_submit').removeClass('btn-warning');
	$('#settings_submit').removeClass('btn-success');});

$(document).on('shown.bs.modal', '#myModal', function (e) {
	$('#user_administration').load(
		routes.getSettingsUserAdministration.url(),
		{
			'gToken': getGoogleIdToken()});});
 