package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class HikariCPConfig {

    @Value("${db.url}")
    private String dbUrl;

    // Allow overriding via db.user/db.password but fall back to credentials embedded in the URL
    @Value("${db.user:}")
    private String dbUser;

    @Value("${db.password:}")
    private String dbPassword;

    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();

        // Database connection settings
        String normalizedJdbcUrl = normalizeJdbcUrl(dbUrl);
        config.setJdbcUrl(normalizedJdbcUrl);

        // Use explicit credentials when provided; otherwise allow the URL to contain them.
        if (dbUser != null && !dbUser.isBlank()) {
            config.setUsername(dbUser.trim());
        } else {
            Credentials creds = extractCredentialsFromUrl(normalizedJdbcUrl);
            if (creds.user != null) {
                config.setUsername(creds.user);
            }
        }

        if (dbPassword != null && !dbPassword.isBlank()) {
            config.setPassword(dbPassword);
        } else {
            Credentials creds = extractCredentialsFromUrl(normalizedJdbcUrl);
            if (creds.password != null) {
                config.setPassword(creds.password);
            }
        }

        config.setDriverClassName("org.postgresql.Driver");

        // Connection pool settings
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(5);
        config.setIdleTimeout(600000); // 10 minutes
        config.setMaxLifetime(1800000); // 30 minutes
        config.setConnectionTimeout(30000); // 30 seconds
        config.setLeakDetectionThreshold(60000); // 1 minute
        config.setValidationTimeout(5000); // 5 seconds

        // Pool name for monitoring
        config.setPoolName("HikariCP-Pool");

        // Connection testing
        config.setConnectionTestQuery("SELECT 1");

        // Auto-commit and read-only settings
        config.setAutoCommit(true);
        config.setReadOnly(false);

        // Register MBeans for JMX monitoring
        config.setRegisterMbeans(true);

        // Isolation for internal queries
        config.setIsolateInternalQueries(false);

        // Initialization fail timeout (0 = fail fast)
        config.setInitializationFailTimeout(0);

        return new HikariDataSource(config);
    }

    private String normalizeJdbcUrl(String rawUrl) {
        if (rawUrl == null) {
            return null;
        }

        String url = rawUrl.trim();

        // Allow users to provide a standard Postgres URI (postgresql://...) and normalize it to jdbc:postgresql://
        if (url.startsWith("postgresql://")) {
            url = "jdbc:" + url;
        }

        // Ensure SSL is required when connecting to Supabase (and should typically be on for remote Postgres)
        if (url.contains("supabase") && !url.contains("sslmode=")) {
            if (url.contains("?")) {
                url += "&sslmode=require";
            } else {
                url += "?sslmode=require";
            }
        }

        return url;
    }

    private Credentials extractCredentialsFromUrl(String url) {
        if (url == null) return new Credentials(null, null);

        String workingUrl = url;
        if (workingUrl.startsWith("jdbc:")) {
            workingUrl = workingUrl.substring("jdbc:".length());
        }

        // Try to parse query parameters like ?user=...&password=...
        int queryIndex = workingUrl.indexOf('?');
        if (queryIndex >= 0) {
            String query = workingUrl.substring(queryIndex + 1);
            Map<String, String> params = parseQueryParams(query);
            if (params.containsKey("user") || params.containsKey("password")) {
                return new Credentials(params.get("user"), params.get("password"));
            }
        }

        // Try to parse user:password@host part
        try {
            URI uri = new URI(workingUrl);
            String userInfo = uri.getUserInfo();
            if (userInfo != null && userInfo.contains(":")) {
                String[] parts = userInfo.split(":", 2);
                return new Credentials(parts[0], parts[1]);
            }
        } catch (URISyntaxException ignored) {
            // Ignore parsing errors; credentials might be provided elsewhere
        }

        return new Credentials(null, null);
    }

    private Map<String, String> parseQueryParams(String query) {
        Map<String, String> params = new HashMap<>();
        if (query == null || query.isBlank()) {
            return params;
        }

        for (String pair : query.split("&")) {
            int idx = pair.indexOf('=');
            if (idx > 0) {
                String key = pair.substring(0, idx).trim();
                String value = pair.substring(idx + 1).trim();
                params.put(key, value);
            }
        }
        return params;
    }

    private static class Credentials {
        final String user;
        final String password;

        Credentials(String user, String password) {
            this.user = user;
            this.password = password;
        }
    }
}
