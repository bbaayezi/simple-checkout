package com.robin.checkoutSystem.interfaces;

import com.robin.checkoutSystem.Item;

import java.util.List;

public interface Deal {
    void setPrice(Double price);
    Double getPrice();
    List<String> getItemSKUs();
}
