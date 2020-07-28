package com.vvmarkets.components;

import com.vvmarkets.core.IListContent;
import com.vvmarkets.core.Utils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Dialog;

public class QuantityDialog extends Dialog<Double> {

    public QuantityDialog(IListContent data, boolean disableDot, boolean allowZero) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/quantityDialog.fxml"));
            Parent root = loader.load();
            QuantityDialogController controller = loader.getController();
            controller.setProduct(data);
            this.setHeaderText(data.getName());
            getDialogPane().setContent(root);

            controller.btnConfirm.setOnAction(
                    actionEvent -> {
                        if (controller.txtValue.getText().isEmpty()) {
                            if (allowZero) {
                                setResult(0.0);
                            } else {
                                setResult(1.0);
                            }
                        } else {
                            setResult(Utils.getDoubleOrZero(controller.txtValue.getText(), 3));
                        }
                        this.close();
                    }
            );

            if (disableDot) {
                controller.btnDot.setVisible(false);
            }

            controller.btnCancel.setOnAction(actionEvent -> {
                setResult(-1.0);
                this.close();
            });

        } catch (Exception e) {
            Utils.logException(e, "cannot load fxml for quantity dialog");
        }
    }

}
