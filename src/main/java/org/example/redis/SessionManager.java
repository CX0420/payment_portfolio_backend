package org.example.redis;

import redis.clients.jedis.Jedis;
import org.example.model.MobileUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SessionManager {

    private static final int SESSION_TTL = (int) TimeUnit.DAYS.toSeconds(7); // 7 days
    private static final String SESSION_PREFIX = "session:";
    private final ObjectMapper objectMapper;

    public SessionManager() {
        // ✅ Configure ObjectMapper to handle Java 8 date/time types
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Create a new session for authenticated user
     */
    public String createSession(MobileUser user) {
        String sessionId = generateSessionId();
        String sessionKey = SESSION_PREFIX + sessionId;

        // Use try-with-resources to ensure Jedis is closed
        try (Jedis jedis = RedisConfig.getJedis()) {
            String userJson = objectMapper.writeValueAsString(user);
            jedis.setex(sessionKey, SESSION_TTL, userJson);

            // Also store user ID to session mapping (optional)
            jedis.setex("user:" + user.getMobileUserId() + ":last_session",
                    SESSION_TTL, sessionId);

            System.out.println("✅ Session created: " + sessionId + " for user: " + user.getMobileUserId());
            return sessionId;

        } catch (Exception e) {
            System.err.println("❌ Failed to create session: " + e.getMessage());
            throw new RuntimeException("Failed to create session", e);
        }
    }

    /**
     * Get user from session ID
     */
    public MobileUser getSession(String sessionId) {
        if (sessionId == null || sessionId.isEmpty()) {
            return null;
        }

        String sessionKey = SESSION_PREFIX + sessionId;

        try (Jedis jedis = RedisConfig.getJedis()) {
            String userJson = jedis.get(sessionKey);

            if (userJson != null) {
                // Refresh TTL on access (optional)
                jedis.expire(sessionKey, SESSION_TTL);
                return objectMapper.readValue(userJson, MobileUser.class);
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to get session: " + e.getMessage());
        }

        return null;
    }

    /**
     * Delete session (logout)
     */
    public void deleteSession(String sessionId) {
        if (sessionId == null) return;

        try (Jedis jedis = RedisConfig.getJedis()) {
            String sessionKey = SESSION_PREFIX + sessionId;

            // Get user ID before deleting (for cleanup)
            String userJson = jedis.get(sessionKey);
            if (userJson != null) {
                MobileUser user = objectMapper.readValue(userJson, MobileUser.class);
                // Clean up user-related keys
                jedis.del("user:" + user.getMobileUserId() + ":last_session");
            }

            // Delete session
            jedis.del(sessionKey);
            System.out.println("✅ Session deleted: " + sessionId);

        } catch (Exception e) {
            System.err.println("❌ Failed to delete session: " + e.getMessage());
        }
    }

    /**
     * Check if session is valid
     */
    public boolean isValidSession(String sessionId) {
        if (sessionId == null) return false;

        try (Jedis jedis = RedisConfig.getJedis()) {
            return jedis.exists(SESSION_PREFIX + sessionId);
        }
    }

    /**
     * Get session TTL in seconds
     */
    public long getSessionTtl(String sessionId) {
        if (sessionId == null) return -2;

        try (Jedis jedis = RedisConfig.getJedis()) {
            return jedis.ttl(SESSION_PREFIX + sessionId);
        }
    }

    private String generateSessionId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}