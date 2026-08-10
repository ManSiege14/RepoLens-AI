package com.repolens.analysis.snapshot;

import com.repolens.analysis.architecture.ArchitectureAnalysis;
import com.repolens.analysis.architecture.ArchitectureDetector;
import com.repolens.analysis.detector.BuildTool;
import com.repolens.analysis.detector.DefaultDockerDetector;
import com.repolens.analysis.detector.DefaultGitHubActionsDetector;
import com.repolens.analysis.detector.DefaultLicenseDetector;
import com.repolens.analysis.detector.DefaultReadmeDetector;
import com.repolens.analysis.detector.ProgrammingLanguage;
import com.repolens.analysis.detector.SnapshotBuildToolDetector;
import com.repolens.analysis.detector.SnapshotLanguageDetector;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DefaultRepositorySnapshotAnalyzer
        implements RepositorySnapshotAnalyzer {

    private final DefaultReadmeDetector readmeDetector;
    private final DefaultDockerDetector dockerDetector;
    private final DefaultGitHubActionsDetector githubActionsDetector;
    private final DefaultLicenseDetector licenseDetector;
    private final SnapshotBuildToolDetector buildToolDetector;
    private final SnapshotLanguageDetector languageDetector;
    private final RepositoryStructureAnalyzer structureAnalyzer;
    private final ArchitectureDetector architectureDetector;

    public DefaultRepositorySnapshotAnalyzer(
            DefaultReadmeDetector readmeDetector,
            DefaultDockerDetector dockerDetector,
            DefaultGitHubActionsDetector githubActionsDetector,
            DefaultLicenseDetector licenseDetector,
            SnapshotBuildToolDetector buildToolDetector,
            SnapshotLanguageDetector languageDetector,
            RepositoryStructureAnalyzer structureAnalyzer,
            ArchitectureDetector architectureDetector
    ) {
        this.readmeDetector = readmeDetector;
        this.dockerDetector = dockerDetector;
        this.githubActionsDetector = githubActionsDetector;
        this.licenseDetector = licenseDetector;
        this.buildToolDetector = buildToolDetector;
        this.languageDetector = languageDetector;
        this.structureAnalyzer = structureAnalyzer;
        this.architectureDetector = architectureDetector;
    }

    @Override
    public RepositorySnapshotAnalysis analyze(
            RepositorySnapshot snapshot
    ) {

        boolean readmePresent =
                readmeDetector.detect(snapshot);

        boolean dockerPresent =
                dockerDetector.detect(snapshot);

        boolean githubActionsPresent =
                githubActionsDetector.detect(snapshot);

        boolean licensePresent =
                licenseDetector.detect(snapshot);

        Set<BuildTool> buildTools =
                buildToolDetector.detect(snapshot);

        Set<ProgrammingLanguage> languages =
                languageDetector.detect(snapshot);

        RepositoryStructure structure =
                structureAnalyzer.analyze(snapshot);

        ArchitectureAnalysis architecture =
                architectureDetector.detect(snapshot);

        return new RepositorySnapshotAnalysis(
                readmePresent,
                dockerPresent,
                githubActionsPresent,
                licensePresent,
                buildTools,
                languages,
                structure,
                architecture,
                null
        );
    }
}