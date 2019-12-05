package com.vvmarkets.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vvmarkets.core.Utils;
import com.vvmarkets.utils.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class OptionResponse {
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
    private String Value;

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

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

    public void save(String id) {
        try(Connection c = db.getConnection()) {
            PreparedStatement ps = c.prepareStatement("replace into cash_config_options(id, name, description, value, cash_config_id) values(?, ?, ?, ?, ?)");
            ps.setString(1, getId());
            ps.setString(2, getName());
            ps.setString(3, getDescription());
            ps.setString(4, getValue());
            ps.setString(5, id);
            ps.executeUpdate();
            ps.close();
        } catch(Exception e) {
            Utils.logException(e, "cannot save setting option");
        }
    }
}
