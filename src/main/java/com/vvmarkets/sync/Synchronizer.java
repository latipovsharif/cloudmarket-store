package com.vvmarkets.sync;

import com.vvmarkets.configs.Config;
//import com.vvmarkets.core.IListContent;
//import com.vvmarkets.core.ListUtil;
import com.vvmarkets.core.Utils;
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
        scheduler.scheduleAtFixedRate(Synchronizer::syncSold, 0, 100, TimeUnit.SECONDS);

        if (Config.getOfflineMode()) {
            scheduler.schedule(Synchronizer::syncProducts, 0, TimeUnit.SECONDS);
        }
    }

    private static void syncSold() {
        try {
            System.out.println("processing sold");
            ExpenseBody body = new ExpenseBody().getUnfinished();
            if (body != null) {
                ExpenseResponse response = body.SaveToNetwork();
                if (response != null && !response.getReceiptId().isEmpty()) {
                    body.deleteFromDB();
                }
            }
        } catch (Exception e) {
            Utils.logException(e, "cannot send unfinished sold");
        }
    }

    private static void syncProducts() {
        try {
            System.out.println("syncing products");
            ProductService productService = RestClient.getClient().create(ProductService.class);
            Call<ResponseBody<List<ProductResponse>>> productCall = productService.productList();

            Response<ResponseBody<List<ProductResponse>>> response = productCall.execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    ProductResponse.ClearAndSave(response.body().getBody());
                }
            }

            SettingResponse.sync();
        } catch (Exception e) {
            Utils.logException(e, "cannot sync products");
        }
    }

    public void stopSync() {
        scheduler.shutdown();
    }
}
