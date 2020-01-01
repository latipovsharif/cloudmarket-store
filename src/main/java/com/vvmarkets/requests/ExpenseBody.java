package com.vvmarkets.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vvmarkets.core.HttpConnectionHolder;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.Product;
import com.vvmarkets.responses.ExpenseResponse;
import com.vvmarkets.services.ExpenseService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.utils.ResponseBody;
import com.vvmarkets.utils.db;
import javafx.scene.control.TableView;
import retrofit2.Call;
import retrofit2.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ExpenseBody {
    public PaymentBody getPayment() {
        return payment;
    }

    public void setPayment(PaymentBody payment) {
        this.payment = payment;
    }

    public List<ProductBody> getProducts() {
        return products;
    }

    public void setProducts(List<ProductBody> products) {
        this.products = products;
    }

    public int getSoldSource() {
        return soldSource;
    }

    public void setSoldSource(int soldSource) {
        this.soldSource = soldSource;
    }

    public String getDocumentHash() {
        return documentHash;
    }

    public void setDocumentHash(String documentHash) {
        this.documentHash = documentHash;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getShiftId() {
        return shiftId;
    }

    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    @SerializedName("payment")
    @Expose
    private PaymentBody payment;

    @SerializedName("products")
    @Expose
    private List<ProductBody> products;

    @SerializedName("sold_source")
    @Expose
    private int soldSource;

    @SerializedName("document_hash")
    @Expose
    private String documentHash;

    @SerializedName("seller_id")
    @Expose
    private String sellerId;

    @SerializedName("shift_id")
    @Expose
    private String shiftId;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    private String Id;

    public ExpenseBody() {

    }

    public ExpenseBody getUnfinished() {
        ExpenseBody expense = null;
        PreparedStatement stmt;

        try (Connection connection = db.getConnection()) {
            stmt = connection.prepareStatement("select " +
                    "id, document_hash, seller_id, discount_type, card_paid, cash_paid, to_pay, remained, change " +
                    "from sold");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                expense = new ExpenseBody();
                expense.soldSource = 1;
                expense.setId(rs.getString(1));
                expense.setDocumentHash(rs.getString(2));
                expense.setSellerId(rs.getString(3));
                expense.setPayment(new PaymentBody(rs.getDouble(7),rs.getDouble(5),rs.getDouble(6)));
                expense.setProducts(getProductsFromDB(rs.getString(1)));

            }
        } catch (Exception e) {
            Utils.logException(e, "cannot get product from DB");
        }

        return expense;
    }

    private List<ProductBody> getProductsFromDB(String soldId) {
        List<ProductBody> products = new ArrayList<>();
        PreparedStatement stmt;

        try (Connection connection = db.getConnection()) {
            stmt = connection.prepareStatement("select " +
                    " id, sold_id, product_id, sell_price, quantity, discount_percent " +
                    "from sold_details where sold_id = ?");
            stmt.setString(1, soldId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductBody product = new ProductBody();
                product.setProductId(rs.getString(3));
                product.setDiscountPercent(rs.getInt(6));
                product.setQuantity(rs.getDouble(5));
                product.setSellPrice(rs.getDouble(4));
                products.add(product);
            }
        } catch (Exception e) {
            Utils.logException(e, "cannot get product from DB");
        }

        return products;
    }

    public ExpenseBody(TableView<Product> tableView, PaymentBody payment, String sellerId, String shiftId) {
        this.soldSource = 1;
        this.documentHash = UUID.randomUUID().toString();
        this.payment = payment;
        this.sellerId = sellerId;
        this.shiftId = shiftId;

        setProducts(tableView);
    }

    public void setProducts(TableView<Product> products) {
        List<ProductBody> res = new ArrayList<>();
        for (Product p: products.getItems()) {
            ProductBody pb = new ProductBody();
            pb.setProductId(p.getProductProperties().getId());
            pb.setDiscountPercent(p.getDiscount());
            pb.setQuantity(p.getQuantity());
            pb.setSellPrice(p.getPrice());
            pb.setName(p.getProductProperties().getName());
            res.add(pb);
        }

        this.products = res;
    }

    public ExpenseResponse SaveToNetwork() {
        if (HttpConnectionHolder.INSTANCE.shouldRetry()) {
            ExpenseService documentService = RestClient.getClient().create(ExpenseService.class);
            Call<ResponseBody<ExpenseResponse>> listProductCall = documentService.create(this);
            try {
                Response<ResponseBody<ExpenseResponse>> response = listProductCall.execute();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 0) {
                            return response.body().getBody();
                        }
                    }
                }
            } catch (Exception e) {
                Utils.logException(e, "cannot save transaction to the cloud");
            }
        }

        return null;
    }

    public void deleteFromDB() {
        PreparedStatement stmt;

        try (Connection connection = db.getConnection()) {
            connection.setAutoCommit(false);
            stmt = connection.prepareStatement("delete from sold_details where sold_id = ?");
            stmt.setString(1, this.getId());
            stmt.execute();

            stmt = connection.prepareStatement("delete from sold where id = ?");
            stmt.setString(1, this.getId());
            stmt.execute();

            connection.commit();
        } catch (Exception e) {
            Utils.logException(e, "cannot get product from DB");
        }
    }
}
