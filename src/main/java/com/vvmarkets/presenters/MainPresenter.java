package com.vvmarkets.presenters;

import com.vvmarkets.controllers.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.IOException;

public class MainPresenter implements IPresenter {
    private MainController controller;
    private Node previousNode;

    public MainPresenter(Node previousNode) {
        this.previousNode = previousNode;
    }

    public Node getPreviousView() {
        return previousNode;
    }

    public MainController getController() {
        return this.controller;
    }

    public Parent getView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/main.fxml"));
        Parent root = loader.load();
        this.controller = loader.getController();
        return root;
    }
}
