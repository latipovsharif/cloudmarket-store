package com.vvmarkets.services;

import com.vvmarkets.configs.Config;
import com.vvmarkets.interceptors.BaseClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {
    private static String BASE_URL = "/api/v1/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null){
            BASE_URL = Config.getServerIP() + BASE_URL;
            retrofit = new Retrofit.Builder()
                    .client(BaseClient.getClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(BASE_URL).build();
        }

        return retrofit;
    }
}
