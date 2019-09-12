package com.vvmarkets.presenters;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class LogInPresenter {
    @FXML
    public Parent getView() throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/keyboard.fxml"));
        return root;
    }
}
