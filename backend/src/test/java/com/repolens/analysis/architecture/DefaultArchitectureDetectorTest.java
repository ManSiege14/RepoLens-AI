package com.repolens.analysis.architecture;

import com.repolens.analysis.snapshot.RepositoryDirectory;
import com.repolens.analysis.snapshot.RepositoryFile;
import com.repolens.analysis.snapshot.RepositorySnapshot;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefaultArchitectureDetectorTest {

    @Test
    void shouldDetectLayeredRestArchitecture() {

        RepositorySnapshot snapshot =
                new RepositorySnapshot(
                        Path.of("/repo"),

                        List.of(
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
                                        "UserController.java",
                                        "src/main/java/controller/UserController.java",
                                        100L,
                                        "java"
                                )
                        )
                );

        ArchitectureAnalysis result =
                new DefaultArchitectureDetector()
                        .detect(snapshot);

        assertEquals(
                ArchitectureType.LAYERED,
                result.primaryArchitecture()
        );

        assertTrue(
                result.detectedArchitectures()
                        .contains(ArchitectureType.LAYERED)
        );

        assertTrue(
                result.detectedArchitectures()
                        .contains(ArchitectureType.REST_API)
        );

        assertEquals(
                2,
                result.evidence().size()
        );
    }

    @Test
    void shouldReturnUnknownWhenNoArchitectureIsDetected() {

        RepositorySnapshot snapshot =
                new RepositorySnapshot(
                        Path.of("/repo"),
                        List.of(),
                        List.of(
                                new RepositoryFile(
                                        "Main.java",
                                        "Main.java",
                                        100L,
                                        "java"
                                )
                        )
                );

        ArchitectureAnalysis result =
                new DefaultArchitectureDetector()
                        .detect(snapshot);

        assertEquals(
                ArchitectureType.UNKNOWN,
                result.primaryArchitecture()
        );

        assertTrue(
                result.detectedArchitectures().isEmpty()
        );

        assertEquals(
                1,
                result.evidence().size()
        );
    }
    @Test
    void shouldDetectFrontendArchitecture() {

        RepositorySnapshot snapshot =
                new RepositorySnapshot(
                        Path.of("/repo"),

                        List.of(
                                new RepositoryDirectory(
                                        "components",
                                        "src/components"
                                )
                        ),

                        List.of(
                                new RepositoryFile(
                                        "package.json",
                                        "package.json",
                                        500L,
                                        "json"
                                )
                        )
                );

        ArchitectureAnalysis result =
                new DefaultArchitectureDetector()
                        .detect(snapshot);

        assertEquals(
                ArchitectureType.FRONTEND,
                result.primaryArchitecture()
        );

        assertTrue(
                result.detectedArchitectures()
                        .contains(ArchitectureType.FRONTEND)
        );
    }
}