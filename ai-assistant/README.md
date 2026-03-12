# AI Assistant

A Spring Boot application that integrates with **OpenAI** using **Spring AI**. It exposes a chat tutor and an AI-powered code reviewer, plus a simple conversation history system.

## What is Spring AI?

**Spring AI** is a framework that makes it easy to connect Spring Boot applications to AI models (OpenAI, Anthropic, Ollama, etc.). Think of it as a **REST client, but for AI providers**.

Just like Spring provides `RestTemplate` or `WebClient` to call REST APIs, Spring AI provides `ChatClient` to call AI models:

```
Traditional REST client                     Spring AI ChatClient
─────────────────────────                   ─────────────────────
restTemplate                                chatClient
  .getForObject(                              .prompt()
      "https://api.com/weather?city=Madrid",    .user("What is the weather in Madrid?")
      String.class                              .call()
  );                                            .content();

      │                                             │
      ▼                                             ▼
HTTP GET to a REST API                      HTTP POST to OpenAI's API
Returns structured JSON                     Returns natural language text
```

The difference: instead of calling an API with a structured URL, you call an AI model with a **natural language prompt**. Spring AI handles the HTTP connection, authentication, and serialization — just like Spring's REST clients do for regular APIs.

## How This Project Works

### The AI part (ChatController)

```
User                         Spring Boot                           OpenAI
─────────────────────────────────────────────────────────────────────────────

GET /ai/chat                 ┌────────────────┐    HTTP POST       ┌────────┐
  ?question=What is DI? ───▶ │ ChatController  │ ─────────────────▶│ GPT-4o │
                             │                 │   api.openai.com   │  mini  │
                             │  chatClient     │   /v1/chat/        │        │
                             │   .prompt()     │   completions      │        │
                  ◀───────── │   .user(q)      │ ◀─────────────────│        │
  "Dependency Injection      │   .call()       │   JSON response    └────────┘
   is a design pattern..."   │   .content()    │
                             └────────────────┘
```

Here is the full code of the `ChatController`:

```java
@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;

    // Spring AI auto-injects ChatClient.Builder (configured via application.yml)
    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("You are a helpful tutor for university students.")
                .build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String question) {
        return chatClient.prompt()   // 1. Create a new prompt
                .user(question)      // 2. Set the user's message
                .call()              // 3. Send HTTP POST to OpenAI
                .content();          // 4. Extract the text from the response
    }
}
```

Breaking down the chain:

| Step | Method | What it does | Analogy |
|---|---|---|---|
| 1 | `.prompt()` | Creates a new chat request | Like creating a new `HttpRequest` |
| 2 | `.user(question)` | Adds the user's message to the request | Like setting the request body |
| 3 | `.call()` | Sends the request to OpenAI over HTTP | Like `restTemplate.exchange()` |
| 4 | `.content()` | Extracts the text from the AI's response | Like `response.getBody()` |

### What is a "system prompt"?

The `.defaultSystem(...)` call sets a **system prompt** — an instruction that tells the AI model how to behave. It's sent with every request but is invisible to the user:

```java
.defaultSystem("You are a helpful tutor for university students studying software engineering.")
```

This is like giving the AI a role before the conversation starts. The user never sees this instruction, but it shapes how the AI responds.

### The conversation part (ConversationController)

The `/conversations/*` endpoints are a standard REST CRUD — they **do not call OpenAI**. They store messages in an in-memory `HashMap` to simulate conversation history. This part exists mainly to provide more code for SonarCloud to analyze.

### How does Spring AI know which AI provider to use?

It's all configured in `application.yml`:

```yaml
spring:
  ai:
    openai:
      api-key: ${OPENAI_API_KEY:}    # Reads from environment variable
      chat:
        options:
          model: gpt-4o-mini         # Which OpenAI model to use
```

When Spring Boot starts, the `spring-ai-starter-model-openai` dependency auto-configures a `ChatClient.Builder` bean that:
1. Reads the API key from the config
2. Points to `https://api.openai.com/v1/chat/completions`
3. Uses `gpt-4o-mini` as the default model

This is the same **auto-configuration** pattern Spring Boot uses everywhere (e.g., adding a MySQL driver auto-configures a `DataSource`).

## Running the Application

### Prerequisites

You need an **OpenAI API key**. Get one at [platform.openai.com/api-keys](https://platform.openai.com/api-keys).

### Start the app

```bash
cd ai-assistant
export OPENAI_API_KEY=sk-your-key-here
mvn spring-boot:run
```

The app starts on **http://localhost:8081**.

## API Endpoints

### AI Endpoints (call OpenAI)

| Endpoint | Description | Example |
|---|---|---|
| `GET /ai/chat?question=...` | Ask the AI tutor a question | `curl "http://localhost:8081/ai/chat?question=What+is+Spring+Boot"` |
| `GET /ai/review?code=...` | AI reviews your code for quality issues | `curl "http://localhost:8081/ai/review?code=int+x+%3D+1/0%3B"` |

### Conversation Endpoints (no AI, in-memory storage)

| Endpoint | Description |
|---|---|
| `POST /conversations/start?userId=...` | Start a new conversation session |
| `POST /conversations/{sessionId}/message?message=...` | Add a message to a session |
| `GET /conversations/{sessionId}` | Get full conversation history |
| `GET /conversations/{sessionId}/summary` | Get a text summary of the conversation |

### Swagger UI

Once the app is running, open **http://localhost:8081/swagger-ui.html** in your browser to explore and test all endpoints interactively.

## Example Usage

```bash
# Ask the AI tutor a question
curl "http://localhost:8081/ai/chat?question=Explain dependency injection in simple terms"

# Ask the AI to review some code
curl "http://localhost:8081/ai/review?code=public+void+foo(){+int+x=null;+x.toString();+}"
```

## Intentional Code Quality Issues

This project contains **deliberate** bugs and code smells for SonarCloud to detect. These are teaching examples — do **not** write code like this in real projects.

| Category | Rule | File | Issue |
|---|---|---|---|
| Bug | S1206 | `ConversationHistory.java` | `equals()` overridden without `hashCode()` |
| Bug | S2259 | `ConversationService.java` | Null pointer dereference (x2) |
| Code Smell | S1104 | `ConversationHistory.java` | Public mutable field |
| Code Smell | S106 | `ConversationService.java` | Using `System.out` instead of a proper logger |
| Code Smell | S1481 | `ConversationService.java` | Unused local variable |
| Code Smell | S1192 | `ConversationService.java` | Duplicated string literals |
| Code Smell | S1128 | `ConversationController.java` | Unused import (`java.util.Calendar`) |
| Code Smell | S1118 | `TextAnalyzer.java` | Utility class with public constructor |
| Code Smell | S3776 | `TextAnalyzer.java` | High cognitive complexity (deeply nested `if/else`) |
| Security | S2068 | `ConversationService.java` | Hard-coded encryption key |
| Security | S2245 | `TextAnalyzer.java` | `Math.random()` used for token generation |
