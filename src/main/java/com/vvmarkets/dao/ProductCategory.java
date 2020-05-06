package com.vvmarkets.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vvmarkets.core.IListContent;
import com.vvmarkets.core.ListContentType;
import com.vvmarkets.utils.ImageDownloader;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ProductCategory implements IListContent{
    private static final Logger log = LogManager.getLogger(ProductCategory.class);
    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("image")
    private ImageContainer thumb;

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
        return ImageDownloader.getImagePathForProduct(getId());
    }

    public void setThumb(ImageContainer thumb) {
        this.thumb = thumb;
    }

}
