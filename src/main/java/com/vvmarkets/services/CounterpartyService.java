package com.vvmarkets.services;

import com.vvmarkets.dao.Client;
import com.vvmarkets.utils.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface CounterpartyService {
    @GET("/api/v1/cashes/counterparty/")
    Call<List<Client>> counterpartyList(@Query("q") String query);

    @GET("/api/v1/cashes/product/get/")
    Call<ResponseBody<Client>> getCounterparty(@Query("barcode") String barcode);

    @POST("/api/v1/cashes/product/search/")
    Call<ResponseBody<Void>> counterpartyCreate(@Body Client counterparty);

}
