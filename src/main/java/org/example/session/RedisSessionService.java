package org.example.session;

import org.example.model.MobileUser;
import org.example.redis.SessionManager;
import org.springframework.stereotype.Service;

@Service
public class RedisSessionService {
    private final SessionManager sessionManager = new SessionManager();

    public String create(MobileUser user) {
        return sessionManager.createSession(user);
    }

    public MobileUser get(String sessionId) {
        return sessionManager.getSession(sessionId);
    }

    public void delete(String sessionId) {
        sessionManager.deleteSession(sessionId);
    }

    public boolean isValid(String sessionId) {
        return sessionManager.isValidSession(sessionId);
    }
}

