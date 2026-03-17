package org.example;

import org.example.config.AppConfig;
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
        };
    }
}