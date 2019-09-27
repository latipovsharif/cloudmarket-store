package com.vvmarkets.core;

import com.vvmarkets.dao.Product;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Arrays;

public class TabUtil {

    public static javafx.scene.control.Tab NewTab() {
        Tab newTab = new Tab("this should be super tab");
        newTab.getStyleClass().add("mainTab");
        newTab.getStyleClass().add("mainTabActive");

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

        TableView<Product> productTableView = new TableView<>();

//        productTableView.setItems(Product.GetProducts());
        productTableView.getColumns().addAll(Arrays.asList(id, article, quantity, price));
        newTab.setContent(productTableView);

        return newTab;
    }
}
