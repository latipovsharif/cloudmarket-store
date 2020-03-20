package com.vvmarkets.controllers;

import com.vvmarkets.configs.Config;
import com.vvmarkets.core.DialogUtil;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.Authorization;
import com.vvmarkets.presenters.MainPresenter;
import com.vvmarkets.requests.AuthorizationBody;
import com.vvmarkets.services.AuthorizationService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.sync.Synchronizer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.core.jackson.ContextDataAsEntryListDeserializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.vvmarkets.configs.Config.getLoginSuffix;

public class LogInController implements Initializable {
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
    @FXML
    private VBox keyboardContainer;

    boolean isUpper = false;
    String focused = "login";

    private Synchronizer s = new Synchronizer();

    public void signIn() {
        String cashToken = Config.getCashToken();

        if (cashToken.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Не указан токен");
            alert.setHeaderText("Токен не установлен");
            alert.setContentText("Пожалуйста установите токен для кассы.");
            alert.show();
            return;
        }

        String login = txtLogin.getText();
        if (!login.contains("@")) {
            login = login + getLoginSuffix();
        }

        AuthorizationService authService = RestClient.getClient().create(AuthorizationService.class);
        Call<Authorization> listProductCall = authService.auth(new AuthorizationBody(login, txtPassword.getText()));
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

                                s.startSync();
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
        Dialog<ButtonType> dialogPane = new Dialog<>();
        VBox box = new VBox();
        box.setSpacing(10);
        TextField textField = new TextField();
        textField.setPromptText("Токен");
        textField.setAlignment(Pos.CENTER);
        box.getChildren().add(textField);

        dialogPane.getDialogPane().setContent(box);

        ButtonType setTokenButton = new ButtonType("Установить", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Отменить", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialogPane.getDialogPane().getButtonTypes().addAll(setTokenButton, cancelButton);
        Optional<ButtonType> result = dialogPane.showAndWait();

        result.ifPresent(btn -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Невозможно установить токен");
            alert.setHeaderText("Невозможно установить токен");

            if (!btn.getButtonData().isCancelButton()) {

                if (!textField.getText().isEmpty()) {
                    if (!Config.setCashToken(textField.getText())) {
                        alert.setContentText("Просьба обратиться к администратору");
                        alert.show();
                    }
                } else {
                    alert.setContentText("Токен не может быть пустым");
                    alert.show();
                }
            }
        });
    }

    public void exit(ActionEvent actionEvent) {
        System.out.println("exiting application");
        s.stopSync();
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtLogin.focusedProperty().addListener((obs, newVal, oldVal)-> {
            if (obs.getValue()) {
                focused = "login";
            }
        });

        txtPassword.focusedProperty().addListener((obs, newVal, oldVal) -> {
            if(obs.getValue()) {
                focused = "password";
            }
        });
    }

    public void btnKeyboardClick(ActionEvent actionEvent) {
        String val = ((Button)actionEvent.getSource()).getText();
        if(focused.equals("password")) {
            txtPassword.setText(txtPassword.getText() + val);
        } else {
            txtLogin.setText(txtLogin.getText() + val);
        }
    }

    public void btnBackspaceClick(ActionEvent actionEvent) {
        if(focused.equals("password")) {
            txtPassword.setText(txtPassword.getText().substring(0, txtPassword.getText().length() - 1));
        } else {
            txtLogin.setText(txtLogin.getText().substring(0, txtLogin.getText().length() - 1));
        }
    }

    public void btnShiftClick(ActionEvent actionEvent) {
        for (Node node : keyboardContainer.getChildren()) {
            if (node instanceof HBox) {
                for (Node childNode: ((HBox) node).getChildren()) {
                    Button btn = (Button) childNode;
                    if (isUpper) {
                        switch (btn.getText()) {
                            case "!":
                                btn.setText("1");
                                break;
                            case "@":
                                btn.setText("2");
                                break;
                            case "#":
                                btn.setText("3");
                                break;
                            case "$":
                                btn.setText("4");
                                break;
                            case "%":
                                btn.setText("5");
                                break;
                            case "^":
                                btn.setText("6");
                                break;
                            case "&":
                                btn.setText("7");
                                break;
                            case "*":
                                btn.setText("8");
                                break;
                            case "(":
                                btn.setText("9");
                                break;
                            case ")":
                                btn.setText("0");
                                break;
                            case "BACKSPACE":
                            case "SHIFT":
                                break;
                            default:
                                btn.setText(btn.getText().toLowerCase());
                        }
                    } else {
                        switch (btn.getText()) {
                            case "1":
                                btn.setText("!");
                                break;
                            case "2":
                                btn.setText("@");
                                break;
                            case "3":
                                btn.setText("#");
                                break;
                            case "4":
                                btn.setText("$");
                                break;
                            case "5":
                                btn.setText("%");
                                break;
                            case "6":
                                btn.setText("^");
                                break;
                            case "7":
                                btn.setText("&");
                                break;
                            case "8":
                                btn.setText("*");
                                break;
                            case "9":
                                btn.setText("(");
                                break;
                            case "0":
                                btn.setText(")");
                                break;
                            case "BACKSPACE":
                            case "SHIFT":
                                break;
                            default:
                                btn.setText(btn.getText().toUpperCase());
                        }
                    }
                }
            }
        }
        isUpper = !isUpper;
    }

    public void passwordKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            signIn();
        }
    }
}
