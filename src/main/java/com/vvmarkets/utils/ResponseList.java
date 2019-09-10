package com.vvmarkets.utils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ResponseList<T> {

    @SerializedName("body")
    @Expose
    private ArrayList<T> list;

    @SerializedName("page_count")
    @Expose
    private int PageCount;

    public int getPageCount() {
        return PageCount;
    }


    public int getTotalItems() {
        return TotalItems;
    }

    @SerializedName("total_items")
    @Expose
    private int TotalItems;

    public int getItemPerPage() {
        return ItemPerPage;
    }

    @SerializedName("item_per_page")
    @Expose
    private int ItemPerPage;

    public ArrayList<T> getList() {
        return list;
    }

    public void setList(ArrayList<T> list) {
        this.list = list;
    }
}
