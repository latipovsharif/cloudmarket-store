package com.vvmarkets.components;

import com.vvmarkets.controllers.ClientController;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.Client;
import com.vvmarkets.services.CounterpartyService;
import com.vvmarkets.services.RestClient;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;
import retrofit2.Call;
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
            Call<List<Client>> clientResponse = clientService.counterpartyList(controller.txtSearch.getText());
            try {
                Response<List<Client>> response = clientResponse.execute();
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        controller.tableView.getItems().clear();
                        controller.tableView.getItems().addAll(response.body());
                    }
                }
            } catch (Exception e) {
                Utils.logException(e, "cannot get client list");
            }
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
                        setResult(controller.tableView.getSelectionModel().getSelectedItem());
                        this.close();
                    }
            );

            controller.btnCancel.setOnAction(
                    actionEvent -> {
                        setResult(new Client());
                        this.close();
                    }
            );

            controller.btnNew.setOnAction(actionEvent -> {
                if (controller.jfxDrawer.isClosed()) {
                    controller.jfxDrawer.open();
                } else {
                    controller.jfxDrawer.close();
                }
            });
        } catch (Exception e) {
            Utils.logException(e, "cannot load fxml for quantity dialog");
        }
    }
}
