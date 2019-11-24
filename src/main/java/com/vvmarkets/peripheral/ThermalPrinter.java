package com.vvmarkets.peripheral;

import com.vvmarkets.requests.ExpenseBody;
import com.vvmarkets.requests.ProductBody;
import javafx.collections.ObservableSet;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import javax.print.*;
import javax.print.attribute.DocAttributeSet;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;

public class ThermalPrinter {

    ExpenseBody expenseBody = null;

    public ThermalPrinter(ExpenseBody body) {
        this.expenseBody = body;
    }

    private String getBody(List<ProductBody> expense) {
        StringBuilder body = new StringBuilder();
        for (ProductBody p : expense) {
            body.append(getLineString(p));
            body.append("\n");
        }

        return body.toString();
    }

    private String getProductLineTemplate() {
        return "{product} \n {quantity} x {price} - {discount} = {lineTotal}";
    }

    private String getLineString(ProductBody product) {
        return getProductLineTemplate()
                .replace("{product}", product.getName())
                .replace("{quantity}", String.valueOf(product.getQuantity()))
                .replace("{discount}", String.valueOf(product.getDiscountPercent()))
                .replace("{lineTotal}", String.valueOf(product.getTotal()));
    }

    private String getHeader() {
        return "";

    }

    private String getFooter() {
        return "";
    }

    public void print() throws PrintException {
        StringBuilder builder = new StringBuilder();
        builder.append(getHeader());
        builder.append(getBody(expenseBody.getProducts()));
        builder.append(getFooter());

        Doc doc = new SimpleDoc(builder.toString(), DocFlavor.STRING.TEXT_HTML, null);
        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        DocPrintJob job = service.createPrintJob();
        job.print(doc, null);

//        ObservableSet<Printer> printers = Printer.getAllPrinters();
//        Printer defaultPrinter = Printer.getDefaultPrinter();
//
//        for (Printer printer :
//                printers) {
//
//        }
    }
}

