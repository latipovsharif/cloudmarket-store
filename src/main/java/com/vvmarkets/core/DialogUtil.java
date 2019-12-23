package com.vvmarkets.core;

import com.vvmarkets.dao.ProductProperties;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextInputDialog;
import org.controlsfx.control.Notifications;

public class DialogUtil {
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

    public static void showInformationNotification(String header, String content) {
        showNotification("info",header,content);
    }

    public static void showWarningNotification(String header, String content) {
        showNotification("warn",header,content);
    }

    public static void showErrorNotification(String content) {
            showNotification("err","Непредвиденная ошибка",content);
    }

    private static void showNotification(String notificationType, String header, String content) {
        Platform.runLater(() -> {
            Notifications n = Notifications.create().title(header).text(content);
            switch (notificationType) {
                case "warn":
                    n.showWarning();
                    break;
                case "err":
                    n.showError();
                    break;
                default:
                    n.showInformation();
                    break;
            }
        });
    }
}
