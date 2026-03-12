# CI/CD Basics

---

## What is CI/CD?

**An automated pipeline that builds, tests, and deploys your code every time you push a change.**

| Term | Stands for | What it means |
|---|---|---|
| **CI** | Continuous Integration | Automatically build and test every code change |
| **CD** | Continuous Delivery/Deployment | Automatically release passing code to an environment |

> Without CI/CD: "It works on my machine."
> With CI/CD: "It works — and we have proof."

---

## Continuous Integration (CI)

**Every push triggers an automated check.**

```
Developer pushes code
        ↓
CI server detects the change
        ↓
Compiles the code
        ↓
Runs all tests
        ↓
Reports pass or fail
```

**Goal:** Catch broken code before it reaches `main`.

---

## Continuous Delivery vs Deployment

| | Continuous Delivery | Continuous Deployment |
|---|---|---|
| **What** | Code is always ready to deploy | Code is deployed automatically |
| **Human step** | Someone clicks "Deploy" | No human needed |
| **Best for** | Teams that need release approval | Teams that deploy many times a day |

---

## Why CI/CD?

- Catch bugs early — before they reach production
- No manual build steps — everything is automated
- Consistent process — same steps every time, no human error
- Faster feedback — know in minutes if something is broken
- Safer releases — small, frequent changes are less risky than big ones

---

## Popular CI/CD Tools

| Tool | Type | Best known for |
|---|---|---|
| **GitHub Actions** | Cloud (GitHub-native) | Built into GitHub, easy YAML config |
| **GitLab CI** | Cloud / Self-hosted | Built into GitLab, powerful pipelines |
| **Jenkins** | Self-hosted | Highly configurable, large plugin ecosystem |
| **CircleCI** | Cloud | Fast builds, Docker-first |
| **Travis CI** | Cloud | Popular in open source projects |
| **Bitbucket Pipelines** | Cloud (Bitbucket-native) | Built into Bitbucket |

---

## GitHub Actions

**CI/CD built directly into GitHub — no extra tools needed.**

- Triggered by events: push, pull request, schedule
- Configured with YAML files in `.github/workflows/`
- Runs on GitHub-hosted virtual machines called **runners**

```
.github/
└── workflows/
    └── my-app.yml    ← Your pipeline definition
```

---

## Anatomy of a GitHub Actions Workflow

```yaml
name: "My App"               # Pipeline name

on:                          # What triggers it
  push:
    branches: [main]
  pull_request:

jobs:
  build:                     # A job (runs on one machine)
    runs-on: ubuntu-latest

    steps:                   # Steps run in order
      - uses: actions/checkout@v4       # Download the code
      - name: Set up Java
        uses: actions/setup-java@v4
        with:
          java-version: "17"
      - name: Build and Test
        run: mvn -B verify              # Run your command
```

---

## Key Concepts in GitHub Actions

| Term | Definition |
|---|---|
| **Workflow** | The full pipeline — defined in a `.yml` file |
| **Trigger** | The event that starts the pipeline (push, PR, schedule) |
| **Job** | A set of steps that run on the same machine |
| **Step** | A single action or shell command |
| **Runner** | The virtual machine that executes the job |
| **Action** | A reusable step from the GitHub marketplace (`uses:`) |
| **Secret** | An encrypted variable stored in GitHub settings |

---

## CI/CD + Code Quality (SonarCloud)

**Add a quality gate to your pipeline — block merges if code quality drops.**

```
Push code
    ↓
Build & Test (mvn verify)
    ↓
SonarCloud Analysis
    ↓
Quality Gate check
    ↓
PASS → merge allowed
FAIL → pipeline blocked
```

Key flag: `-Dsonar.qualitygate.wait=true`
Makes the pipeline wait for SonarCloud's verdict before finishing.

---

## The Full CI/CD Picture

```
Developer pushes to a branch
            ↓
GitHub Actions triggers
            ↓
    ┌───────────────────┐
    │  CI Pipeline      │
    │  · Compile        │
    │  · Run tests      │
    │  · Code coverage  │
    │  · Sonar analysis │
    └───────────────────┘
            ↓
       Pass?
      /      \
    YES       NO
     ↓         ↓
PR can      Pipeline
be merged   is blocked
     ↓
  Merge to main
     ↓
  CD Pipeline
  · Deploy to staging
  · Deploy to production
```

---

## Quick Reference

| Term | Definition |
|---|---|
| CI | Auto build and test on every push |
| CD | Auto release code that passes CI |
| Pipeline | The full sequence of automated steps |
| Workflow | A GitHub Actions pipeline file (`.yml`) |
| Job | Steps that run together on one machine |
| Runner | The virtual machine running your job |
| Secret | Encrypted value (e.g. API keys, tokens) |
| Quality Gate | A pass/fail threshold for code quality |
