package com.example.aiassistant.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("You are a helpful tutor for university students studying software engineering.")
                .build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String question) {
        return chatClient.prompt()
                .user(question)
                .call()
                .content();
    }

    @GetMapping("/review")
    public String reviewCode(@RequestParam String code) {
        return chatClient.prompt()
                .user("Review the following code for quality issues and suggest improvements:\n\n" + code)
                .call()
                .content();
    }

}
