package com.vvmarkets.interceptors;

import com.vvmarkets.configs.Config;
import com.vvmarkets.core.HttpConnectionHolder;
import com.vvmarkets.errors.NotAuthorized;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class Authorization implements Interceptor {
    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;

        if (request.url().encodedPath().contains("/api/v1/authorization/get/token/")) {
            try {
                response = chain.proceed(request);
            } catch (IOException io) {
                HttpConnectionHolder.INSTANCE.connectionUnavailable();
            }

            if (response == null) {
                throw new IOException("Response is null");
            }

            HttpConnectionHolder.INSTANCE.connectionAvailable();
            return response;
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

        try {
            response = chain.proceed(builder.build());
        } catch (IOException ex) {
            HttpConnectionHolder.INSTANCE.connectionUnavailable();
        }

        if (response == null) {
            throw new IOException("Response is null");
        }

        HttpConnectionHolder.INSTANCE.connectionAvailable();
        return response;
    }
}
