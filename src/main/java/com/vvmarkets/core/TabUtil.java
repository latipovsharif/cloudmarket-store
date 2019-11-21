package com.vvmarkets.core;

import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TabUtil {

    public static Tab NewTab() {
        LocalTime time = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        Tab newTab = new Tab();
        Label lbl = new Label(time.format(formatter));
        lbl.setStyle("-fx-rotate: 0; -fx-wrap-text: true;");
        lbl.setWrapText(true);
        lbl.setTextAlignment(TextAlignment.JUSTIFY);


        newTab.setGraphic(lbl);

        newTab.setContent(TableUtil.getTable());
        return newTab;
    }
}
