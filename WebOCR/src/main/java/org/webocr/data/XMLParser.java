package org.webocr.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.StringReader;
import java.util.Iterator;
import java.util.StringTokenizer;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.webocr.model.Invoice;

public class XMLParser {

    public static String DELIMITER = " ";

    public static Invoice readXML(String ocrText, File template) {
	StringReader stringReader = new StringReader(ocrText);
	BufferedReader bufferedReader = new BufferedReader(stringReader);
	Invoice invoice = new Invoice();
	int cols = 1;
	String lineValue = null;
	StringTokenizer tokenizer = null;

	XMLInputFactory inputFactory = XMLInputFactory.newInstance();

	try (FileInputStream in = new FileInputStream(template)) {
	    XMLEventReader reader = inputFactory.createXMLEventReader(in);

	    while (reader.hasNext()) {
		XMLEvent event = reader.nextEvent();

		if (event.isStartElement()) {
		    StartElement startElement = event.asStartElement();

		    if (startElement.getName().getLocalPart().equals("line")) {
			String fieldName = null;

			Iterator<?> attributes = startElement.getAttributes();

			while (attributes.hasNext()) {
			    Attribute attribute = (Attribute) attributes.next();

			    if (attribute.getName().getLocalPart().equals("cols")) {
				cols = Integer.parseInt(attribute.getValue());
			    } else if (attribute.getName().getLocalPart().equals("field_name")) {
				fieldName = attribute.getValue();
			    }
			}

			lineValue = bufferedReader.readLine();
			tokenizer = new StringTokenizer(lineValue, DELIMITER);

			if (fieldName != null && cols == 1) {
			    setFieldValue(invoice, fieldName, lineValue);
			}
		    } else if (startElement.getName().getLocalPart().equals("field")) {
			String name = null;

			Iterator<?> attributes = startElement.getAttributes();

			while (attributes.hasNext()) {
			    Attribute attribute = (Attribute) attributes.next();

			    if (attribute.getName().getLocalPart().equals("name")) {
				name = attribute.getValue();
			    }
			}

			if (tokenizer.hasMoreTokens()) {
			    String token = tokenizer.nextToken();

			    if (name != null) {
				setFieldValue(invoice, name, token);
				name = null;
			    }
			}
		    }
		}
	    }
	} catch (Exception e) {
	    System.out.println("Cannot parse the text. An error occurred!\n" + e.getLocalizedMessage());
	}

	return invoice;
    }

    /**
     * Set the field value to the invoice.
     * 
     * @param invoice Invoice to be modified.
     * @param fieldName Field name to be set.
     * @param fieldValue Field value to be set.
     * @return
     */
    private static Invoice setFieldValue(Invoice invoice, String fieldName, String fieldValue) {
	switch (fieldName) {
	case "company_name":
	    invoice.setCompanyName(fieldValue);

	    break;
	case "address1":
	    invoice.setAddress(fieldValue);

	    break;
	case "address2":
	    invoice.setAddress(invoice.getAddress() + "\n" + fieldValue);

	    break;
	case "address3":
	    invoice.setAddress(invoice.getAddress() + "\n" + fieldValue);

	    break;
	case "invoice_type":
	    invoice.setType(fieldValue);

	    break;
	case "date":
	    invoice.setDate(fieldValue);

	    break;
	case "time":
	    invoice.setTime(fieldValue);

	    break;
	case "invoice_number":
	    invoice.setInvoiceNumber(fieldValue);

	    break;
	}

	return invoice;
    }
}