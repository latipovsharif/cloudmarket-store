package com.vvmarkets.peripheral;

import com.vvmarkets.Main;
import com.vvmarkets.configs.RemoteConfig;
import com.vvmarkets.controllers.MainController;
import com.vvmarkets.core.Utils;
import com.vvmarkets.requests.ExpenseBody;
import com.vvmarkets.requests.ProductBody;
import javafx.print.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.print.*;
import java.util.List;

public class ThermalPrinter {

    ExpenseBody expenseBody = null;
    TextFlow textFlow = new TextFlow();

    private static final Logger log = LogManager.getLogger(Main.class);

    public ThermalPrinter(ExpenseBody body) {
        this.expenseBody = body;
    }

    private void getBody(List<ProductBody> expense) {
        getProductLineHeader();

        int i = 1;
        for (ProductBody p : expense) {
            textFlow.getChildren().add(getLineString(p, i));
            i++;
        }
    }

    private void getProductLineHeader() {
        textFlow.getChildren().add(new Text("Кассир:           " + MainController.seller.getFullName() + "\n"));
        Text storeText = new Text(RemoteConfig.getConfig(RemoteConfig.ConfigType.GENERAL, RemoteConfig.ConfigSubType.STORE_NAME) + "\n");
        storeText.setStyle("-fx-font-size: 20px");

        String headerText = RemoteConfig.getConfig(RemoteConfig.ConfigType.PRINTER,RemoteConfig.ConfigSubType.LABEL_HEADER);
        Text text = new Text(headerText.
                replace("{counter}", "#").
                replace("{product}", "Товар").
                replace("{quantity}", "Кол-во").
                replace("{price}", "Цена").
                replace("{discount}", "Скидка").
                replace("{lineTotal}","Итого")
                + "\n"
        );
        text.setStyle("-fx-font-weight: bold");
        textFlow.getChildren().add(text);
        textFlow.getChildren().add(new Text("--------------------------"));
    }

    private String getProductLineTemplate() {
        return RemoteConfig.getConfig(RemoteConfig.ConfigType.PRINTER,RemoteConfig.ConfigSubType.LABEL_ROW);
    }

    private Text getLineString(ProductBody product, int counter) {
        return new Text(getProductLineTemplate()
                .replace("{counter}", String.valueOf(counter))
                .replace("{product}", product.getName())
                .replace("{quantity}", String.valueOf(product.getQuantity()))
                .replace("{price}", String.valueOf(product.getSellPrice()))
                .replace("{discount}", String.valueOf(product.getDiscountPercent()))
                .replace("{lineTotal}", String.valueOf(product.getTotal())) + "\n");
    }

    private void getHeader() {
    }

    private void getFooter() {
        Text documentNumber = new Text("\n" + expenseBody.getId() + "\n");
        documentNumber.setStyle("-fx-font-size: 28; -fx-alignment: center");
        textFlow.getChildren().add(documentNumber);
    }

    private void formCheck() {
        getHeader();
        getBody(expenseBody.getProducts());
        getFooter();
    }

    public void print() {
        textFlow.setStyle("-fx-min-width: 500px; -fx-alignment: right");


        formCheck();

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            PageLayout layout = job.getPrinter().createPageLayout(
                    Paper.NA_LETTER,
                    PageOrientation.PORTRAIT,
                    Printer.MarginType.HARDWARE_MINIMUM
            );

            boolean printed = job.printPage(layout, textFlow);
            if (printed) {
                job.endJob();
            }

        } else {
            log.error("cannot create printer job");
        }
    }
}
