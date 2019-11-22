package com.vvmarkets.services;

import com.vvmarkets.requests.ExpenseBody;
import com.vvmarkets.responses.ExpenseResponse;
import com.vvmarkets.utils.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ExpenseService {
    @POST("documents/expense/create/")
    Call<ResponseBody<ExpenseResponse>> create(@Body ExpenseBody body);
}
