package com.repolens.analysis.detector;

import com.repolens.analysis.snapshot.RepositoryDirectory;
import com.repolens.analysis.snapshot.RepositoryFile;
import com.repolens.analysis.snapshot.RepositorySnapshot;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DefaultReadmeDetectorTest {

    private final ReadmeDetector detector =
            new DefaultReadmeDetector();

    @Test
    void shouldDetectReadme() {

        RepositorySnapshot snapshot =
                new RepositorySnapshot(
                        Path.of("/repo"),
                        List.of(),
                        List.of(
                                new RepositoryFile(
                                        "README.md",
                                        "README.md",
                                        100,
                                        "md"
                                )
                        )
                );

        assertTrue(detector.detect(snapshot));
    }

    @Test
    void shouldReturnFalseWhenReadmeDoesNotExist() {

        RepositorySnapshot snapshot =
                new RepositorySnapshot(
                        Path.of("/repo"),
                        List.of(),
                        List.of(
                                new RepositoryFile(
                                        "pom.xml",
                                        "pom.xml",
                                        100,
                                        "xml"
                                )
                        )
                );

        assertFalse(detector.detect(snapshot));
    }
}