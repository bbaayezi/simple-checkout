package com.robin.checkoutSystem.abstractClass;

import com.robin.checkoutSystem.DealBundle;
import com.robin.checkoutSystem.ShoppingCart;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

public abstract class AbstractDeal {
    private Map<String, Integer> rule;
    private Double price;

    public abstract List<DealBundle> makeDeal(ShoppingCart cart);

    public abstract void checkRule(ShoppingCart cart) throws InvalidParameterException;

    public AbstractDeal(Map<String, Integer> rule, Double price) {
        this.rule = rule;
        this.price = price;
    }

    public Map<String, Integer> getRule() {
        return rule;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setRule(Map<String, Integer> rule) {
        this.rule = rule;
    }
}
