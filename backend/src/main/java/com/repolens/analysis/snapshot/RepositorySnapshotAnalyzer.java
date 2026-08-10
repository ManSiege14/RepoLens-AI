package com.repolens.analysis.snapshot;

public interface RepositorySnapshotAnalyzer {

    RepositorySnapshotAnalysis analyze(
            RepositorySnapshot snapshot
    );
}