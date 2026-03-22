package org.example.dto;

import org.example.model.MobileUser;

public class SessionInfo {
    private String sessionId;
    private MobileUser user;

    public SessionInfo() {}

    public SessionInfo(String sessionId, MobileUser user) {
        this.sessionId = sessionId;
        this.user = user;
    }

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

