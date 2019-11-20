package com.vvmarkets.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vvmarkets.Main;
import com.vvmarkets.controllers.MainController;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.services.SellerService;
import com.vvmarkets.utils.ResponseBody;
import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class Seller {
    private static final Logger log = LogManager.getLogger(Main.class);

    @SerializedName("id")
    @Expose
    private String Id;
    @SerializedName("full_name")
    @Expose
    private String FullName;
    @SerializedName("email")
    @Expose
    private String Email;
    @SerializedName("is_seller")
    @Expose
    private Boolean IsSeller;

    @SerializedName("can_sell")
    @Expose
    private Boolean CanSell;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Boolean getSeller() {
        return IsSeller;
    }

    public void setSeller(Boolean seller) {
        IsSeller = seller;
    }

    public Boolean getCanSell() {
        return CanSell;
    }

    public void setCanSell(Boolean canSell) {
        CanSell = canSell;
    }

    @Override
    public String toString() {
        return this.FullName;
    }

    public static void fillSeller(ComboBox<Seller> sellerComboBox) {
        SellerService sellerService = RestClient.getClient().create(SellerService.class);
        Call<ResponseBody<List<Seller>>> sellerList = sellerService.sellerList();

        sellerList.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody<List<Seller>>> call, Response<ResponseBody<List<Seller>>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 0) {
                            Platform.runLater(() -> {
                                sellerComboBox.getItems().addAll(response.body().getBody());
                                if (sellerComboBox.getItems().size() == 1) {
                                    sellerComboBox.getSelectionModel().select(0);
                                    sellerComboBox.setDisable(true);
                                    MainController.sellerId = sellerComboBox.getSelectionModel().getSelectedItem().getId();
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody<List<Seller>>> call, Throwable t) {
                log.error(t.getMessage());
            }
        });
    }
}
