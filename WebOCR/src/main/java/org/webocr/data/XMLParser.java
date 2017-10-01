package org.webocr.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang3.math.NumberUtils;
import org.webocr.model.Invoice;
import org.webocr.model.InvoiceItem;

public class XMLParser {

    public static final String DELIMITER = " ";

    private static int cols;
    private static int itemPos;
    private static boolean iterator;
    private static String lineValue;
    private static StringTokenizer tokenizer;
    private static String pattern;

    private static Queue<XMLEvent> eventQueue = new LinkedList<>();
    private static Invoice invoice = new Invoice();

    private static void initialize() {
	cols = 1;
	itemPos = 1;
	iterator = false;
	lineValue = null;
	tokenizer = null;
	pattern = null;

	eventQueue = new LinkedList<>();
	invoice = new Invoice();
    }

    public static Invoice readXML(String ocrText, File template) {
	initialize();
	XMLInputFactory inputFactory = XMLInputFactory.newInstance();
	StringReader stringReader = new StringReader(ocrText);
	BufferedReader bufferedReader = new BufferedReader(stringReader);

	try (FileInputStream in = new FileInputStream(template)) {
	    XMLEventReader reader = inputFactory.createXMLEventReader(in);

	    while (reader.hasNext()) {
		XMLEvent event = reader.nextEvent();

		if (event.isStartElement()) {
		    StartElement startElement = event.asStartElement();

		    if (iterator) {
			eventQueue.add(event);
		    } else {
			processStartElement(startElement, bufferedReader);
		    }
		} else if (event.isEndElement()) {
		    EndElement endElement = event.asEndElement();

		    processEndElement(endElement, bufferedReader);
		}
	    }
	} catch (Exception e) {
	    System.out.println("Cannot parse the text. An error occurred!\n" + e.getLocalizedMessage());
	}

	return invoice;
    }

    /**
     * Process StartElement chain.
     * 
     * @param startElement Start element.
     * @param bufferedReader
     * @throws IOException
     */
    private static void processStartElement(StartElement startElement, BufferedReader bufferedReader)
	    throws IOException {

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

	    bufferedReader.mark(0);
	    lineValue = bufferedReader.readLine();

	    // Check the line value for a pattern. If matches set the
	    // iterator to false.
	    if (iterator) {
		if (lineValue.matches(pattern)) {
		    iterator = false;
		    itemPos = 0;

		    // Clear the event queue.
		    eventQueue.clear();
		    // Reset the buffered reader.
		    bufferedReader.reset();
		}
	    }

	    tokenizer = new StringTokenizer(lineValue, DELIMITER);

	    if (fieldName != null && cols == 1) {
		setFieldValue(invoice, fieldName, lineValue);
	    }
	} else if (startElement.getName().getLocalPart().equals("field")) {
	    String name = null;
	    String fieldPattern = null;

	    Iterator<?> attributes = startElement.getAttributes();

	    while (attributes.hasNext()) {
		Attribute attribute = (Attribute) attributes.next();

		if (attribute.getName().getLocalPart().equals("name")) {
		    name = attribute.getValue();
		} else if (attribute.getName().getLocalPart().equals("pattern")) {
		    fieldPattern = attribute.getValue();
		}
	    }

	    // If has a pattern defined use it.
	    if (fieldPattern != null) {
		Pattern p = Pattern.compile(fieldPattern);
		Matcher m = p.matcher(lineValue);

		if (m.find()) {
		    setFieldValue(invoice, name, m.group());
		}
	    } else {
		if (tokenizer.hasMoreTokens()) {
		    String token = tokenizer.nextToken();

		    if (name != null) {
			setFieldValue(invoice, name, token);
			name = null;
		    }
		}
	    }
	} else if (startElement.getName().getLocalPart().equals("iterator")) {
	    pattern = null;

	    Iterator<?> attributes = startElement.getAttributes();

	    while (attributes.hasNext()) {
		Attribute attribute = (Attribute) attributes.next();

		if (attribute.getName().getLocalPart().equals("pattern")) {
		    pattern = attribute.getValue();
		}
	    }

	    if (pattern != null) {
		iterator = true;
	    }
	}
    }

    /**
     * Process the EndElement
     * 
     * @param endElement End element.
     * @param bufferedReader
     */
    private static void processEndElement(EndElement endElement, BufferedReader bufferedReader) throws IOException {
	if (endElement.getName().getLocalPart().equals("iterator")) {

	    // Process the event queue.
	    while (iterator) {
		Iterator<XMLEvent> eventIterator = eventQueue.iterator();

		while (eventIterator.hasNext()) {
		    XMLEvent event = eventIterator.next();

		    if (event.isStartElement()) {
			StartElement startElement = event.asStartElement();

			processStartElement(startElement, bufferedReader);
		    }
		}

		itemPos++;
	    }
	}
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
	case "telephone":
	    invoice.setTelephone(fieldValue);

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
	case "item_id":
	    InvoiceItem item = null;
	    int curItemIndex = -1;

	    for (int i = 0; i < invoice.getItems().size(); i++) {
		if (invoice.getItems().get(i).getListPos() == itemPos) {
		    item = invoice.getItems().get(i);
		    curItemIndex = i;

		    break;
		}
	    }

	    if (curItemIndex < 0) {
		item = new InvoiceItem(itemPos);
	    }

	    // item.setItemId(fieldValue);

	    if (curItemIndex < 0) {
		invoice.getItems().add(item);
	    } else {
		invoice.getItems().set(curItemIndex, item);
	    }

	    break;
	case "item_qty":
	    item = null;
	    curItemIndex = -1;

	    for (int i = 0; i < invoice.getItems().size(); i++) {
		if (invoice.getItems().get(i).getListPos() == itemPos) {
		    item = invoice.getItems().get(i);
		    curItemIndex = i;

		    break;
		}
	    }

	    if (curItemIndex < 0) {
		item = new InvoiceItem(itemPos);
	    }

	    if (NumberUtils.isNumber(fieldValue)) {
		item.setQty(Integer.parseInt(fieldValue));
	    } else {
		item.setQty(1);
	    }

	    if (curItemIndex < 0) {
		invoice.getItems().add(item);
	    } else {
		invoice.getItems().set(curItemIndex, item);
	    }

	    break;
	case "item_price":
	    item = null;
	    curItemIndex = -1;

	    for (int i = 0; i < invoice.getItems().size(); i++) {
		if (invoice.getItems().get(i).getListPos() == itemPos) {
		    item = invoice.getItems().get(i);
		    curItemIndex = i;

		    break;
		}
	    }

	    if (curItemIndex < 0) {
		item = new InvoiceItem(itemPos);
	    }

	    item.setPrice(Float.parseFloat(fieldValue));

	    if (curItemIndex < 0) {
		invoice.getItems().add(item);
	    } else {
		invoice.getItems().set(curItemIndex, item);
	    }

	    break;
	case "total_price":
	    item = null;
	    curItemIndex = -1;

	    for (int i = 0; i < invoice.getItems().size(); i++) {
		if (invoice.getItems().get(i).getListPos() == itemPos) {
		    item = invoice.getItems().get(i);
		    curItemIndex = i;

		    break;
		}
	    }

	    if (curItemIndex < 0) {
		item = new InvoiceItem(itemPos);
	    }

	    if (NumberUtils.isNumber(fieldValue)) {
		item.setTotalPrice(Float.parseFloat(fieldValue));
	    } else {
		item.setTotalPrice(0.00f);
	    }

	    if (curItemIndex < 0) {
		invoice.getItems().add(item);
	    } else {
		invoice.getItems().set(curItemIndex, item);
	    }

	    break;
	case "item_desc":
	    item = null;
	    curItemIndex = -1;

	    for (int i = 0; i < invoice.getItems().size(); i++) {
		if (invoice.getItems().get(i).getListPos() == itemPos) {
		    item = invoice.getItems().get(i);
		    curItemIndex = i;

		    break;
		}
	    }

	    if (curItemIndex < 0) {
		item = new InvoiceItem(itemPos);
	    }

	    item.setItemName(fieldValue);

	    if (curItemIndex < 0) {
		invoice.getItems().add(item);
	    } else {
		invoice.getItems().set(curItemIndex, item);
	    }

	    break;
	case "invoice_total":
	    if (NumberUtils.isNumber(fieldValue)) {
		invoice.setTotal(Float.parseFloat(fieldValue));
	    } else {
		invoice.setTotal(0.00f);
	    }

	    break;
	}

	return invoice;
    }
}