package com.devertix.utils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private final DataSource dataSource;

    public DatabaseInitializer(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void createSchemaIfNotExists() {
        try (Connection conn = dataSource.getConnection()) {
            if (!tableExists(conn, "groceries")) {
                try (Statement stmt = conn.createStatement()) {
                    String sql = "CREATE TABLE groceries (" +
                            "id INT PRIMARY KEY AUTO_INCREMENT," +
                            "name VARCHAR(255) NOT NULL," +
                            "quantity INT NOT NULL DEFAULT 1" +
                            ")";
                    stmt.execute(sql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing database schema", e);
        }
    }

    private boolean tableExists(Connection conn, String tableName) throws SQLException {
        DatabaseMetaData dbMetaData = conn.getMetaData();
        try (ResultSet tables = dbMetaData.getTables(null, null, tableName, null)) {
            return tables.next();
        }
    }
}
