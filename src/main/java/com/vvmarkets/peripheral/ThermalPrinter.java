package com.vvmarkets.peripheral;

import com.vvmarkets.Main;
import com.vvmarkets.requests.ExpenseBody;
import com.vvmarkets.requests.ProductBody;
import javafx.print.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ThermalPrinter {

    ExpenseBody expenseBody = null;
    private static final Logger log = LogManager.getLogger(Main.class);

    public ThermalPrinter(ExpenseBody body) {
        this.expenseBody = body;
    }

    private String getBody(List<ProductBody> expense) {
        StringBuilder body = new StringBuilder();
        int i = 1;
        for (ProductBody p : expense) {
            body.append(getLineString(p, i));
            body.append("\n");
            i++;
        }

        return body.toString();
    }

    private String getProductLineTemplate() {
        return "{counter}. {product} \n {quantity} x {price} - {discount} = {lineTotal}";
    }

    private String getLineString(ProductBody product, int counter) {
        return getProductLineTemplate()
                .replace("{counter}", String.valueOf(counter))
                .replace("{product}", product.getName())
                .replace("{quantity}", String.valueOf(product.getQuantity()))
                .replace("{price}", String.valueOf(product.getSellPrice()))
                .replace("{discount}", String.valueOf(product.getDiscountPercent()))
                .replace("{lineTotal}", String.valueOf(product.getTotal()));
    }

    private String getHeader() {
        return "";

    }

    private String getFooter() {
        return "";
    }

    public void print() {
        StringBuilder builder = new StringBuilder();
        builder.append(getHeader());
        builder.append(getBody(expenseBody.getProducts()));
        builder.append(getFooter());

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            TextFlow textFlow = new TextFlow(new Text(builder.toString()));
            textFlow.setStyle("-fx-font-size: 10");
            PageLayout layout = job.getPrinter().createPageLayout(Paper.NA_LETTER, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);
            job.printPage(layout, textFlow);
        } else {
            log.error("cannot create printer job");
        }
    }
}
