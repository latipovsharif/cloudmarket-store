package com.vvmarkets.dao;

import com.vvmarkets.Main;
import com.vvmarkets.configs.Config;
import com.vvmarkets.core.Utils;
import com.vvmarkets.errors.NotFound;
import com.vvmarkets.presenters.MainPresenter;
import com.vvmarkets.requests.AuthorizationBody;
import com.vvmarkets.services.AuthorizationService;
import com.vvmarkets.services.ProductService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.utils.ResponseBody;
import com.vvmarkets.utils.db;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private static final Logger log = LogManager.getLogger(Main.class);
    private String id;
    private String article;
    private String barcode;

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    private double quantity;

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    private double price;

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
            ResultSet rs = stmt.executeQuery("select id, article, barcode, price from products");
            while (rs.next()) {
                Product product = new Product();
                product.id = rs.getString("id");
                product.article = rs.getString("article");
                product.barcode = rs.getString("barcode");
                product.price = rs.getDouble("price");
                products.add(product);
            }
        } catch (Exception e) {
            log.debug(e.getClass().getName() + ": " + e.getMessage());
        }

        return products;
    }

    public static Product getProduct(String barcode) throws Exception {

        getProductFromNetByBarcode(barcode);
        Product product;
        PreparedStatement stmt = null;

        try (Connection c = db.getConnection()) {
            stmt = c.prepareStatement("select id, article, barcode, price from products where barcode = ?");
            stmt.setString(1, barcode);
            ResultSet rs = stmt.executeQuery();
            if (!rs.isBeforeFirst()) {
                throw new NotFound("Продукт не найден: " + barcode);
            }
            product = new Product();
            product.id = rs.getString("id");
            product.article = rs.getString("article");
            product.barcode = rs.getString("barcode");
            product.price = rs.getDouble("price");

        } catch (Exception e) {
            log.debug(e.getClass().getName() + ": " + e.getMessage());
            throw e;
        }

        return product;
    }

    private static Product getProductFromNetByBarcode(String barcode) {
        ProductService productService = RestClient.getClient().create(ProductService.class);
        Call<ResponseBody<Product>> productCall = productService.productFromBarcode(barcode);
        productCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody<Product>> call, Response<ResponseBody<Product>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 0) {
                            log.info(response.body());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody<Product>> call, Throwable t) {
                log.error(t.getMessage());
            }
        });

        return null;
    }
}
