# High-Level Design

## System Architecture

                    Browser
                       │
             Next.js Frontend
                       │
        ------------------------------
        │            REST API         │
        ------------------------------
                       │
              Spring Boot Backend
                       │
┌───────────┬────────────┬────────────┬────────────┐
│           │            │            │
Auth      GitHub      Intelligence     AI
Module    Module         Engine       Module
│           │            │            │
└───────────┼────────────┼────────────┘
▼
Analysis Orchestrator
│
┌───────┴────────┐
▼                ▼
PostgreSQL        Redis Cache
│
▼
Gemini API

**Note:** AI is not the center of the architecture. The Intelligence Engine is the core.

## Repository Intelligence Engine (RIE)
The Intelligence Engine is split into 5 internal deterministic engines, each with a single responsibility.

1. **Fetch Engine:** Gets README, file tree, metadata, repository info.
2. **Scan Engine:** Detects pom.xml, package.json, Docker, CI/CD, languages.
3. **Detect Engine:** Discovers architectural patterns (MVC, Hexagonal, REST, GraphQL, Spring Security, DBs).
4. **Knowledge Engine:** Builds Repository → Modules → Classes → Relationships → Statistics (Pure Java, no AI).
5. **Explain Engine:** Sends *only* structured information to Gemini.

## Confidence Scoring
Every detected insight gets a confidence score to distinguish between facts and educated guesses.
* *Example:* Spring MVC (98%), Spring Security (100%), Hexagonal (42% - Needs AI verification).

## The Three Layers of RepoLens
1. **Layer 1 — Facts:** Everything deterministic (Java, GitHub API, Parser, Scanner).
2. **Layer 2 — Knowledge:** Relationships, Architecture, Metrics, Dependencies.
3. **Layer 3 — AI:** Human explanations, Learning path, Q&A, Summary.

*Architectural takeaway: If the AI provider goes down, RepoLens still functions as a powerful deterministic analysis tool.*