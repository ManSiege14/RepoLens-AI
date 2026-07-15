package com.repolens.github.validation;

import com.repolens.github.model.GitHubRepositoryCoordinates;

import java.net.URI;

public class GitHubRepositoryUrlParser {

    private static final String GITHUB_HOST = "github.com";

    public GitHubRepositoryCoordinates parse(String repositoryUrl) {

        if (repositoryUrl == null || repositoryUrl.isBlank()) {
            throw new InvalidGitHubRepositoryUrlException(
                    "Repository URL must not be blank"
            );
        }

        final URI uri;

        try {
            uri = URI.create(repositoryUrl);
        } catch (IllegalArgumentException exception) {
            throw new InvalidGitHubRepositoryUrlException(
                    "Repository URL is malformed"
            );
        }

        validateScheme(uri);
        validateHost(uri);

        String path = uri.getPath();

        if (path == null) {
            throw invalidUrl();
        }

        String normalizedPath = removeTrailingSlash(path);

        String[] segments = normalizedPath.split("/");

        if (segments.length != 3) {
            throw invalidUrl();
        }

        String owner = segments[1];
        String repository = removeGitSuffix(segments[2]);

        if (owner.isBlank() || repository.isBlank()) {
            throw invalidUrl();
        }

        return new GitHubRepositoryCoordinates(owner, repository);
    }

    private void validateScheme(URI uri) {
        if (!"https".equalsIgnoreCase(uri.getScheme())) {
            throw new InvalidGitHubRepositoryUrlException(
                    "Repository URL must use HTTPS"
            );
        }
    }

    private void validateHost(URI uri) {
        if (!GITHUB_HOST.equalsIgnoreCase(uri.getHost())) {
            throw new InvalidGitHubRepositoryUrlException(
                    "Repository URL must point to github.com"
            );
        }
    }

    private String removeTrailingSlash(String path) {
        if (path.endsWith("/")) {
            return path.substring(0, path.length() - 1);
        }

        return path;
    }

    private String removeGitSuffix(String repository) {
        if (repository.endsWith(".git")) {
            return repository.substring(0, repository.length() - 4);
        }

        return repository;
    }

    private InvalidGitHubRepositoryUrlException invalidUrl() {
        return new InvalidGitHubRepositoryUrlException(
                "Repository URL must have the format https://github.com/{owner}/{repository}"
        );
    }
}