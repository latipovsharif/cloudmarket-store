package com.vvmarkets.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vvmarkets.core.IListContent;
import com.vvmarkets.core.ListContentType;
import javafx.scene.image.Image;

public class ProductCategory implements IListContent{
    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("name")
    private String name;

    private String thumb;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductCategory getParent() {
        return parent;
    }

    public void setParent(ProductCategory parent) {
        this.parent = parent;
    }

    @Expose
    @SerializedName("parent")
    private ProductCategory parent;

    @Override
    public String toString() {
        return this.getName();
    }

    public ListContentType getType() {
        return ListContentType.Category;
    }

    public String getQueryId() {
        return getId();
    }

    public Image getThumb() {
        if (thumb == null || thumb.isBlank() || thumb.isEmpty()) {
            return new Image("images/no_image.png");
        }

        return new Image(thumb);
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

}
