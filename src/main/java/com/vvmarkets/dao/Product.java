package com.vvmarkets.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vvmarkets.Main;
import com.vvmarkets.configs.Config;
import com.vvmarkets.configs.RemoteConfig;
import com.vvmarkets.core.HttpConnectionHolder;
import com.vvmarkets.errors.InvalidFormat;
import com.vvmarkets.errors.NotFound;
import com.vvmarkets.services.ProductService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.utils.ResponseBody;
import com.vvmarkets.utils.db;
import javafx.util.Pair;
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

        if (product == null) {
            throw new NotFound(String.format("cannot get product from db: %s", barcode));
        }

        return product;
    }

    public static Product getProduct(String barcode) throws Exception {
        var cleanedCode = getProductCodeFromBarcode(barcode);
        System.out.println(String.format("code is %s and quantity is %f",cleanedCode.getValue(), cleanedCode.getKey()));
        Product product = null;

        if (Config.getOfflineMode()) {
            product = getProductFromDb(cleanedCode.getValue());
            product.setQuantity(cleanedCode.getKey());
            return product;
        }

        if (HttpConnectionHolder.INSTANCE.shouldRetry()) {
            product = getProductFromNetByBarcode(cleanedCode.getValue());
        } else {
            product = getProductFromDb(cleanedCode.getValue());
        }

        product.setQuantity(cleanedCode.getKey());
        return product;
    }

    private static Pair<Double, String> getProductCodeFromBarcode(String barcode) throws InvalidFormat {
        var format = RemoteConfig.getConfig(RemoteConfig.ConfigType.PIECEMEAL, RemoteConfig.ConfigSubType.FORMAT);
        if (!format.matches("\\d{2}-\\d[C|W]-\\d[C|W]")) {
            throw new InvalidFormat("invalid server format from the server");
        }

        String[] fmt = format.split("-");
        if (!barcode.startsWith(fmt[0])) {
            return new Pair<>(1.0, barcode);
        }

        Double q = null;
        String c = null;

        int f = Integer.parseInt(fmt[1].substring(0, 1));
        int s = Integer.parseInt(fmt[2].substring(0, 1));

        String fs = barcode.substring(2, 2 + f);
        String ss = barcode.substring(2 + f, 2 + f + s);

        if (fmt[1].contains("C"))  {
            c = fs;
        } else if (fmt[1].contains("W")) {
            q = Double.parseDouble(fs);
        }

        if (fmt[2].contains("C")) {
            c = ss;
        } else if (fmt[2].contains("W")) {
            q = Double.parseDouble(ss);
        }

        if (q == null || c == null) {
            throw new InvalidFormat("invalid format no C's or no W's");
        }

        if (q < 0) {
            throw new InvalidFormat("quantity cannot be less than 0");
        }

        return new Pair<>(q / 100, c);
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
