package com.robin.checkoutSystem;

import com.robin.checkoutSystem.abstractClass.DealRule;
import com.robin.checkoutSystem.interfaces.Deal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
    private Map<String, Double> productPrice;
    private Map<String, Integer> productQuantityMap;
    private List<DealRule> dealRules;
    private List<Deal> allDeals;

    public Double checkout() {
        for (DealRule dealRule : dealRules) {
            Deal newDeal = dealRule.makeDeal(this);
            if (newDeal != null) allDeals.add(newDeal);
        }
        return calculatePrice();
    }

    private Double calculatePrice() {
        Double res = .0;
        // first calculate the price of all non-deal products
        for (Map.Entry<String, Integer> entry : productQuantityMap.entrySet()) {
            String sku = entry.getKey();
            res += productPrice.get(sku);
        }
        // if there are deal bundles, calculate the price of these deals
        if (!allDeals.isEmpty()) {
            for (Deal deal : allDeals) {
                res += deal.getPrice();
            }
        }
        return res;
    }

    public void setDealRules(List<DealRule> dealRules) {
        this.dealRules = dealRules;
    }

    private void initProductQuantityMap(List<String> skus) {
        for (String sku: skus) {
            addItem(sku);
        }
    }

    public void addItem(String sku) {
        productQuantityMap.computeIfPresent(sku, (k, v) -> v = v + 1);
        productQuantityMap.putIfAbsent(sku, 1);
    }

    public void addItem(String... skus) {
        for (String sku : skus) {
            productQuantityMap.computeIfPresent(sku, (k, v) -> v = v + 1);
            productQuantityMap.putIfAbsent(sku, 1);
        }
    }

    public Map<String, Integer> getProductQuantityMap() {
        return productQuantityMap;
    }

    public void updateProductQuantity(String productSKU, Integer newQuantity) {
        if (newQuantity == 0) {
            productQuantityMap.remove(productSKU);
            return;
        }
        productQuantityMap.put(productSKU, newQuantity);
    }

    public ShoppingCart(List<String> skus, Map<String, Double> productPrice) {
        this.productPrice = productPrice;
        productQuantityMap = new HashMap<>();
        allDeals = new ArrayList<>();
        dealRules = new ArrayList<>();
        initProductQuantityMap(skus);
    }


}
