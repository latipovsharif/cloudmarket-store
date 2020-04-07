package com.vvmarkets.peripheral;

import com.vvmarkets.configs.RemoteConfig;
import com.vvmarkets.controllers.MainController;
import com.vvmarkets.core.Utils;
import com.vvmarkets.requests.ExpenseBody;
import com.vvmarkets.requests.ProductBody;
import javafx.collections.ObservableSet;
import javafx.print.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
//            addDashes(
//                    RemoteConfig.getConfig(
//                            RemoteConfig.ConfigType.PRINTER,
//                            RemoteConfig.ConfigSubType.LABEL_WIDTH)
//            );

            i++;
        }
    }

    private void getProductLineHeader() {
        String headerText = RemoteConfig.getConfig(RemoteConfig.ConfigType.PRINTER,RemoteConfig.ConfigSubType.LABEL_HEADER);
        headerText = headerText.
                replace("{counter}", "#").
                replace("{product}", "Товар").
                replace("{quantity}", "Кол-во").
                replace("{price}", "Цена").
                replace("{discount}", "Скидка").
                replace("{lineTotal}","Итого");

        addTextLine(headerText, "-fx-font-weight: bold; -fx-font-size: 9");
    }

    private void addTextLine(String text) {
        addTextLine(text, "");
    }



    private void addTextLine(String text, String style) {
        style = "-fx-font-family: 'Courier New'; " + style;
        Text t = new Text(text);
        t.setStyle(style);

        textFlow.getChildren().add(t);
        addNewLine();
    }

    private void addDashes(String paperSize) {
        String style = "-fx-font-weight: bold; -fx-padding: 0; -fx-border-insets: 0;";
        if (paperSize.equals("58")) {
            addTextLine("--------------------------", style);
        } else {
            addTextLine("------------------------------------------", style);
        }
    }

    private void addNewLine(int lines) {
        StringBuilder b = new StringBuilder();
        for(int i = 0; i < lines; i++) {
            b.append("\n");
        }

        textFlow.getChildren().add(new Text(b.toString()));
    }

    private void addNewLine() {
        addNewLine(1);
    }

    private String getProductLineTemplate() {
        return RemoteConfig.getConfig(RemoteConfig.ConfigType.PRINTER,RemoteConfig.ConfigSubType.LABEL_ROW);
    }

    private void getLineString(ProductBody product, int counter) {
        addTextLine(getProductLineTemplate()
                .replace("{counter}", String.valueOf(counter))
                .replace("{product}", product.getShortName())
                .replace("{quantity}", Utils.getFormatted(product.getQuantity()))
                .replace("{price}", Utils.getFormatted(product.getSellPrice()))
                .replace("{discount}", Utils.getFormatted(product.getDiscountPercent()))
                .replace("{lineTotal}", Utils.getFormatted(product.getTotal())),
                "-fx-font-size: 7; -fx-padding: 0; -fx-border-insets: 0;");
    }

    private void getHeader() {
        addTextLine("Кассир: " + MainController.seller.getFullName(),
                "-fx-font-size: 10");

        addTextLine(
                RemoteConfig.getConfig(
                        RemoteConfig.ConfigType.GENERAL,
                        RemoteConfig.ConfigSubType.STORE_NAME
                ),"-fx-font-size: 22; -fx-font-weight: bold");

        addNewLine();
    }

    private void getFooter() {
        String style = "-fx-font-size: 10px; -fx-font-weight: bold";
        addTextLine("Итого:       " + Utils.getFormatted(expenseBody.getPayment().getToPay()) + " c.", style);
        addTextLine("Получено:    " + Utils.getFormatted(expenseBody.getPayment().getTotalPayed()) + " c.", style);
        addTextLine("Сдача:       " + Utils.getFormatted(expenseBody.getPayment().getReturn()) + " c.", style);
        addNewLine();
        LocalDateTime time = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        addTextLine("Дата: " + formatter.format(time), style);
//        addTextLine("Номер в очереди","-fx-font-size: 15; -fx-alignment: center");
//
//        addTextLine("  " + MainController.getCheckCounter(),"-fx-font-size: 32; -fx-font-weight: bold; -fx-alignment: center");
//        addNewLine();
//        addTextLine("Служба доставки:","-fx-font-size: 14; -fx-font-weight: bold; -fx-alignment: center");
//        addTextLine(" (92) 100 0200","-fx-font-size: 14; -fx-font-weight: bold; -fx-alignment: center");
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

        Printer p;
        if (printer!=null) {
            p = getPrinter(printer);
        } else {
            p = Printer.getDefaultPrinter();
        }

        if (p != null) {
            doPrint(p);
        }

        String secondPrinter = RemoteConfig.getConfig(
                RemoteConfig.ConfigType.PRINTER_SECOND,
                RemoteConfig.ConfigSubType.NAME
        );

        if (secondPrinter != null) {
            p = getPrinter(secondPrinter);
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
