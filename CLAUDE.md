# CLAUDE.md — Project Context for AI Assistants

## Purpose

This repository is a **teaching tool for a university course on software quality**. It is NOT a production application. It contains two Spring Boot apps with **intentional bugs, code smells, and security issues** that students analyze using SonarCloud.

The three pillars of the course are:
1. **SonarCloud** — static code analysis (free cloud tier, no local installation)
2. **GitHub Actions** — CI/CD pipeline that builds, tests, and runs Sonar analysis
3. **Spring AI** — demonstrating AI-assisted development with OpenAI

## Repository Layout

```
code-quality-fundamentals/
├── weather-api/                  # App 1: Classic Spring Boot REST API
│   ├── pom.xml                   # Spring Boot 3.4.1, JaCoCo, Sonar plugin, springdoc-openapi
│   ├── src/main/resources/application.yml
│   └── src/main/java/com/example/weather/
│       ├── WeatherApplication.java
│       ├── controller/WeatherController.java
│       ├── model/WeatherData.java
│       ├── service/WeatherService.java
│       └── util/TemperatureConverter.java
│
├── ai-assistant/                 # App 2: Spring AI + OpenAI integration
│   ├── pom.xml                   # Spring Boot 3.4.1, Spring AI 1.0.0, JaCoCo, Sonar, springdoc
│   ├── src/main/resources/application.yml
│   └── src/main/java/com/example/aiassistant/
│       ├── AiAssistantApplication.java
│       ├── controller/ChatController.java        # Calls OpenAI via Spring AI ChatClient
│       ├── controller/ConversationController.java # Standard REST CRUD (no AI)
│       ├── model/ConversationHistory.java
│       ├── service/ConversationService.java
│       └── util/TextAnalyzer.java
│
├── .github/workflows/
│   ├── weather-api.yml           # CI for weather-api only (triggers on weather-api/** changes)
│   └── ai-assistant.yml          # CI for ai-assistant only (triggers on ai-assistant/** changes)
├── .gitignore
├── README.md                     # Root overview and SonarCloud setup instructions
└── CLAUDE.md                     # This file
```

## The Two Applications

### weather-api (port 8080)

A read-only REST API serving hardcoded weather data for 5 cities (Madrid, London, New York, Tokyo, Sydney). Uses an in-memory `HashMap` — no database, no external API calls, no configuration needed. This is the "step 1" app that students work with first.

**Endpoints:**
- `GET /weather` — all cities
- `GET /weather/{city}` — one city
- `GET /weather/alert/{city}` — weather alerts
- `GET /weather/search?city=...` — search (intentional XSS)
- Swagger UI at `/swagger-ui.html`

**Run:** `cd weather-api && mvn spring-boot:run`

### ai-assistant (port 8081)

Integrates with OpenAI using Spring AI's `ChatClient`. The `ChatController` sends user prompts to GPT-4o-mini and returns the response. The `ConversationController` is plain REST CRUD for conversation history (does NOT call OpenAI — it's scaffolding for Sonar to analyze).

**Endpoints:**
- `GET /ai/chat?question=...` — forwards question to OpenAI, returns AI response
- `GET /ai/review?code=...` — sends code to OpenAI for quality review
- `POST /conversations/start?userId=...` — create session (in-memory)
- `POST /conversations/{sessionId}/message?message=...` — add message
- `GET /conversations/{sessionId}` — get history
- `GET /conversations/{sessionId}/summary` — text summary
- Swagger UI at `/swagger-ui.html`

**Run:** `cd ai-assistant && export OPENAI_API_KEY=sk-... && mvn spring-boot:run`

## Tech Stack

| Component | Version | Notes |
|---|---|---|
| Java | 17 | Minimum required |
| Spring Boot | 3.4.1 | Both apps |
| Spring AI | 1.0.0 | ai-assistant only, via BOM |
| OpenAI model | gpt-4o-mini | Configured in application.yml |
| springdoc-openapi | 2.8.4 | Swagger UI for both apps |
| JaCoCo | 0.8.12 | Code coverage reports |
| sonar-maven-plugin | 5.0.0.4389 | SonarCloud scanner |

## Build & Test

The two apps are **independent Maven projects** (not a multi-module build). Each has its own `pom.xml` and must be built separately:

```bash
# Weather API — no configuration needed
cd weather-api && mvn verify

# AI Assistant — tests do NOT call OpenAI (no API key needed for CI)
cd ai-assistant && mvn verify
```

Each app has 6 unit tests. The ai-assistant tests instantiate services directly (no Spring context) to avoid requiring an OpenAI API key during CI.

## CI/CD Pipeline

Each app has its **own workflow file**, triggered only when its files change:

- `.github/workflows/weather-api.yml` — triggers on `weather-api/**` changes
- `.github/workflows/ai-assistant.yml` — triggers on `ai-assistant/**` changes

Each workflow does: checkout → setup JDK 17 → `mvn verify` (compile + test + JaCoCo) → `sonar:sonar` with `sonar.qualitygate.wait=true`.

The `sonar.qualitygate.wait=true` flag makes the build **wait for SonarCloud's quality gate result** and **fail if the gate doesn't pass**. This means a PR that introduces too many issues will show a failed check and can be blocked from merging.

**Required GitHub secret:** `SONAR_TOKEN`

**Required pom.xml placeholders to fill:** `sonar.organization` and `sonar.projectKey` in each app's pom.xml.

## Intentional Code Quality Issues

**IMPORTANT:** Both apps contain deliberate bugs and code smells. These are teaching material, not accidental mistakes. Do NOT fix them unless explicitly asked to — they are the whole point of the project.

### weather-api (12 issues)

| Sonar Rule | File | Issue |
|---|---|---|
| S1206 (Bug) | `WeatherData.java` | `equals()` without `hashCode()` |
| S2259 (Bug) | `WeatherService.java` | Null pointer dereference — `getWeather()` doesn't check if city exists |
| S2095 (Bug) | `WeatherService.java` | Resource leak — `BufferedReader` never closed in `loadWeatherData()` |
| S1104 (Smell) | `WeatherData.java` | Public mutable fields (`city`, `condition`) |
| S106 (Smell) | `WeatherService.java` | `System.out.println` instead of logger |
| S1481 (Smell) | `WeatherService.java` | Unused variable `unusedConfig` in `getWeatherAlert()` |
| S108 (Smell) | `WeatherService.java` | Empty catch block in `loadWeatherData()` |
| S1128 (Smell) | `WeatherController.java` | Unused import `java.util.Date` |
| S1118 (Smell) | `TemperatureConverter.java` | Utility class with public constructor |
| S3776 (Smell) | `TemperatureConverter.java` | High cognitive complexity in `classifyTemperature()` |
| S2068 (Security) | `WeatherService.java` | Hard-coded `API_KEY` constant |
| S5131 (Vuln) | `WeatherController.java` | Reflected XSS in `searchCity()` |

### ai-assistant (11 issues)

| Sonar Rule | File | Issue |
|---|---|---|
| S1206 (Bug) | `ConversationHistory.java` | `equals()` without `hashCode()` |
| S2259 (Bug) | `ConversationService.java` | Null pointer in `addMessage()` — no null check on `conversations.get()` |
| S2259 (Bug) | `ConversationService.java` | Null pointer in `getConversation()` — same pattern |
| S1104 (Smell) | `ConversationHistory.java` | Public mutable field `userId` |
| S106 (Smell) | `ConversationService.java` | `System.out.println` instead of logger |
| S1481 (Smell) | `ConversationService.java` | Unused variable `unusedFormat` in `summarizeConversation()` |
| S1192 (Smell) | `ConversationService.java` | Duplicated string literal `"Conversation summary"` |
| S1128 (Smell) | `ConversationController.java` | Unused import `java.util.Calendar` |
| S1118 (Smell) | `TextAnalyzer.java` | Utility class with public constructor |
| S3776 (Smell) | `TextAnalyzer.java` | High cognitive complexity in `classifyPrompt()` |
| S2068 (Security) | `ConversationService.java` | Hard-coded `ENCRYPTION_KEY` constant |
| S2245 (Security) | `TextAnalyzer.java` | `Math.random()` used for token generation |

## Key Design Decisions

- **Two independent folders (not multi-module):** Chosen for simplicity — each app is self-contained, students can understand one without knowing the other.
- **weather-api has no external dependencies:** Students can clone and run it immediately. No API keys, no database, no Docker.
- **ai-assistant tests skip Spring context:** `AiAssistantApplicationTests` does NOT use `@SpringBootTest` to avoid requiring an OpenAI API key in CI.
- **In-memory data only:** Both apps use `HashMap` for storage. No database setup required.
- **application.yml over .properties:** User preference — YAML format for Spring Boot configuration.
- **Swagger (springdoc-openapi) on both apps:** Students can explore and test endpoints from the browser at `/swagger-ui.html`.

## Common Tasks

**Add a new intentional code issue:** Add the buggy code to the relevant file, then document the Sonar rule ID in the app's `README.md` table and in this file's issues table above.

**Add a new endpoint:** Follow the existing pattern — add method to the controller, business logic to the service. Keep the layered architecture (Controller → Service → Model).

**Change the AI model:** Edit `ai-assistant/src/main/resources/application.yml`, change `spring.ai.openai.chat.options.model`.

**Switch from OpenAI to Ollama:** Replace `spring-ai-starter-model-openai` with `spring-ai-starter-model-ollama` in `ai-assistant/pom.xml` and update `application.yml` accordingly. No code changes needed — `ChatClient` API is the same.
