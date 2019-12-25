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
import javafx.scene.control.TableView;
import retrofit2.Call;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


//{
//        "document_hash": "cd01asdf472e-36asdfe8-42c7-bd6asdfa-eadac3354810",
//        "seller_id": "471f0faf-0caa-4994-9306-2370a76315b6",
//        "shift_id": "3f584e1c-e2f5-4dc4-addb-e5000dae9889",
//        "payment": {
//        "discount_type": "percent",
//        "paid_by_credit_card": 0,
//        "paid_in_cash": 0,
//        "to_pay": 44,
//        "remained": 0
//        },
//        "sold_source": 1,
//        "products": [
//            {
//                "sell_price": 22,
//                "product_id": "478bd4d4-1d4b-4003-9934-36c83f1c85a9",
//                "quantity": 2,
//                "discount_percent": 0
//            }
//        ]
//        }
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

    public ExpenseBody(TableView<Product> tableView, PaymentBody payment, String sellerId, String shiftId) {
        this.soldSource = 1;
        this.documentHash = UUID.randomUUID().toString();
        this.payment = payment;
        this.sellerId = sellerId;
        this.shiftId = shiftId;

        setProducts(tableView);
    }

    public void setProducts(TableView<Product> products) {
        List<ProductBody> res = new ArrayList<ProductBody>();
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

    public boolean SaveToNetwork() {
        if (HttpConnectionHolder.INSTANCE.shouldRetry()) {
            ExpenseService documentService = RestClient.getClient().create(ExpenseService.class);
            Call<ResponseBody<ExpenseResponse>> listProductCall = documentService.create(this);
            try {
                Response<ResponseBody<ExpenseResponse>> response = listProductCall.execute();

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getStatus() == 0) {
                            return false;
                        }
                    }
                }
            } catch (Exception e) {
                Utils.logException(e, "cannot save transaction to the cloud");
            }
        }

        return true;
    }
}
