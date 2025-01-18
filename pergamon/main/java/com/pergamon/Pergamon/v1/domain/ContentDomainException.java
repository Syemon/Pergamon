package com.pergamon.Pergamon.v1.domain;

public class ContentDomainException extends RuntimeException {
    public ContentDomainException(String message) {
        super(message);
    }

    public ContentDomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public ContentDomainException(Throwable cause) {
        super(cause);
    }
}
