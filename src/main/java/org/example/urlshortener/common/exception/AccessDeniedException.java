package org.example.urlshortener.common.exception;

public class AccessDeniedException
        extends RuntimeException {

    public AccessDeniedException(
            String message
    ) {
        super(message);
    }
}