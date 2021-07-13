package com.robin.checkoutSystem.abstractClass;

import com.robin.checkoutSystem.DealBundle;
import com.robin.checkoutSystem.ShoppingCart;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

/**
 * This abstract class defines the common properties and abstract/non-abstract methods for a deal
 */
public abstract class AbstractDeal {
    // Rule map of the deal, the key indicates product SKU and the value indicates the quantity needed to make the deal
    private Map<String, Integer> rule;
    // The price of the deal
    private Double price;

    /**
     * This method will query and update the shopping cart to create deal bundles according to the rule
     * @param cart The shopping cart object
     * @return A list of Deal Bundles created
     */
    public abstract List<DealBundle> makeDeal(ShoppingCart cart);

    /**
     * This method will check if the deal rule can be applied to the products in the shopping cart
     * @param cart The shopping cart object
     * @throws InvalidParameterException Indicates the deal rule is invalid and no deal bundles can be created
     */
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
