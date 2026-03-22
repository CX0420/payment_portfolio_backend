package org.example.service;

import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Service
public class PasswordResetTokenService {

    // Simple in-memory storage for tokens (replace with Redis/database in
    // production)
    private final Map<String, TokenInfo> tokenStore = new ConcurrentHashMap<>();

    private static final String TOKEN_PREFIX = "password_reset:";
    private static final long TOKEN_EXPIRATION_HOURS = 24;

    private static class TokenInfo {
        String mobileUserId;
        long expiryTime;

        TokenInfo(String mobileUserId) {
            this.mobileUserId = mobileUserId;
            this.expiryTime = System.currentTimeMillis() + TimeUnit.HOURS.toMillis(TOKEN_EXPIRATION_HOURS);
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }

    public void createToken(Long userId, String token) {
        String key = TOKEN_PREFIX + token;
        tokenStore.put(key, new TokenInfo(userId.toString()));
    }

    public boolean validateToken(Long userId, String token) {
        String key = TOKEN_PREFIX + token;
        TokenInfo tokenInfo = tokenStore.get(key);

        if (tokenInfo == null) {
            return false;
        }

        if (tokenInfo.isExpired()) {
            tokenStore.remove(key);
            return false;
        }

        return tokenInfo.mobileUserId.equals(userId.toString());
    }

    public void removeToken(Long userId) {
        // Remove all tokens for this user
        tokenStore.entrySet().removeIf(entry -> entry.getValue().mobileUserId.equals(userId.toString()));
    }

    // Legacy methods for backward compatibility
    public void saveToken(String token, String mobileUserId) {
        String key = TOKEN_PREFIX + token;
        tokenStore.put(key, new TokenInfo(mobileUserId));
    }

    public String getMobileUserIdByToken(String token) {
        String key = TOKEN_PREFIX + token;
        TokenInfo tokenInfo = tokenStore.get(key);

        if (tokenInfo == null || tokenInfo.isExpired()) {
            if (tokenInfo != null) {
                tokenStore.remove(key);
            }
            return null;
        }

        return tokenInfo.mobileUserId;
    }

    public void deleteToken(String token) {
        String key = TOKEN_PREFIX + token;
        tokenStore.remove(key);
    }
}
