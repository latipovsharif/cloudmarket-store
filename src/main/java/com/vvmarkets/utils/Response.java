package com.vvmarkets.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("status")
    @Expose
    private int status;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

}
