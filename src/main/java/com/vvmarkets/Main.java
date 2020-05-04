package com.vvmarkets;

import com.vvmarkets.controllers.LogInController;
import com.vvmarkets.presenters.LogInPresenter;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class Main extends Application {
    public static final String ApplicationVersion = "1.0.3";

    private static final Logger log = LogManager.getLogger(Main.class);
    private static AnchorPane mainContainer;
    private static LogInController mainController;

    public static AnchorPane getMainContainer(){
        return mainContainer;
    }

    public static LogInController getMainController() {
        return mainController;
    }


    @Override
    public void start(Stage primaryStage) throws Exception{
        System.setProperty("java.net.useSystemProxies", "true");
        log.debug("Starting application");

        LogInPresenter logIn = new LogInPresenter();
        Parent root  = logIn.getView();
        mainContainer = (AnchorPane) root.lookup("#mainContainer");
        mainController = logIn.controller;

        primaryStage.setScene(new Scene(root));
        setPrimaryStageAttrs(primaryStage);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void setPrimaryStageAttrs(Stage primaryStage) {
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setFullScreen(true);
        primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    }
}
