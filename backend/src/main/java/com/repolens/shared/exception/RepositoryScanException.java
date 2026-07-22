package com.repolens.shared.exception;

public class RepositoryScanException extends RuntimeException {

    public RepositoryScanException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepositoryScanException(String message) {
        super(message);
    }
}