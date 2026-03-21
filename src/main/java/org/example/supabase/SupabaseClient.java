package org.example.supabase;

import org.example.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.concurrent.TimeUnit;

@Component  // 添加 @Component 让 Spring 管理
public class SupabaseClient {

    private final DataSource dataSource;  // 使用 Spring 的 DataSource
    private final String dbUrl;

    @Autowired  // Spring 会自动注入 DataSource
    public SupabaseClient(DataSource dataSource) {
        this.dataSource = dataSource;
        this.dbUrl = AppConfig.getDbUrl();

        System.out.println("🚀 Initialized Supabase DB client with Spring DataSource for: " + AppConfig.getEnvName());
        System.out.println("📡 Using DB URL: " + redactJdbcUrl(dbUrl));
        testConnection();
    }

    /**
     * 测试连接是否正常
     */
    private void testConnection() {
        try (Connection conn = getConnection()) {
            System.out.println("✅ Successfully connected to database");
        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to database: " + e.getMessage());
        }
    }

    /**
     * 从 Spring 的 DataSource 获取连接
     * 现在使用的是 application-dev.properties 中配置的 HikariCP 连接池
     */
    public Connection getConnection() throws SQLException {
        try {
            Connection conn = dataSource.getConnection();
            // 可选：验证连接
            if (!conn.isValid(5)) {
                conn.close();
                throw new SQLException("Invalid connection from pool");
            }
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Failed to get connection from pool: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 简单连接测试
     */
    public boolean ping() {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT 1");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() && rs.getInt(1) == 1;
        } catch (SQLException e) {
            System.err.println("❌ Ping failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取连接池统计信息（通过 HikariCP 的 MXBean）
     */
    public String getPoolStats() {
        if (dataSource instanceof com.zaxxer.hikari.HikariDataSource) {
            com.zaxxer.hikari.HikariDataSource hikariDS = (com.zaxxer.hikari.HikariDataSource) dataSource;
            return String.format(
                    "Active: %d, Idle: %d, Total: %d, Waiting: %d",
                    hikariDS.getHikariPoolMXBean().getActiveConnections(),
                    hikariDS.getHikariPoolMXBean().getIdleConnections(),
                    hikariDS.getHikariPoolMXBean().getTotalConnections(),
                    hikariDS.getHikariPoolMXBean().getThreadsAwaitingConnection()
            );
        }
        return "Not using HikariCP";
    }

    private String redactJdbcUrl(String url) {
        if (url == null) return null;
        return url.replaceAll("(?i)(password=)[^&]+", "$1***");
    }

    // 用于测试
    public static void main(String[] args) {
        // 这个 main 方法现在不能直接运行，因为需要 Spring 上下文
        // 建议创建一个测试类或移除 main 方法
        System.out.println("Please run this class within Spring Boot context");
    }
}