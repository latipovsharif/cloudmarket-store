package com.vvmarkets.components;

import com.vvmarkets.dao.Product;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.List;

public class ProductComponent extends VBox {

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    private Product product;

    public ProductComponent(Product product) {
        this.product = product;
    }

    public static List<ProductComponent> getList() {
        return getList("");
    }

    public static List<ProductComponent> getList(String productId) {
        if (productId == ""){

        }

        List<ProductComponent> res = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Product p = new Product();
            p.setId(String.valueOf(i));
            ProductComponent pc = new ProductComponent(p);
            pc.setBackground(new Background(new BackgroundFill(Paint.valueOf("#fff"), null, null)));
            ImageView iv = new ImageView();
            iv.setImage(new Image("images/no_image.png"));
            Label lbl = new Label(p.getId());
            pc.getChildren().add(iv);
            pc.getChildren().add(lbl);

            res.add(pc);
        }
        return res;
    }

    @Override
    public String toString() {
        return "ProductComponent{" +
                "product=" + product.getId() +
                '}';
    }
}
