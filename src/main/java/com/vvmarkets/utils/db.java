package com.vvmarkets.utils;

import com.vvmarkets.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;

public class db {
    private static final Logger log = LogManager.getLogger(Main.class);

    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:db.db");
        } catch (Exception e) {
            connection = null;
            log.debug(e.getClass().getName() + ": " + e.getMessage());
        }

        return connection;
    }
}
