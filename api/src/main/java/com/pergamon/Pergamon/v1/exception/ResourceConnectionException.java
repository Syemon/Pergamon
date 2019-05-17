package com.pergamon.Pergamon.v1.exception;

public class ResourceConnectionException extends RuntimeException {
    public ResourceConnectionException(String message) {
        super(message);
    }

    public ResourceConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceConnectionException(Throwable cause) {
        super(cause);
    }
}
