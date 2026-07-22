package com.repolens.analysis.scanner;

import com.repolens.shared.exception.RepositoryScanException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class DefaultFileScanner implements FileScanner {

    private static final Set<String> IGNORED_DIRECTORIES = Set.of(
            ".git",
            "target",
            "build",
            "node_modules",
            ".idea",
            ".gradle"
    );

    @Override
    public ScannedRepository scan(Path repositoryRoot) {

        if (repositoryRoot == null) {
            throw new IllegalArgumentException("Repository root must not be null.");
        }

        if (!Files.exists(repositoryRoot)) {
            throw new IllegalArgumentException("Repository root does not exist.");
        }

        if (!Files.isDirectory(repositoryRoot)) {
            throw new IllegalArgumentException("Repository root is not a directory.");
        }

        List<ScannedFile> files = new ArrayList<>();

        try {

            Files.walkFileTree(repositoryRoot, new SimpleFileVisitor<>() {

                @Override
                public FileVisitResult preVisitDirectory(
                        Path directory,
                        BasicFileAttributes attributes
                ) {

                    Path name = directory.getFileName();

                    if (name != null &&
                            IGNORED_DIRECTORIES.contains(name.toString())) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(
                        Path file,
                        BasicFileAttributes attributes
                ) {

                    if (attributes.isRegularFile()) {

                        files.add(new ScannedFile(
                                file,
                                repositoryRoot.relativize(file)
                        ));
                    }

                    return FileVisitResult.CONTINUE;
                }

            });

            return new ScannedRepository(repositoryRoot, files);

        } catch (IOException exception) {

            throw new RepositoryScanException(
                    "Failed to scan repository.",
                    exception
            );
        }
    }
}