package com.vvmarkets.core;

import com.vvmarkets.Main;
import javafx.application.Platform;
import javafx.scene.Node;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

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
//            NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
//            Number n = nf.parse(value);
//            res = n.doubleValue();
        } catch (Exception e) {
            log.error("cannot parse string to double " + e.getMessage());
        }

        return res;
    }

    public static String getFormatted(double value) {
        NumberFormat nf = new DecimalFormat("#0.00");
        return nf.format(value);
    }

    public static Double round(double v) {
        NumberFormat nf = new DecimalFormat("#0.00");
        return Double.valueOf(nf.format(v));
    }

    public String formattedValue(String value) {
        double res = getDoubleOrZero(value);
        return getFormatted(res);
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
