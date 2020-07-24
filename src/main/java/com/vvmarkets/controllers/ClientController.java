package com.vvmarkets.controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

public class ClientController implements Initializable {
    private Timer textTimer;
    private TimerTask textTimerTask;

    @FXML
    public TextField txtSearch;
    @FXML
    public Button btnSearch;

    @FXML
    public TableView tableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
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
    }

    public void txtSearchChange() {
        if (!txtSearch.getText().isEmpty() && !txtSearch.getText().isBlank()) {

        }
    }
}
