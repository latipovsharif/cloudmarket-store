package com.vvmarkets.presenters;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class KeyboardPresenter {
    private String[] keys = {
        "1", "2", "3", "4", "5", "6", "7", "8", "9", "0",
        "q", "w", "e", "r", "t", "y", "u", "i", "o", "p",
        "a", "s", "d", "f", "g", "h", "j", "k", "l",
            "z", "x", "c", "v", "b", "n", "m", "."
    };

    @FXML
    public Parent getView() throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("views/keyboard.fxml"));
        return root;
    }
}
