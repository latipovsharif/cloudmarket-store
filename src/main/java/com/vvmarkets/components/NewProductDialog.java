package com.vvmarkets.components;

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
                        try {
                            Pair<Double, String> p = Product.getProductCodeFromBarcode(barcode);
                            boolean b = Product.createProduct(p.getValue(),
                                    Utils.getDoubleOrZero(controller.txtPrice.getText().replace(",", ".")));
                            setResult(b);
                        } catch (Exception e) {
                            setResult(false);
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
