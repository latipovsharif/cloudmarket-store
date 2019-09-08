package com.vvmarkets.dao;

import com.vvmarkets.Main;
import com.vvmarkets.utils.db;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private static final Logger log = LogManager.getLogger(Main.class);
    private String Id;

    public String getId() {
        return Id;
    }

    public static List<Product> GetProducts() {
        List<Product> products = new ArrayList<Product>();
        Statement stmt = null;
        Connection connection = null;

        connection = db.getConnection();
        try{
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select * from products");
            while (rs.next()) {
                Product product = new Product();
                product.Id = rs.getString("id");
                products.add(product);
            }
        } catch (Exception e) {
            log.debug(e.getClass().getName() + ": " + e.getMessage());
        }

        return products;
    }
}
