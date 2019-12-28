package com.vvmarkets.controllers;

import com.vvmarkets.core.DialogUtil;
import com.vvmarkets.core.TableUtil;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.Product;
import com.vvmarkets.peripheral.ThermalPrinter;
import com.vvmarkets.requests.ExpenseBody;
import com.vvmarkets.requests.PaymentBody;
import com.vvmarkets.responses.ExpenseResponse;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;


public class ConfirmController implements Initializable {

    @FXML
    private TextField total;
    @FXML
    private TextField toPay;
    @FXML
    private TextField discount;
    @FXML
    private TextField txtCash;
    @FXML
    private TextField card;
    @FXML
    private TextField change;
    @FXML
    private Button btnCloseCheck;



    private Node previousScene;
    private TableView<Product> products;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        txtCash.textProperty().addListener((observableValue, s, t1) -> recalculateChange());
    }

    public void totalChanged() {
        calculateToPay();
    }

    private void calculateToPay() {
        double d = Utils.getDoubleOrZero(discount.getText());
        double t = Utils.getDoubleOrZero(total.getText());

        if (d > 0) {
            t = t - (t * d / 100);
        }
        toPay.setText(Utils.getFormatted(t));
    }

    public void discountChanged() {
        double dis, toP;

        dis = Utils.getDoubleOrZero(discount.getText());
        toP = Utils.getDoubleOrZero(total.getText());

        toP = toP - (toP * dis / 100);

        toPay.setText(Utils.getFormatted(toP));
    }

    private void recalculateChange() {
        double ch, top, csh, crd;
        top = Utils.getDoubleOrZero(toPay.getText());
        csh = Utils.getDoubleOrZero(txtCash.getText());
        crd = Utils.getDoubleOrZero(card.getText());

        ch = (csh + crd) - top;

        btnCloseCheck.setDisable(ch < 0);
        change.setText(Utils.getFormatted(ch));
    }

    public void toPayChanged() {
        recalculateChange();
    }

    public void cardChanged() {
        if (Utils.getDoubleOrZero(toPay.getText()) < Utils.getDoubleOrZero(card.getText())) {
            Alert a = DialogUtil.newWarning("Неправильное значение",
                    "Сумма оплаты по карте не может превышать сумму покупки");
            a.show();
            card.setText(toPay.getText());
            txtCash.setText("0");
        }

        recalculateChange();
    }

    public void setPreviousScene(Node node) {
        this.previousScene = node;
    }

    public void setProducts(TableView<Product> products) {
        this.products = products;
        total.setText(Utils.getFormatted(TableUtil.calculateTotal(products)));
        calculateToPay();
    }


    public void btnNumClick(ActionEvent actionEvent) {
        txtCash.setText(txtCash.getText() + ((Button)actionEvent.getSource()).getText());
    }

    public void btnClearClicked(ActionEvent actionEvent) {
        txtCash.setText("");
    }

    public void btnDotClicked(ActionEvent actionEvent) {
        if (txtCash.getText().contains(".")){
            return;
        }

        txtCash.setText(txtCash.getText() + ".");
    }

    public void btnBackClicked(ActionEvent actionEvent) {
        if (txtCash.getText().length() > 0) {
            txtCash.setText(txtCash.getText(0, txtCash.getText().length() - 1));
        }
    }

    public void closeCheck(ActionEvent actionEvent) {
        btnCloseCheck.setDisable(true);
        PaymentBody payment = new PaymentBody(
                Utils.getDoubleOrZero(toPay.getText()),
                Utils.getDoubleOrZero(card.getText()),
                Utils.getDoubleOrZero(txtCash.getText()));
        if (!payment.isValid()) {
            Alert a = DialogUtil.newError("Неправильная сумма",
                    "Сумма оплаты безналичными не может превышать сумму чека");
            a.show();
            btnCloseCheck.setDisable(false);
            return;
        }

        ExpenseBody expense = new ExpenseBody(this.products,
                payment,
                MainController.seller.getId(),
                "");

        ExpenseResponse expenseResponse = expense.SaveToNetwork();
        boolean hasErr = true;

        if (expenseResponse == null) {
            if (TableUtil.saveToDb(expense)) {
                hasErr = false;
            }
        } else {
            expense.setId(expenseResponse.getDocumentNumber());
            hasErr = false;
        }

        if (!hasErr) {
            try {
                ThermalPrinter p = new ThermalPrinter(expense);
                p.print();
            } catch (Exception e) {
                Utils.logException(e, "cannot print check");
            }

            Utils.showScreen(previousScene);
            products.getItems().clear();
        } else {
            DialogUtil.showErrorNotification("Возникла критическая ошибка при сохранении документ, пожалуйста обратитесь к администратору.");
        }

        btnCloseCheck.setDisable(false);
    }

    public void chooseClient(ActionEvent actionEvent) {

    }

    public void cancel(ActionEvent actionEvent) {
        Utils.showScreen(previousScene);
    }

}
