package com.vvmarkets.services;

import com.vvmarkets.dao.Product;
import com.vvmarkets.dao.ProductProperties;
import com.vvmarkets.responses.ProductResponse;
import com.vvmarkets.utils.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface ProductService {
    @GET("/api/v1/cashes/product/update/{version}")
    Call<ResponseBody<List<ProductResponse>>> productList(@Path("version") Long version);

    @GET("/api/v1/cashes/product/versions/")
    Call<ResponseBody<List<Long>>> productVersion(@Query("version") Long version, @Query("app_version") String appVersion);

    @GET("/api/v1/cashes/product/get/")
    Call<ResponseBody<Product>> productFromBarcode(@Query("barcode") String barcode);
    
    @GET("/api/v1/cashes/product/category/")
    Call<ResponseBody<List<ProductProperties>>> productForCategory(@Query("category") String categoryID);

    @GET("/api/v1/cashes/product/search/")
    Call<ResponseBody<List<ProductProperties>>> productSearch(@Query("product") String product);

}
