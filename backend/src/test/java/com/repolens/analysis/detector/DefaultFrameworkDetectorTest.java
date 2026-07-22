package com.repolens.analysis.detector;

import com.repolens.analysis.scanner.ScannedFile;
import com.repolens.analysis.scanner.ScannedRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultFrameworkDetectorTest {

    private final FrameworkDetector detector = new DefaultFrameworkDetector();

    @TempDir
    Path tempDir;

    @Test
    void shouldDetectSpringBoot() throws IOException {

        Path pom = createFile(
                "pom.xml",
                "<dependency>spring-boot-starter-web</dependency>"
        );

        assertEquals(
                Set.of(Framework.SPRING_BOOT),
                detector.detect(repository(pom))
        );
    }

    @Test
    void shouldDetectReact() throws IOException {

        Path pkg = createFile(
                "package.json",
                """
                {
                  "dependencies": {
                    "react": "^18.0.0"
                  }
                }
                """
        );

        assertEquals(
                Set.of(Framework.REACT),
                detector.detect(repository(pkg))
        );
    }

    @Test
    void shouldDetectNextJs() throws IOException {

        Path pkg = createFile(
                "package.json",
                """
                {
                  "dependencies": {
                    "next": "^14.0.0"
                  }
                }
                """
        );

        assertEquals(
                Set.of(Framework.NEXT_JS),
                detector.detect(repository(pkg))
        );
    }

    @Test
    void shouldDetectExpress() throws IOException {

        Path pkg = createFile(
                "package.json",
                """
                {
                  "dependencies": {
                    "express": "^4.0.0"
                  }
                }
                """
        );

        assertEquals(
                Set.of(Framework.EXPRESS),
                detector.detect(repository(pkg))
        );
    }

    @Test
    void shouldDetectMultipleFrameworks() throws IOException {

        Path pom = createFile(
                "backend/pom.xml",
                "<dependency>spring-boot-starter-web</dependency>"
        );

        Path pkg = createFile(
                "frontend/package.json",
                """
                {
                  "dependencies": {
                    "react": "^18.0.0"
                  }
                }
                """
        );

        assertEquals(
                Set.of(
                        Framework.SPRING_BOOT,
                        Framework.REACT
                ),
                detector.detect(repository(pom, pkg))
        );
    }

    @Test
    void shouldReturnEmptySet() throws IOException {

        Path readme = createFile(
                "README.md",
                "# RepoLens"
        );

        assertEquals(
                Set.of(),
                detector.detect(repository(readme))
        );
    }

    private Path createFile(String relativePath, String content) throws IOException {

        Path file = tempDir.resolve(relativePath);

        Files.createDirectories(file.getParent());

        Files.writeString(file, content);

        return file;
    }

    private ScannedRepository repository(Path... files) {

        List<ScannedFile> scannedFiles = Arrays.stream(files)
                .map(file -> new ScannedFile(
                        file,
                        tempDir.relativize(file)
                ))
                .toList();

        return new ScannedRepository(tempDir, scannedFiles);
    }
}