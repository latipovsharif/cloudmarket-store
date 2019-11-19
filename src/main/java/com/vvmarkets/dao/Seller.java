package com.vvmarkets.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Seller {
    @SerializedName("id")
    @Expose
    private String Id;
    @SerializedName("full_name")
    @Expose
    private String FullName;
    @SerializedName("email")
    @Expose
    private String Email;
    @SerializedName("is_seller")
    @Expose
    private Boolean IsSeller;
    @SerializedName("can_sell")
    @Expose
    private Boolean CanSell;
}
