package org.example.controller;

import org.example.supabase.SupabaseClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {

    private final SupabaseClient supabaseClient;

    @Autowired
    public HealthController(SupabaseClient supabaseClient) {
        this.supabaseClient = supabaseClient;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("ok", true);
        out.put("timestamp", System.currentTimeMillis());

        try {
            boolean dbOk = supabaseClient.ping();
            out.put("database", dbOk ? "connected" : "disconnected");

            // 添加连接池统计信息（可选）
            try {
                String poolStats = supabaseClient.getPoolStats();
                out.put("poolStats", poolStats);
            } catch (Exception e) {
                // 忽略，可能没有实现
            }

        } catch (Exception e) {
            out.put("database", "error");
            out.put("databaseError", e.getMessage());
        }

        return out;
    }

    // 添加一个更详细的健康检查端点
    @GetMapping("/health/detailed")
    public Map<String, Object> detailedHealth() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("ok", true);
        out.put("environment", System.getProperty("app.env", "default"));

        // 数据库健康
        Map<String, Object> dbHealth = new LinkedHashMap<>();
        try {
            long start = System.currentTimeMillis();
            boolean dbOk = supabaseClient.ping();
            long latency = System.currentTimeMillis() - start;

            dbHealth.put("status", dbOk ? "up" : "down");
            dbHealth.put("latency", latency + "ms");

            // 添加连接池统计
            try {
                String poolStats = supabaseClient.getPoolStats();
                dbHealth.put("poolStats", poolStats);
            } catch (Exception e) {
                // 忽略
            }

        } catch (Exception e) {
            dbHealth.put("status", "down");
            dbHealth.put("error", e.getMessage());
        }
        out.put("database", dbHealth);

        return out;
    }
}