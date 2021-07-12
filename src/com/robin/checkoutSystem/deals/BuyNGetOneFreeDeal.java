package com.robin.checkoutSystem.deals;

import com.robin.checkoutSystem.DealBundle;
import com.robin.checkoutSystem.ShoppingCart;
import com.robin.checkoutSystem.abstractClass.AbstractDeal;

import java.security.InvalidParameterException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuyNGetOneFreeDeal extends AbstractDeal {
    private final Logger LOGGER = Logger.getLogger(this.getClass().getName());

    @Override
    public void checkRule(ShoppingCart cart) throws InvalidParameterException {
        Map<String, Integer> rule = this.getRule();
        Map<String, Integer> productQuantityMap = cart.getProductQuantityMap();
        // check if the skus in the rule is the subset of skus in the cart
        if (Collections.disjoint(productQuantityMap.keySet(), rule.keySet())) throw new InvalidParameterException();
        for (Map.Entry<String, Integer> entry : rule.entrySet()) {
            String sku = entry.getKey();
            if (productQuantityMap.get(sku) < rule.get(sku)) throw new InvalidParameterException();
        }
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
        for (String sku : rule.keySet()) {
            int numOfDeals = productQuantityMap.get(sku) / rule.get(sku);
            // get one for free
            String[] arr = new String[rule.get(sku) + 1];
            Arrays.fill(arr, sku);
            List<String> itemSKUs = new ArrayList<>(Arrays.asList(arr));
            // update dealBundles
            dealBundles = Collections.nCopies(numOfDeals, new DealBundle(itemSKUs, this.getPrice()));
            cart.updateProductQuantity(sku, cart.getProductQuantityMap().get(sku) - rule.get(sku) * numOfDeals);
        }
        return dealBundles;
    }

    public BuyNGetOneFreeDeal(Map<String, Integer> rule, Double price) {
        super(rule, price);
    }
}
