package org.example.controller;

import org.example.supabase.SupabaseClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("ok", true);

        try {
            boolean dbOk = new SupabaseClient().ping();
            out.put("db", dbOk ? "ok" : "fail");
        } catch (Exception e) {
            out.put("db", "fail");
            out.put("dbError", e.getMessage());
        }

        return out;
    }
}

