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
import javafx.scene.control.ListView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.*;

public class ListUtil {
    private static final Logger log = LogManager.getLogger(ListUtil.class);

    public static ListUtil INSTANCE = new ListUtil();

    private Map categorized = Collections.synchronizedMap(new HashMap<String, ObservableList<ProductProperties>>());

    public List<ProductCategory> getMain() {
        return main;
    }

    private List<ProductCategory> main = FXCollections.observableList(new ArrayList<>());

    public void fillMainListView(ListView<IListContent> listView) {
        if (main.size() == 0) {
            fillMain(listView);
        } else {
            listView.getItems().clear();
            listView.getItems().addAll(main);
        }
    }

    private void fillMain(ListView<IListContent> listView) {
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
                                if (listView != null) {
                                    listView.getItems().clear();
                                    listView.getItems().addAll(main);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody<List<ProductCategory>>> call, Throwable t) {
                if (!(t instanceof IOException)) {
                    DialogUtil.showErrorNotification(Utils.stackToString(t.getStackTrace()));
                }
            }
        });

    }

    public void syncFillMain() {
        fillMain(null);
    }

    public void fillForCategory(ListView<IListContent> listView, String category) {
        ObservableList<ProductProperties> list = (ObservableList<ProductProperties>)categorized.get(category);

        if (list == null) {
            list = FXCollections.observableList(new ArrayList<>());
            categorized.put(category, list);
        }

        if (list.size() == 0) {
            cashForCategory(listView, category);
        }

        listView.getItems().clear();
        listView.getItems().addAll(list);
    }

    private void cashForCategory(ListView<IListContent> listView, String category) {
        ObservableList<ProductProperties> list = (ObservableList<ProductProperties>)categorized.get(category);
        ProductProperties back = new ProductProperties();
        back.setName("НАЗАД");

        if (list.size() == 0) {
            list.add(back);
        }

        ProductService productService = RestClient.getClient().create(ProductService.class);
        Call<ResponseBody<List<ProductProperties>>> listProductForCategoryCall = productService.productForCategory(category);
        listProductForCategoryCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody<List<ProductProperties>>> call, Response<ResponseBody<List<ProductProperties>>> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getStatus() == 0) {
                                Platform.runLater(() -> {
                                    list.clear();
                                    list.add(back);
                                    list.addAll(response.body().getBody());

                                    if (listView != null) {
                                        listView.getItems().clear();
                                        listView.getItems().addAll(list);
                                    }
                                });
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody<List<ProductProperties>>> call, Throwable t) {
                if (!(t instanceof IOException)) {
                    DialogUtil.showErrorNotification(Utils.stackToString(t.getStackTrace()));
                }
            }
        });
    }

    public void syncCashForCategory(String category) {
        fillForCategory(null, category);
    }
}
