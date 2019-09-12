package com.vvmarkets.controllers;

import com.vvmarkets.configs.Config;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class LogInController {
    @FXML
    public PasswordField txtPassword;

    @FXML
    public Button btnSetToken;

    @FXML
    public Button btnSingIn;

    @FXML
    private TextField txtLogin;

    public void signIn(MouseEvent mouseEvent) {
        String cashToken = Config.getCashToken();

        if (cashToken.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Не указан токен");
            alert.setHeaderText("Токен не установлен");
            alert.setContentText("Пожалуйста установите токен для кассы.");
            alert.show();
            System.out.println("cash token is empty");
        }

    }

    public void setToken(MouseEvent mouseEvent) {
        Dialog<String> dialogPane = new Dialog<String>();
        VBox box = new VBox();
        box.setSpacing(10);
        TextField textField = new TextField();
        textField.setPromptText("Токен");
        textField.setAlignment(Pos.CENTER);
        box.getChildren().add(textField);

        dialogPane.getDialogPane().setContent(box);
        dialogPane.setResultConverter(dBtn -> textField.getText());

        ButtonType setTokenButton = new ButtonType("Установить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialogPane.getDialogPane().getButtonTypes().addAll(setTokenButton, cancelButton);

        Optional<String> result = dialogPane.showAndWait();

        result.ifPresent(token -> {
            if (!Config.setCashToken(token)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Невозможно установить токен");
                alert.setHeaderText("Невозможно установить токен");
                alert.setContentText("Просьба обратиться к администратору");
                alert.show();
            };
        });
    }
}
