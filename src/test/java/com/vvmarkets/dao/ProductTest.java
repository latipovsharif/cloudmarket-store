package com.vvmarkets.dao;

import javafx.util.Pair;
import junit.framework.TestCase;

public class ProductTest extends TestCase {

    public void testGetProductCodeFromBarcode() {
        try {
            Pair<Double, String> p = Product.getProductCodeFromBarcode("1303437014855", "13-5C-5W");
            assertEquals("Should be equals", 1.485, p.getKey());
            assertEquals("Should be equals", "3437", p.getValue());
            p = Product.getProductCodeFromBarcode("1303437014855", "20-5C-5W");
            assertEquals("Should be equals", 1.0, p.getKey());
            assertEquals("Should be equals", "1303437014855", p.getValue());
        } catch (Exception e) {
          assertNull("Should not throw exception", e);
        }
    }
}