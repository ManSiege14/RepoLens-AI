# Data Model

We use PostgreSQL. The database stores users, repositories, analysis runs, deterministic findings, generated insights, and chat history.

## High-Level Relationship
User
│
│ 1
▼
SavedRepository
│
│ *
▼
Repository
│
│ 1
▼
AnalysisRun
├──────────► RepositorySnapshot
├──────────► Finding
├──────────► GeneratedInsight
└──────────► ChatSession
│
▼
ChatMessage

**The important distinction is: Repository ≠ AnalysisRun**
A repository is the long-lived identity of a GitHub project. An analysis run represents RepoLens analyzing a particular commit.

If someone analyzes the same repository at the same commit (`owner + repository + commit SHA`), we reuse the existing completed analysis. If the repository changes, we create a new analysis.

## Core Tables

### `users`
* `id` UUID
* `github_id` BIGINT
* `github_login` VARCHAR
* `display_name` VARCHAR
* `avatar_url` VARCHAR
* `created_at` TIMESTAMP
* `updated_at` TIMESTAMP

### `repositories`
* `id` UUID
* `github_repository_id` BIGINT
* `owner` VARCHAR
* `name` VARCHAR
* `full_name` VARCHAR
* `html_url` VARCHAR
* `default_branch` VARCHAR
* `visibility` VARCHAR
* `description` TEXT
* `stars` INTEGER
* `forks` INTEGER
* `primary_language` VARCHAR
* `created_at` TIMESTAMP
* `updated_at` TIMESTAMP

### `analysis_runs`
* `id` UUID
* `repository_id` UUID FK
* `requested_by` UUID FK nullable
* `commit_sha` VARCHAR
* `status` ENUM (QUEUED, FETCHING, SCANNING, PARSING, DETECTING, BUILDING_KNOWLEDGE, GENERATING_INSIGHTS, COMPLETED, FAILED)
* `progress_percentage` INTEGER
* `rie_version` VARCHAR *(Tracks which engine version produced the result)*
* `ai_provider` VARCHAR
* `ai_model` VARCHAR
* `started_at` TIMESTAMP
* `completed_at` TIMESTAMP
* `created_at` TIMESTAMP
* `failure_code` VARCHAR nullable
* `failure_message` TEXT nullable

### `repository_snapshots`
Stores the structured output from RIE. We use JSONB because RIE's output structure will evolve quickly.
* `id` UUID
* `analysis_run_id` UUID FK
* `repository_tree` JSONB
* `language_stats` JSONB
* `detected_tech` JSONB
* `detected_components` JSONB
* `detected_endpoints` JSONB
* `dependency_summary` JSONB
* `metrics` JSONB
* `created_at` TIMESTAMP

### `findings`
A finding is one deterministic conclusion. This allows us to answer *why* RepoLens concluded something, rather than just stating it.
* `id` UUID
* `analysis_run_id` UUID FK
* `type` VARCHAR
* `name` VARCHAR
* `confidence` DECIMAL
* `detector` VARCHAR
* `evidence` JSONB
* `created_at` TIMESTAMP

### `generated_insights`
* `id` UUID
* `analysis_run_id` UUID FK
* `type` VARCHAR (SUMMARY, ARCHITECTURE_EXPLANATION, LEARNING_PATH, README_REVIEW)
* `content` JSONB
* `provider` VARCHAR
* `model` VARCHAR
* `prompt_version` VARCHAR
* `created_at` TIMESTAMP

### `saved_repositories`
* `user_id` UUID FK
* `repository_id` UUID FK
* `created_at` TIMESTAMP

### `chat_sessions`
* `id` UUID
* `user_id` UUID FK
* `analysis_run_id` UUID FK
* `created_at` TIMESTAMP

### `chat_messages`
* `id` UUID
* `session_id` UUID FK
* `role` USER | ASSISTANT
* `content` TEXT
* `context_references` JSONB *(Crucial for grounding responses to specific files)*
* `provider` VARCHAR
* `model` VARCHAR
* `created_at` TIMESTAMP