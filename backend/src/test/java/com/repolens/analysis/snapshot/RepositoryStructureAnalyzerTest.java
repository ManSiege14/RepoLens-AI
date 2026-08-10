package com.repolens.analysis.snapshot;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RepositoryStructureAnalyzerTest {

    private final RepositoryStructureAnalyzer analyzer =
            new DefaultRepositoryStructureAnalyzer();

    @Test
    void shouldAnalyzeRepositoryStructure() {

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
                                        "java",
                                        "src/main/java"
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
                                        "App.java",
                                        "src/main/java/App.java",
                                        300L,
                                        "java"
                                ),
                                new RepositoryFile(
                                        "Service.java",
                                        "src/main/java/Service.java",
                                        300L,
                                        "java"
                                ),
                                new RepositoryFile(
                                        "application.yml",
                                        "src/main/resources/application.yml",
                                        150L,
                                        "yml"
                                )
                        )
                );

        RepositoryStructure result =
                analyzer.analyze(snapshot);

        assertEquals(5, result.totalFiles());
        assertEquals(3, result.totalDirectories());
        assertEquals(2, result.sourceFiles());
        assertEquals(1, result.documentationFiles());
        assertEquals(2, result.configurationFiles());
    }
}