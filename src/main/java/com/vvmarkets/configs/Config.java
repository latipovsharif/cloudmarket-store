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
    private static final String networkRetryTimeout = "NETWORK_UNREACHABLE_RETRY_TIMEOUT";

    private static final String offlineMode = "OFFLINE_MODE";
    private static final String syncTimeout = "SYNC_TIMEOUT";
    private static final String loginSuffix = "LOGIN_SUFFIX";
    

    public static int getSyncTimeout() {
        int res;
        try {
            res = Integer.parseInt(getConfig(syncTimeout));
        } catch (Exception e) {
            res = 60;
        }
        return res;
    }

    public static boolean setSyncTimeout(int value) {
        return setConfig(syncTimeout, String.valueOf(value));
    }

    private static String getConfig(String key) {
        try (Connection c = db.getConnection()) {
            PreparedStatement stmt = c.prepareStatement("select val from configs where key = ? limit 1");
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();
            return rs.getString("val");
        } catch (Exception e) {
            return "";
        }
    }

    private static boolean checkConfigExists(String key, String val) {
        try (Connection c = db.getConnection()){
            PreparedStatement stmt = c.prepareStatement("select count(*) from configs where key = ?");
            stmt.setString(1, key);
            ResultSet rs = stmt.executeQuery();
            int count = rs.getInt(1);
            return count > 0;
        } catch (Exception e) {
            log.error(e);
            return false;
        }
    }

    private static boolean setConfig(String key, String val) {
        try (Connection c = db.getConnection()){
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

    public static boolean setAuthorizationKey(String token) {
        return setConfig(authorizationKey, token);
    }

    public static int getNetworkRetryTimeout() {
        int res;
        try{
            res = Integer.parseInt(getConfig(networkRetryTimeout));
        } catch (Exception e) {
            res = 5;
            log.error("cannot parse network unreachable timeout from database");
        }
        return res;
    }

    public static boolean getOfflineMode() {
        boolean off= false;
        try {
           off = Boolean.parseBoolean(getConfig(offlineMode));
        } catch (Exception e) {
            log.warn("cannot get offline mode from database");
        }
        return off;
    }

    public static String getLoginSuffix() {
        return getConfig(loginSuffix);
    }

    public static boolean setLoginSuffix(String suffix){
        return setConfig(loginSuffix, suffix);
    }
}
