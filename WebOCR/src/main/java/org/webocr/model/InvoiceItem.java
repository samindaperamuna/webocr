package org.webocr.model;

public class InvoiceItem {

    private String itemId;
    private String itemName;
    private int qty;
    private float price;
    private float totalPrice;

    public String getItemId() {
	return itemId;
    }

    public void setItemId(String itemId) {
	this.itemId = itemId;
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
