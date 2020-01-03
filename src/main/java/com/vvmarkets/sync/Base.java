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

//import static com.vvmarkets.configs.Config.getSyncTimeout;

public class Base {
    public static void sync() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

//        Runnable croneRunnable = Base::syncMain;
//        executorService.scheduleAtFixedRate(croneRunnable, 0, getSyncTimeout(), TimeUnit.SECONDS);

        Runnable soldSync = Base::syncSold;
        executorService.scheduleAtFixedRate(soldSync, 0,10, TimeUnit.SECONDS);

        if (Config.getOfflineMode()) {
            Runnable productCron = Base::syncProducts;

            ScheduledExecutorService productService = Executors.newScheduledThreadPool(1);
            productService.schedule(productCron, 0, TimeUnit.SECONDS);
        }
    }

    private static void syncSold() {
        try {
            ExpenseBody body = new ExpenseBody().getUnfinished();
            if (body != null) {
                ExpenseResponse response = body.SaveToNetwork();
                if (response != null) {
                    body.deleteFromDB();
                }
            }
        } catch (Exception e) {
            Utils.logException(e, "cannot send unfinished sold");
        }
    }

    private static void syncProducts() {
        try {
            ProductService productService = RestClient.getClient().create(ProductService.class);
            Call<ResponseBody<List<ProductResponse>>> productCall = productService.productList();

            Response<ResponseBody<List<ProductResponse>>> response = productCall.execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    ProductResponse.Save(response.body().getBody());
                }
            }

            SettingResponse.sync();
        } catch (Exception e) {
            Utils.logException(e, "cannot sync products");
        }
    }

//    private static void syncMain() {
//        try {
//            ListUtil.INSTANCE.syncFillMain();
//
//            for (IListContent category: ListUtil.INSTANCE.getMain()) {
//                try {
//                    ListUtil.INSTANCE.syncCashForCategory(category.getQueryId());
//                } catch (Exception ex) {
//                    Utils.logException(ex,"cannot sync subcategories for main hot access");
//                }
//            }
//        } catch (Exception e) {
//            Utils.logException(e,"cannot sync main list");
//        }
//    }
}
