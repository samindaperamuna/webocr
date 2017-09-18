package org.webocr.data.test;

import java.io.File;

import org.junit.Test;
import org.webocr.data.XMLParser;
import org.webocr.util.FileUtil;

public class XMLParserTest {

    @Test
    public void testReadXML() {
	File xmlFile = new File(getClass().getResource("/static/xml/template.xml").getPath());
	File ocrFile = new File(getClass().getResource("/sample_ocr.txt").getPath());

	String ocrText = FileUtil.readFileAsString(ocrFile, false);
	XMLParser.readXML(ocrText, xmlFile);
    }
}