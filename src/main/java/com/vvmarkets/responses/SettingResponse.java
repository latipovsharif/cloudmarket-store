package com.vvmarkets.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SettingResponse {
    @SerializedName("id")
    @Expose
    private String Id;
    @SerializedName("name")
    @Expose
    private String Name;
    @SerializedName("description")
    @Expose
    private String Description;
    @SerializedName("value")
    @Expose
    private List<String> Value;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public List<String> getValue() {
        return Value;
    }

    public void setValue(List<String> value) {
        Value = value;
    }
}
