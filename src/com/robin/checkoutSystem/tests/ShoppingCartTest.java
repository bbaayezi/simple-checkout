package com.robin.checkoutSystem.tests;

import com.robin.checkoutSystem.ShoppingCart;
import com.robin.checkoutSystem.abstractClass.DealRule;
import com.robin.checkoutSystem.interfaces.Deal;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class ShoppingCartTest {
    List<String> skus;
    Map<String, Double> productPrice;
    Map<String, Integer> rule;

    @Before
    public void setUp() throws Exception {
        skus = new ArrayList<>();
        skus.add("A");
        skus.add("B");
        skus.add("C");
        productPrice = new HashMap<>();
        productPrice.put("A", 0.5);
        productPrice.put("B", 0.6);
        productPrice.put("C", 0.25);
        rule = new HashMap<>();
    }

    @Test
    public void checkoutWithNoDealRule() {
        ShoppingCart cart = new ShoppingCart(skus, productPrice);
        Double finalPrice = cart.checkout();
        System.out.println("Cart checkout price: " + finalPrice);
        assertEquals(Double.valueOf(1.35), finalPrice);
    }

    @Test
    public void checkoutWithMultiPricedDeal() {
        rule.clear();
        // this rule requires 2 A product
        rule.put("A", 2);
        // 2 for 0.65
        Double dealPrice = 0.65;
        DealRule multiPricedDealRule = new DealRule(rule, dealPrice) {
            @Override
            public Deal makeDeal(ShoppingCart cart) {
                return super.makeDeal(cart);
            }
        };
        List<DealRule> dealRules = new ArrayList<>();
        dealRules.add(multiPricedDealRule);
        ShoppingCart cart = new ShoppingCart(skus, productPrice);
        cart.addItem("A", "A");
        cart.setDealRules(dealRules);
        Double finalPrice = cart.checkout();
        System.out.println("Cart checkout price: " + finalPrice);
        assertEquals(Double.valueOf(2.0), finalPrice);
        cart.addItem("A", "A");
        finalPrice = cart.checkout();
        System.out.println("Cart checkout price: " + finalPrice);
        assertEquals(Double.valueOf(2.65), finalPrice);
    }

    @Test
    public void checkoutWithMultiPricedDealButRequirementsNotMeet() {
        rule.clear();
        rule.put("A", 2);
        Double dealPrice = 0.65;

        DealRule multiPricedDealRule = new DealRule(rule, dealPrice) {
            @Override
            public Deal makeDeal(ShoppingCart cart) {
                return super.makeDeal(cart);
            }
        };
        List<DealRule> dealRules = new ArrayList<>();
        dealRules.add(multiPricedDealRule);
        ShoppingCart cart = new ShoppingCart(skus, productPrice);
        cart.setDealRules(dealRules);
        Double finalPrice = cart.checkout();
        System.out.println("Cart checkout price: " + finalPrice);
        assertEquals(Double.valueOf(1.35), finalPrice);
    }
}