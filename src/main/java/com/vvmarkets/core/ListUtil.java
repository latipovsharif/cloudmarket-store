package com.vvmarkets.core;

import com.vvmarkets.dao.ProductCategory;
import com.vvmarkets.dao.ProductProperties;
import com.vvmarkets.services.CategoryService;
import com.vvmarkets.services.ProductService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.utils.ResponseBody;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.*;

public class ListUtil {
    public static ListUtil INSTANCE = new ListUtil();

    private Map categorized = Collections.synchronizedMap(new HashMap<String, ArrayList<IListContent>>());

    public ObservableList<IListContent> getMain() {
        return main;
    }

    private ObservableList<IListContent> main = FXCollections.observableList(new ArrayList<>());

    public ObservableList<IListContent> fillMain() {
        if (!main.isEmpty()) {
            CategoryService categoryService = RestClient.getClient().create(CategoryService.class);
            Call<ResponseBody<List<ProductCategory>>> listCategoryCall = categoryService.categoryList();

            listCategoryCall.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ResponseBody<List<ProductCategory>>> call, Response<ResponseBody<List<ProductCategory>>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 0) {
                                Platform.runLater(() -> {
                                    main.clear();
                                    main.addAll(response.body().getBody());
                                });
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody<List<ProductCategory>>> call, Throwable t) {
                    if (!(t instanceof IOException)) {
                        Utils.logException((Exception) t, "cannot fill main hot access list");
                    }
                }
            });
        }
        return main;
    }

    public static List<ProductCategory> fillMainSync() {
        try {
            CategoryService categoryService = RestClient.getClient().create(CategoryService.class);
            Response<ResponseBody<List<ProductCategory>>> response = categoryService.categoryList().execute();

            if (response.isSuccessful()) {
                if (response.body() != null) {
                    if (response.body().getStatus() == 0) {
                        INSTANCE.main.clear();
                        INSTANCE.main.addAll(response.body().getBody());
                        return response.body().getBody();
                    }
                }
            }
        } catch(Exception e) {
            Utils.logException(e, "cannot get sync version of main list");
        }

        return new ArrayList<>();
    }

    public void syncFillMain() {
        fillMain();
    }

    public List<? extends IListContent> listForCategory(String category, boolean forceUpdate) {

        ArrayList<IListContent> res = (ArrayList<IListContent>) categorized.get(category);
        if (res == null) {
            categorized.put(category, new ArrayList<IListContent>());
        }

        if (forceUpdate || res == null || res.isEmpty()) {
            ProductService productService = RestClient.getClient().create(ProductService.class);
            Call<ResponseBody<List<ProductProperties>>> listProductForCategoryCall = productService.productForCategory(category);
            try{
                Response<ResponseBody<List<ProductProperties>>> response = listProductForCategoryCall.execute();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 0) {
                            ((ArrayList<IListContent>) categorized.get(category)).clear();
                            categorized.put(category, response.body().getBody());
                            return response.body().getBody();
                        }
                    }
                }
            } catch (Exception e) {
                Utils.logException(e, "error while handling response of category for product list");
            }
        }
        return res;
    }

    public void syncCashForCategory(String category) {
        listForCategory(category, true);
    }
}
