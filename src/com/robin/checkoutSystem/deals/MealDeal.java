package com.robin.checkoutSystem.deals;

import java.util.Map;

/**
 * Meal deal share the same deal rule as the multi priced deal
 */
public class MealDeal extends MultiPricedDeal{
    public MealDeal(Map<String, Integer> rule, Double price) {
        super(rule, price);
    }
}
