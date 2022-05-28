package com.example.store;

import com.google.firebase.firestore.Exclude;

public class Product {

    private String name;
    private int quantity;
    private int price;
    private String company;
    private String DocumentId;

    public Product(String name, int quantity, int price, String company) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.company = company;

    }

    public Product(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public Product() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
    @Exclude
    public String getDocumentId() {
        return DocumentId;
    }

    public void setDocumentId(String documentId) {
        DocumentId = documentId;
    }
}
