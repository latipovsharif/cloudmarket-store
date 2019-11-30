package com.vvmarkets.sync;

import com.vvmarkets.configs.Config;
import com.vvmarkets.core.ListUtil;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.ProductCategory;
import com.vvmarkets.responses.ProductResponse;
import com.vvmarkets.services.ProductService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.utils.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Response;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.vvmarkets.configs.Config.getSyncTimeout;

public class Base {
    private static final Logger log = LogManager.getLogger(ListUtil.class);

    public static void sync() {
            Runnable croneRunnable = Base::syncMainList;

            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(croneRunnable, 0, getSyncTimeout(), TimeUnit.SECONDS);

        if (Config.getOfflineMode()) {
            Runnable productCron = Base::syncProducts;

            ScheduledExecutorService productService = Executors.newScheduledThreadPool(1);
            productService.schedule(productCron, getSyncTimeout(), TimeUnit.SECONDS);
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
        } catch (Exception e) {
            log.error(e.getMessage() + "\n" + Utils.stackToString(e.getStackTrace()));
        }
    }

    private static void syncMainList() {
        try {
            ListUtil.INSTANCE.syncFillMain();

            for (ProductCategory category: ListUtil.INSTANCE.getMain()) {
                try {
                    ListUtil.INSTANCE.syncCashForCategory(category.getQueryId());

                } catch (Exception ex) {
                    log.error(Utils.stackToString(ex.getStackTrace()));
                }
            }
        } catch (Exception e) {
            log.error(Utils.stackToString(e.getStackTrace()));
        }
    }
}
