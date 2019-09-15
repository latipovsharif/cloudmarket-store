package com.vvmarkets.dao;

import com.vvmarkets.Main;
import com.vvmarkets.errors.NotFound;
import com.vvmarkets.utils.db;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private static final Logger log = LogManager.getLogger(Main.class);
    private String id;
    private String article;
    private String barcode;

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public static ObservableList<Product> GetProducts() {
        ObservableList<Product> products = FXCollections.observableArrayList();
        Statement stmt = null;

        try (Connection connection = db.getConnection()){
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select id, article, barcode from products");
            while (rs.next()) {
                Product product = new Product();
                product.id = rs.getString("id");
                product.article = rs.getString("article");
                product.barcode = rs.getString("barcode");
                products.add(product);
            }
        } catch (Exception e) {
            log.debug(e.getClass().getName() + ": " + e.getMessage());
        }

        return products;
    }

    public static Product getProduct(String barcode) throws Exception {
        PreparedStatement stmt = null;
        Product product;

        try (Connection c = db.getConnection()) {
            stmt = c.prepareStatement("select id, article, barcode from products where barcode = ?");
            stmt.setString(1, barcode);
            ResultSet rs = stmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                throw new NotFound("Продукт не найден: " + barcode);
            }
            product = new Product();
            product.id = rs.getString("id");
            product.article = rs.getString("article");
            product.barcode = rs.getString("barcode");

        } catch (Exception e) {
            log.debug(e.getClass().getName() + ": " + e.getMessage());
            throw e;
        }

        return product;
    }
}
