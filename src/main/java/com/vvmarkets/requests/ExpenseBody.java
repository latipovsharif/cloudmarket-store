package com.vvmarkets.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vvmarkets.configs.Config;
import com.vvmarkets.core.HttpConnectionHolder;
import com.vvmarkets.core.Utils;
import com.vvmarkets.dao.Product;
import com.vvmarkets.responses.ExpenseResponse;
import com.vvmarkets.services.ExpenseService;
import com.vvmarkets.services.RestClient;
import com.vvmarkets.utils.ResponseBody;
import com.vvmarkets.utils.db;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Call;
import retrofit2.Response;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExpenseBody {
    private static final Logger log = LogManager.getLogger(ExpenseBody.class);

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

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        Id = id;
    }

    @SerializedName("id")
    @Expose
    private String Id;

    public String getSoldBy() {
        return SoldBy;
    }

    public void setSoldBy(String soldBy) {
        SoldBy = soldBy;
    }

    @SerializedName("sold_by")
    @Expose
    private String SoldBy;

    public ExpenseBody() { }

    public List<ExpenseBody> getUnfinished() {
        List<ExpenseBody> lst = new ArrayList<>();
        ExpenseBody expense = null;
        PreparedStatement stmt;

        try (Connection connection = db.getConnection()) {
            stmt = connection.prepareStatement("select " +
                    "id, document_hash, seller_id, discount_type, card_paid, cash_paid, to_pay, remained, change, timestamp, sold_by" +
                    " from sold");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                expense = new ExpenseBody();
                expense.soldSource = 1;
                expense.setId(rs.getString(1));
                expense.setDocumentHash(rs.getString(2));
                expense.setSellerId(rs.getString(3));
                expense.setPayment(new PaymentBody(rs.getDouble(7),rs.getDouble(5),rs.getDouble(6)));
                expense.setProducts(getProductsFromDB(rs.getString(1)));
                expense.setCreatedAt(rs.getString(10));
                expense.setSoldBy(rs.getString(11));

                lst.add(expense);
            }
        } catch (Exception e) {
            Utils.logException(e, "cannot get product from DB");
        }

        return lst;
    }

    public boolean saveToDb() {
        try (Connection connection = db.getConnection()) {
            connection.setAutoCommit(false);

            String sql = "insert into sold(seller_id, document_hash, discount_type, card_paid, cash_paid, to_pay, remained, change, timestamp, sold_by) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, getSellerId());
            stmt.setString(2, getDocumentHash());
            stmt.setString(3, "percent");
            stmt.setDouble(4, getPayment().getCardPaid());
            stmt.setDouble(5, getPayment().getCashPaid());
            stmt.setDouble(6, getPayment().getToPay());
            stmt.setDouble(7, getPayment().getRemained());
            stmt.setDouble(8, 0);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            stmt.setString(9, sdf.format(new Date()));
            stmt.setString(10, Config.getAuthorizationKey());

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

            setId(String.valueOf(savedKey));

            sql = "insert into sold_details(sold_id, product_id, sell_price, quantity, discount_percent, barcode) values (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);

            for (ProductBody p : getProducts()) {
                ps.setLong(1, savedKey);
                ps.setString(2, p.getProductId());
                ps.setDouble(3, p.getSellPrice());
                ps.setDouble(4, p.getQuantity());
                ps.setDouble(5, p.getDiscountPercent());
                ps.setString(6, p.getBarcode());
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
    private List<ProductBody> getProductsFromDB(String soldId) {
        List<ProductBody> products = new ArrayList<>();
        PreparedStatement stmt;

        try (Connection connection = db.getConnection()) {
            stmt = connection.prepareStatement("select " +
                    " id, sold_id, product_id, sell_price, quantity, discount_percent, barcode " +
                    "from sold_details where sold_id = ?");
            stmt.setString(1, soldId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ProductBody product = new ProductBody();
                product.setProductId(rs.getString(3));
                product.setSellPrice(rs.getDouble(4));
                product.setQuantity(rs.getDouble(5));
                product.setDiscountPercent(rs.getInt(6));
                product.setBarcode(rs.getString(7));
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        this.setCreatedAt(sdf.format(new Date()));
        this.setSoldBy(Config.getAuthorizationKey());

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
            pb.setBarcode(p.getProductProperties().getBarcode());
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
