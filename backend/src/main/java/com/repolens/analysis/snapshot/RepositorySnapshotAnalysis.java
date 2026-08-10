package com.repolens.analysis.snapshot;

import com.repolens.analysis.architecture.ArchitectureAnalysis;
import com.repolens.analysis.detector.BuildTool;
import com.repolens.analysis.detector.ProgrammingLanguage;
import com.repolens.analysis.health.RepositoryHealthAnalysis;

import java.util.Set;

public record RepositorySnapshotAnalysis(

        boolean readmePresent,

        boolean dockerPresent,

        boolean githubActionsPresent,

        boolean licensePresent,

        Set<BuildTool> buildTools,

        Set<ProgrammingLanguage> languages,

        RepositoryStructure structure,

        ArchitectureAnalysis architecture,

        RepositoryHealthAnalysis health

) {
}