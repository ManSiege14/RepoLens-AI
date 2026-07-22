package com.repolens.analysis.detector;

import com.repolens.analysis.scanner.ScannedFile;
import com.repolens.analysis.scanner.ScannedRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultInfrastructureDetectorTest {

    private final InfrastructureDetector detector =
            new DefaultInfrastructureDetector();

    @TempDir
    Path tempDir;

    @Test
    void shouldDetectDocker() {

        assertEquals(
                Set.of(Infrastructure.DOCKER),
                detector.detect(repository("Dockerfile"))
        );
    }

    @Test
    void shouldDetectDockerCompose() {

        assertEquals(
                Set.of(Infrastructure.DOCKER_COMPOSE),
                detector.detect(repository("docker-compose.yml"))
        );
    }

    @Test
    void shouldDetectGithubActions() {

        assertEquals(
                Set.of(Infrastructure.GITHUB_ACTIONS),
                detector.detect(repository(".github/workflows/build.yml"))
        );
    }

    @Test
    void shouldDetectJenkins() {

        assertEquals(
                Set.of(Infrastructure.JENKINS),
                detector.detect(repository("Jenkinsfile"))
        );
    }

    @Test
    void shouldDetectGitLabCi() {

        assertEquals(
                Set.of(Infrastructure.GITLAB_CI),
                detector.detect(repository(".gitlab-ci.yml"))
        );
    }

    @Test
    void shouldDetectMultipleInfrastructureTools() {

        assertEquals(
                Set.of(
                        Infrastructure.DOCKER,
                        Infrastructure.DOCKER_COMPOSE,
                        Infrastructure.GITHUB_ACTIONS
                ),
                detector.detect(repository(
                        "Dockerfile",
                        "docker-compose.yml",
                        ".github/workflows/build.yml"
                ))
        );
    }

    @Test
    void shouldReturnEmptySet() {

        assertEquals(
                Set.of(),
                detector.detect(repository(
                        "README.md",
                        "pom.xml"
                ))
        );
    }

    private ScannedRepository repository(String... relativePaths) {

        List<ScannedFile> files = Arrays.stream(relativePaths)
                .map(relPathStr -> {
                    Path relPath = Path.of(relPathStr);
                    Path absPath = tempDir.resolve(relPath);
                    return new ScannedFile(absPath, relPath);
                })
                .toList();

        return new ScannedRepository(tempDir, files);
    }
}