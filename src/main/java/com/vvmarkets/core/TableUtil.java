package com.vvmarkets.core;

import com.vvmarkets.dao.Product;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import kotlin.Pair;
import kotlin.internal.ProgressionUtilKt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TableUtil {

    public static TableView<Product> getTable() {
        TableColumn<Product, String> id = new TableColumn<>("Id");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setVisible(false);

        TableColumn<Product, String> article = new TableColumn<>("Article");
        article.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getProductProperties().getArticle())
        );

        TableColumn<Product, String> barcode = new TableColumn<>("Barcode");
        barcode.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getProductProperties().getBarcode())
        );

        TableColumn<Product, Double> price = new TableColumn<>("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Double> quantity = new TableColumn<>("Quantity");
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableView<Product> table = new TableView<>();

        table.getColumns().addAll(Arrays.asList(id, article, quantity, price));

        return table;

    }

    public static void addProduct(TableView<Product> productTableView, Product product) {
        Pair<Integer, Product> pair = getProductIndex(productTableView, product);

        if(pair != null) {
            pair.getSecond().setQuantity(pair.getSecond().getQuantity() + product.getQuantity());
            setProduct(productTableView, pair);
        } else {
            productTableView.getItems().add(product);
        }
    }

    private static Pair<Integer, Product> getProductIndex(TableView<Product> tableView, Product product) {
        Pair<Integer, Product> pair = null;
        for (int i = 0; i < tableView.getItems().size(); i++) {
            Product existingProduct = tableView.getItems().get(i);

            if (product.getId().equals(existingProduct.getId())) {
                pair = new Pair<>(i, existingProduct);
                break;
            }
        }

        return pair;
    }

    private static void setProduct(TableView<Product> tableView, Pair<Integer, Product> data) {
        tableView.getItems().set(data.getFirst(), data.getSecond());
    }

}