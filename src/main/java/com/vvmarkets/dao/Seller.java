package com.vvmarkets.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.services.SellerService;
import com.vvmarkets.utils.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

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

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Boolean getSeller() {
        return IsSeller;
    }

    public void setSeller(Boolean seller) {
        IsSeller = seller;
    }

    public Boolean getCanSell() {
        return CanSell;
    }

    public void setCanSell(Boolean canSell) {
        CanSell = canSell;
    }

    @Override
    public String toString() {
        return this.FullName;
    }

    public static List<Seller> fillSeller() throws IOException {
        SellerService sellerService = RestClient.getClient().create(SellerService.class);
        Call<ResponseBody<List<Seller>>> sellerList = sellerService.sellerList();

        Response<ResponseBody<List<Seller>>> response = sellerList.execute();
        return response.body().getBody();
    }
}
