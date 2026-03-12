package com.example.aiassistant;

import org.junit.jupiter.api.Test;

class AiAssistantApplicationTests {

    @Test
    void applicationClassExists() {
        // No @SpringBootTest — avoids requiring OpenAI API key in CI
        AiAssistantApplication app = new AiAssistantApplication();
    }

}
