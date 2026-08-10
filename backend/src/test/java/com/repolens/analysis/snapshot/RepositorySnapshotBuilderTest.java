package com.repolens.analysis.snapshot;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class RepositorySnapshotBuilderTest {

    @TempDir
    Path repositoryRoot;

    @Test
    void shouldBuildRepositorySnapshot() throws IOException {

        Files.createDirectories(
                repositoryRoot.resolve("src/main/java")
        );

        Files.createDirectories(
                repositoryRoot.resolve(".github/workflows")
        );

        Files.writeString(
                repositoryRoot.resolve("README.md"),
                "# RepoLens"
        );

        Files.writeString(
                repositoryRoot.resolve("pom.xml"),
                "<project></project>"
        );

        Files.writeString(
                repositoryRoot.resolve("src/main/java/App.java"),
                "public class App {}"
        );

        Files.writeString(
                repositoryRoot.resolve(".github/workflows/build.yml"),
                "name: build"
        );

        RepositorySnapshotBuilder builder =
                new DefaultRepositorySnapshotBuilder();

        RepositorySnapshot snapshot =
                builder.build(repositoryRoot);

        assertNotNull(snapshot);

        assertEquals(repositoryRoot, snapshot.root());

        assertEquals(4, snapshot.files().size());

        assertTrue(
                snapshot.files().stream()
                        .anyMatch(file ->
                                file.name().equals("README.md")
                        )
        );

        assertTrue(
                snapshot.files().stream()
                        .anyMatch(file ->
                                file.name().equals("pom.xml")
                        )
        );

        assertTrue(
                snapshot.files().stream()
                        .anyMatch(file ->
                                file.relativePath()
                                        .equals("src/main/java/App.java")
                        )
        );

        assertTrue(
                snapshot.directories().stream()
                        .anyMatch(directory ->
                                directory.relativePath()
                                        .equals("src/main/java")
                        )
        );
    }
}