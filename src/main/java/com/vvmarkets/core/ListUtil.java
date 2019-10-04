package com.vvmarkets.core;

import com.vvmarkets.dao.ProductCategory;
import com.vvmarkets.dao.ProductProperties;
import com.vvmarkets.services.CategoryService;
import com.vvmarkets.services.ProductService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.utils.ResponseBody;
import javafx.geometry.Orientation;
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class ListUtil {
    private static final Logger log = LogManager.getLogger(ListUtil.class);


    public static void fillCategoryLisView(ListView<IListContent> productCategoryListView) {
        
        productCategoryListView.setOrientation(Orientation.HORIZONTAL);

        CategoryService categoryService = RestClient.getClient().create(CategoryService.class);
        Call<ResponseBody<List<ProductCategory>>> listCategoryCall = categoryService.categoryList();
        
        listCategoryCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody<List<ProductCategory>>> call, Response<ResponseBody<List<ProductCategory>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 0) {
                            for (ProductCategory pp :
                                    response.body().getBody()) {
                                productCategoryListView.getItems().add(pp);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody<List<ProductCategory>>> call, Throwable t) {
                log.error(t.getMessage());
            }
        });
    }
    
    public static void fillProductList(ListView<IListContent> productPropertiesListView, String categoryID) {
        productPropertiesListView.setOrientation(Orientation.HORIZONTAL);

        ProductService productService = RestClient.getClient().create(ProductService.class);
        Call<ResponseBody<List<ProductProperties>>> listProductForCategoryCall = productService.productForCategory(categoryID);
        listProductForCategoryCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody<List<ProductProperties>>> call, Response<ResponseBody<List<ProductProperties>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 0) {
                            for (ProductProperties pp :
                                    response.body().getBody()) {
                                productPropertiesListView.getItems().add(pp);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody<List<ProductProperties>>> call, Throwable t) {
                log.error(t.getMessage());
            }
        });
    }
}
