package com.repolens.analysis.clone;

import com.repolens.shared.exception.RepositoryCloneException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class JGitRepositoryCloner implements RepositoryCloner {

    @Override
    public Path cloneRepository(String repositoryUrl) {


        try {
            Path tempDirectory = Files.createTempDirectory("repolens-");

            try (Git git = Git.cloneRepository()
                    .setURI(repositoryUrl)
                    .setDirectory(tempDirectory.toFile())
                    .call()) {

                return tempDirectory;
            }

        } catch (IOException | GitAPIException e) {

            throw new RepositoryCloneException(
                    "Failed to clone repository: " + repositoryUrl,
                    e
            );
        }
    }
}