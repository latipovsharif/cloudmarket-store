package com.vvmarkets.sync;

import com.vvmarkets.Main;
import com.vvmarkets.configs.Config;
//import com.vvmarkets.core.IListContent;
//import com.vvmarkets.core.ListUtil;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.ProductUpdate;
import com.vvmarkets.requests.ExpenseBody;
import com.vvmarkets.responses.ExpenseResponse;
import com.vvmarkets.responses.ProductResponse;
import com.vvmarkets.responses.SettingResponse;
import com.vvmarkets.services.ProductService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.utils.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Synchronizer {
    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(0);

    public void startSync() {
        scheduler.scheduleAtFixedRate(Synchronizer::syncSold, 0, 10, TimeUnit.SECONDS);

        if (Config.getOfflineMode()) {
            scheduler.scheduleAtFixedRate(Synchronizer::syncProducts, 0, 10, TimeUnit.SECONDS);
        }

        SettingResponse.sync();
    }

    private static void syncSold() {
        try {
            List<ExpenseBody> body = new ExpenseBody().getUnfinished();
            for (ExpenseBody b : body) {
                if (b != null) {
                    ExpenseResponse response = b.SaveToNetwork();
                    if (response != null) {
                        b.deleteFromDB();
                    }
                }
            }
        } catch (Exception e) {
            Utils.logException(e, "cannot send unfinished sold");
        }
    }

    private static void syncProducts() {
        try {
            ProductService productService = RestClient.getClient().create(ProductService.class);

            long version = ProductUpdate.getCurrentVersion();
            Call<ResponseBody<List<Long>>> productVersions = productService.productVersion(version, Main.ApplicationVersion);
            Response<ResponseBody<List<Long>>> responseBodyCall = productVersions.execute();
            if (responseBodyCall.isSuccessful()) {
                if (responseBodyCall.body() != null && responseBodyCall.body().getBody() != null) {
                    List<Long> lst = responseBodyCall.body().getBody();
                    for (var l: lst) {
                        Call<ResponseBody<List<ProductResponse>>> productCall = productService.productList(l);

                        Response<ResponseBody<List<ProductResponse>>> response = productCall.execute();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                ProductResponse.Update(response.body().getBody());
                            }
                        }

                        ProductUpdate.setCurrentVersion(l);
                    }
                }
            }
        } catch (Exception e) {
            Utils.logException(e, "cannot sync products");
        }
    }

    public void stopSync() {
        System.out.println("shutting down now");
        scheduler.shutdown();
    }
}
