package com.vvmarkets.core;

import com.vvmarkets.Main;
import com.vvmarkets.dao.Product;
import com.vvmarkets.requests.ExpenseBody;
import com.vvmarkets.requests.ProductBody;
import com.vvmarkets.utils.db;
import io.reactivex.subjects.PublishSubject;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.util.Callback;
import kotlin.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Arrays;
import java.util.Optional;

public class TableUtil {

    public static PublishSubject<Double> changed = PublishSubject.create();
    private static final Logger log = LogManager.getLogger(Main.class);

    public static TableView<Product> getTable() {
        TableColumn<Product, String> id = new TableColumn<>("Id");
        id.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getProductProperties().getId())
        );
        id.setVisible(false);

        TableColumn<Product, String> article = new TableColumn<>("Артикул");
        article.setPrefWidth(95);
        article.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getProductProperties().getArticle())
        );
        article.setVisible(false);

        TableColumn<Product, String> name = new TableColumn<>("Наименование");
        name.setPrefWidth(95);
        name.setCellValueFactory(param ->
                new SimpleStringProperty(param.getValue().getProductProperties().getName())
        );

        TableColumn<Product, Double> total = new TableColumn<>("Итог");
        total.setPrefWidth(80);
        total.setCellValueFactory(new PropertyValueFactory<>("total"));

        TableColumn<Product, Double> price = new TableColumn<>("Цена");
        price.setPrefWidth(45);
        price.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableView<Product> table = new TableView<>();

        table.getColumns().addAll(Arrays.asList(id, article, name, price));

        TableColumn subtract = new TableColumn("");
        subtract.setPrefWidth(40);
        subtract.setCellValueFactory(new PropertyValueFactory<>(""));


        Callback<TableColumn<Product, String>, TableCell<Product, String>> subtractCellFactory =  new Callback<>() {

            @Override
            public TableCell call(final TableColumn<Product, String> param) {
                final TableCell<Product, String> cell = new TableCell<>() {

                    final Button btn = new Button("-");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.getStyleClass().add("subtractBtn");

                            btn.setOnAction(event -> {
                                Product p = getTableView().getItems().get(getIndex());
                                if (p.getQuantity() > 1) {
                                    p.setQuantity(p.getQuantity() - 1);
                                    getTableView().getItems().set(getIndex(), p);
                                } else {
                                    getTableView().getItems().remove(getIndex());
                                }
                                changed.onNext(calculateTotal(getTableView()));
                            });
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        subtract.setCellFactory(subtractCellFactory);
        table.getColumns().add(subtract);

        TableColumn<Product, Double> quantity = new TableColumn<>("Количество");
        quantity.setPrefWidth(45);
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        table.getColumns().add(quantity);

        TableColumn add = new TableColumn("");
        add.setPrefWidth(40);
        add.setCellValueFactory(new PropertyValueFactory<>(""));

        Callback<TableColumn<Product, String>, TableCell<Product, String>> addCellFactory =  new Callback<>() {

            @Override
            public TableCell call(final TableColumn<Product, String> param) {
                final TableCell<Product, String> cell = new TableCell<>() {

                    final Button btn = new Button("+");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            btn.getStyleClass().add("addBtn");

                            btn.setOnAction(event -> {
                                Product p = getTableView().getItems().get(getIndex());
                                p.setQuantity(p.getQuantity() + 1);
                                getTableView().getItems().set(getIndex(), p);
                                changed.onNext(calculateTotal(getTableView()));
                            });
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        add.setCellFactory(addCellFactory);
        table.getColumns().addAll(add, total);


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
        PreparedStatement stmt = null;

        try (Connection connection = db.getConnection()) {
            connection.setAutoCommit(false);

            String sql = "insert into sold(seller_id, document_hash, discount_type, card_paid, cash_paid, to_pay, remained, change) values (?, ?, ?, ?, ?, ?, ?, ?);";
            stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, expense.getSellerId());
            stmt.setString(2, expense.getDocumentHash());
            stmt.setString(3, "percent");
            stmt.setDouble(4, expense.getPayment().getCardPaid());
            stmt.setDouble(5, expense.getPayment().getCashPaid());
            stmt.setDouble(6, expense.getPayment().getToPay());
            stmt.setDouble(7, expense.getPayment().getRemained());
            stmt.setDouble(8, 0);

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                log.error("cannot save sold affected rows equals zero");
                connection.rollback();
                return false;
            }

            long savedKey;

            try (ResultSet generatedKey = stmt.getGeneratedKeys()){
                if (generatedKey.next()) {
                    savedKey = generatedKey.getLong(1);
                } else {
                    log.error("cannot save sold cannot get generated key");
                    connection.rollback();
                    return false;
                }
            }

            sql = "insert into sold_details(sold_id, product_id, sell_price, quantity, discount_percent) values (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);

            for (ProductBody p : expense.getProducts()) {
                ps.setLong(1, savedKey);
                ps.setString(2, p.getProductId());
                ps.setDouble(3, p.getSellPrice());
                ps.setDouble(4, p.getQuantity());
                ps.setDouble(5, p.getDiscountPercent());
                ps.addBatch();
            }

            ps.executeBatch();

            connection.commit();
        } catch (Exception e) {
            Utils.logException(e, "cannot execute sold insert");
            return false;
        }

        return true;
    }
}