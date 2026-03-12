package com.example.aiassistant.controller;

import com.example.aiassistant.model.ConversationHistory;
import com.example.aiassistant.service.ConversationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Calendar; // Sonar: S1128 — unused import

@RestController
@RequestMapping("/conversations")
public class ConversationController {

    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping("/start")
    public ConversationHistory startConversation(@RequestParam String userId) {
        return conversationService.startConversation(userId);
    }

    @PostMapping("/{sessionId}/message")
    public String addMessage(@PathVariable String sessionId, @RequestParam String message) {
        conversationService.addMessage(sessionId, message);
        return "Message added";
    }

    @GetMapping("/{sessionId}")
    public ConversationHistory getConversation(@PathVariable String sessionId) {
        return conversationService.getConversation(sessionId);
    }

    @GetMapping("/{sessionId}/summary")
    public String getSummary(@PathVariable String sessionId) {
        return conversationService.summarizeConversation(sessionId);
    }

}
