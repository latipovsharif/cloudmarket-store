package com.vvmarkets.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import javax.swing.text.TableView;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    public Button mainBtnNewTab;
    public Button mainBtnExit;
    public Button btnNewTab11;
    public TabPane mainTabPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tab newTab = new Tab("this should be super tab");
        newTab.getStyleClass().add("mainTab");
        newTab.getStyleClass().add("mainTabActive");
        mainTabPane.getTabs().add(0, newTab);
    }
}
