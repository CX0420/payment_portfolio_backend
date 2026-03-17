package org.example.supabase;

import org.example.config.AppConfig;

import java.sql.*;

public class SupabaseClient {
    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public SupabaseClient() {
        // Get the full database URL from config
        this.dbUrl = ensureSslMode(AppConfig.getDbUrl());

        // Parse username and password from the URL
        String[] credentials = parseCredentialsFromUrl(dbUrl);
        this.dbUser = credentials[0];
        this.dbPassword = credentials[1];

        System.out.println("🚀 Initialized Supabase DB client for: " + AppConfig.getEnvName());
        System.out.println("📡 Using DB URL: " + redactJdbcUrl(dbUrl));
        System.out.println("👤 Connecting as user: " + dbUser);
    }

    /**
     * Parse username and password from JDBC URL
     * Format: jdbc:postgresql://host:port/database?user=username&password=password
     */
    private String[] parseCredentialsFromUrl(String url) {
        String[] result = new String[2]; // [0] = user, [1] = password
        result[0] = "";
        result[1] = "";

        try {
            // Find the query part after '?'
            int queryStart = url.indexOf('?');
            if (queryStart > 0) {
                String query = url.substring(queryStart + 1);
                String[] params = query.split("&");

                for (String param : params) {
                    String[] keyValue = param.split("=");
                    if (keyValue.length == 2) {
                        if ("user".equals(keyValue[0])) {
                            result[0] = keyValue[1];
                        } else if ("password".equals(keyValue[0])) {
                            result[1] = keyValue[1];
                        }
                    }
                }
            }

            // If not found in URL, try to get from separate config
            if (result[0].isEmpty()) {
                result[0] = AppConfig.getDbUser(); // Fallback to separate property
            }
            if (result[1].isEmpty()) {
                result[1] = AppConfig.getDbPassword(); // Fallback to separate property
            }

        } catch (Exception e) {
            System.err.println("⚠️ Error parsing credentials from URL: " + e.getMessage());
            // Fallback to separate properties
            result[0] = AppConfig.getDbUser();
            result[1] = AppConfig.getDbPassword();
        }

        return result;
    }

    private String redactJdbcUrl(String url) {
        if (url == null) return null;
        // Redact any password=... in query string
        return url.replaceAll("(?i)(password=)[^&]+", "$1***");
    }

    /**
     * Supabase Postgres requires SSL. If sslmode is missing, default to require.
     */
    private String ensureSslMode(String url) {
        if (url == null || url.isBlank()) return url;
        if (!url.startsWith("jdbc:postgresql://")) return url;
        if (url.matches("(?i).*([?&])sslmode=.*")) return url;

        String separator = url.contains("?") ? "&" : "?";
        return url + separator + "sslmode=require";
    }

    /**
     * Get connection to Supabase PostgreSQL
     */
    public Connection getConnection() throws SQLException {
        try {
            // Ensure PostgreSQL driver is loaded
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found. Add dependency: org.postgresql:postgresql", e);
        }

        // Use the URL directly - it already contains user and password
        return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    /**
     * Simple connectivity check.
     * Uses a trivial query so it works on any Postgres DB.
     */
    public boolean ping() throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement("select 1");
             ResultSet rs = ps.executeQuery()) {
            return rs.next() && rs.getInt(1) == 1;
        }
    }

    public static void main(String[] args) {
        SupabaseClient client = new SupabaseClient();
        try {
            System.out.println("✅ DB ping: " + (client.ping() ? "OK" : "FAILED"));
        } catch (Exception e) {
            System.err.println("❌ Error: " + e.getMessage());
            e.printStackTrace();
        }
    }


}