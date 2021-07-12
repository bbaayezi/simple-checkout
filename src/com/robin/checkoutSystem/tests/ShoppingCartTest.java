package com.robin.checkoutSystem.tests;

import com.robin.checkoutSystem.ShoppingCart;
import com.robin.checkoutSystem.abstractClass.AbstractDeal;
import com.robin.checkoutSystem.abstractClass.DealRule;
import com.robin.checkoutSystem.deals.BuyNGetOneFreeDeal;
import com.robin.checkoutSystem.deals.MultiPricedDeal;
import com.robin.checkoutSystem.interfaces.Deal;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class ShoppingCartTest {
    List<String> skus;
    Map<String, Double> productPrice;
    Map<String, Integer> rule;
    List<AbstractDeal> dealRules;

    @Before
    public void setUp() {
        skus = new ArrayList<>();
        skus.add("A");
        skus.add("B");
        skus.add("C");
        productPrice = new HashMap<>();
        productPrice.put("A", 0.5);
        productPrice.put("B", 0.6);
        productPrice.put("C", 0.25);
        productPrice.put("D", 1.5);
        productPrice.put("E", 2.0);
        rule = new HashMap<>();
        dealRules = new ArrayList<>();
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
        dealRules.clear();
        rule.clear();
        // this rule requires 2 A product
        rule.put("A", 2);
        // 2 for 0.65
        Double dealPrice = 0.65;
        MultiPricedDeal multiPricedDeal = new MultiPricedDeal(rule, dealPrice);
        dealRules.add(multiPricedDeal);

        ShoppingCart cart = new ShoppingCart(skus, productPrice);
        cart.setDealRules(dealRules);
        cart.addItem("A", "A");

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
        dealRules.clear();
        rule.clear();
        rule.put("A", 2);
        Double dealPrice = 0.65;

        MultiPricedDeal multiPricedDeal = new MultiPricedDeal(rule, dealPrice);
        dealRules.add(multiPricedDeal);

        ShoppingCart cart = new ShoppingCart(skus, productPrice);
        cart.setDealRules(dealRules);

        Double finalPrice = cart.checkout();
        System.out.println("Cart checkout price: " + finalPrice);
        assertEquals(Double.valueOf(1.35), finalPrice);
    }

    @Test
    public void checkoutWithBuyNGetOneFreeDeal() {
        dealRules.clear();
        rule.clear();
        rule.put("C", 3);
        Double dealPrice = 0.75;

        BuyNGetOneFreeDeal buyNGetOneFreeDeal = new BuyNGetOneFreeDeal(rule, dealPrice);
        dealRules.add(buyNGetOneFreeDeal);

        List<String> skus = new ArrayList<>(Arrays.asList("A", "B", "C", "C", "C"));
        ShoppingCart cart = new ShoppingCart(skus, productPrice);
        cart.setDealRules(dealRules);

        assertEquals(Double.valueOf(1.85), cart.checkout());
        // expect to have one more C at the shopping list
        assertEquals(4, Collections.frequency(cart.getShoppingList(), "C"));
    }

    @Test
    public void checkoutWithBuyNGetOneFreeDealButRequirementsNotMeet() {
        dealRules.clear();
        rule.clear();
        rule.put("C", 3);
        Double dealPrice = 0.75;

        BuyNGetOneFreeDeal buyNGetOneFreeDeal = new BuyNGetOneFreeDeal(rule, dealPrice);
        dealRules.add(buyNGetOneFreeDeal);

        List<String> skus = new ArrayList<>(Arrays.asList("A", "B", "C", "C"));
        ShoppingCart cart = new ShoppingCart(skus, productPrice);
        cart.setDealRules(dealRules);

        assertEquals(Double.valueOf(1.60), cart.checkout());
        assertEquals(2, Collections.frequency(cart.getShoppingList(), "C"));
    }
}