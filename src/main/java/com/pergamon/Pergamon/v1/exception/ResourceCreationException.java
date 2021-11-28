package com.pergamon.Pergamon.v1.exception;

public class ResourceCreationException extends RuntimeException {
    public ResourceCreationException(String message) {
        super(message);
    }

    public ResourceCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceCreationException(Throwable cause) {
        super(cause);
    }
}
