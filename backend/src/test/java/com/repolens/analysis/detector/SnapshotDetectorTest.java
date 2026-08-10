package com.repolens.analysis.detector;

import com.repolens.analysis.snapshot.RepositoryFile;
import com.repolens.analysis.snapshot.RepositorySnapshot;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SnapshotDetectorTest {

    @Test
    void shouldDetectDocker() {

        RepositorySnapshot snapshot = snapshot(
                "Dockerfile",
                "README.md"
        );

        DockerDetector detector =
                new DefaultDockerDetector();

        assertTrue(detector.detect(snapshot));
    }

    @Test
    void shouldDetectGitHubActions() {

        RepositorySnapshot snapshot = snapshot(
                ".github/workflows/build.yml"
        );

        GitHubActionsDetector detector =
                new DefaultGitHubActionsDetector();

        assertTrue(detector.detect(snapshot));
    }

    @Test
    void shouldDetectLicense() {

        RepositorySnapshot snapshot = snapshot(
                "LICENSE"
        );

        LicenseDetector detector =
                new DefaultLicenseDetector();

        assertTrue(detector.detect(snapshot));
    }

    @Test
    void shouldReturnFalseWhenSpecialFilesAreMissing() {

        RepositorySnapshot snapshot = snapshot(
                "README.md",
                "pom.xml"
        );

        assertFalse(
                new DefaultDockerDetector().detect(snapshot)
        );

        assertFalse(
                new DefaultGitHubActionsDetector().detect(snapshot)
        );

        assertFalse(
                new DefaultLicenseDetector().detect(snapshot)
        );
    }

    private RepositorySnapshot snapshot(String... paths) {

        List<RepositoryFile> files =
                java.util.Arrays.stream(paths)
                        .map(path -> new RepositoryFile(
                                Path.of(path)
                                        .getFileName()
                                        .toString(),
                                path,
                                100L,
                                getExtension(path)
                        ))
                        .toList();

        return new RepositorySnapshot(
                Path.of("/repo"),
                List.of(),
                files
        );
    }

    private String getExtension(String path) {

        int lastDot = path.lastIndexOf('.');

        if (lastDot <= 0 || lastDot == path.length() - 1) {
            return "";
        }

        return path.substring(lastDot + 1);
    }
}