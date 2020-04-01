package com.vvmarkets.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductBody {
    @SerializedName("sell_price")
    @Expose
    private Double SellPrice;

    @SerializedName("product_id")
    @Expose
    private String ProductId;

    @SerializedName("quantity")
    @Expose
    private Double Quantity;

    @SerializedName("discount_percent")
    @Expose
    private int DiscountPercent;


    transient private String Name;

    public Double getSellPrice() {
        return SellPrice;
    }

    public void setSellPrice(Double sellPrice) {
        SellPrice = sellPrice;
    }

    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public Double getQuantity() {
        return Quantity;
    }

    public void setQuantity(Double quantity) {
        Quantity = quantity;
    }

    public int getDiscountPercent() {
        return DiscountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        DiscountPercent = discountPercent;
    }

    public String getName() {
        return Name;
    }

    public String getShortName() {
        if (Name.length() > 10) {
            return Name.substring(0, 9);
        }

        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getTotal() {
        return getQuantity() * getSellPrice() - (getQuantity() * getSellPrice() * getDiscountPercent() / 100);
    }
}
