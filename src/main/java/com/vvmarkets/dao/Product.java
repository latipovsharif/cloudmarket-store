package com.vvmarkets.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vvmarkets.Main;
import com.vvmarkets.errors.NotFound;
import com.vvmarkets.services.ProductService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.utils.ResponseBody;
import com.vvmarkets.utils.db;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.sql.*;

public class Product {
    private static final Logger log = LogManager.getLogger(Main.class);

    @SerializedName("id")
    @Expose
    private String id;

    @Expose
    @SerializedName("product")
    private ProductProperties productProperties;

    @Expose
    @SerializedName("quantity")
    private double quantity;

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Expose
    @SerializedName("discount")
    private double discount;


    @Expose
    @SerializedName("sell_price")
    private double price;

    private double total;

    public ProductProperties getProductProperties() {
        return productProperties;
    }

    public void setProductProperties(ProductProperties productProperties) {
        this.productProperties = productProperties;
    }

    public double getTotal() {
        return quantity * price - (quantity * price * discount / 100);
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

        try (Connection connection = db.getConnection()) {
            stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("select id, price from products");
            while (rs.next()) {
                Product product = new Product();
                product.id = rs.getString("id");
                product.price = rs.getDouble("price");
                products.add(product);
            }
        } catch (Exception e) {
            log.debug(e.getClass().getName() + ": " + e.getMessage());
        }

        return products;
    }

    public static Product getProduct(String barcode) throws Exception {
        Product product = null;

        try{
            product = getProductFromNetByBarcode(barcode);
        } catch (IOException io) {

        } catch (NotFound notFound) {

        }

        if (product == null) {
            throw new NotFound("product with barcode:" + barcode + " not found");
        }
        return product;
    }

    private static Product getProductFromNetByBarcode(String barcode) throws Exception {
        Product product = null;

        ProductService productService = RestClient.getClient().create(ProductService.class);
        Call<ResponseBody<Product>> productCall = productService.productFromBarcode(barcode);

        Response<ResponseBody<Product>> response = productCall.execute();
        if (response.isSuccessful()) {
            if (response.body() != null) {
                if (response.body().getStatus() == 0) {
                    product = response.body().getBody();
                }
            }
        } else {
            throw new NotFound("Product with barcode: " + barcode + "was not found in the server");
        }

        return product;
    }
}
