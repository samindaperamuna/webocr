package org.webocr.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Invoice implements Serializable {

    String companyName;
    String address;
    String type;
    String telephone;
    String fax;
    String email;
    String comment;
    String date;
    String time;
    String invoiceNumber;
    int totalItems;
    float total;
    float discount;
    float payment;
    float cash;
    float change;
    List<InvoiceItem> items;

    public Invoice() {
	this.items = new ArrayList<>();
    }

    public String getCompanyName() {
	return companyName;
    }

    public void setCompanyName(String companyName) {
	this.companyName = companyName;
    }

    public String getAddress() {
	return address;
    }

    public void setAddress(String address) {
	this.address = address;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getTelephone() {
	return telephone;
    }

    public void setTelephone(String telephone) {
	this.telephone = telephone;
    }

    public String getFax() {
	return fax;
    }

    public void setFax(String fax) {
	this.fax = fax;
    }

    public String getEmail() {
	return email;
    }

    public void setEmail(String email) {
	this.email = email;
    }

    public String getComment() {
	return comment;
    }

    public void setComment(String comment) {
	this.comment = comment;
    }

    public String getDate() {
	return date;
    }

    public void setDate(String date) {
	this.date = date;
    }

    public String getTime() {
	return time;
    }

    public void setTime(String time) {
	this.time = time;
    }

    public List<InvoiceItem> getItems() {
	return items;
    }

    public void setInvoiceNumber(String invoiceNumber) {
	this.invoiceNumber = invoiceNumber;
    }

    public int getTotalItems() {
	return totalItems;
    }

    public void setTotalItems(int totalItems) {
	this.totalItems = totalItems;
    }

    public float getTotal() {
	return total;
    }

    public void setTotal(float total) {
	this.total = total;
    }

    public float getDiscount() {
	return discount;
    }

    public void setDiscount(float discount) {
	this.discount = discount;
    }

    public float getPayment() {
	return payment;
    }

    public void setPayment(float payment) {
	this.payment = payment;
    }

    public float getCash() {
	return cash;
    }

    public void setCash(float cash) {
	this.cash = cash;
    }

    public float getChange() {
	return change;
    }

    public void setChange(float change) {
	this.change = change;
    }

    public void setItems(List<InvoiceItem> items) {
	this.items = items;
    }

    public String getInvoiceNumber() {
	return invoiceNumber;
    }
}