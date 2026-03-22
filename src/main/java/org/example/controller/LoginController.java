package org.example.controller;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.example.dto.*;
import org.example.model.MobileUser;
import org.example.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    @Resource
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<UniversalDTO>> login(@Valid @RequestBody UniversalDTO dto) {

        ApiResponse<UniversalDTO> response = (ApiResponse<UniversalDTO>) loginService.processLogin(dto);
        if (response.getSuccess()) {
            if (response.getStatusCode() == 201) {
                return ResponseEntity.status(201).body(response);
            }
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response);
        }
    }

    /**
     * Returns the current user from Redis session.
     * Provide session id via header: X-Session-Id
     */
    @GetMapping("/me")
    public Result<MobileUser> me(@RequestHeader(value = "X-Session-Id", required = false) String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return new Result<>(401, "Missing session", null);
        }
        MobileUser user = loginService.me(sessionId);
        if (user == null) {
            return new Result<>(401, "Invalid session", null);
        }
        return Result.success(user);
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "X-Session-Id", required = false) String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return new Result<>(400, "Missing session", null);
        }
        loginService.logout(sessionId);
        return Result.success(null);
    }
}
