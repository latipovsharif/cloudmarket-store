package com.vvmarkets.controllers;

import com.vvmarkets.core.Utils;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class ConfirmController {

    private Node previousScene;

    public void setPreviousScene(Node node) {
        this.previousScene = node;
    }

    public void btnNumClick(ActionEvent actionEvent) {
        System.out.println(((Button)actionEvent.getSource()).getText());
    }

    public void closeCheck(ActionEvent actionEvent) {
        Utils.showScreen(previousScene);
    }

    public void chooseClient(ActionEvent actionEvent) {

    }
}
