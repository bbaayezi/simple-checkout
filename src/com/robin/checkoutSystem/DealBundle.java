package com.robin.checkoutSystem;

import java.util.List;

/**
 * A deal bundle consists of a list of product SKUs and a deal price
 */
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
