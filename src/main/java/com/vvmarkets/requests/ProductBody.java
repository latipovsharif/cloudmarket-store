package com.vvmarkets.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
        //        {
            //        "sell_price": 22,
            //        "product_id": "478bd4d4-1d4b-4003-9934-36c83f1c85a9",
            //        "quantity": 2,
            //        "discount_percent": 0
        //        }
    //        ]
//        }
public class ProductBody {
    @SerializedName("sell_price")
    @Expose
    private Double SellPrice;

    @SerializedName("product_id")
    @Expose
    private String ProductId;

    @SerializedName("quantity")
    @Expose
    private Double Quantity;

    @SerializedName("discount_percent")
    @Expose
    private Double DiscountPercent;
}
