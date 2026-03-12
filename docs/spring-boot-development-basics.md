# Development with Spring Boot

---

## What is a Framework?

**Pre-written code that gives your app a structure.**

You focus on business logic. The framework handles the rest.

| Without a Framework | With a Framework |
|---|---|
| Parse HTTP requests manually | Done for you |
| Manage threads | Done for you |
| Serialize JSON | Done for you |
| Wire dependencies together | Done for you |

> Like building a house — the framework gives you the foundation and walls. You just decorate the inside.

---

## What is Maven?

**A build tool and dependency manager for Java.**

It does two things:

**1 — Manages your dependencies**
Declare the libraries you need in `pom.xml`. Maven downloads them automatically.

**2 — Builds your project**

| Command | What it does |
|---|---|
| `mvn compile` | Compiles your Java code |
| `mvn test` | Runs your tests |
| `mvn package` | Packages your app into a runnable JAR |
| `mvn verify` | Compile + test + check (used in CI) |
| `mvn clean` | Deletes build output |

---

## The pom.xml File

**The heart of every Maven project.**

Defines:
- Project name and version
- Dependencies (libraries you need)
- Plugins (build, test, analyze tools)

```
my-project/
├── pom.xml          ← Maven config
├── src/
│   ├── main/java/   ← Application code
│   └── test/java/   ← Test code
└── target/          ← Build output (generated)
```

---

## What is Spring Boot?

**A Java framework for building production-ready apps — with almost zero configuration.**

Built on top of the Spring Framework, but removes all the complex setup it traditionally required.

**Core principle: Convention over Configuration**

```java
@SpringBootApplication
public class WeatherApplication {
    public static void main(String[] args) {
        SpringApplication.run(WeatherApplication.class, args);
    }
}
```

That's a complete, runnable web application in 7 lines.

---

## What are Spring Boot Starters?

**Pre-packaged bundles of dependencies for common use cases.**

One starter replaces many individual libraries.

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

This one line gives you: embedded web server + JSON + HTTP handling + error handling.

**Common Starters**

| Starter | What it gives you |
|---|---|
| `starter-web` | Web server, REST support, JSON |
| `starter-test` | JUnit, Mockito, assertions |
| `starter-data-jpa` | Database access |
| `starter-security` | Authentication and authorization |

---

## What is a REST API?

**A service that sends and receives data over HTTP.**

Returns JSON — not web pages.

**HTTP Methods**

| Method | Purpose | Example |
|---|---|---|
| `GET` | Read data | Get weather for Madrid |
| `POST` | Create data | Add a new city |
| `PUT` | Update data | Update a temperature |
| `DELETE` | Remove data | Delete a city |

**Request → Response**

```
GET /weather/madrid
        ↓
{
  "city": "Madrid",
  "temperatureCelsius": 28.5,
  "condition": "Sunny"
}
```

---

## How a Request Flows Through Spring Boot

```
HTTP Request
      ↓
@RestController    ← Receives request, returns response
      ↓
@Service           ← Business logic
      ↓
Data / Database    ← Where the data lives
      ↓
JSON Response
```

---

## Key Spring Boot Annotations

| Annotation | What it does |
|---|---|
| `@RestController` | Marks a class as a REST API controller |
| `@RequestMapping("/weather")` | Base URL for all endpoints in this class |
| `@GetMapping("/{city}")` | Maps GET requests to a method |
| `@PathVariable` | Reads a value from the URL path |
| `@RequestParam` | Reads a query parameter (`?city=madrid`) |
| `@Service` | Marks a class as business logic |
| `@PostConstruct` | Runs a method automatically on startup |

---

## The Full Picture

```
You write code
      ↓
Maven reads pom.xml → downloads Starters
      ↓
Spring Boot starts
  · Launches embedded Tomcat (port 8080)
  · Registers all @RestController endpoints
  · Injects dependencies automatically
      ↓
Client sends:  GET /weather/madrid
      ↓
Spring routes it → returns JSON
```

---

## Quick Reference

| Term | Definition |
|---|---|
| Framework | Pre-written structure so you focus on business logic |
| Maven | Manages dependencies and builds your app |
| pom.xml | Maven config — dependencies, plugins, project info |
| Spring Boot | Java framework, production-ready with minimal config |
| Starter | A bundle of related dependencies for a common use case |
| REST API | A service that exchanges data over HTTP |
| Endpoint | A URL your API exposes — e.g. `GET /weather/madrid` |
| JSON | The data format REST APIs use for responses |
