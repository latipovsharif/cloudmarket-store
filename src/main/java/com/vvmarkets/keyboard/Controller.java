package com.vvmarkets.keyboard;

import javafx.fxml.FXML;

import java.awt.*;

public class Controller {
    @FXML
    Button btnEnter;

    @FXML
    void initialize() {
        btnEnter.addActionListener(e -> {
            System.out.println("btn clicked");
        });
    }
}
