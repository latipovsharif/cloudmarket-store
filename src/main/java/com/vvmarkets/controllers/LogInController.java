package com.vvmarkets.controllers;

import com.vvmarkets.configs.Config;
import com.vvmarkets.core.DialogUtil;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.Authorization;
import com.vvmarkets.presenters.MainPresenter;
import com.vvmarkets.requests.AuthorizationBody;
import com.vvmarkets.services.AuthorizationService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.sync.Base;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.Optional;

public class LogInController {
    @FXML
    public PasswordField txtPassword;
    @FXML
    public Button btnSetToken;
    @FXML
    public Button btnSingIn;
    @FXML
    public Button btnExit;
    @FXML
    public AnchorPane loginContainer;
    @FXML
    private TextField txtLogin;

    public void signIn(ActionEvent e) {
        String cashToken = Config.getCashToken();

        if (cashToken.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Не указан токен");
            alert.setHeaderText("Токен не установлен");
            alert.setContentText("Пожалуйста установите токен для кассы.");
            alert.show();
        }

        AuthorizationService authService = RestClient.getClient().create(AuthorizationService.class);
        Call<Authorization> listProductCall = authService.auth(new AuthorizationBody(txtLogin.getText(), txtPassword.getText()));
        listProductCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Authorization> call, Response<Authorization> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 0) {
                            Config.setAuthorizationKey(response.body().getToken());
                            try {
                                Utils.showScreen(new MainPresenter(loginContainer).getView());
                                txtLogin.setText("");
                                txtPassword.setText("");

                                Base.sync();
                            } catch (Exception ex) {
                                Platform.runLater(() -> {
                                    Alert a = DialogUtil.newWarning("Error", ex.getMessage());
                                    a.show();
                                });
                            }
                        } else {
                            Platform.runLater(() -> {
                                Alert a = DialogUtil.newWarning("Error", response.body().getMessage());
                                a.show();
                            });
                        }
                    }
                } else {
                    Platform.runLater(() -> {
                        Alert a = DialogUtil.newWarning("Неправильный логин/пароль", "Пожалуйста введите правильный логин и/или пароль.");
                        a.show();
                    });
                }
            }

            @Override
            public void onFailure(Call<Authorization> call, Throwable t) {
                if (!(t instanceof IOException)) {
                    Utils.logException((Exception) t, "cannot sign in");
                    DialogUtil.showErrorNotification(t.getMessage());
                }
            }
        });
    }

    public void setToken(MouseEvent mouseEvent) {
        Dialog<String> dialogPane = new Dialog<>();
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
            }
        });
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }
}
