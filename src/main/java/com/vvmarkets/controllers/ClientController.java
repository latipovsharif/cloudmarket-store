package com.vvmarkets.controllers;

import com.jfoenix.controls.JFXDrawer;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.Client;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML
    public TextField txtSearch;
    @FXML
    public Button btnSelect;
    @FXML
    public Button btnCancel;
    @FXML
    public Button btnNew;

    @FXML
    public TableView<Client> tableView;

//    @FXML
//    public JFXDrawer jfxDrawer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<Client, String> id = new TableColumn<>("Id");
        id.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getID()));
        id.setVisible(false);

        TableColumn<Client, String> fullName = new TableColumn<>("ФИО");
        fullName.setPrefWidth(250);
        fullName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getFullName()));

        TableColumn<Client, String> discountCardNumber = new TableColumn<>("Номер карты");
        discountCardNumber.setPrefWidth(150);
        discountCardNumber.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDiscountCardNumber()));

        TableColumn<Client, String> discountPercent = new TableColumn<>("% скидки");
        discountPercent.setPrefWidth(150);
        discountPercent.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDiscountPercent().toString()));

        TableColumn<Client, String> phone = new TableColumn<>("Телефон");
        phone.setPrefWidth(150);
        phone.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getPhone()));

        TableColumn<Client, String> balance = new TableColumn<>("Баланс");
        balance.setPrefWidth(150);
        Callback<TableColumn<Client, String>, TableCell<Client, String>> balanceFactory =  new Callback<>() {
            @Override
            public TableCell call(final TableColumn<Client, String> param) {
                final TableCell<Client, String> cell = new TableCell<>() {

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        try {
                            Client c = getTableView().getItems().get(getIndex());
                            if (Double.parseDouble(c.getBalance()) < 0) {
                                setStyle("-fx-background-color: yellow");
                            }
                            setText(c.getBalance());
                        } catch (Exception e) {
                            Utils.logException(e, "cannot cast balance to double");
                        }
                    }
                };
                return cell;
            }
        };

        balance.setCellFactory(balanceFactory);

        tableView.getColumns().addAll(id, fullName, discountPercent, phone, balance);
    }
}
