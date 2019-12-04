package com.vvmarkets.services;

import com.vvmarkets.responses.SettingResponse;
import com.vvmarkets.utils.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface SettingsService {
    @GET("cashes/config/get/")
    Call<ResponseBody<SettingResponse>> get();
}
