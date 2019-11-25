package com.vvmarkets.sync;

import com.vvmarkets.core.ListUtil;
import com.vvmarkets.dao.ProductCategory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.vvmarkets.configs.Config.getSyncTimeout;

public class Base {
    public static void sync() {
        Runnable croneRunnable = Base::syncMainList;

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleAtFixedRate(croneRunnable, 0, getSyncTimeout(), TimeUnit.SECONDS);
    }

    private static void syncMainList() {
        ListUtil.INSTANCE.syncFillMain();

        for (ProductCategory category: ListUtil.INSTANCE.getMain()) {
            ListUtil.INSTANCE.syncCashForCategory(category.getQueryId());
        }
    }
}
