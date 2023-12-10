package com.unipi.vsmyris.mscjavaproject2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

public class Product {
    private int number;
    private String productCode;
    private String title;
    private long timestamp;
    private float price;
    private String description;
    private String category;
    private int previousEntryNumber;

    public Product(int number, String productCode, String title, float price, String description, String category) {
        this.number = 0;
        this.productCode = productCode;
        this.title = title;
        this.price = price;
        this.description = description;
        this.category = category;
        this.previousEntryNumber = 0;

        this.timestamp = new Date().getTime();
    }

    public int getNumber() {
        return number;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getTitle() {
        return title;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public float getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public int getPreviousEntryNumber() {
        return previousEntryNumber;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setPreviousEntryNumber(int previousEntryNumber) {
        this.previousEntryNumber = previousEntryNumber;
    }
}
