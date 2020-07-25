package com.vvmarkets.controllers;

import com.vvmarkets.dao.Client;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML
    public TextField txtSearch;
    @FXML
    public Button btnSelect;

    @FXML
    public TableView<Client> tableView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TableColumn<Client, String> id = new TableColumn<>("Id");
        id.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getID()));
        id.setVisible(false);

        TableColumn<Client, String> fullName = new TableColumn<>("ФИО");
        fullName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getFullName()));

        TableColumn<Client, String> discountCardNumber = new TableColumn<>("Номер карты");
        discountCardNumber.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDiscountCardNumber()));

        TableColumn<Client, String> discountPercent = new TableColumn<>("% скидки");
        discountPercent.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getDiscountPercent().toString()));

        TableColumn<Client, String> phone = new TableColumn<>("Телефон");
        phone.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getPhone()));

        TableColumn<Client, String> balance = new TableColumn<>("Баланс");
        balance.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getBalance()));



        tableView.getColumns().addAll(id, fullName, discountPercent, phone, balance);
    }
}
