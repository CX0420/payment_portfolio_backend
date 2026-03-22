package org.example.dto;

import org.example.model.MobileUser;

public class LoginResponse {
    private String sessionId;
    private MobileUser user;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public MobileUser getUser() {
        return user;
    }

    public void setUser(MobileUser user) {
        this.user = user;
    }
}

