package com.robin.checkoutSystem.deals;

import com.robin.checkoutSystem.DealBundle;
import com.robin.checkoutSystem.ShoppingCart;
import com.robin.checkoutSystem.abstractClass.AbstractDeal;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MultiPricedDeal extends AbstractDeal {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    private Map.Entry<String, Integer> minEntry = null;

    @Override
    public void checkRule(ShoppingCart cart) throws InvalidParameterException {
        Map<String, Integer> rule = this.getRule();
        Map<String, Integer> productQuantityMap = cart.getProductQuantityMap();
        // check if the skus in the rule is the subset of skus in the cart
        if (!productQuantityMap.keySet().containsAll(rule.keySet())) throw new InvalidParameterException();
        for (Map.Entry<String, Integer> entry : rule.entrySet()) {
            if (minEntry == null || entry.getValue() < minEntry.getValue()) minEntry = entry;
            String sku = entry.getKey();
            if (rule.get(sku) > productQuantityMap.get(sku)) {
                // can't make a deal
                throw new InvalidParameterException();
            }
        }
        if (minEntry == null) throw new InvalidParameterException();
    }

    @Override
    public List<DealBundle> makeDeal(ShoppingCart cart) {
        // check rule first
        try {
            this.checkRule(cart);
        } catch (InvalidParameterException e) {
            // log
            LOGGER.log(Level.WARNING, "Cannot make a deal bundle according to the rule.");
            // return null
            return null;
        }
        Map<String, Integer> rule = this.getRule();
        Map<String, Integer> productQuantityMap = cart.getProductQuantityMap();
        // make a deal
        List<DealBundle> dealBundles = new ArrayList<>();
        assert minEntry != null;
        int numOfDeals = productQuantityMap.get(minEntry.getKey()) / minEntry.getValue();
        for (String sku : rule.keySet()) {
            String[] arr = new String[rule.get(sku)];
            Arrays.fill(arr, sku);
            List<String> itemSKUs = new ArrayList<>(Arrays.asList(arr));
            // update dealBundles
            dealBundles = Collections.nCopies(numOfDeals, new DealBundle(itemSKUs, this.getPrice()));
            cart.updateProductQuantity(sku, cart.getProductQuantityMap().get(sku) - rule.get(sku) * numOfDeals);
        }
        return dealBundles;
    }

    public MultiPricedDeal(Map<String, Integer> rule, Double price) {
        super(rule, price);
    }
}
