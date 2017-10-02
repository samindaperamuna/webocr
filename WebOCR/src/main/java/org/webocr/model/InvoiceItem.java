package org.webocr.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem implements Serializable {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long itemId;

    @Column(name = "list_pos")
    private int listPos;

    @Column(name = "item_name")
    private String itemName;

    private int qty;
    private float price;

    @Column(name = "total_price")
    private float totalPrice;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Invoice invoice;

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
