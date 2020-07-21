package com.vvmarkets.core;

import com.vvmarkets.Main;
import javafx.application.Platform;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Utils {

    private static final Logger log = LogManager.getLogger(Utils.class);

    public static void showScreen(Node node) {
        Platform.runLater(() -> {
            Main.getMainContainer().getChildren().clear();
            Main.getMainContainer().getChildren().add(node);
        });
    }

    public static double getDoubleOrZero(String value) {
        if (value.isEmpty()) {
            return 0;
        }

        double res = 0;
        try {
            res = Double.parseDouble(value);
        } catch (Exception e) {
            log.error("cannot parse string to double " + e.getMessage());
        }

        return round(res, 3);
    }

    private static double round(double value, int scale) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(scale, RoundingMode.CEILING);
        return bd.doubleValue();
    }

    public static String getFormatted(double value) {
        NumberFormat nf = new DecimalFormat("#0.000");
        return nf.format(value);
    }

    public static Double round(double v) {
        NumberFormat nf = new DecimalFormat("#0.000");
        return Double.valueOf(nf.format(v));
    }

    private static String stackToString(StackTraceElement[] traces) {
        StringBuilder res = new StringBuilder();
        for (StackTraceElement t : traces) {
            res.append(t.toString());
            res.append("\n\t");
        }
        return res.toString();
    }

    public static void logException(Exception e, String description) {
        log.error("Description: " + description + ": \n\t Message: " + e.getMessage() + "\n\t Stack: " +
                stackToString(e.getStackTrace()));
    }
}
