/**
 * Contains the functionality of the index.html
 */
$(document).ready(function() {

	toDataURL('../img/vic_computer.png', function(data) {
		$('#originalImage')[0].src = data;
	});

	$('img#originalImage').selectAreas({
		onChanged : debugQtyAreas
	});

	// On select list item click.
	$('#selectTemplate').on('change', function(event) {
		var imgName = $("#selectTemplate option:selected").attr('data');

		$('#templateImage').attr('src', '../img/' + imgName + '.png');
		$('#templateImage').attr('data-magnify-src', '../img/' + imgName + '.png');

		// Enable Magnify plug-in.
		$('.zoom').magnify({
			finalHeight : 300
		});

		// Destroy existing zoom object.
		if (zoom) {
			zoom.destroy();
		}

		// Enable Magnify plug-in.
		zoom = $('.zoom').magnify({
			finalHeight : 300
		});
	});

	// On Send to Server button click.
	$('#sendButton').on('click', function() {
		var dataUrl = $('#originalImage')[0].src;
		var templateName = $('#selectTemplate option:selected').attr('data');
		var areas = $('img#originalImage').selectAreas('areas');
		var areasList = [];

		$.each(areas, function(id, area) {
			areasList.push({
			'x' : area.x,
			'y' : area.y,
			'width' : area.width,
			'height' : area.height
			});
		});

		var imageBlob = dataURLtoBlob(dataUrl);

		postCoordData(imageBlob, templateName, areasList)
	});

	// On file input changed.
	$('#imageInput').on('change', function(event) {
		var imgElem = document.getElementById("originalImage");
		var tgt = event.target || window.event.srcElement;
		var files = tgt.files;

		// Check for FileReader support.
		if (FileReader && files && files.length) {
			var fr = new FileReader();

			fr.onload = function() {
				imgElem.src = fr.result;
			}

			fr.readAsDataURL(files[0]);
		}
	});

	// On image has completed loading.
	$('#originalImage').on('load', function(event) {
		var imgElem = document.getElementById("originalImage");

		// Resize container to fit image.
		$('.image-decorator').css('width', imgElem.width);
		$('.image-decorator').css('height', imgElem.height);

		// Destroy and recreate the plug-in on the new image.
		$('img#originalImage').selectAreas('destroy');
		$('img#originalImage').selectAreas({
			onChanged : debugQtyAreas
		});
	});

	// Select the first template.
	$('#selectTemplate').val(1).change();
});

var zoom;

// Log the quantity of selections
function debugQtyAreas(event, id, areas) {
	console.log(areas.length + " areas", arguments);
}

/**
 * Get base64String from the image view.
 */
function getBase64String(imgId) {
	var canvas = document.createElement('canvas');
	var ctx = canvas.getContext('2d');

	var img = document.getElementById(imgId);

	ctx.drawImage(img, 0, 0);

	return canvas.toDataURL();
}

/**
 * Convert base64 to blob.
 * 
 * @param dataURL
 * @returns
 */
function dataURLtoBlob(dataURL) {

	var BASE64_MARKER = ';base64,';

	if (dataURL.indexOf(BASE64_MARKER) == -1) {
		var parts = dataURL.split(',');
		var contentType = parts[0].split(':')[1];
		var raw = decodeURIComponent(parts[1]);

		return new Blob([ raw ], {
			type : contentType
		});
	}

	var parts = dataURL.split(BASE64_MARKER);
	var contentType = parts[0].split(':')[1];
	var raw = window.atob(parts[1]);
	var rawLength = raw.length;
	var uInt8Array = new Uint8Array(rawLength);

	for (var i = 0; i < rawLength; ++i) {
		uInt8Array[i] = raw.charCodeAt(i);
	}

	return new Blob([ uInt8Array ], {
		type : contentType
	});
}

/**
 * Post data to server.
 * 
 * @param blob
 * @returns
 */
function postCoordData(blob, templateName, coords) {
	var formData = new FormData();
	formData.append('image', blob);
	formData.append('templateName', templateName);
	formData.append('selectionList', JSON.stringify({
		'coords' : coords
	}));

	$.ajax({
	type : 'POST',
	url : '/process',
	data : formData,
	processData : false,
	contentType : false,
	success : function(data) {
		console.log('Image data sent to the server.');

		$.redirect('/invoice', {
			"jsonInvoice" : JSON.stringify(data)
		});
	},
	error : function(data) {
		console.log('An error occurred.');
		console.log(data);
	}
	});
}

/**
 * Converts file system URL to a base64Image.
 * 
 * @param url
 * @param callback
 * @returns
 */
function toDataURL(url, callback) {
	var xhr = new XMLHttpRequest();

	xhr.onload = function() {
		var reader = new FileReader();
		reader.onloadend = function() {
			callback(reader.result);
		}
		reader.readAsDataURL(xhr.response);
	};

	xhr.open('GET', url);
	xhr.responseType = 'blob';
	xhr.send();
}