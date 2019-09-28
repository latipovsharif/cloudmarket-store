package com.vvmarkets.core;

import javafx.scene.control.Alert;

public class AlertUtil {

    public static Alert newWarning(String headerText, String contentText) {
        return newAlert(Alert.AlertType.WARNING, headerText, contentText);
    }

    public static Alert newError(String headerText, String contentText) {
        return newAlert(Alert.AlertType.ERROR, headerText, contentText);
    }

    private static Alert newAlert(Alert.AlertType alertType, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        return alert;
    }
}
