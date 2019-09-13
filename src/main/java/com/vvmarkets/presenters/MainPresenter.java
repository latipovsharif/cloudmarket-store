package com.vvmarkets.presenters;

import com.vvmarkets.controllers.MainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class MainPresenter {
    public MainController controller = null;

    @FXML
    public Parent getView() throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/main.fxml"));
        Parent root = loader.load();
        MainController controller = loader.getController();
        this.controller = controller;
        return root;
    }
}
