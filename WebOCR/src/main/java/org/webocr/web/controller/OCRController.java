package org.webocr.web.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.webocr.data.SelectionListEditor;
import org.webocr.data.XMLParser;
import org.webocr.data.service.InvoiceService;
import org.webocr.model.Invoice;
import org.webocr.model.SelectionData;
import org.webocr.model.SelectionList;
import org.webocr.ocr.OCRHandle;
import org.webocr.util.ImageUtil;

@Controller
public class OCRController {

    @Autowired
    private InvoiceService invoiceService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
	binder.registerCustomEditor(SelectionList.class, new SelectionListEditor());
    }

    @PostMapping("/process")
    public @ResponseBody Invoice process(@RequestParam(value = "image") MultipartFile image,
	    @RequestParam(value = "templateName") String templateName,
	    @RequestParam(value = "selectionList") SelectionList selectionList) {

	File templateFile = null;

	if (!templateName.isEmpty()) {
	    templateFile = new File(getClass().getResource("/static/xml/" + templateName + ".xml").getPath());
	} else {
	    templateFile = new File(getClass().getResource("/static/xml/default.xml").getPath());
	}

	Invoice invoice = null;
	String ocrData = null;
	byte[] imgBytes = null;

	try {
	    imgBytes = image.getBytes();
	} catch (IOException e) {
	    System.err.println("Cannot get image bytes.\n" + e.getLocalizedMessage());
	}

	BufferedImage img = null;

	try {
	    img = ImageUtil.getBufferedImage(imgBytes);
	} catch (IOException e) {
	    System.err.println("Cannot conver byte array into a buffred image.\n" + e.getLocalizedMessage());
	}

	if (selectionList.getSelectionList().isEmpty()) {
	    SelectionData s = new SelectionData();
	    s.setX(0);
	    s.setY(0);
	    s.setWidth(img.getWidth());
	    s.setHeight(img.getHeight());

	    ocrData = doOCR(img, s);
	} else {
	    for (SelectionData s : selectionList.getSelectionList()) {
		ocrData = doOCR(img, s);
	    }
	}

	if (ocrData != null && templateFile != null) {
	    invoice = XMLParser.readXML(ocrData, templateFile);
	    invoice.setImage(imgBytes);
	    invoiceService.Save(invoice);
	}

	return invoice;
    }

    /**
     * Crop the selection of the image, save it on to the disk and feed it to
     * the OCR Handle.
     * 
     * @param img Image to be processed.
     * @param s Selection to crop.
     * @return
     */
    private String doOCR(BufferedImage img, SelectionData s) {
	BufferedImage selection = ImageUtil.cropImage(img, s.getX(), s.getY(), s.getWidth(), s.getHeight());

	// Apply an up scale.
	selection = ImageUtil.upscaleImage(selection);

	// Apply a sharpening effect.
	selection = ImageUtil.sharpenImage(selection);

	String ocrData = null;

	try {
	    File file = ImageUtil.writeToTempFile(selection);
	    ocrData = OCRHandle.processImage(file.getAbsolutePath());
	    System.out.println(ocrData + "\n");
	} catch (IOException e) {
	    System.err.println("Cannot write to file.\n" + e.getLocalizedMessage());
	}

	return ocrData;
    }
}
