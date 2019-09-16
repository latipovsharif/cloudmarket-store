package com.vvmarkets.controllers;

import com.vvmarkets.configs.Config;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.Authorization;
import com.vvmarkets.presenters.MainPresenter;
import com.vvmarkets.requests.AuthorizationBody;
import com.vvmarkets.services.AuthorizationService;
import com.vvmarkets.services.RestClient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Optional;

public class LogInController {
    private static final Logger log = LogManager.getLogger(LogInController.class);


    @FXML
    public PasswordField txtPassword;

    @FXML
    public Button btnSetToken;

    @FXML
    public Button btnSingIn;
    public Button btnExit;

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
            System.out.println("cash token is empty");
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
                                Utils.showScreen(new MainPresenter().getView());
                            } catch (Exception ex) {
                                log.error(ex.getMessage());
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Authorization> call, Throwable t) {
                log.error(t.getMessage());
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
            ;
        });
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }
}
