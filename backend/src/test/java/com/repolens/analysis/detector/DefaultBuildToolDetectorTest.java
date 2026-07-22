package com.repolens.analysis.detector;

import com.repolens.analysis.scanner.ScannedFile;
import com.repolens.analysis.scanner.ScannedRepository;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultBuildToolDetectorTest {

    private final BuildToolDetector detector = new DefaultBuildToolDetector();

    @Test
    void shouldDetectMaven() {
        ScannedRepository repository = repository(
                "pom.xml"
        );

        assertEquals(
                Set.of(BuildTool.MAVEN),
                detector.detect(repository)
        );
    }

    @Test
    void shouldDetectGradle() {
        ScannedRepository repository = repository(
                "build.gradle"
        );

        assertEquals(
                Set.of(BuildTool.GRADLE),
                detector.detect(repository)
        );
    }

    @Test
    void shouldDetectMultipleBuildTools() {
        ScannedRepository repository = repository(
                "pom.xml",
                "frontend/package.json"
        );

        assertEquals(
                Set.of(
                        BuildTool.MAVEN,
                        BuildTool.NPM
                ),
                detector.detect(repository)
        );
    }

    @Test
    void shouldReturnEmptySetWhenNoBuildToolExists() {
        ScannedRepository repository = repository(
                "README.md",
                "LICENSE"
        );

        assertEquals(
                Set.of(),
                detector.detect(repository)
        );
    }

    @Test
    void shouldIgnoreDuplicateBuildToolFiles() {
        ScannedRepository repository = repository(
                "module1/pom.xml",
                "module2/pom.xml"
        );

        assertEquals(
                Set.of(BuildTool.MAVEN),
                detector.detect(repository)
        );
    }

    private ScannedRepository repository(String... paths) {
        List<ScannedFile> files = Arrays.stream(paths)
                .map(this::file)
                .toList();

        return new ScannedRepository(
                Path.of("/repo"),
                files
        );
    }

    private ScannedFile file(String relativePath) {
        Path root = Path.of("/repo");
        return new ScannedFile(
                root.resolve(relativePath),
                Path.of(relativePath)
        );
    }
}