package com.vvmarkets.services;

import com.vvmarkets.dao.Product;
import com.vvmarkets.utils.ResponseList;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.ArrayList;
import java.util.List;

public interface ProductService {
    @GET("products/product")
    Call<ResponseList<Product>> productList();
}
