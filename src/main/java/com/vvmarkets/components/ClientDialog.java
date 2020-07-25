package com.vvmarkets.components;

import com.vvmarkets.controllers.ClientController;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.Client;
import com.vvmarkets.services.CounterpartyService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.utils.ResponseBody;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ClientDialog extends Dialog<Client> {
    private Timer textTimer;
    private TimerTask textTimerTask;
    private ClientController controller;


    public void txtSearchChange() {
        if (!controller.txtSearch.getText().isEmpty() && !controller.txtSearch.getText().isBlank()) {
            CounterpartyService clientService = RestClient.getClient().create(CounterpartyService.class);
            Call<ResponseBody<List<Client>>> clientResponse = clientService.counterpartyList(controller.txtSearch.getText());
            clientResponse.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<ResponseBody<List<Client>>> call, Response<ResponseBody<List<Client>>> response) {
                    if (response.isSuccessful()) {
                        for (Client c : response.body().getBody()) {
                            controller.tableView.getItems().add(c);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody<List<Client>>> call, Throwable t) {

                }
            });
        }
    }

    public ClientDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/client.fxml"));
            Parent root = loader.load();
            getDialogPane().setContent(root);
            controller = loader.getController();


            controller.txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                if (textTimer != null) {
                    textTimer.cancel();
                }
                textTimer = new Timer();
                textTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        Platform.runLater(() -> txtSearchChange());
                    }
                };
                textTimer.schedule(textTimerTask, 1000);
            });

            controller.btnSelect.setOnAction(
                    actionEvent -> {
                        setResult(new Client());
                        this.close();
                    }
            );
        } catch (Exception e) {
            Utils.logException(e, "cannot load fxml for quantity dialog");
        }
    }
}
