package com.vvmarkets.services;

import com.vvmarkets.dao.Seller;
import com.vvmarkets.utils.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface SellerService {
    @GET("/api/v2/cashes/seller/")
    Call<ResponseBody<List<Seller>>> sellerList();
}
