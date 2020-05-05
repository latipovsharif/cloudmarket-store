package com.vvmarkets.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageContainer {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("path")
    private String path;
}
