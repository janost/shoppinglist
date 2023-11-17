package com.devertix.dao;

import com.devertix.model.GroceryItem;
import com.devertix.utils.DatabaseInitializer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroceryItemDAO {
    private final HikariDataSource dataSource;

    public GroceryItemDAO() {
        String dbUrl = String.format("jdbc:mysql://%s:%s/%s", System.getenv("RDS_HOSTNAME"), System.getenv("RDS_PORT"), System.getenv("RDS_DB_NAME"));
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(dbUrl);
        config.setUsername(System.getenv("RDS_USERNAME"));
        config.setPassword(System.getenv("RDS_PASSWORD"));
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        dataSource = new HikariDataSource(config);
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(dataSource);
        databaseInitializer.createSchemaIfNotExists();
    }

    public List<GroceryItem> getAllGroceryItems() {
        List<GroceryItem> groceries = new ArrayList<>();
        String sql = "SELECT id, name, quantity FROM groceries";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                GroceryItem grocery = new GroceryItem();
                grocery.setId(rs.getInt("id"));
                grocery.setName(rs.getString("name"));
                grocery.setQuantity(rs.getInt("quantity"));
                groceries.add(grocery);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // In a real-world scenario, handle exceptions more gracefully
        }
        return groceries;
    }

    public void addGrocery(GroceryItem groceryItem) {
        String sql = "INSERT INTO groceries (name, quantity) VALUES (?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, groceryItem.getName());
            stmt.setInt(2, groceryItem.getQuantity());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // In a real-world scenario, handle exceptions more gracefully
        }
    }

    public boolean removeGroceryItemById(int id) {
        String sql = "DELETE FROM groceries WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // In a real-world scenario, handle exceptions more gracefully
            return false;
        }
    }
}
