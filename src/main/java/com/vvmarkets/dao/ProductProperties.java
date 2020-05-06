package com.vvmarkets.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vvmarkets.core.IListContent;
import com.vvmarkets.core.ListContentType;
import com.vvmarkets.utils.ImageDownloader;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ProductProperties implements IListContent{
    private static final Logger log = LogManager.getLogger(ProductProperties.class);

    @Expose
    @SerializedName("id")
    private String id;

    private String thumb;

    @Expose
    @SerializedName("name")
    private String name;

    @Expose
    @SerializedName("barcode")
    private String barcode;

    @Expose
    @SerializedName("article")
    private String article;
    @Expose
    @SerializedName("origin")
    private String origin;
    @Expose
    @SerializedName("description")
    private String description;

    @Expose
    @SerializedName("category")
    private ProductCategory productCategory;

    @Expose
    @SerializedName("product_group")
    private ProductGroup productGroup;

    @Expose
    @SerializedName("images")
    private List<Images> images;

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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public ProductGroup getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(ProductGroup productGroup) {
        this.productGroup = productGroup;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    public ListContentType getType(){
        return ListContentType.Product;
    }

    public String getQueryId(){
        return getBarcode();
    }

    public Image getThumb() {
        if (thumb != null && thumb.length() > 0) {
            return new Image(thumb);
        }

        return ImageDownloader.getImagePathForProduct(getId());
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
