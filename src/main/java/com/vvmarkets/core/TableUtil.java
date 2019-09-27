package com.vvmarkets.core;

import com.vvmarkets.dao.Product;
import javafx.scene.control.TableView;
import org.jetbrains.annotations.Contract;

public class TableUtil {

    public static void AddProduct(TableView<Product> productTableView, Product product) throws Exception {
        boolean found = false;

        for (int i = 0; i < productTableView.getItems().size(); i++) {
            Product existingProduct = productTableView.getItems().get(i);

            if (product.getId().equals(existingProduct.getId())) {
                existingProduct.setQuantity(product.getQuantity() + existingProduct.getQuantity());
                productTableView.getItems().set(i, existingProduct);
                found = true;
                break;
            }
        }

        if (!found) {
            productTableView.getItems().add(product);
        }
    }
}