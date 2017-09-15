package org.webocr.model;

import java.util.ArrayList;
import java.util.List;

public class ImageData {

    private List<ImageSelection> selectionsList;
    private String base64Image;

    public ImageData() {
	this.selectionsList = new ArrayList<>();
    }

    public List<ImageSelection> getSelectionsList() {
	return selectionsList;
    }

    public void setSelectionsList(List<ImageSelection> selectionsList) {
	this.selectionsList = selectionsList;
    }

    public String getBase64Image() {
	return base64Image;
    }

    public void setBase64Image(String base64Image) {
	this.base64Image = base64Image;
    }
}
