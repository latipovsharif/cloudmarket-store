package com.vvmarkets.controllers;

import com.vvmarkets.Main;
import com.vvmarkets.core.DialogUtil;
import com.vvmarkets.core.TableUtil;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.Product;
import com.vvmarkets.requests.ExpenseBody;
import com.vvmarkets.requests.PaymentBody;
import com.vvmarkets.services.ExpenseService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.responses.ExpenseResponse;
import com.vvmarkets.utils.ResponseBody;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ConfirmController {

    private static final Logger log = LogManager.getLogger(Main.class);

    @FXML
    private TextField total;
    @FXML
    private TextField toPay;
    @FXML
    private TextField discount;
    @FXML
    private TextField cash;
    @FXML
    private TextField card;
    @FXML
    private TextField change;
    @FXML
    private Button btnCloseCheck;


    private Node previousScene;
    private TableView<Product> products;

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
        double dis = 0, toP = 0;

        try {
            dis = Double.parseDouble(discount.getText());
        } catch (Exception e) {
            log.error("cannot parse discount value " + e.getMessage());
        }

        try {
            toP = Double.parseDouble(total.getText());
        } catch (Exception e) {
            log.error("cannot parse total value " + e.getMessage());
        }

        toP = toP - (toP * dis / 100);

        toPay.setText(Utils.getFormatted(toP));
    }

    private void recalculateChange() {
        double ch = 0, top, csh, crd;
        top = Utils.getDoubleOrZero(toPay.getText());
        csh = Utils.getDoubleOrZero(cash.getText());
        crd = Utils.getDoubleOrZero(card.getText());

        ch = (csh + crd) - top;

        btnCloseCheck.setDisable(ch < 0);
        change.setText(Utils.getFormatted(ch));
    }

    public void toPayChanged() {
        recalculateChange();
    }

    public void cashChanged() {
        recalculateChange();
    }

    public void cardChanged() {
        if (Utils.getDoubleOrZero(toPay.getText()) < Utils.getDoubleOrZero(card.getText())) {
            Alert a = DialogUtil.newWarning("Неправильное значение", "Сумма оплаты по карте не может превышать сумму покупки");
            a.show();
            card.setText(toPay.getText());
            cash.setText("0");
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
        System.out.println(products.getItems().get(0).getProductProperties().getId());
        System.out.println(((Button)actionEvent.getSource()).getText());
    }

    public void closeCheck(ActionEvent actionEvent) {
        PaymentBody payment = new PaymentBody(
                Utils.getDoubleOrZero(toPay.getText()),
                Utils.getDoubleOrZero(card.getText()),
                Utils.getDoubleOrZero(cash.getText()));
        if (!payment.isValid()) {
            Alert a = DialogUtil.newError("Неправильная сумма", "Сумма оплаты безналичными не может превышать сумму чека");
            a.show();
            return;
        }

        ExpenseBody expense = new ExpenseBody(this.products,
                        payment,
                        MainController.sellerId,
                        "");

        ExpenseService documentService = RestClient.getClient().create(ExpenseService.class);
        Call<ResponseBody<ExpenseResponse>> listProductCall = documentService.create(expense);
        listProductCall.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody<ExpenseResponse>> call, Response<ResponseBody<ExpenseResponse>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 0) {
                            Utils.showScreen(previousScene);
                            products.getItems().clear();
                            return;
                        }
                    }
                }

                if (!TableUtil.saveToDb(expense)) {
                    Alert a = DialogUtil.newError("Не предвиденная ошибка", "Невозможно сохранить чек. Просьба обратиться к администратору.");
                    a.show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody<ExpenseResponse>> call, Throwable t) {
                log.error(t.getMessage());
            }
        });
    }

    public void chooseClient(ActionEvent actionEvent) {

    }
}
