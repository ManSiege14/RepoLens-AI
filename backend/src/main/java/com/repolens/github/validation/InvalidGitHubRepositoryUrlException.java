package com.repolens.github.validation;

public class InvalidGitHubRepositoryUrlException extends RuntimeException {

    public InvalidGitHubRepositoryUrlException(String message) {
        super(message);
    }
}