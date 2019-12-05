package com.vvmarkets.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vvmarkets.core.Utils;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.services.SettingsService;
import com.vvmarkets.utils.ResponseBody;
import com.vvmarkets.utils.db;
import retrofit2.Call;
import retrofit2.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class SettingResponse {
    @SerializedName("id")
    @Expose
    private String Id;
    @SerializedName("name")
    @Expose
    private String Name;

    @SerializedName("options")
    @Expose
    private List<OptionResponse> options;

    public List<OptionResponse> getOptions() {
        return options;
    }

    public void setOptions(List<OptionResponse> options) {
        this.options = options;
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


    public static void sync() {
        try {
            SettingsService settingsService = RestClient.getClient().create(SettingsService.class);
            Call<ResponseBody<List<SettingResponse>>> settingCall = settingsService.get();

            Response<ResponseBody<List<SettingResponse>>> response = settingCall.execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    SettingResponse.saveList(response.body().getBody());
                }
            }
        } catch (Exception e) {
            Utils.logException(e, "cannot sync settings");
        }
    }

    private static void saveList(List<SettingResponse> data) {
        for(SettingResponse response : data) {
            response.save();
        }
    }

    private void save() {
        try(Connection c = db.getConnection()) {
            PreparedStatement ps;
            ps = c.prepareStatement("replace into cash_configs(id, name) values(?, ?)");
            ps.setString(1, this.getId());
            ps.setString(2, this.getName());
            ps.executeUpdate();
            ps.close();

            for (OptionResponse option: this.options) {
                option.save(this.getId());
            }
        } catch (Exception e) {
            Utils.logException(e, "cannot save response");
        }
    }
}
