package com.vvmarkets.requests;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class  PaymentBody {
    public String getDiscountType() {
        return DiscountType;
    }

    public void setDiscountType(String discountType) {
        DiscountType = discountType;
    }

    public Double getCardPaid() {
        return CardPaid;
    }

    public void setCardPaid(Double cardPaid) {
        CardPaid = cardPaid;
    }

    public Double getCashPaid() {
        return CashPaid;
    }

    public void setCashPaid(Double cashPaid) {
        CashPaid = cashPaid;
    }

    public Double getToPay() {
        return ToPay;
    }

    public void setToPay(Double toPay) {
        ToPay = toPay;
    }

    public Double getRemained() {
        return 0.0;
    }

    public Double getReturn() {
        return getTotalPayed() - getToPay();
    }

    public int getDiscountPercent() {
        return DiscountPercent;
    }

    public void setDiscountPercent(int discountPercent) {
        DiscountPercent = discountPercent;
    }

    @SerializedName("discount_type")
    @Expose
    private String DiscountType;

    @SerializedName("discount")
    @Expose
    private int DiscountPercent;

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

    @SerializedName("total_payed")
    @Expose
    private Double TotalPayed;

    public Double getTotalPayed() {
        return this.TotalPayed;
    }

    public PaymentBody(double totalCost, double cardPaid, double cashPaid, int discount) {
        this.DiscountType = "percent";

        double toPay = totalCost - (totalCost * discount / 100);
        this.CardPaid = cardPaid;
        // make sure that cash paid + card paid not more than
        // total payed
        this.CashPaid = cashPaid;
        this.TotalPayed = CardPaid + CashPaid;
        this.DiscountPercent = discount;
        this.ToPay = toPay;
    }

    public boolean isValid() {
        if (CardPaid > ToPay || CardPaid + CashPaid < ToPay) {
            return false;
        }

        double payed = CardPaid + CashPaid;
        if (payed >= ToPay) {
            CashPaid = ToPay - CardPaid;
        }

        return payed >= ToPay;
    }
}
