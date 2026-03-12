# Code Quality Fundamentals

A repository with **two Spring Boot apps** for teaching software quality fundamentals. It demonstrates code analysis with **SonarCloud**, CI/CD with **GitHub Actions**, and AI-assisted development with **Spring AI**.

## Repository Structure

```
code-quality-fundamentals/
├── weather-api/          ← Step 1: Classic Spring Boot REST API (no external deps)
│   └── src/main/java/com/example/weather/
│       ├── controller/WeatherController.java
│       ├── model/WeatherData.java
│       ├── service/WeatherService.java
│       └── util/TemperatureConverter.java
│
├── ai-assistant/         ← Step 2: Spring AI powered assistant (requires OpenAI key)
│   └── src/main/java/com/example/aiassistant/
│       ├── controller/ChatController.java
│       ├── controller/ConversationController.java
│       ├── model/ConversationHistory.java
│       ├── service/ConversationService.java
│       └── util/TextAnalyzer.java
│
├── .github/workflows/weather-api.yml    ← CI for weather-api only
└── .github/workflows/ai-assistant.yml   ← CI for ai-assistant only
```

---

## Step 1 — Weather API

A classic Spring Boot REST API with hardcoded weather data. **No external dependencies** — runs out of the box.

### Run it

```bash
cd weather-api
mvn spring-boot:run
```

### Endpoints

| Endpoint | Description |
|---|---|
| `GET /weather` | List all cities |
| `GET /weather/{city}` | Get weather for a city (e.g., `madrid`, `london`) |
| `GET /weather/alert/{city}` | Get weather alerts |
| `GET /weather/search?city=...` | Search (has XSS issue) |

---

## Step 2 — AI Assistant

A Spring AI app that connects to OpenAI. Provides a chat tutor and an AI code reviewer.

### Run it

```bash
cd ai-assistant
export OPENAI_API_KEY=sk-your-key-here
mvn spring-boot:run
```

### Endpoints

| Endpoint | Description |
|---|---|
| `GET /ai/chat?question=...` | Ask the AI tutor a question |
| `GET /ai/review?code=...` | Get an AI code review |
| `POST /conversations/start?userId=...` | Start a conversation |
| `POST /conversations/{sessionId}/message?message=...` | Add a message |
| `GET /conversations/{sessionId}/summary` | Get conversation summary |

---

## Intentional Code Quality Issues

Both apps contain **deliberate** issues for Sonar to detect:

### Weather API

| Category | Rule | File | Issue |
|---|---|---|---|
| Bug | S1206 | `WeatherData.java` | `equals()` without `hashCode()` |
| Bug | S2259 | `WeatherService.java` | Null pointer dereference |
| Bug | S2095 | `WeatherService.java` | Resource leak (unclosed reader) |
| Code Smell | S1104 | `WeatherData.java` | Public mutable fields |
| Code Smell | S106 | `WeatherService.java` | `System.out` instead of logger |
| Code Smell | S1481 | `WeatherService.java` | Unused local variable |
| Code Smell | S108 | `WeatherService.java` | Empty catch block |
| Code Smell | S1128 | `WeatherController.java` | Unused import |
| Code Smell | S1118 | `TemperatureConverter.java` | Utility class with public constructor |
| Code Smell | S3776 | `TemperatureConverter.java` | High cognitive complexity |
| Security | S2068 | `WeatherService.java` | Hard-coded API key |
| Vulnerability | S5131 | `WeatherController.java` | Reflected XSS |

### AI Assistant

| Category | Rule | File | Issue |
|---|---|---|---|
| Bug | S1206 | `ConversationHistory.java` | `equals()` without `hashCode()` |
| Bug | S2259 | `ConversationService.java` | Null pointer dereference (x2) |
| Code Smell | S1104 | `ConversationHistory.java` | Public mutable field |
| Code Smell | S106 | `ConversationService.java` | `System.out` instead of logger |
| Code Smell | S1481 | `ConversationService.java` | Unused local variable |
| Code Smell | S1192 | `ConversationService.java` | Duplicated string literals |
| Code Smell | S1128 | `ConversationController.java` | Unused import |
| Code Smell | S1118 | `TextAnalyzer.java` | Utility class with public constructor |
| Code Smell | S3776 | `TextAnalyzer.java` | High cognitive complexity |
| Security | S2068 | `ConversationService.java` | Hard-coded encryption key |
| Security | S2245 | `TextAnalyzer.java` | `Math.random()` for token generation |

---

## SonarCloud Setup

1. Go to [sonarcloud.io](https://sonarcloud.io) and log in with GitHub
2. Create a new organization → import from GitHub
3. Import both projects (weather-api and ai-assistant) — choose the **Free** plan
4. Generate a token: **My Account → Security → Generate Token**
5. Add the token as a GitHub secret: **Repo Settings → Secrets → Actions → `SONAR_TOKEN`**
6. Update each app's `pom.xml` with your actual organization and project keys:

```xml
<sonar.organization>your-org-key</sonar.organization>
<sonar.projectKey>your-project-key</sonar.projectKey>
```

## CI/CD Pipeline

Each app has its **own workflow** that only triggers when files in its folder change:

| Workflow | Triggers on changes to | What it does |
|---|---|---|
| `.github/workflows/weather-api.yml` | `weather-api/**` | Build → Test → JaCoCo → SonarCloud |
| `.github/workflows/ai-assistant.yml` | `ai-assistant/**` | Build → Test → JaCoCo → SonarCloud |

The Sonar step uses `sonar.qualitygate.wait=true`, which means:
- The pipeline **waits** for SonarCloud to finish the analysis
- If the **Quality Gate fails** (too many bugs, low coverage, etc.), the **build fails**
- On a PR, this shows as a failed check — blocking the merge

## Running Analysis Locally

```bash
export SONAR_TOKEN=your-sonar-token

# Weather API
cd weather-api
mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar

# AI Assistant
cd ../ai-assistant
mvn -B verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar
```
