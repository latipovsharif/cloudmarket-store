package com.vvmarkets.core;

import com.vvmarkets.configs.Config;
import com.vvmarkets.controllers.LogInController;
import com.vvmarkets.dao.Authorization;
import com.vvmarkets.dao.ProductCategory;
import com.vvmarkets.dao.ProductProperties;
import com.vvmarkets.presenters.MainPresenter;
import com.vvmarkets.services.CategoryService;
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


    public static ListView<ProductCategory> getCategoryList() {
        ListView<ProductCategory> productPropertiesListView = new ListView<>();
        productPropertiesListView.setOrientation(Orientation.HORIZONTAL);

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
                                productPropertiesListView.getItems().add(pp);
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

        return productPropertiesListView;
    }
}
