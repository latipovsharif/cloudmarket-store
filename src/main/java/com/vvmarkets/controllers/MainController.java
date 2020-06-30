package com.vvmarkets.controllers;

import com.jfoenix.controls.JFXMasonryPane;
import com.vvmarkets.components.NewProductDialog;
import com.vvmarkets.components.ProductComponent;
import com.vvmarkets.core.*;
import com.vvmarkets.dao.Product;
import com.vvmarkets.dao.ProductProperties;
import com.vvmarkets.dao.Seller;
import com.vvmarkets.errors.InvalidFormat;
import com.vvmarkets.errors.NotFound;
import com.vvmarkets.presenters.ConfirmPresenter;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.input.MouseEvent;

public class MainController implements Initializable, IController {
    public static Seller seller;
    public AnchorPane mainContainer;

    @FXML
    public AnchorPane hotAccessPane;

    private String tmpBarcode = "";

    @FXML
    private JFXMasonryPane mainMasonryPane;

    @Override
    public Node getPreviousView() {
        return previousView;
    }

    @Override
    public void setPreviousView(Node previousView) {
        this.previousView = previousView;
    }

    public void setSeller(String username) {
        try {
            for (Seller s: Seller.fillSeller()) {
                if (s.getEmail().equals(username)) {
                    seller = s;
                    return;
                }
            }

            DialogUtil.newError("Доступ запрещен", "Вы не имеете права продовать на этой кассе.\r\nПожалуйста обратитесь к администратору.").show();
        } catch (IOException e) {
            Utils.logException(e, "cannot get seller");
            Alert a = DialogUtil.newWarning("Ошибка при получении продавца", "Невозможно получить продавца\r\n либо он не установлен либо сеть недоступна");
            a.show();
        } catch (Exception ex) {
            Utils.logException(ex, "cannot get seller");
            DialogUtil.showErrorNotification("Невозможно получить продавца\r\nданный кассир не имеет полномочий продавать с данной кассы");
        }
    }

    private Node previousView;

    @FXML
    public Button mainBtnNewTab;
    @FXML
    public Button mainBtnExit;
    @FXML
    public Button btnCloseTab;
    @FXML
    public TabPane mainTabPane;
    @FXML
    public Label lblTotal;
    @FXML
    public Button btnConfirm;

    @FXML
    private TextField searchTxtField;

    private static int checkCounter = 1;

    private Timer textTimer;
    private TimerTask textTimerTask;

    public static int getCheckCounter() {
        if (checkCounter > 200) checkCounter = 1;
        return ++checkCounter;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tab newTab = TabUtil.NewTab();
        newTab.setOnSelectionChanged(this::selectedTabChanged);
        mainTabPane.getTabs().add(0, newTab);

        TableUtil.changed.subscribe(aDouble -> {
            lblTotal.setText(Utils.getFormatted(aDouble));
        });

        mainMasonryPane.getChildren().addAll(
                ProductComponent.listToComponent(
                        ListUtil.fillMainSync(),
                        false)
        );

        searchTxtField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (textTimer != null) {
                textTimer.cancel();
            }
            textTimer = new Timer();
            textTimerTask = new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(MainController.this::searchOnType);
                }
            };
            textTimer.schedule(textTimerTask, 1000);
        });
    }

    public void keyPressed(@NotNull KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            addProduct(tmpBarcode);
            tmpBarcode = "";
        } else if(keyEvent.getCode().isDigitKey()) {
            tmpBarcode += keyEvent.getText();
        }
    }

    public void confirm(ActionEvent actionEvent) throws Exception {
        TableView tableView = (TableView) mainTabPane.getSelectionModel().getSelectedItem().getContent();
        if (tableView.getItems().size() == 0) {
            Alert a = DialogUtil.newWarning("Корзина пуста", "Для продолжения нужно добавить товар в корзину");
            a.show();
            return;
        }
        ConfirmPresenter cp = new ConfirmPresenter(tableView);
        Utils.showScreen(cp.getView(mainContainer));
    }

    public void newTabClicked(MouseEvent mouseEvent) {
        Tab newTab = TabUtil.NewTab();
        newTab.setOnSelectionChanged(this::selectedTabChanged);
        mainTabPane.getTabs().add(newTab);
        mainTabPane.getSelectionModel().select(newTab);
    }

    private void selectedTabChanged(Event event) {
        TableView tableView = (TableView) mainTabPane.getSelectionModel().getSelectedItem().getContent();
        lblTotal.setText(Utils.getFormatted(TableUtil.calculateTotal(tableView)));
    }

    public void closeTabClicked(MouseEvent mouseEvent) {
        mainTabPane.getTabs().remove(mainTabPane.getSelectionModel().getSelectedIndex());
    }

    public void containerClicked(MouseEvent mouseEvent) {
        ProductComponent pc = null;

        if (mouseEvent.getPickResult().getIntersectedNode() instanceof ProductComponent) {
            pc = (ProductComponent) mouseEvent.getPickResult().getIntersectedNode();
        } else if (mouseEvent.getPickResult().getIntersectedNode() instanceof ImageView) {
            pc = (ProductComponent) mouseEvent.getPickResult().getIntersectedNode().getParent().getParent();
        } else if (mouseEvent.getPickResult().getIntersectedNode() instanceof Label) {
            pc = (ProductComponent) mouseEvent.getPickResult().getIntersectedNode().getParent().getParent();
        } else {
            try {
                pc = (ProductComponent) mouseEvent.getPickResult().getIntersectedNode().getParent().getParent().getParent();
            } catch (Exception e) {
                return;
            }
        }

        if (pc != null) {
            if (pc.getProduct() instanceof ProductProperties) {
                if (pc.getProduct().getQueryId() == null) {
                    showMainMenu();
                }
            }

            if (pc.getProduct() == null || pc.getProduct().getQueryId() == null || pc.getProduct().getQueryId().isEmpty()) {
                return;
            }

            switch (pc.getProduct().getType()) {
                case Category:
                    mainMasonryPane.getChildren().clear();
                    mainMasonryPane.getChildren().addAll(ProductComponent.getList(pc.getProduct().getQueryId()));
                    break;
                case Product:
                    addProduct(pc.getProduct().getQueryId());
                    break;
                default:
                    break;
            }
        }
    }

    private void addProduct(String barcode) {
        try {
            Product clickedRow = Product.getProduct(barcode);
            TableView tableView = (TableView) mainTabPane.getSelectionModel().getSelectedItem().getContent();
            TableUtil.addProduct(tableView, clickedRow);
        } catch (NotFound nf) {
            // FIXME should check settings from server to do this
            // if I've got permission than I can do the following
            if (barcode.length() > 2) {
                NewProductDialog dialog = new NewProductDialog(barcode);
                dialog.showAndWait();
                boolean res = dialog.getResult();
                if (res) {
                    try {
                        Product clickedRow = Product.getProduct(barcode);
                        TableView tableView = (TableView) mainTabPane.getSelectionModel().getSelectedItem().getContent();
                        TableUtil.addProduct(tableView, clickedRow);
                    } catch (Exception e) {
                        DialogUtil.newWarning("Не найден", String.format("Товар с кодом %s не найден %s", tmpBarcode, nf.getMessage())).show();
                    }
                }
            } else {
                DialogUtil.newWarning("Неправильный формат", String.format("Длина штрихкода должна быть больше 2 символов %s", tmpBarcode)).show();
            }
            // else I should show warning that product is not found
        } catch (InvalidFormat ifmt) {
            DialogUtil.newError("Неправильный формат",
                    String.format("Неправильно настроена функция работы с весовыми товарами или задан неправильный код для товара %s.\r\n %s", tmpBarcode, ifmt.getMessage())).show();
        } catch (Exception e) {
            DialogUtil.newError("Непредвиденная ошибка", e.getMessage()).show();
        }
    }

    public void logout(ActionEvent actionEvent) {
        Utils.showScreen(previousView);
    }

    public void showMainMenu() {
        mainMasonryPane.getChildren().clear();
        mainMasonryPane.getChildren().addAll(ProductComponent.getList());
    }

    public void searchOnType() {
        if (!searchTxtField.getText().isEmpty() && !searchTxtField.getText().isBlank()) {
                mainMasonryPane.getChildren().clear();
                mainMasonryPane.getChildren().addAll(ProductComponent.getSearchList(searchTxtField.getText()));
        } else {
            showMainMenu();
        }
    }
}
