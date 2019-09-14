package com.vvmarkets.peripheral;

import javafx.collections.ObservableSet;
import javafx.print.Printer;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class ThermalPrinter {

    public ThermalPrinter() {
        TextArea textArea = new TextArea();
        Button button = new Button("Get all printers");
        button.setOnAction(
            actionEvent -> {
                ObservableSet<Printer> printers = Printer.getAllPrinters();
                Printer defaultPrinter = Printer.getDefaultPrinter();

                for (Printer printer :
                        printers) {
                    textArea.appendText(printer.getName() + "\n");
                }
            }
        );
    }
}
