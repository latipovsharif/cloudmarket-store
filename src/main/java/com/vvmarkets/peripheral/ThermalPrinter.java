package com.vvmarkets.peripheral;

import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.Units;
import com.vvmarkets.Main;
import com.vvmarkets.requests.ExpenseBody;
import com.vvmarkets.requests.ProductBody;
import javafx.print.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.print.PrintException;
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
        return "<b>{counter}.</b> {product} \n {quantity} x {price} - {discount} = {lineTotal}";
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
//            Paper p = PrintHelper.createPaper("58mm",58,58, Units.MM);
//            PageLayout layout = job.getPrinter().createPageLayout(p, PageOrientation.LANDSCAPE, Printer.MarginType.DEFAULT);
//
//            textFlow.setMaxWidth(layout.getPrintableWidth());
            job.printPage(textFlow);
        } else {
            log.error("cannot create printer job");
        }
    }
}

