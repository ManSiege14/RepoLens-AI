package com.repolens.shared.exception;

public class InvalidGitHubUrlException extends RuntimeException {

    public InvalidGitHubUrlException(String message) {
        super(message);
    }

}