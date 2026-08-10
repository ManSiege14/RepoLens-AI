package com.repolens.analysis.health;

import java.util.List;

public record RepositoryHealthAnalysis(
        int score,
        HealthGrade grade,
        List<HealthFinding> findings
) {
}