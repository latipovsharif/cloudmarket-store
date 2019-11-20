package com.vvmarkets.core;

import javafx.scene.control.Tab;

public class TabUtil {

    public static Tab NewTab() {
        Tab newTab = new Tab("this should be super tab");
//        newTab.getStyleClass().add("mainTab");
//        newTab.getStyleClass().add("mainTabActive");

        newTab.setContent(TableUtil.getTable());

        return newTab;
    }
}
