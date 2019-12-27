package com.vvmarkets.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpenseResponse {
    @SerializedName("receipt_id")
    @Expose
    private String ReceiptId;

    public String getReceiptId() {
        return ReceiptId;
    }

    public void setReceiptId(String receiptId) {
        ReceiptId = receiptId;
    }

    public String getDocumentNumber() {
        return DocumentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        DocumentNumber = documentNumber;
    }

    @SerializedName("document_number")
    @Expose
    private String DocumentNumber;
}
