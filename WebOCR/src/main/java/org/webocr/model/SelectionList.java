package org.webocr.model;

import java.util.ArrayList;
import java.util.List;

public class SelectionList {

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
