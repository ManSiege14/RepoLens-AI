package com.repolens.analysis;

import com.repolens.analysis.architecture.DefaultArchitectureDetector;
import com.repolens.analysis.detector.DefaultDockerDetector;
import com.repolens.analysis.detector.DefaultGitHubActionsDetector;
import com.repolens.analysis.detector.DefaultLicenseDetector;
import com.repolens.analysis.detector.DefaultReadmeDetector;
import com.repolens.analysis.detector.SnapshotBuildToolDetector;
import com.repolens.analysis.detector.SnapshotLanguageDetector;
import com.repolens.analysis.health.DefaultRepositoryHealthAnalyzer;
import com.repolens.analysis.snapshot.DefaultRepositorySnapshotAnalyzer;
import com.repolens.analysis.snapshot.DefaultRepositoryStructureAnalyzer;
import com.repolens.analysis.snapshot.RepositorySnapshot;
import com.repolens.analysis.snapshot.RepositorySnapshotAnalysis;
import com.repolens.analysis.snapshot.RepositorySnapshotAnalyzer;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepositoryAnalysisOrchestratorTest {

    @Test
    void shouldProduceCompleteRepositoryAnalysis() {

        RepositorySnapshotAnalyzer snapshotAnalyzer =
                new DefaultRepositorySnapshotAnalyzer(
                        new DefaultReadmeDetector(),
                        new DefaultDockerDetector(),
                        new DefaultGitHubActionsDetector(),
                        new DefaultLicenseDetector(),
                        new SnapshotBuildToolDetector(),
                        new SnapshotLanguageDetector(),
                        new DefaultRepositoryStructureAnalyzer(),
                        new DefaultArchitectureDetector()
                );

        RepositoryAnalysisOrchestrator orchestrator =
                new RepositoryAnalysisOrchestrator(
                        snapshotAnalyzer,
                        new DefaultRepositoryHealthAnalyzer()
                );

        RepositorySnapshot snapshot =
                new RepositorySnapshot(
                        Path.of("/repo"),

                        List.of(
                                new com.repolens.analysis.snapshot.RepositoryDirectory(
                                        "controller",
                                        "src/main/java/controller"
                                ),
                                new com.repolens.analysis.snapshot.RepositoryDirectory(
                                        "service",
                                        "src/main/java/service"
                                ),
                                new com.repolens.analysis.snapshot.RepositoryDirectory(
                                        "repository",
                                        "src/main/java/repository"
                                )
                        ),

                        List.of(
                                new com.repolens.analysis.snapshot.RepositoryFile(
                                        "README.md",
                                        "README.md",
                                        100L,
                                        "md"
                                ),
                                new com.repolens.analysis.snapshot.RepositoryFile(
                                        "pom.xml",
                                        "pom.xml",
                                        200L,
                                        "xml"
                                ),
                                new com.repolens.analysis.snapshot.RepositoryFile(
                                        "Dockerfile",
                                        "Dockerfile",
                                        100L,
                                        ""
                                ),
                                new com.repolens.analysis.snapshot.RepositoryFile(
                                        "LICENSE",
                                        "LICENSE",
                                        100L,
                                        ""
                                ),
                                new com.repolens.analysis.snapshot.RepositoryFile(
                                        "build.yml",
                                        ".github/workflows/build.yml",
                                        100L,
                                        "yml"
                                ),
                                new com.repolens.analysis.snapshot.RepositoryFile(
                                        "UserController.java",
                                        "src/main/java/controller/UserController.java",
                                        100L,
                                        "java"
                                )
                        )
                );

        RepositorySnapshotAnalysis result =
                orchestrator.analyze(snapshot);

        assertNotNull(result);
        assertNotNull(result.architecture());
        assertNotNull(result.health());

        assertTrue(result.readmePresent());
        assertTrue(result.dockerPresent());
        assertTrue(result.githubActionsPresent());
        assertTrue(result.licensePresent());

        assertTrue(result.health().score() > 0);
    }
}