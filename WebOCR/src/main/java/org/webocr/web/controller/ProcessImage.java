package org.webocr.web.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.webocr.data.SelectionListEditor;
import org.webocr.model.SelectionData;
import org.webocr.model.SelectionList;
import org.webocr.ocr.TesseractHandle;
import org.webocr.util.ImageUtil;

@RestController
public class ProcessImage {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
	binder.registerCustomEditor(SelectionList.class, new SelectionListEditor());
    }

    @PostMapping("/process")
    public ResponseEntity<?> process(@RequestParam(value = "base64Image") MultipartFile base64Image,
	    @RequestParam(value = "selectionList") SelectionList selectionList) {

	byte[] imgBytes = null;

	try {
	    imgBytes = base64Image.getBytes();
	} catch (IOException e) {
	    System.err.println("Cannot get image bytes.\n" + e.getLocalizedMessage());
	}

	BufferedImage img = null;

	try {
	    img = ImageUtil.getBufferedImage(imgBytes);
	} catch (IOException e) {
	    System.err.println("Cannot conver byte array into a buffred image.\n" + e.getLocalizedMessage());
	}

	for (SelectionData s : selectionList.getSelectionList()) {
	    BufferedImage selection = ImageUtil.cropImage(img, s.getX(), s.getY(), s.getWidth(), s.getHeight());
	    BufferedImage upscaledImage = ImageUtil.upscaleImage(selection);

	    try {
		File file = ImageUtil.writeToTempFile(upscaledImage);
		String data = TesseractHandle.processImage(file.getAbsolutePath());
		System.out.println(data + "\n");
	    } catch (IOException e) {
		System.err.println("Cannot write to file.\n" + e.getLocalizedMessage());
	    }
	}

	return new ResponseEntity<>("Successfully Received", new HttpHeaders(), HttpStatus.OK);
    }
}
