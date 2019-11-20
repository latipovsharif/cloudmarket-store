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

        if (request.url().encodedPath().contains("/api/v1/authorization/get/token/")) {
            return chain.proceed(request);
        }


        String key = Config.getAuthorizationKey();
        if (key.isEmpty()) {
            throw new NotAuthorized("Authorization key is empty");
        }

        Request.Builder builder = request.newBuilder().addHeader("Authorization", key);

        if (
                request.url().encodedPath().contains("/api/v1/cashes/") ||
                request.url().encodedPath().contains("/api/v2/cashes/") ||
                request.url().encodedPath().contains("/documents/expense/create/")
        ) {
            String cashToken = Config.getCashToken();
            if (cashToken.isEmpty()) {
                throw new NotAuthorized("Cash token is empty");
            }

            builder = builder.addHeader("Cash-Authorization", cashToken);
        }

        return chain.proceed(builder.build());
    }
}
