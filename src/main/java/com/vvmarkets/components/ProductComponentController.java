package com.vvmarkets.components;

import com.vvmarkets.core.IListContent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class ProductComponentController {
    @FXML
    public ImageView imageView;

    @FXML
    public Image image;

    @FXML
    public Label lblProductName;


    public void setProduct(IListContent product) {
        lblProductName.setText(product.getName());
    }
}
