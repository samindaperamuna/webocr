package org.webocr.model;

import java.io.Serializable;

public class InvoiceItem implements Serializable {

    private int listPos;
    private String itemName;
    private int qty;
    private float price;
    private float totalPrice;

    public InvoiceItem() {

    }

    public InvoiceItem(int listPos) {
	this.listPos = listPos;
    }

    public int getListPos() {
	return listPos;
    }

    public void setListPos(int listPos) {
	this.listPos = listPos;
    }

    public String getItemName() {
	return itemName;
    }

    public void setItemName(String itemName) {
	this.itemName = itemName;
    }

    public int getQty() {
	return qty;
    }

    public void setQty(int qty) {
	this.qty = qty;
    }

    public float getPrice() {
	return price;
    }

    public void setPrice(float price) {
	this.price = price;
    }

    public float getTotalPrice() {
	return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
	this.totalPrice = totalPrice;
    }
}
