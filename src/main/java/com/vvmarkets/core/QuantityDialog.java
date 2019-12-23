package com.vvmarkets.core;

import com.vvmarkets.controllers.QuantityDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;

public class QuantityDialog extends Dialog<Double> {

    public QuantityDialog(IListContent data) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/quantityDialog.fxml"));
            Parent root = loader.load();
            QuantityDialogController controller = loader.getController();
            controller.setProduct(data);
            this.setHeaderText(data.getName());
            getDialogPane().setContent(root);

            controller.btnConfirm.setOnAction(
                    actionEvent -> {
                        setResult(Utils.getDoubleOrZero(controller.txtValue.getText()));
                        this.close();
                    }
            );

            controller.btnCancel.setOnAction(actionEvent -> {
                setResult(-1.0);
                this.close();
            });

        } catch (Exception e) {
            Utils.logException(e, "cannot load fxml for quantity dialog");
        }
    }

}
