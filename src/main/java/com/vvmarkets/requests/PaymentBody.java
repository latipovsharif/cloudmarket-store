package com.vvmarkets.requests;

//        "discount_type": "percent",
//        "paid_by_credit_card": 0,
//        "paid_in_cash": 0,
//        "to_pay": 44,
//        "remained": 0

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentBody {
    @SerializedName("discount_type")
    @Expose
    private String DiscountType;

    @SerializedName("paid_by_credit_card")
    @Expose
    private Double CardPaid;

    @SerializedName("paid_in_cash")
    @Expose
    private Double CashPaid;

    @SerializedName("to_pay")
    @Expose
    private Double ToPay;

    @SerializedName("remained")
    @Expose
    private Double Remained;
}
