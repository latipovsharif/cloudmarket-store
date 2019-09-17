package com.vvmarkets.presenters;

import com.vvmarkets.controllers.ConfirmController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

public class ConfirmPresenter {
    public ConfirmController controller = null;

    @FXML
    public Parent getView(Node node) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/confirm.fxml"));
        Parent root = loader.load();
        ConfirmController controller = loader.getController();
        controller.setPreviousScene(node);
        this.controller = controller;
        return root;
    }
}
