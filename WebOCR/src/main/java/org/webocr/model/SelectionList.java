package org.webocr.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectionList implements Serializable {

    private List<SelectionData> selectionList;

    public SelectionList() {
	this.selectionList = new ArrayList<SelectionData>();
    }

    public List<SelectionData> getSelectionList() {
	return selectionList;
    }

    public void setSelectionList(List<SelectionData> selectionList) {
	this.selectionList = selectionList;
    }
}
