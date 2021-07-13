package com.robin.checkoutSystem.tests;

import com.robin.checkoutSystem.ShoppingCart;
import com.robin.checkoutSystem.abstractClass.AbstractDeal;
import com.robin.checkoutSystem.abstractClass.DealRule;
import com.robin.checkoutSystem.deals.BuyNGetOneFreeDeal;
import com.robin.checkoutSystem.deals.MealDeal;
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
        assertEquals(Double.valueOf(2.0), finalPrice);

        cart.addItem("A", "A");
        finalPrice = cart.checkout();
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

    @Test
    public void checkoutWithMealDeal() {
        dealRules.clear();
        rule.clear();
        rule.put("D", 1);
        rule.put("E", 1);
        Double dealPrice = 3.25;

        MealDeal multiPricedDeal = new MealDeal(rule, dealPrice);
        dealRules.add(multiPricedDeal);

        List<String> skus = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E"));
        ShoppingCart cart = new ShoppingCart(skus, productPrice);
        cart.setDealRules(dealRules);

        assertEquals(Double.valueOf(4.60), cart.checkout());
    }

    @Test
    public void checkoutWithMealDealButRequirementsNotMeet() {
        dealRules.clear();
        rule.clear();
        rule.put("D", 1);
        rule.put("E", 1);
        Double dealPrice = 3.25;

        MealDeal mealDeal = new MealDeal(rule, dealPrice);
        dealRules.add(mealDeal);

        List<String> skus = new ArrayList<>(Arrays.asList("A", "B", "C", "D"));
        ShoppingCart cart = new ShoppingCart(skus, productPrice);
        cart.setDealRules(dealRules);

        assertEquals(Double.valueOf(2.85), cart.checkout());
    }

    @Test
    public void checkoutWithMultipleDeals() {
        List<AbstractDeal> dealRules = new ArrayList<>();

        Map<String, Integer> rule1 = new HashMap<>();
        rule1.put("A", 2);
        MultiPricedDeal multiPricedDeal = new MultiPricedDeal(rule1, 0.8);

        Map<String, Integer> rule2 = new HashMap<>();
        rule2.put("D", 1);
        rule2.put("E", 1);
        MealDeal meadDeal = new MealDeal(rule2, 3.25);

        Map<String, Integer> rule3 = new HashMap<>();
        rule3.put("C", 3);
        BuyNGetOneFreeDeal buyNGetOneFreeDeal = new BuyNGetOneFreeDeal(rule3, 0.75);

        List<String> skus = new ArrayList<>();
        skus.addAll(Collections.nCopies(5, "A"));
        skus.addAll(Collections.nCopies(2, "B"));
        skus.addAll(Collections.nCopies(6, "C"));
        skus.addAll(Collections.nCopies(4, "D"));
        skus.addAll(Collections.nCopies(5, "E"));
        ShoppingCart cart = new ShoppingCart(skus, productPrice);

        dealRules.add(multiPricedDeal);
        dealRules.add(meadDeal);
        dealRules.add(buyNGetOneFreeDeal);

        cart.setDealRules(dealRules);

        // (2 * 0.8 + 0.5) + (4 * 3.25 + 2.0) + 6 * 0.25
        assertEquals(Double.valueOf(19.8), cart.checkout());

        assertEquals(8, Collections.frequency(cart.getShoppingList(), "C"));
    }
}