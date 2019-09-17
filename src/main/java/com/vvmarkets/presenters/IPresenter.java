package com.vvmarkets.presenters;

import com.vvmarkets.controllers.IController;
import javafx.scene.Node;

public interface IPresenter {
    public IController getController();
    public Node getPreviousView();
}
