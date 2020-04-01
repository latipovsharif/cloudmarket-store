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
        return Remained;
    }

    public Double getReturn() {
        return getTotalPayed() - getToPay();
    }

    public void setRemained(Double remained) {
        Remained = remained;
    }

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
    private Double TotalPayed;

    public Double getTotalPayed() {
        return this.TotalPayed;
    }

    public PaymentBody(double toPay, double cardPaid, double cashPaid) {
        this.DiscountType = "percent";
        this.CardPaid = cardPaid;
        this.CashPaid = cashPaid;
        this.TotalPayed = cashPaid + cardPaid;
        if (toPay < getTotalPayed()) {
            setRemained(0.0);
        } else {
            setRemained(toPay - getTotalPayed());
        }

        this.ToPay = toPay;
    }

    // FIXME возможно следует возвращать ошибку валидации если
    // сумма оплаты наличными + безналичными больше чем сумма чека
    // если сумма оплаты безналичными больше 0
    public boolean isValid() {
        if (CardPaid > ToPay) {
            return false;
        }

        // TODO сделать возможность оплатить авансом в далеком будущем
        double payed = CardPaid + CashPaid;
        if (payed > ToPay) {
            CashPaid = ToPay - CardPaid;
        }

        if (payed < ToPay) {
            return false;
        }

        return true;
    }
}
