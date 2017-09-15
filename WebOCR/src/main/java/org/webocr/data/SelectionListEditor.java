package org.webocr.data;

import java.beans.PropertyEditorSupport;
import java.io.IOException;

import org.webocr.model.SelectionData;
import org.webocr.model.SelectionList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SelectionListEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
	ObjectMapper mapper = new ObjectMapper();

	SelectionList collection = null;

	try {
	    JsonNode root = mapper.readTree(text).get("coords");

	    if (root.isArray()) {
		collection = new SelectionList();

		for (final JsonNode node : root) {
		    SelectionData data = new SelectionData();
		    data.setX(node.path("x").asInt());
		    data.setY(node.path("y").asInt());
		    data.setWidth(node.path("width").asInt());
		    data.setHeight(node.path("height").asInt());

		    collection.getSelectionList().add(data);
		}
	    }

	} catch (IOException e) {
	    System.err.println("Cannot map the JSON Object.\n" + e.getLocalizedMessage());
	}

	setValue(collection);
    }
}
