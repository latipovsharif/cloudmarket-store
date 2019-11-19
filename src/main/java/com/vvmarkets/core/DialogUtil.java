package com.vvmarkets.core;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;

public class DialogUtil {

    public static Alert newWarning(String headerText, String contentText) {
        return newAlert(Alert.AlertType.WARNING, headerText, contentText);
    }

    public static Alert newError(String headerText, String contentText) {
        return newAlert(Alert.AlertType.ERROR, headerText, contentText);
    }

    public static void newWarningOnUIThread(String headerText, String contentText) {
        Platform.runLater(() -> {
            newAlert(Alert.AlertType.WARNING, headerText, contentText);
        });
    }

    private static Alert newAlert(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert;
    }

    public static Dialog getQuantityDialog(double quantity) {
        Dialog dialog = new TextInputDialog(String.valueOf(quantity));
        dialog.setTitle("Количество");
        dialog.setHeaderText("Введите количество");
        return dialog;
    }
}
