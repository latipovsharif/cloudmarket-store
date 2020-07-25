package com.vvmarkets.components;

import com.vvmarkets.controllers.ClientController;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.Client;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;

import java.util.Timer;
import java.util.TimerTask;

public class ClientDialog extends Dialog<Client> {
    private Timer textTimer;
    private TimerTask textTimerTask;
    private ClientController controller;


    public void txtSearchChange() {
        if (!controller.txtSearch.getText().isEmpty() && !controller.txtSearch.getText().isBlank()) {
            Client c = new Client();
            c.setBalance("100");
            c.setFullName("Hello world");
            controller.tableView.getItems().add(c);
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
