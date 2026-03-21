package org.example.redis;

import org.example.config.AppConfig;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;
import java.time.Duration;
import java.net.URI;
import java.net.URISyntaxException;

public class RedisConfig {
    private static final String REDIS_URL = AppConfig.getRedisUrl(); // Get from properties
    private static JedisPool jedisPool;
    private static boolean redisFailed = false;

    static {
        initializePool();
    }

    private static void initializePool() {
        if (redisFailed) {
            return; // Already failed, don't retry
        }

        try {
            // Parse Upstash Redis URL
            // Format: rediss://default:YOUR_TOKEN@us1-xxxx.upstash.io:6379
            URI redisUri = new URI(REDIS_URL);

            // Extract host and port
            String host = redisUri.getHost();
            int port = redisUri.getPort();

            // Extract password (token) from user info
            String userInfo = redisUri.getUserInfo();
            String password = null;
            if (userInfo != null && userInfo.contains(":")) {
                password = userInfo.split(":")[1]; // Get token after "default:"
            }

            // Configure connection pool for Upstash
            JedisPoolConfig poolConfig = new JedisPoolConfig();
            poolConfig.setMaxTotal(5);              // Lower for free tier
            poolConfig.setMaxIdle(3);                // Maximum idle connections
            poolConfig.setMinIdle(1);                 // Minimum idle connections
            poolConfig.setMaxWait(Duration.ofSeconds(2)); // Wait timeout
            poolConfig.setTestOnBorrow(true);         // Test connection before use
            poolConfig.setTestWhileIdle(true);        // Test idle connections

            // For Upstash, always use SSL (rediss:// protocol)
            boolean useSsl = REDIS_URL.startsWith("rediss://");

            // Create JedisPool with SSL
            jedisPool = new JedisPool(poolConfig, host, port,
                    Protocol.DEFAULT_TIMEOUT,
                    password,
                    Protocol.DEFAULT_DATABASE,
                    null, // client name
                    useSsl);

            System.out.println("✅ Connected to Upstash Redis at: " + host);
            testConnection();

        } catch (Exception e) {
            redisFailed = true;
            jedisPool = null;
            System.err.println("❌ Failed to initialize Redis connection: " + e.getMessage());
            System.err.println("⚠️ Redis will be unavailable. Please check your Redis URL and credentials.");
        }
    }

    private static void testConnection() {
        try (Jedis jedis = getJedis()) {
            String pong = jedis.ping();
            System.out.println("📡 Redis ping response: " + pong);
        } catch (Exception e) {
            System.err.println("⚠️ Redis connection test failed: " + e.getMessage());
        }
    }

    public static Jedis getJedis() {
        if (jedisPool == null) {
            throw new RuntimeException("Redis connection is not available. Please check your Redis configuration.");
        }
        if (jedisPool.isClosed()) {
            initializePool();
            if (jedisPool == null) {
                throw new RuntimeException("Redis connection is not available. Please check your Redis configuration.");
            }
        }
        return jedisPool.getResource();
    }

    public static void closePool() {
        if (jedisPool != null && !jedisPool.isClosed()) {
            jedisPool.close();
            System.out.println("🛑 Redis connection pool closed");
        }
    }

    // Utility method to check connection health
    public static boolean isHealthy() {
        if (jedisPool == null) {
            return false;
        }
        try (Jedis jedis = getJedis()) {
            return "PONG".equals(jedis.ping());
        } catch (Exception e) {
            return false;
        }
    }
}