package com.repolens.shared.exception;

public class RepositoryCloneException extends RuntimeException {

    public RepositoryCloneException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryCloneException(String message) {
        super(message);
    }
}