package com.example.aiassistant.service;

import com.example.aiassistant.model.ConversationHistory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConversationServiceTest {

    private ConversationService conversationService;

    @BeforeEach
    void setUp() {
        conversationService = new ConversationService();
    }

    @Test
    void startConversationCreatesHistory() {
        ConversationHistory history = conversationService.startConversation("user1");
        assertNotNull(history);
        assertEquals("user1", history.getUserId());
        assertNotNull(history.getSessionId());
    }

    @Test
    void addMessageStoresMessage() {
        ConversationHistory history = conversationService.startConversation("user1");
        conversationService.addMessage(history.getSessionId(), "Hello!");
        assertEquals(1, history.getMessages().size());
        assertEquals("Hello!", history.getMessages().get(0));
    }

    @Test
    void getConversationReturnsCorrectHistory() {
        ConversationHistory history = conversationService.startConversation("user1");
        ConversationHistory retrieved = conversationService.getConversation(history.getSessionId());
        assertEquals(history.getSessionId(), retrieved.getSessionId());
    }

    @Test
    void summarizeConversationReturnsNotFoundForInvalidSession() {
        String summary = conversationService.summarizeConversation("invalid-session");
        assertEquals("Conversation not found", summary);
    }

    @Test
    void summarizeConversationIncludesMessages() {
        ConversationHistory history = conversationService.startConversation("user1");
        conversationService.addMessage(history.getSessionId(), "What is Spring?");
        conversationService.addMessage(history.getSessionId(), "How does DI work?");
        String summary = conversationService.summarizeConversation(history.getSessionId());
        assertTrue(summary.contains("user1"));
        assertTrue(summary.contains("What is Spring?"));
    }

}
