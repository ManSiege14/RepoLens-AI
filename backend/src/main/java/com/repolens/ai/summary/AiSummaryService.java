package com.repolens.ai.summary;

import com.repolens.analysis.snapshot.RepositorySnapshotAnalysis;

public interface AiSummaryService {

    AiRepositorySummary generateSummary(
            RepositorySnapshotAnalysis analysis
    );
}