package org.webocr.ocr;

import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OCRHandle {

    private static final String DATA_PATH = "/usr/share/tesseract-ocr/tessdata";

    public static String processImage(String imagePath) {
	File imageFile = new File(imagePath);
	String processedText = null;

	ITesseract instance = new Tesseract();
	instance.setDatapath(DATA_PATH);

	try {
	    processedText = instance.doOCR(imageFile);
	} catch (TesseractException te) {
	    System.err.print(te.getMessage());
	}

	return processedText;
    }
}