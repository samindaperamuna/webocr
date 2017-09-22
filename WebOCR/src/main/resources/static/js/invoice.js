/**
 * Contains the functionality of the invoice.html
 */
$(document).ready(function() {
	$(function() {
		$('[data-toggle="tooltip"]').tooltip();
	});

	// On delete item button click.
	$('.closeItem').on('click', function(event) {
		var id = event.currentTarget.id.split('-')[1];

		$('#row-' + id).remove();
	});

	// On add item button click.
	$('#deleteItems').on('click', function(event) {
		$('#itemContainer').empty();
	});
});