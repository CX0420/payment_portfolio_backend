package org.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public class AppConfig {
    private static final Properties properties = new Properties();

    static {
        loadProperties("application.properties");

        String env = resolveAppEnv();
        if (!env.isBlank()) {
            String envFile = "application-" + env + ".properties";
            loadProperties(envFile);
        }

        applyEnvironmentOverrides();
    }

    private static void loadProperties(String resourceName) {
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream(resourceName)) {
            if (input == null) {
                if ("application.properties".equals(resourceName)) {
                    System.err.println("⚠️ Warning: unable to find application.properties; using defaults");
                    setDefaults();
                }
                return;
            }

            properties.load(input);
            System.out.println("✅ Loaded configuration from " + resourceName);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config: " + resourceName, e);
        }
    }

    private static void setDefaults() {
        properties.setProperty("supabase.url", "https://your-project.supabase.co");
        properties.setProperty("supabase.key", "your-key");
        properties.setProperty("db.url", "jdbc:postgresql://localhost:5432/postgres");
        properties.setProperty("env.name", "development");
    }

    /**
     * Resolves the runtime environment for selecting application-<env>.properties.
     * Priority:
     * - JVM system property: -Dapp.env=dev
     * - OS env var: APP_ENV=dev
     *
     * Supported aliases:
     * - local, dev, sit, test, prod
     * - development -> dev, staging -> sit, production -> prod
     */
    private static String resolveAppEnv() {
        String raw = firstNonBlank(System.getProperty("app.env"), System.getenv("APP_ENV"));
        if (raw == null) return "";

        String env = raw.trim().toLowerCase(Locale.ROOT);
        return switch (env) {
            case "development" -> "dev";
            case "staging" -> "sit";
            case "production" -> "prod";
            default -> env;
        };
    }

    private static void applyEnvironmentOverrides() {
        overrideIfPresent("supabase.url", System.getenv("SUPABASE_URL"));
        overrideIfPresent("supabase.key", System.getenv("SUPABASE_KEY"));
        overrideIfPresent("db.url", System.getenv("DB_URL"));
        overrideIfPresent("db.user", System.getenv("DB_USER"));
        overrideIfPresent("db.password", System.getenv("DB_PASSWORD"));
        overrideIfPresent("env.name", System.getenv("ENV_NAME"));
    }

    private static void overrideIfPresent(String key, String value) {
        if (value != null && !value.isBlank()) {
            properties.setProperty(key, value.trim());
        }
    }

    private static String firstNonBlank(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    public static String getSupabaseUrl() {
        return properties.getProperty("supabase.url");
    }

    public static String getSupabaseKey() {
        return properties.getProperty("supabase.key");
    }

    public static String getDbUrl() {
        return properties.getProperty("db.url");
    }

    public static String getDbUser() {
        return properties.getProperty("db.user", "");
    }

    public static String getDbPassword() {
        return properties.getProperty("db.password", "");
    }

    public static String getEnvName() {
        return properties.getProperty("env.name");
    }

    public static void main(String[] args) {
        System.out.println("🔧 Current Environment: " + getEnvName());
        System.out.println("📡 Supabase URL: " + getSupabaseUrl());
        System.out.println("🗄️  Database URL: " + getDbUrl());
    }
}