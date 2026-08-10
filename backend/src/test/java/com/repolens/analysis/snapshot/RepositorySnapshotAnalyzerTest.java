package com.repolens.analysis.snapshot;

import com.repolens.analysis.detector.DefaultDockerDetector;
import com.repolens.analysis.detector.DefaultGitHubActionsDetector;
import com.repolens.analysis.detector.DefaultLicenseDetector;
import com.repolens.analysis.detector.DefaultReadmeDetector;
import com.repolens.analysis.detector.SnapshotBuildToolDetector;
import com.repolens.analysis.detector.SnapshotLanguageDetector;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.repolens.analysis.architecture.ArchitectureType;
import com.repolens.analysis.architecture.DefaultArchitectureDetector;
class RepositorySnapshotAnalyzerTest {

    @Test
    void shouldAnalyzeRepositorySnapshot() {

        RepositorySnapshot snapshot =
                new RepositorySnapshot(
                        Path.of("/repo"),

                        List.of(
                                new RepositoryDirectory(
                                        "src",
                                        "src"
                                ),
                                new RepositoryDirectory(
                                        "main",
                                        "src/main"
                                ),
                                new RepositoryDirectory(
                                        "controller",
                                        "src/main/java/controller"
                                ),
                                new RepositoryDirectory(
                                        "service",
                                        "src/main/java/service"
                                ),
                                new RepositoryDirectory(
                                        "repository",
                                        "src/main/java/repository"
                                )
                        ),

                        List.of(
                                new RepositoryFile(
                                        "README.md",
                                        "README.md",
                                        100L,
                                        "md"
                                ),
                                new RepositoryFile(
                                        "pom.xml",
                                        "pom.xml",
                                        200L,
                                        "xml"
                                ),
                                new RepositoryFile(
                                        "Dockerfile",
                                        "Dockerfile",
                                        150L,
                                        ""
                                ),
                                new RepositoryFile(
                                        "LICENSE",
                                        "LICENSE",
                                        100L,
                                        ""
                                ),
                                new RepositoryFile(
                                        "build.yml",
                                        ".github/workflows/build.yml",
                                        100L,
                                        "yml"
                                ),
                                new RepositoryFile(
                                        "App.java",
                                        "src/main/App.java",
                                        300L,
                                        "java"
                                ),
                                new RepositoryFile(
                                        "main.py",
                                        "src/main/main.py",
                                        300L,
                                        "py"
                                )
                        )
                );

        RepositorySnapshotAnalyzer analyzer =
                new DefaultRepositorySnapshotAnalyzer(
                        new DefaultReadmeDetector(),
                        new DefaultDockerDetector(),
                        new DefaultGitHubActionsDetector(),
                        new DefaultLicenseDetector(),
                        new SnapshotBuildToolDetector(),
                        new SnapshotLanguageDetector(),
                        new DefaultRepositoryStructureAnalyzer(),
                        new com.repolens.analysis.architecture.DefaultArchitectureDetector()
                );

        RepositorySnapshotAnalysis result =
                analyzer.analyze(snapshot);

        assertTrue(result.readmePresent());
        assertTrue(result.dockerPresent());
        assertTrue(result.githubActionsPresent());
        assertTrue(result.licensePresent());

        assertTrue(
                result.buildTools()
                        .contains(
                                com.repolens.analysis.detector.BuildTool.MAVEN
                        )
        );

        assertTrue(
                result.languages()
                        .contains(
                                com.repolens.analysis.detector.ProgrammingLanguage.JAVA
                        )
        );

        assertTrue(
                result.languages()
                        .contains(
                                com.repolens.analysis.detector.ProgrammingLanguage.PYTHON
                        )
        );

        assertEquals(7, result.structure().totalFiles());
        assertEquals(5, result.structure().totalDirectories());
        assertEquals(2, result.structure().sourceFiles());
        assertEquals(1, result.structure().documentationFiles());
        assertEquals(2, result.structure().configurationFiles());
        assertEquals(ArchitectureType.LAYERED,result.architecture().primaryArchitecture());
    }
}