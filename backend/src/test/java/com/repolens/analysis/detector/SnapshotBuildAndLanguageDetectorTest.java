package com.repolens.analysis.detector;

import com.repolens.analysis.snapshot.RepositoryFile;
import com.repolens.analysis.snapshot.RepositorySnapshot;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertTrue;

class SnapshotBuildAndLanguageDetectorTest {

    private final RepositorySnapshot snapshot =
            new RepositorySnapshot(
                    Path.of("/repo"),
                    List.of(),
                    List.of(
                            new RepositoryFile(
                                    "pom.xml",
                                    "pom.xml",
                                    100L,
                                    "xml"
                            ),
                            new RepositoryFile(
                                    "App.java",
                                    "src/main/java/App.java",
                                    200L,
                                    "java"
                            ),
                            new RepositoryFile(
                                    "main.py",
                                    "src/main/python/main.py",
                                    150L,
                                    "py"
                            )
                    )
            );

    @Test
    void shouldDetectMaven() {

        Set<BuildTool> buildTools =
                new SnapshotBuildToolDetector()
                        .detect(snapshot);

        assertTrue(buildTools.contains(BuildTool.MAVEN));
    }

    @Test
    void shouldDetectJavaAndPython() {

        Set<ProgrammingLanguage> languages =
                new SnapshotLanguageDetector()
                        .detect(snapshot);

        assertTrue(
                languages.contains(ProgrammingLanguage.JAVA)
        );

        assertTrue(
                languages.contains(ProgrammingLanguage.PYTHON)
        );
    }
}