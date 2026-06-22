package org.example.urlshortener.common.exception;

public class NotFoundException
        extends RuntimeException {

    public NotFoundException(
            String message
    ) {
        super(message);
    }
}