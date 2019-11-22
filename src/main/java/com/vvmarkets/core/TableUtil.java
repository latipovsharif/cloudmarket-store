package com.vvmarkets.core;

import com.vvmarkets.dao.Product;
import com.vvmarkets.requests.ExpenseBody;
import io.reactivex.subjects.PublishSubject;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import kotlin.Pair;

import java.util.Arrays;
import java.util.Optional;

public class TableUtil {

    public static PublishSubject<Double> changed = PublishSubject.create();

    public static TableView<Product> getTable() {
        TableColumn<Product, String> id = new TableColumn<>("Id");
        id.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getProductProperties().getId())
        );
        id.setVisible(false);

        TableColumn<Product, String> article = new TableColumn<>("Артикул");
        article.setPrefWidth(280);
        article.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getProductProperties().getArticle())
        );
        article.setVisible(false);

        TableColumn<Product, String> name = new TableColumn<>("Наименование");
        name.setPrefWidth(280);
        name.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getProductProperties().getName())
        );

        TableColumn<Product, Double> total = new TableColumn<>("Итог");
        total.setPrefWidth(180);
        total.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Product, Double> price = new TableColumn<>("Цена");
        price.setPrefWidth(100);
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<Product, Double> discount = new TableColumn<>("Скидка");
        discount.setPrefWidth(100);
        discount.setCellValueFactory(new PropertyValueFactory<>("discount"));

        TableColumn<Product, Double> quantity = new TableColumn<>("Количество");
        quantity.setPrefWidth(100);
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableView<Product> table = new TableView<>();

        table.getColumns().addAll(Arrays.asList(id, article, name, price, quantity, discount, total));

        table.setRowFactory(rf -> {
            TableRow<Product> tr = new TableRow<>();
            tr.setOnMouseClicked(event -> {
                if (!tr.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    Product clickedRow = tr.getItem();
                    Dialog dialog = DialogUtil.getQuantityDialog(clickedRow.getQuantity());

                    Optional<String> result = dialog.showAndWait();

                    if (result.isPresent()) {
                        double entered = Double.parseDouble(result.get());
                        clickedRow.setQuantity(entered);
                        tr.getTableView().getItems().set(tr.getIndex(), clickedRow);

                        changed.onNext(calculateTotal(tr.getTableView()));
                    }
                }
            });

            return tr;
        });

        TableColumn actionColumn = new TableColumn("Удалить");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>(""));


        Callback<TableColumn<Product, String>, TableCell<Product, String>> cellFactory =  new Callback<>() {

            @Override
            public TableCell call(final TableColumn<Product, String> param) {
                final TableCell<Product, String> cell = new TableCell<>() {

                    final Button btn = new Button("");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setMinWidth(50);
                            btn.setMinHeight(30);

                            btn.setOnAction(event -> {
                                getTableView().getItems().remove(getIndex());
                                changed.onNext(calculateTotal(getTableView()));
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
                return cell;
            }
        };

        actionColumn.setCellFactory(cellFactory);
        table.getColumns().add(actionColumn);

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

        changed.onNext(calculateTotal(productTableView));
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

    public static boolean saveToDb(ExpenseBody expense) {
        return true;
    }
}