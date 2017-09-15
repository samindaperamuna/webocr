package org.webocr.web.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.webocr.model.ImageData;
import org.webocr.model.ImageSelection;
import org.webocr.ocr.TesseractHandle;
import org.webocr.util.ImageUtil;

@RestController
public class ProcessImage {

    @PostMapping("/process")
    public ResponseEntity<?> process(@RequestBody ImageData imageData) {
	String base64Image = imageData.getBase64Image();
	byte[] imgBytes = ImageUtil.getImageFromBase64(base64Image);
	BufferedImage img = null;

	try {
	    img = ImageUtil.getBufferedImage(imgBytes);
	} catch (IOException e) {
	    System.err.println("Cannot conver byte array into a buffred image.\n" + e.getLocalizedMessage());
	}

	List<ImageSelection> selectionsList = imageData.getSelectionsList();

	for (ImageSelection s : selectionsList) {
	    BufferedImage selection = ImageUtil.cropImage(img, s.getX(), s.getY(), s.getWidth(), s.getHeight());

	    try {
		File file = ImageUtil.writeToTempFile(selection);
		String data = TesseractHandle.processImage(file.getAbsolutePath());
		System.out.println(data + "\n");
	    } catch (IOException e) {
		System.err.println("Cannot write to file.\n" + e.getLocalizedMessage());
	    }
	}

	return new ResponseEntity<>("Successfully login", new HttpHeaders(), HttpStatus.OK);
    }
}
