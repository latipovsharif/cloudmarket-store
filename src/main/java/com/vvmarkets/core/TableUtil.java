package com.vvmarkets.core;

import com.vvmarkets.dao.Product;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import kotlin.Pair;
import kotlin.internal.ProgressionUtilKt;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TableUtil {

    public static TableView<Product> getTable() {
        TableColumn<Product, String> id = new TableColumn<>("Id");
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        id.setVisible(false);

        TableColumn<Product, String> article = new TableColumn<>("Article");
        article.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getProductProperties().getArticle())
        );

        TableColumn<Product, Double> total = new TableColumn<>("Total");
        total.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Product, Double> price = new TableColumn<>("Price");
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Double> discount = new TableColumn<>("Discount");
        discount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        TableColumn<Product, Double> quantity = new TableColumn<>("Quantity");
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableView<Product> table = new TableView<>();

        table.getColumns().addAll(Arrays.asList(id, article, price, quantity, discount, total));

        table.setRowFactory(rf -> {
            TableRow<Product> tr = new TableRow<>();
            tr.setOnMouseClicked(event -> {
                if (! tr.isEmpty() && event.getButton()== MouseButton.PRIMARY) {

                    Product clickedRow = tr.getItem();

                    Dialog dialog = new TextInputDialog(String.valueOf(clickedRow.getQuantity()));
                    dialog.setTitle("Количество");
                    dialog.setHeaderText("Введите новое количество");

                    Optional<String> result = dialog.showAndWait();

                    if (result.isPresent()) {
                        double entered = Double.parseDouble(result.get());
                        clickedRow.setQuantity(entered);
                        tr.getTableView().getItems().set(tr.getIndex(), clickedRow);
                    }
                }
            });

            return tr;
        });

        return table;
    }

    public static void addProduct(TableView<Product> productTableView, Product product) {
        Pair<Integer, Product> pair = getProductIndex(productTableView, product);

        if(pair != null) {
            pair.getSecond().setQuantity(pair.getSecond().getQuantity() + product.getQuantity());

//            pair.getSecond().setQuantity(pair.getSecond().getQuantity() + product.getQuantity());

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

    public static double calculateTotal(TableView<Product> tableView) {
        double total = 0;

        for (Product p : tableView.getItems()) {
            total += p.getTotal();
        }
        return total;
    }
}