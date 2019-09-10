package com.vvmarkets.configs;

import com.vvmarkets.utils.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Config {
    private static final String authorizationKey = "AUTHORIZATION";
    private static final String serverIP = "SERVER_IP";

    private static String getConfig(String key) {
        try {
            Connection c = db.getConnection();
            PreparedStatement stmt = c.prepareStatement("select val from configs where key = ? limit 1");
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();
            return rs.getString("val");
        } catch (Exception e) {
            return "";
        }
    }

    public static String getAuthorizationKey() {
        return getConfig(authorizationKey);
    }

    public static String getServerIP() {
        return getConfig(serverIP);
    }
}
