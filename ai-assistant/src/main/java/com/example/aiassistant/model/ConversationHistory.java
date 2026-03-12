package com.example.aiassistant.model;

import java.util.ArrayList;
import java.util.List;

public class ConversationHistory {

    // Sonar: S1104 — public mutable field
    public String userId;

    private String sessionId;
    private List<String> messages = new ArrayList<>();
    private long createdAt;

    public ConversationHistory() {
    }

    public ConversationHistory(String userId, String sessionId) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.createdAt = System.currentTimeMillis();
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public List<String> getMessages() { return messages; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    // Sonar: S1206 — "equals" overridden without "hashCode"
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ConversationHistory other = (ConversationHistory) obj;
        return sessionId.equals(other.sessionId);
    }

    // hashCode() intentionally missing — Sonar will flag this as a Bug

}
