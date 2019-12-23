package com.vvmarkets.controllers;

import com.vvmarkets.core.IListContent;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class QuantityDialogController {
    @FXML
    public Button btnConfirm;
    @FXML
    public TextField txtValue;
    @FXML
    public Button btnCancel;

    public IListContent getProduct() {
        return content;
    }

    public void setProduct(IListContent product) {
        this.content = product;
    }

    private IListContent content;

    public void btnNumClicked(ActionEvent actionEvent) {
        System.out.println(((Button)actionEvent.getSource()).getText());
    }
}
