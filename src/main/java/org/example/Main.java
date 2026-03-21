package org.example;

import org.example.config.AppConfig;
import org.example.redis.RedisConfig;
import org.example.supabase.SupabaseClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner startupLog(DataSource dataSource) {  // 注入 DataSource
        return args -> {
            System.out.println("=".repeat(50));
            System.out.println("🚀 Payment Portfolio Backend (Spring Boot)");
            System.out.println("=".repeat(50));
            System.out.println("Environment: " + AppConfig.getEnvName());
            System.out.println("Supabase URL: " + AppConfig.getSupabaseUrl());

            // 使用注入的 DataSource 创建 SupabaseClient
            try {
                SupabaseClient supabaseClient = new SupabaseClient(dataSource);
                boolean ok = supabaseClient.ping();
                System.out.println("✅ DB ping: " + (ok ? "OK" : "FAILED"));

                // 显示连接池状态
                System.out.println("📊 Pool stats: " + supabaseClient.getPoolStats());

            } catch (Exception e) {
                System.err.println("❌ DB ping failed: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("🚀 Testing Upstash Redis Connection");
            System.out.println("=".repeat(50));

            // Test 1: Basic connection
            if (RedisConfig.isHealthy()) {
                System.out.println("✅ Redis connection is healthy");
            } else {
                System.err.println("❌ Redis connection failed");
                return;
            }

            // 可选：测试 Redis 会话功能（如果需要）
            // testRedisSession();

            RedisConfig.closePool();
            System.out.println("=".repeat(50));
        };
    }

    // 如果需要测试 Redis 会话，可以取消注释这个方法
    /*
    private void testRedisSession() {
        try {
            // Test 2: Create a test session
            SessionManager sessionManager = new SessionManager();
            MobileUser testUser = new MobileUser();
            testUser.setMobileUserId("test123");
            testUser.setMobileUserName("Test User");
            testUser.setEmail("test@example.com");

            String sessionId = sessionManager.createSession(testUser);
            System.out.println("✅ Created session: " + sessionId);

            // Test 3: Retrieve session
            MobileUser retrievedUser = sessionManager.getSession(sessionId);
            if (retrievedUser != null) {
                System.out.println("✅ Retrieved user: " + retrievedUser.getMobileUserName());
            }

            // Test 4: Check TTL
            long ttl = sessionManager.getSessionTtl(sessionId);
            System.out.println("⏰ Session TTL: " + ttl + " seconds");

            // Test 5: Clean up
            sessionManager.deleteSession(sessionId);
            System.out.println("✅ Cleaned up test session");
        } catch (Exception e) {
            System.err.println("❌ Redis session test failed: " + e.getMessage());
        }
    }
    */
}