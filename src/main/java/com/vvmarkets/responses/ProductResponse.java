package com.vvmarkets.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vvmarkets.core.ListUtil;
import com.vvmarkets.core.Utils;
import com.vvmarkets.utils.db;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class ProductResponse {
    private static final Logger log = LogManager.getLogger(ListUtil.class);

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public String getArticle() {
        return Article;
    }

    public void setArticle(String article) {
        Article = article;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getSellPrice() {
        return SellPrice;
    }

    public void setSellPrice(double sellPrice) {
        SellPrice = sellPrice;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("name")
    @Expose
    private String Name;

    @SerializedName("barcode")
    @Expose
    private String Barcode;

    @SerializedName("origin")
    @Expose
    private String Origin;

    @SerializedName("article")
    @Expose
    private String Article;

    @SerializedName("description")
    @Expose
    private String Description;

    @SerializedName("sell_price")
    @Expose
    private double SellPrice;

    @SerializedName("discount")
    @Expose
    private double Discount;


    public static void Update(List<ProductResponse> products) {
        String sql = "replace into products(id, name, barcode, article, origin, description, price, discount) values (?, ?, ?, ?, ?, ?, ?, ?)";
        try(Connection c = db.getConnection()) {
            PreparedStatement ps = c.prepareStatement(sql);

            for (ProductResponse p: products) {
                ps.setString(1, p.getId());
                ps.setString(2, p.getName());
                ps.setString(3, p.getBarcode());
                ps.setString(4, p.getArticle());
                ps.setString(5, p.getOrigin());
                ps.setString(6, p.getDescription());
                ps.setDouble(7, p.getSellPrice());
                ps.setDouble(8, p.getDiscount());
                ps.addBatch();
            }

            ps.executeBatch();
            ps.close();
        } catch (Exception e) {
            Utils.logException(e, "cannot insert batch of products");
        }
    }
}
