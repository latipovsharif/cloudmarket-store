package com.vvmarkets.services;

import com.vvmarkets.dao.ProductCategory;
import com.vvmarkets.dao.ProductProperties;
import com.vvmarkets.utils.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface CategoryService {
    @GET("/api/v1/cashes/category/show-on-cash/")
    Call<ResponseBody<List<ProductCategory>>> categoryList();

    @GET("/api/v1/cashes/product/category")
    Call<ResponseBody<ProductProperties>> productsFromCategory(@Query("category") String category);
}
