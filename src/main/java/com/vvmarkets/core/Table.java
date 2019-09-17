package com.vvmarkets.core;

import com.vvmarkets.dao.Product;
import javafx.scene.control.TableView;
import org.jetbrains.annotations.Contract;

public class Table {
    TableView<Product> productTableView;

    public Table(TableView<Product> productTableView) {
        this.productTableView = productTableView;
    }
}
