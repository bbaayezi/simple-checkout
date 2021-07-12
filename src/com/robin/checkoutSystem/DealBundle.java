package com.robin.checkoutSystem;

import java.util.List;

public class DealBundle {
    private List<String> skus;
    private Double price;

    public DealBundle(List<String> skus, Double price) {
        this.skus = skus;
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public List<String> getSkus() {
        return skus;
    }
}
