package com.repolens.analysis;

import com.repolens.analysis.health.RepositoryHealthAnalyzer;
import com.repolens.analysis.snapshot.RepositorySnapshot;
import com.repolens.analysis.snapshot.RepositorySnapshotAnalysis;
import com.repolens.analysis.snapshot.RepositorySnapshotAnalyzer;
import org.springframework.stereotype.Component;

@Component
public class RepositoryAnalysisOrchestrator {

    private final RepositorySnapshotAnalyzer snapshotAnalyzer;
    private final RepositoryHealthAnalyzer healthAnalyzer;

    public RepositoryAnalysisOrchestrator(
            RepositorySnapshotAnalyzer snapshotAnalyzer,
            RepositoryHealthAnalyzer healthAnalyzer
    ) {
        this.snapshotAnalyzer = snapshotAnalyzer;
        this.healthAnalyzer = healthAnalyzer;
    }

    public RepositorySnapshotAnalysis analyze(
            RepositorySnapshot snapshot
    ) {

        RepositorySnapshotAnalysis baseAnalysis =
                snapshotAnalyzer.analyze(snapshot);

        var health =
                healthAnalyzer.analyze(baseAnalysis);

        return new RepositorySnapshotAnalysis(
                baseAnalysis.readmePresent(),
                baseAnalysis.dockerPresent(),
                baseAnalysis.githubActionsPresent(),
                baseAnalysis.licensePresent(),
                baseAnalysis.buildTools(),
                baseAnalysis.languages(),
                baseAnalysis.structure(),
                baseAnalysis.architecture(),
                health
        );
    }
}