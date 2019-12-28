package com.vvmarkets.interceptors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.concurrent.TimeUnit;

public class BaseClient {
    public static OkHttpClient getClient() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(4, TimeUnit.SECONDS)
                .writeTimeout(4, TimeUnit.SECONDS)
                .readTimeout(4, TimeUnit.SECONDS)
                .addInterceptor(new Authorization())
                .addInterceptor(new HttpLoggingInterceptor()).build();

        return client;
    }
}
