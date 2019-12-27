package com.vvmarkets.peripheral;

import com.vvmarkets.configs.RemoteConfig;
import com.vvmarkets.controllers.MainController;
import com.vvmarkets.requests.ExpenseBody;
import com.vvmarkets.requests.ProductBody;
import javafx.collections.ObservableSet;
import javafx.print.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;

public class ThermalPrinter {

    ExpenseBody expenseBody;
    TextFlow textFlow = new TextFlow();

    public ThermalPrinter(ExpenseBody body) {
        this.expenseBody = body;
    }

    private void getBody(List<ProductBody> expense) {
        getProductLineHeader();

        addDashes(
                RemoteConfig.getConfig(
                        RemoteConfig.ConfigType.PRINTER,
                        RemoteConfig.ConfigSubType.LABEL_WIDTH)
        );

        int i = 1;
        for (ProductBody p : expense) {
            getLineString(p, i);
            i++;
        }

        addDashes(
                RemoteConfig.getConfig(
                        RemoteConfig.ConfigType.PRINTER,
                        RemoteConfig.ConfigSubType.LABEL_WIDTH)
        );
    }

    private void getProductLineHeader() {
        addTextLine("Кассир:           " + MainController.seller.getFullName());
        addTextLine(
                RemoteConfig.getConfig(
                        RemoteConfig.ConfigType.GENERAL,
                        RemoteConfig.ConfigSubType.STORE_NAME) ,
                "-fx-font-size: 20px");

        String headerText = RemoteConfig.getConfig(RemoteConfig.ConfigType.PRINTER,RemoteConfig.ConfigSubType.LABEL_HEADER);
        headerText = headerText.
                replace("{counter}", "#").
                replace("{product}", "Товар").
                replace("{quantity}", "Кол-во").
                replace("{price}", "Цена").
                replace("{discount}", "Скидка").
                replace("{lineTotal}","Итого");

        addTextLine(headerText, "-fx-font-weight: bold");
    }

    private void addTextLine(String text) {
        addTextLine(text, "");
    }

    private void addTextLine(String text, String style) {
        Text t = new Text(text);
        t.setStyle(style);

        textFlow.getChildren().add(t);
        addNewLine();
    }

    private void addDashes(String paperSize) {
        String style = "-fx-font-weight: bold;";
        if (paperSize.equals("58")) {
            addTextLine("------------------------------", style);
        } else {
            addTextLine("----------------------------------------", style);
        }
    }

    private void addNewLine() {
        textFlow.getChildren().add(new Text("\n"));
    }

    private String getProductLineTemplate() {
        return RemoteConfig.getConfig(RemoteConfig.ConfigType.PRINTER,RemoteConfig.ConfigSubType.LABEL_ROW);
    }

    private void getLineString(ProductBody product, int counter) {
        addTextLine(getProductLineTemplate()
                .replace("{counter}", String.valueOf(counter))
                .replace("{product}", product.getName())
                .replace("{quantity}", String.valueOf(product.getQuantity()))
                .replace("{price}", String.valueOf(product.getSellPrice()))
                .replace("{discount}", String.valueOf(product.getDiscountPercent()))
                .replace("{lineTotal}", String.valueOf(product.getTotal())));
    }

    private void getHeader() {

    }

    private void getFooter() {
        addTextLine(expenseBody.getId(),"-fx-font-size: 28; -fx-alignment: center");
    }

    private void formCheck() {
        getHeader();
        getBody(expenseBody.getProducts());
        getFooter();
    }

    public void print() {
        formCheck();

        String printer = RemoteConfig.getConfig(
                RemoteConfig.ConfigType.PRINTER,
                RemoteConfig.ConfigSubType.NAME
        );

        if (printer!=null) {
            Printer p = getPrinter(printer);
            doPrint(p);
        }

        String secondPrinter = RemoteConfig.getConfig(
                RemoteConfig.ConfigType.PRINTER_SECOND,
                RemoteConfig.ConfigSubType.NAME
        );

        if (secondPrinter != null) {
            Printer p = getPrinter(secondPrinter);
            doPrint(p);
        }
    }

    private Printer getPrinter(String printerName) {
        Printer myPrinter = null;
        ObservableSet<Printer> printers = Printer.getAllPrinters();
        for(Printer printer : printers){
            if(printer.getName().matches(printerName)){
                myPrinter = printer;
            }
        }
        return myPrinter;
    }

    private void doPrint(Printer printer) {
        PrinterJob job = PrinterJob.createPrinterJob(printer);
        PageLayout layout = job.getPrinter().createPageLayout(
                Paper.NA_LETTER,
                PageOrientation.PORTRAIT,
                Printer.MarginType.HARDWARE_MINIMUM
        );

        boolean printed = job.printPage(layout, textFlow);
        if (printed) {
            job.endJob();
        }
    }
}
