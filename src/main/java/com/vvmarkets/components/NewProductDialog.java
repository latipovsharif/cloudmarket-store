package com.vvmarkets.components;

import com.vvmarkets.configs.RemoteConfig;
import com.vvmarkets.core.Utils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;

import com.vvmarkets.dao.Product;
import javafx.util.Pair;

public class NewProductDialog extends Dialog<Boolean> {
    public NewProductDialog(String barcode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/productDialog.fxml"));
            Parent root = loader.load();
            NewProductDialogController controller = loader.getController();
            getDialogPane().setContent(root);
            controller.txtBarcode.setText(barcode);

            controller.btnConfirm.setOnAction(
                    actionEvent -> {
                        if (controller.txtPrice.getText().length() < 1) {
                            return;
                        }

                        try {
                            double price = Double.parseDouble(controller.txtPrice.getText().replace(",", "."));
                            if (price <= 0) {
                                return;
                            }

                            Pair<Double, String> p = Product.getProductCodeFromBarcode(
                                    barcode,
                                    RemoteConfig.getConfig(RemoteConfig.ConfigType.PIECEMEAL, RemoteConfig.ConfigSubType.FORMAT));
                            boolean b = Product.createProduct(p.getValue(), price);
                            setResult(b);
                        } catch (Exception e) {
                            return;
                        }

                        this.close();
                    }
            );

            controller.btnCancel.setOnAction(actionEvent -> {
                setResult(false);
                this.close();
            });
        } catch (Exception e) {
            Utils.logException(e, "cannot load fxml for quantity dialog");
        }
    }
}
