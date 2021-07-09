package com.robin.checkoutSystem.abstractClass;

import com.robin.checkoutSystem.ShoppingCart;
import com.robin.checkoutSystem.interfaces.Deal;

import java.util.List;
import java.util.Map;

public abstract class DealRule {
    private Map<String, Integer> rule;
    private Double dealPrice;

    public List<Deal> makeDeal(ShoppingCart cart) {
         return null;
    }

    public DealRule(Map<String, Integer> rule, Double dealPrice) {
        this.rule = rule;
        this.dealPrice = dealPrice;
    }
}
