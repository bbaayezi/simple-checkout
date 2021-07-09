package com.robin.checkoutSystem;

public class Item {
    private final String SKU;
    private final Double price;

    public Item(String SKU, Double price) {
        this.SKU = SKU;
        this.price = price;
    }

    public String getSKU() {
        return SKU;
    }

    public Double getPrice() {
        return price;
    }
}
