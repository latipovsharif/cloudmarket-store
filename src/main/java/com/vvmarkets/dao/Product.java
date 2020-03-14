package com.vvmarkets.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vvmarkets.Main;
import com.vvmarkets.configs.Config;
import com.vvmarkets.core.HttpConnectionHolder;
import com.vvmarkets.core.Utils;
import com.vvmarkets.errors.NotFound;
import com.vvmarkets.services.ProductService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.utils.ResponseBody;
import com.vvmarkets.utils.db;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Response;

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

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Expose
    @SerializedName("discount")
    private int discount;


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


    private static Product getProductFromDb(String barcode) throws NotFound {
        Product product = null;
        PreparedStatement stmt = null;

        try (Connection connection = db.getConnection()) {
            stmt = connection.prepareStatement("select id, name, barcode, article, origin, description, price, discount from products where barcode = ?");
            stmt.setString(1, barcode);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                product = new Product();
                product.productProperties = new ProductProperties();
                product.productProperties.setId(rs.getString("id"));
                product.productProperties.setName(rs.getString("name"));
                product.productProperties.setBarcode(rs.getString("barcode"));
                product.productProperties.setArticle(rs.getString("article"));
                product.productProperties.setOrigin(rs.getString("origin"));
                product.productProperties.setDescription(rs.getString("description"));
                product.price = rs.getDouble("price");
                product.discount = rs.getInt("discount");
            }
        } catch (Exception e) {
            throw new NotFound(String.format("cannot get product from db: %s, exc: %s", barcode, e.getMessage()));
        }

        return product;
    }

    public static Product getProduct(String barcode) throws Exception {

        if (Config.getOfflineMode()) {
            return getProductFromDb(barcode);
        }

        Product product = null;
        if (HttpConnectionHolder.INSTANCE.shouldRetry()) {
            product = getProductFromNetByBarcode(barcode);
        } else {
            product = getProductFromDb(barcode);
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
