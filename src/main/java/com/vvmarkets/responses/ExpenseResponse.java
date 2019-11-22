package com.vvmarkets.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseResponse {
    @SerializedName("receipt_id")
    @Expose
    private String ReceiptId;

    @SerializedName("document_number")
    @Expose
    private String DocumentNumber;
}
