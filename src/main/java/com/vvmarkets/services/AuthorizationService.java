package com.vvmarkets.services;

import com.vvmarkets.dao.Authorization;
import com.vvmarkets.requests.AuthorizationBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthorizationService {
    @POST("authorization/get/token/")
    Call<Authorization> auth(@Body AuthorizationBody body);
}
