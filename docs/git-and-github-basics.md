# Git & GitHub Basics

---

## What is Git?

**A version control system — tracks every change to your code over time.**

- Saves snapshots of your project called **commits**
- Lets you go back to any point in history
- Works entirely on your local machine
- Free and open source

---

## What is GitHub?

**A cloud platform for hosting Git repositories.**

| Git | GitHub |
|---|---|
| The tool that tracks changes | The website that hosts your code |
| Works on your local machine | Works in the cloud |
| Used via the terminal | Used via a browser |

> Git is the engine. GitHub is the garage where you park and share your work.

---

## Core Concepts

| Term | Definition |
|---|---|
| **Repository** | A folder Git is tracking — contains your code + full history |
| **Commit** | A saved snapshot with a message, author, and timestamp |
| **Branch** | An independent line of development |
| **Remote** | A copy of your repo hosted elsewhere (e.g. GitHub) — usually called `origin` |

---

## What is a Branch?

**An independent line of development — work without affecting `main`.**

```
main:     A --- B --- C
                       \
feature:                D --- E
```

- `main` is the stable, production-ready branch
- You create a branch for every new feature or fix
- Merge it back when the work is done

---

## Clone — Download a Repo

**Copy a remote repository to your local machine.**

```bash
git clone https://github.com/user/repo.git
```

- Downloads the full history, not just the latest files
- Creates a folder with the repo name
- Only done once per project

```bash
# Clone into a custom folder name
git clone https://github.com/user/repo.git my-folder
```

---

## Branch — Create & Switch

**Always create a branch before starting new work.**

```bash
# See all branches (* = current)
git branch

# Create and switch in one step (recommended)
git switch -c feature/my-feature

# Switch to an existing branch
git switch main
```

**Naming conventions**

| Prefix | Use for |
|---|---|
| `feature/` | New functionality |
| `fix/` | Bug fixes |
| `docs/` | Documentation changes |

---

## Checkout — Navigate Your Repo

**Older command for switching branches and restoring files.**

```bash
# Switch branch (older syntax, still widely used)
git checkout main

# Create and switch to a new branch (older syntax)
git checkout -b feature/my-feature

# Discard local changes to a single file
git checkout -- src/MyFile.java
```

> `git switch` is the modern replacement for branch switching. `git checkout` is still used for restoring files.

---

## Stage & Commit — Save Your Work

**Two-step process: stage what you want, then commit.**

```bash
# See what changed
git status

# See the actual changes
git diff

# Stage a specific file
git add src/main/java/MyFile.java

# Stage everything
git add .

# Save a snapshot
git commit -m "Add weather alert endpoint"
```

**Write commit messages in imperative mood:**
- "Add feature" not "Added feature"
- "Fix bug" not "Fixing bug"

---

## Push & Pull — Sync with GitHub

```bash
# Upload your commits to GitHub
git push origin feature/my-feature

# First push of a new branch
git push -u origin feature/my-feature

# Download and apply the latest changes
git pull

# Download without applying yet
git fetch
```

---

## Pull Request (PR)

**A GitHub feature — a request to merge your branch into `main`, with a review process.**

Not a Git command. It happens on github.com.

**Step-by-step**

```
1. Create a branch
   git switch -c feature/add-search

2. Make changes and commit
   git add .
   git commit -m "Add city search endpoint"

3. Push to GitHub
   git push -u origin feature/add-search

4. Open a Pull Request on github.com
   · Click "Compare & pull request"
   · Add a title and description
   · Request reviewers

5. After approval → Merge into main
```

---

## The Everyday Workflow

```
git switch main
git pull                          ← Start from latest main
        ↓
git switch -c feature/my-feature  ← Create your branch
        ↓
git add . && git commit -m "..."  ← Save your work
        ↓
git push -u origin feature/...    ← Push to GitHub
        ↓
Open Pull Request on github.com   ← Request review
        ↓
Merge approved → back to main
git switch main && git pull       ← Stay up to date
```

---

## Quick Reference

| Command | What it does |
|---|---|
| `git clone <url>` | Download a repo to your machine |
| `git status` | See what files have changed |
| `git diff` | See the actual changes |
| `git add <file>` | Stage a file for commit |
| `git commit -m "msg"` | Save a snapshot |
| `git push` | Upload commits to GitHub |
| `git pull` | Download and apply remote changes |
| `git branch` | List all branches |
| `git switch -c <name>` | Create and switch to a new branch |
| `git switch <name>` | Switch to an existing branch |
| `git checkout <branch>` | Switch branch (older syntax) |
| `git log --oneline` | See commit history, one line each |
