package com.vvmarkets.controllers;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

public interface IController {
    public Node getPreviousView();
    public void setPreviousView(Node previousView);
}
