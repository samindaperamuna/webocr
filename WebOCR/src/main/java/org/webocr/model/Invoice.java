package org.webocr.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "invoice")
public class Invoice implements Serializable {

    @Id
    @Column(name = "invoice_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    long invoiceId;

    @Lob
    @Column(length = 100000)
    byte[] image;

    @Column(name = "company_name")
    String companyName;

    String address;
    String type;
    String telephone;
    String fax;
    String email;

    @Column(name = "invoice_comment")
    String invoiceComment;

    @Column(name = "invoice_date")
    String invoiceDate;

    @Column(name = "invoice_time")
    String invoiceTime;

    @Column(name = "invoice_number")
    String invoiceNumber;

    @Column(name = "total_items")
    int totalItems;

    float total;
    float discount;
    float payment;
    float cash;

    @Column(name = "invoice_change")
    float invoiceChange;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "invoice")
    List<InvoiceItem> items;

    public Invoice() {
	this.items = new ArrayList<>();
    }

    public long getInvoiceId() {
	return invoiceId;
    }

    public byte[] getImage() {
	return image;
    }

    public void setImage(byte[] image) {
	this.image = image;
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

    public String getInvoiceComment() {
	return invoiceComment;
    }

    public void setInvoiceComment(String comment) {
	this.invoiceComment = comment;
    }

    public String getInvoiceDate() {
	return invoiceDate;
    }

    public void setInvoiceDate(String date) {
	this.invoiceDate = date;
    }

    public String getInvoiceTime() {
	return invoiceTime;
    }

    public void setInvoiceTime(String time) {
	this.invoiceTime = time;
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

    public float getInvoiceChange() {
	return invoiceChange;
    }

    public void setInvoiceChange(float change) {
	this.invoiceChange = change;
    }

    public void setItems(List<InvoiceItem> items) {
	this.items = items;
    }

    public String getInvoiceNumber() {
	return invoiceNumber;
    }
}