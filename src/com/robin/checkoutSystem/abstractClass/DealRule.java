package com.robin.checkoutSystem.abstractClass;

import com.robin.checkoutSystem.ShoppingCart;
import com.robin.checkoutSystem.interfaces.Deal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class DealRule {
    private Map<String, Integer> rule;
    private Double dealPrice;

    /**
     * The default makeDeal method is applicable for multi-priced deal and meal deal
     * @param cart The shopping cart object
     * @return
     */
    public Deal makeDeal(ShoppingCart cart) {
        Map<String, Integer> productQuantityMap = cart.getProductQuantityMap();
        if (productQuantityMap.keySet().containsAll(rule.keySet())) {
            Map.Entry<String, Integer> minEntry = null;
            for (Map.Entry<String, Integer> entry : rule.entrySet()) {
                if (minEntry == null || entry.getValue() < minEntry.getValue()) minEntry = entry;
                String sku = entry.getKey();
                if (rule.get(sku) > productQuantityMap.get(sku)) {
                    // can't make a deal
                    return null;
                }
            }
            // make a deal
            int numOfDeals = productQuantityMap.get(minEntry.getKey()) / minEntry.getValue();
            List<String> itemSKUs = new ArrayList<>();
            for (String sku : rule.keySet()) {
                String[] arr = new String[rule.get(sku) * numOfDeals];
                Arrays.fill(arr, sku);
                itemSKUs.addAll(Arrays.asList(arr));
                cart.updateProductQuantity(sku, cart.getProductQuantityMap().get(sku) - rule.get(sku) * numOfDeals);
            }
            return new DefaultDeal(dealPrice, itemSKUs);
        }
        return null;
    }

    public DealRule(Map<String, Integer> rule, Double dealPrice) {
        this.rule = rule;
        this.dealPrice = dealPrice;
    }

    class DefaultDeal implements Deal {
        private Double price;
        private List<String> itemSKUs;

        @Override
        public void setPrice(Double price) {
            this.price = price;
        }

        @Override
        public Double getPrice() {
            return this.price;
        }

        @Override
        public List<String> getItemSKUs() {
            return itemSKUs;
        }

        public DefaultDeal(Double price, List<String> itemSKUs) {
            this.price = price;
            this.itemSKUs = itemSKUs;
        }
    }
}
