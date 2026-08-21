package com.repolens.repository.web.dto;

import java.time.Instant;
import java.util.Set;

public record RepositorySnapshotAnalysisResponse(

        Instant analyzedAt,

        boolean readmePresent,
        boolean dockerPresent,
        boolean githubActionsPresent,
        boolean licensePresent,

        Set<String> buildTools,
        Set<String> languages,

        int totalFiles,
        int totalDirectories,
        int sourceFiles,
        int documentationFiles,
        int configurationFiles,

        String primaryArchitecture,

        int healthScore,
        String healthGrade,

        String architectureData,
        String healthData

) {
}