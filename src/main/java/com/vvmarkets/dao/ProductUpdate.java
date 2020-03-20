package com.vvmarkets.dao;

import com.vvmarkets.utils.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProductUpdate {
    public static long getCurrentVersion() {
        long version = 0;
        PreparedStatement stmt = null;

        try (Connection connection = db.getConnection()) {
            stmt = connection.prepareStatement("select current_version from product_migration");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                version = rs.getInt("current_version");
            }
        } catch (Exception e) {
            return version;
        }

        return version;
    }

    public static boolean setCurrentVersion(long version) {
        PreparedStatement stmt = null;

        try (Connection connection = db.getConnection()) {
            stmt = connection.prepareStatement("update product_migration set current_version = ?");
            stmt.setLong(1, version);
            stmt.executeUpdate();
            stmt.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }
}
