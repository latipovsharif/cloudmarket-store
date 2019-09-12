package com.vvmarkets.configs;

import com.vvmarkets.Main;
import com.vvmarkets.utils.db;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Config {
    private static final Logger log = LogManager.getLogger(Main.class);
    private static final String authorizationKey = "AUTHORIZATION";
    private static final String serverIP = "SERVER_IP";
    private static final String cashToken = "CASH_TOKEN";

    private static String getConfig(String key) {
        try {
            Connection c = db.getConnection();
            PreparedStatement stmt = c.prepareStatement("select val from configs where key = ? limit 1");
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();
            String val = rs.getString("val");
            c.close();
            return val;
        } catch (Exception e) {
            return "";
        }
    }

    private static boolean checkConfigExists(String key, String val) {
        try{
            Connection c = db.getConnection();
            PreparedStatement stmt = c.prepareStatement("select count(*) from configs where key = ?");
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();
            int count = rs.getInt(1);
            c.close();
            return count > 0;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    private static boolean setConfig(String key, String val) {
        try{
            Connection c = db.getConnection();
            PreparedStatement stmt = null;

            if (checkConfigExists(key, val)) {
                stmt = c.prepareStatement("update configs set val = ? where key = ?");
                stmt.setString(1, val);
                stmt.setString(2, key);
            } else {
                stmt = c.prepareStatement("insert into configs (key, val) values (?, ?)");
                stmt.setString(1, key);
                stmt.setString(2, val);
            }
            stmt.execute();
            c.close();
            return true;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    public static String getAuthorizationKey() {
        return getConfig(authorizationKey);
    }

    public static String getServerIP() {
        return getConfig(serverIP);
    }

    public static String getCashToken() {
        return getConfig(cashToken);
    }

    public static boolean setCashToken(String token) {
        return setConfig(cashToken, token);
    }
}
