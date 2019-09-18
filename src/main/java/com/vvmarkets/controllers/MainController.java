package com.vvmarkets.controllers;

import com.vvmarkets.Main;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.Product;
import com.vvmarkets.errors.NotFound;
import com.vvmarkets.presenters.ConfirmPresenter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainController implements Initializable, IController {
    private static final Logger log = LogManager.getLogger(Main.class);

    public AnchorPane mainContainer;
    private String tmpBarcode = "";

    @Override
    public Node getPreviousView() {
        return previousView;
    }

    @Override
    public void setPreviousView(Node previousView) {
        this.previousView = previousView;
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
    public ImageView mainProductImage;
    @FXML
    public Label lblTotal;
    @FXML
    public Button btnConfirm;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Tab newTab = new Tab("this should be super tab");
        newTab.getStyleClass().add("mainTab");
        newTab.getStyleClass().add("mainTabActive");

        TableColumn<Product, String> id = new TableColumn<>("Id");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setVisible(false);

        TableColumn<Product, String> article = new TableColumn<>("Article");
        article.setCellValueFactory(param ->
             new SimpleStringProperty(param.getValue().getProductProperties().getArticle())
        );

        TableColumn<Product, String> barcode = new TableColumn<>("Barcode");
        barcode.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getProductProperties().getBarcode())
        );

        TableColumn<Product, Double> price = new TableColumn<>("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Double> quantity = new TableColumn<>("Quantity");
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableView<Product> productTableView = new TableView<>();

//        productTableView.setItems(Product.GetProducts());
        productTableView.getColumns().addAll(Arrays.asList(id, article, quantity, price));
        newTab.setContent(productTableView);

        mainTabPane.getTabs().add(0, newTab);
    }

    public void keyPressed(@NotNull KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            TableView tableView = (TableView) mainTabPane.getSelectionModel().getSelectedItem().getContent();

            try {
                tableView.getItems().add(Product.getProduct(tmpBarcode));
            } catch (NotFound nf) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.show();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText(e.getMessage());
                alert.show();
            }
            tmpBarcode = "";
        } else if (keyEvent.getCode().isDigitKey()) {
            tmpBarcode += keyEvent.getText();
        }
    }

    public void confirm(ActionEvent actionEvent) throws Exception {
        ConfirmPresenter cp = new ConfirmPresenter();
        Utils.showScreen(cp.getView(mainContainer));
//        TableView tableView = (TableView) mainTabPane.getSelectionModel().getSelectedItem().getContent();
    }
}
