package org.example.controller;

import org.example.dto.ApiResponse;
import org.example.dto.UniversalDTO;
import org.example.service.UniversalServiceRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

/**
 * Universal Controller - Single entry point for ALL services
 * Routes requests based on serviceName in UniversalDTO
 *
 * All API endpoints use this single controller with different serviceName flags
 */
@RestController
@RequestMapping("/api/v1/payment")
public class UniversalController {

    @Autowired
    private UniversalServiceRouter serviceRouter;

    /**
     * Universal POST endpoint - handles create operations for all services
     * Service differentiated by serviceName field in request body
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UniversalDTO>> process(
            @Valid @RequestBody UniversalDTO request,
            @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {

        // For operations that require a session ID (e.g., LOGOUT), carry header into UniversalDTO
        if (sessionId != null && !sessionId.isBlank()) {
            request.addAttribute("sessionId", sessionId);
            request.addAttribute("x-session-id", sessionId);
        }

        ApiResponse<UniversalDTO> response = serviceRouter.process(request);

        // Return appropriate HTTP status based on response
        if (response.getSuccess()) {
            if (response.getStatusCode() == 201) {
                return ResponseEntity.status(201).body(response);
            }
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<String>> health() {
        return ResponseEntity.ok(ApiResponse.success("Universal API is healthy", "Service is running"));
    }
}
