package org.example;

import org.example.config.AppConfig;
import org.example.redis.RedisConfig;
import org.example.supabase.SupabaseClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner startupLog() {
        return args -> {
            System.out.println("=" .repeat(50));
            System.out.println("🚀 Payment Portfolio Backend (Spring Boot)");
            System.out.println("=" .repeat(50));
            System.out.println("Environment: " + AppConfig.getEnvName());
            System.out.println("Supabase URL: " + AppConfig.getSupabaseUrl());

            try {
                boolean ok = new SupabaseClient().ping();
                System.out.println("✅ DB ping: " + (ok ? "OK" : "FAILED"));
            } catch (Exception e) {
                System.err.println("❌ DB ping failed: " + e.getMessage());
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

//            // Test 2: Create a test session
//            SessionManager sessionManager = new SessionManager();
//            MobileUser testUser = new MobileUser();
//            testUser.setMobileUserId("test123");
//            testUser.setMobileUserName("Test User");
//            testUser.setEmail("test@example.com");
//
//            String sessionId = sessionManager.createSession(testUser);
//            System.out.println("✅ Created session: " + sessionId);
//
//            // Test 3: Retrieve session
//            MobileUser retrievedUser = sessionManager.getSession(sessionId);
//            if (retrievedUser != null) {
//                System.out.println("✅ Retrieved user: " + retrievedUser.getMobileUserName());
//            }

//            // Test 4: Check TTL
//            long ttl = sessionManager.getSessionTtl(sessionId);
//            System.out.println("⏰ Session TTL: " + ttl + " seconds");
//
//            // Test 5: Clean up
//            sessionManager.deleteSession(sessionId);
//            System.out.println("✅ Cleaned up test session");

            RedisConfig.closePool();
            System.out.println("=".repeat(50));
        };
    }
}