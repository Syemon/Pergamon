package com.pergamon.Pergamon.v1.domain;

public class ResourceAlreadyCreatedException extends ResourceCreationException {
    public ResourceAlreadyCreatedException(String message) {
        super(message);
    }

    public ResourceAlreadyCreatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceAlreadyCreatedException(Throwable cause) {
        super(cause);
    }
}
