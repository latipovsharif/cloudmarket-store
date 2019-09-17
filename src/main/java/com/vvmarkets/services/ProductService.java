package com.vvmarkets.services;

import com.vvmarkets.dao.Product;
import com.vvmarkets.utils.Response;
import com.vvmarkets.utils.ResponseBody;
import com.vvmarkets.utils.ResponseList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.ArrayList;
import java.util.List;

public interface ProductService {
    @GET("products/product")
    Call<ResponseList<Product>> productList();

    @GET("/api/v1/cashes/product/get/")
    Call<ResponseBody<Product>> productFromBarcode(@Query("barcode") String barcode);
}
