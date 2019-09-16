package com.vvmarkets.core;

import com.vvmarkets.Main;
import com.vvmarkets.presenters.MainPresenter;
import javafx.application.Platform;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Utils {

    private static final Logger log = LogManager.getLogger(Utils.class);

    public static void showScreen(Node node) {
        Platform.runLater(() -> {
            Main.getMainContainer().getChildren().clear();
            try {
                Main.getMainContainer().getChildren().add(node);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        });
    }
}
