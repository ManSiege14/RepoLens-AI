package com.repolens.analysis.health;

import com.repolens.analysis.snapshot.RepositorySnapshotAnalysis;

public interface RepositoryHealthAnalyzer {

    RepositoryHealthAnalysis analyze(
            RepositorySnapshotAnalysis analysis
    );
}