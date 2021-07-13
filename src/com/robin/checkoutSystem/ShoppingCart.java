package com.robin.checkoutSystem;

import com.robin.checkoutSystem.abstractClass.AbstractDeal;

import java.util.*;

public class ShoppingCart {
    // product price map
    private Map<String, Double> productPrice;
    // product quantity map for products that can't make a deal
    private Map<String, Integer> productQuantityMap;
    // a list of deal rules applicable for this shopping cart
    private List<AbstractDeal> dealRules;
    // a list of deal bundles
    private List<DealBundle> allDeals;

    /**
     * This method calculates the final price for products in the shopping cart
     * @return Final price
     */
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

    /**
     * This method is used to set the deal rules which will be used during the checkout process
     * @param deals A list of deal rules
     */
    public void setDealRules(List<AbstractDeal> deals) {
        this.dealRules = deals;
    }

    private void initProductQuantityMap(List<String> skus) {
        for (String sku: skus) {
            addItem(sku);
        }
    }

    /**
     * Adds an item to the shopping cart
     * @param sku Product SKU
     */
    public void addItem(String sku) {
        productQuantityMap.computeIfPresent(sku, (k, v) -> v = v + 1);
        productQuantityMap.putIfAbsent(sku, 1);
    }

    /**
     * Adds multiple items to the shopping cart
     * @param skus Product SKUs
     */
    public void addItem(String... skus) {
        for (String sku : skus) {
            addItem(sku);
        }
    }

    /**
     * Get the product quantity map for products that can't make a deal
     * @return Product quantity map
     */
    public Map<String, Integer> getProductQuantityMap() {
        return productQuantityMap;
    }

    /**
     * Updates a certain entry in the product quantity map
     * @param productSKU Product SKU
     * @param newQuantity New value
     */
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

    /**
     * Returns the full shopping list, which includes products that's in a deal bundle or product that's not in a deal
     * bundle
     * @return A list of product SKUs in the shopping cart
     */
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
