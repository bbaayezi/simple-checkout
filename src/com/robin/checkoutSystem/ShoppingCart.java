package com.robin.checkoutSystem;

import com.robin.checkoutSystem.abstractClass.AbstractDeal;

import java.util.*;

public class ShoppingCart {
    private Map<String, Double> productPrice;
    private Map<String, Integer> productQuantityMap;
    private List<AbstractDeal> dealRules;
    private List<DealBundle> allDeals;

    public Double checkout() {
        for (AbstractDeal dealRule : dealRules) {
            List<DealBundle> newDealBundle = dealRule.makeDeal(this);
            if (newDealBundle != null) {
                allDeals.addAll(newDealBundle);
            }
        }
        return calculatePrice();
    }

    private Double calculatePrice() {
        Double res = .0;
        // first calculate the price of all non-deal products
        for (Map.Entry<String, Integer> entry : productQuantityMap.entrySet()) {
            String sku = entry.getKey();
            res += productPrice.get(sku) * entry.getValue();
        }
        // if there are deal bundles, calculate the price of these deals
        if (!allDeals.isEmpty()) {
            for (DealBundle deal : allDeals) {
                res += deal.getPrice();
            }
        }
        return Math.round(res * 100.0) / 100.0;
    }

    public void setDealRules(List<AbstractDeal> deals) {
        this.dealRules = deals;
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
            addItem(sku);
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

    public List<String> getShoppingList() {
        List<String> res = new ArrayList<>();
        // append skus without deals
        for (Map.Entry<String, Integer> entry : productQuantityMap.entrySet()) {
            res.addAll(Collections.nCopies(entry.getValue(), entry.getKey()));
        }
        // append skus with deals in deal bundles
        for (DealBundle dealBundle : allDeals) {
            res.addAll(dealBundle.getSkus());
        }
        return res;
    }
}
