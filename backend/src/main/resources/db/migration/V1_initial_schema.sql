CREATE TABLE repositories (
                              id UUID PRIMARY KEY,
                              github_repository_id BIGINT NOT NULL UNIQUE,
                              owner VARCHAR(255) NOT NULL,
                              name VARCHAR(255) NOT NULL,
                              full_name VARCHAR(512) NOT NULL UNIQUE,
                              html_url TEXT NOT NULL,
                              default_branch VARCHAR(255),
                              visibility VARCHAR(50),
                              description TEXT,
                              stars INTEGER NOT NULL DEFAULT 0,
                              forks INTEGER NOT NULL DEFAULT 0,
                              primary_language VARCHAR(100),
                              created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                              updated_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE analysis_runs (
                               id UUID PRIMARY KEY,
                               repository_id UUID NOT NULL REFERENCES repositories(id),
                               commit_sha VARCHAR(64) NOT NULL,
                               status VARCHAR(50) NOT NULL,
                               progress_percentage INTEGER NOT NULL DEFAULT 0,
                               rie_version VARCHAR(50) NOT NULL,
                               ai_provider VARCHAR(100),
                               ai_model VARCHAR(100),
                               started_at TIMESTAMPTZ,
                               completed_at TIMESTAMPTZ,
                               created_at TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               failure_code VARCHAR(100),
                               failure_message TEXT,

                               CONSTRAINT uk_analysis_repository_commit_rie
                                   UNIQUE(repository_id, commit_sha, rie_version),

                               CONSTRAINT chk_analysis_progress
                                   CHECK (progress_percentage BETWEEN 0 AND 100)
);

CREATE INDEX idx_analysis_runs_repository_id
    ON analysis_runs(repository_id);

CREATE INDEX idx_analysis_runs_status
    ON analysis_runs(status);