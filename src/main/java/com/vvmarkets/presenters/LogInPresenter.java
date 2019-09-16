package com.vvmarkets.presenters;

import com.vvmarkets.controllers.LogInController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class LogInPresenter {

    public LogInController controller = null;

    @FXML
    public Parent getView() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/keyboard.fxml"));
        Parent root = loader.load();
        LogInController controller = loader.getController();
        this.controller = controller;
        return root;
    }
}
