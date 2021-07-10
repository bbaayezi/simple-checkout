package com.robin.checkoutSystem.tests;

import com.robin.checkoutSystem.ShoppingCart;
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
    }

    @Test
    public void checkoutWithNoDealRule() {
        ShoppingCart cart = new ShoppingCart(skus, productPrice);
        Double finalPrice = cart.checkout();
        System.out.println("Cart checkout price: " + finalPrice);
        assertEquals(Double.valueOf(1.35), finalPrice);
    }
}