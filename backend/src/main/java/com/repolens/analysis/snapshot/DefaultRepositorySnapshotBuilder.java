package com.repolens.analysis.snapshot;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class DefaultRepositorySnapshotBuilder
        implements RepositorySnapshotBuilder {

    @Override
    public RepositorySnapshot build(Path repositoryRoot) {

        if (repositoryRoot == null) {
            throw new IllegalArgumentException(
                    "Repository root must not be null"
            );
        }

        if (!Files.exists(repositoryRoot)) {
            throw new IllegalArgumentException(
                    "Repository root does not exist: " + repositoryRoot
            );
        }

        if (!Files.isDirectory(repositoryRoot)) {
            throw new IllegalArgumentException(
                    "Repository root is not a directory: " + repositoryRoot
            );
        }

        List<RepositoryFile> files = new ArrayList<>();
        List<RepositoryDirectory> directories = new ArrayList<>();

        try (var paths = Files.walk(repositoryRoot)) {

            paths.forEach(path -> {

                if (path.equals(repositoryRoot)) {
                    return;
                }

                String relativePath =
                        repositoryRoot
                                .relativize(path)
                                .toString()
                                .replace('\\', '/');

                if (Files.isDirectory(path)) {

                    directories.add(
                            new RepositoryDirectory(
                                    path.getFileName().toString(),
                                    relativePath
                            )
                    );

                } else if (Files.isRegularFile(path)) {

                    files.add(
                            new RepositoryFile(
                                    path.getFileName().toString(),
                                    relativePath,
                                    getFileSize(path),
                                    getExtension(path)
                            )
                    );
                }
            });

        } catch (IOException exception) {

            throw new IllegalStateException(
                    "Failed to build repository snapshot: "
                            + repositoryRoot,
                    exception
            );
        }

        return new RepositorySnapshot(
                repositoryRoot,
                directories,
                files
        );
    }

    private long getFileSize(Path path) {

        try {
            return Files.size(path);
        } catch (IOException exception) {
            return 0L;
        }
    }

    private String getExtension(Path path) {

        String fileName = path.getFileName().toString();

        int lastDot = fileName.lastIndexOf('.');

        if (lastDot <= 0 || lastDot == fileName.length() - 1) {
            return "";
        }

        return fileName.substring(lastDot + 1).toLowerCase();
    }
}