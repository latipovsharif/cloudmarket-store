package com.vvmarkets.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseBody<T> {
    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("status")
    @Expose
    private int status;

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @SerializedName("body")
    @Expose
    private T body;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

}
