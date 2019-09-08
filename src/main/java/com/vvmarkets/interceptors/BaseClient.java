package com.vvmarkets.interceptors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class BaseClient {
    public static OkHttpClient getClient() {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new Authorization())
                .addInterceptor(new HttpLoggingInterceptor()).build();

        return client;
    }
}
