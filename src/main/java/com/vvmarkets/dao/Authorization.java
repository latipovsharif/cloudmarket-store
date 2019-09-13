package com.vvmarkets.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vvmarkets.utils.Response;

public class Authorization extends Response {
    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("username")
    @Expose
    private String username;

    public String getUsername() {
        return username;
    }

    public String getToken() { return token; }
}
