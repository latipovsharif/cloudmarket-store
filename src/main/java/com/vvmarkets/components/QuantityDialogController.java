package com.vvmarkets.components;

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

    @FXML
    public Button btnDot;

    public IListContent getProduct() {
        return content;
    }

    public void setProduct(IListContent product) {
        this.content = product;
    }

    private IListContent content;

    public void btnNumClicked(ActionEvent actionEvent) {
        txtValue.setText(txtValue.getText() + ((Button)actionEvent.getSource()).getText());
    }

    public void btnClearClicked(ActionEvent actionEvent) {
        txtValue.setText("");
    }

    public void btnDotClicked(ActionEvent actionEvent) {
        if (txtValue.getText().contains(".")){
            return;
        }

        txtValue.setText(txtValue.getText() + ".");
    }

    public void btnBackClicked(ActionEvent actionEvent) {
        if (txtValue.getText().length() > 0) {
            txtValue.setText(txtValue.getText(0, txtValue.getText().length() - 1));
        }
    }
}
