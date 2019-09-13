package com.vvmarkets.interceptors;

import com.vvmarkets.configs.Config;
import com.vvmarkets.errors.NotAuthorized;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Authorization implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        if (!request.url().encodedPath().contains("/api/v1/authorization/get/token/")) {
            String key = Config.getAuthorizationKey();
            if (key.isEmpty()) {
                throw new NotAuthorized("Authorization key is empty");
            }

            request = request.newBuilder()
                    .addHeader("Authorization", key)
                    .build();
        }

        return chain.proceed(request);
    }
}
