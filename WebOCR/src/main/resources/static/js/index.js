/**
 * Contains the functionality of the index.html
 */
$(document).ready(function() {
	toDataURL('../img/example.jpg', function(data) {
		$('#originalImage')[0].src = data;
	});

	$('img#originalImage').selectAreas({
		onChanged : debugQtyAreas
	});

	$('#btnView').click(function() {
		var areas = $('img#originalImage').selectAreas('areas');
		displayAreas(areas);
	});

	$('#btnViewRel').click(function() {
		var areas = $('img#originalImage').selectAreas('relativeAreas');
		displayAreas(areas);
	});

	$('#btnReset').click(function() {
		output("reset");
		$('img#originalImage').selectAreas('reset');
	});

	$('#btnDestroy').click(function() {
		$('img#originalImage').selectAreas('destroy');

		output("destroyed");
		$('.actionOn').attr("disabled", "disabled");
		$('.actionOff').removeAttr("disabled");
	});

	$('#btnCreate').attr("disabled", "disabled").click(function() {
		$('img#originalImage').selectAreas({
			onChanged : debugQtyAreas
		});

		output("created");
		$('.actionOff').attr("disabled", "disabled");
		$('.actionOn').removeAttr("disabled");
	});

	$('#btnNew').click(function() {
		var areaOptions = {
		x : Math.floor((Math.random() * 200)),
		y : Math.floor((Math.random() * 200)),
		width : Math.floor((Math.random() * 100)) + 50,
		height : Math.floor((Math.random() * 100)) + 20,
		};

		output("Add a new area: " + areaToString(areaOptions));
		$('img#originalImage').selectAreas('add', areaOptions);
	});

	$('#btnNews').click(function() {
		var areaOption1 = {
		x : Math.floor((Math.random() * 200)),
		y : Math.floor((Math.random() * 200)),
		width : Math.floor((Math.random() * 100)) + 50,
		height : Math.floor((Math.random() * 100)) + 20,
		}, areaOption2 = {
		x : areaOption1.x + areaOption1.width + 10,
		y : areaOption1.y + areaOption1.height - 20,
		width : 50,
		height : 20,
		};

		output("Add a new area: " + areaToString(areaOption1) + " and " + areaToString(areaOption2));

		$('img#originalImage').selectAreas('add', [ areaOption1, areaOption2 ]);
	});

	// On Send to Server button click.
	$('#sendButton').on('click', function() {
		var dataUrl = $('#originalImage')[0].src;
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

		postCoordData(imageBlob, areasList)
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
});

var selectionExists;

function areaToString(area) {
	return (typeof area.id === "undefined" ? "" : (area.id + ": ")) + area.x + ':' + area.y + ' ' + area.width + 'x' + area.height + '<br />'
}

function output(text) {
	$('#output').html(text);
}

// Log the quantity of selections
function debugQtyAreas(event, id, areas) {
	console.log(areas.length + " areas", arguments);
}

// Display areas coordinates in a DIV
function displayAreas(areas) {
	var text = "";
	$.each(areas, function(id, area) {
		text += areaToString(area);
	});
	output(text);
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
function postCoordData(blob, coords) {
	var formData = new FormData();
	formData.append('base64Image', blob);
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