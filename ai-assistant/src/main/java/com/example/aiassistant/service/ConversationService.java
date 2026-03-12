package com.example.aiassistant.service;

import com.example.aiassistant.model.ConversationHistory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ConversationService {

    // Sonar: S2068 — credentials should not be hard-coded
    private static final String ENCRYPTION_KEY = "super-secret-encryption-key-2024";

    private Map<String, ConversationHistory> conversations = new HashMap<>();

    // Sonar: S106 — System.out should not be used for logging
    public ConversationHistory startConversation(String userId) {
        String sessionId = generateSessionId();
        ConversationHistory history = new ConversationHistory(userId, sessionId);
        conversations.put(sessionId, history);
        System.out.println("Conversation started for user: " + userId);
        return history;
    }

    // Sonar: S2259 — null pointer dereference
    public void addMessage(String sessionId, String message) {
        ConversationHistory history = conversations.get(sessionId);
        history.addMessage(message); // Bug: history may be null if sessionId not found
        System.out.println("Message added to session: " + sessionId);
    }

    // Sonar: S2259 — null pointer dereference
    public ConversationHistory getConversation(String sessionId) {
        ConversationHistory history = conversations.get(sessionId);
        System.out.println("Retrieving conversation: " + history.getSessionId()); // Bug: NPE
        return history;
    }

    // Sonar: S1481 — unused local variable
    // Sonar: S1192 — duplicated string literal
    public String summarizeConversation(String sessionId) {
        String unusedFormat = "markdown";

        ConversationHistory history = conversations.get(sessionId);
        if (history == null) {
            return "Conversation not found";
        }

        StringBuilder summary = new StringBuilder();
        summary.append("Conversation summary for user: ").append(history.getUserId()).append("\n");
        summary.append("Conversation summary - total messages: ").append(history.getMessages().size()).append("\n");
        for (String msg : history.getMessages()) {
            summary.append("Conversation summary - message: ").append(msg).append("\n");
        }
        return summary.toString();
    }

    public Map<String, ConversationHistory> getAllConversations() {
        return conversations;
    }

    private String generateSessionId() {
        return "session-" + System.currentTimeMillis();
    }

}
