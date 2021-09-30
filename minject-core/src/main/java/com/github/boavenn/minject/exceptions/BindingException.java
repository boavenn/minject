package com.github.boavenn.minject.exceptions;

public class BindingException extends RuntimeException {
    public BindingException(String message) {
        super(message);
    }

    public static BindingException bindingAlreadyExists(String objRepresentation) {
        return new BindingException("Binding for %s already exists".formatted(objRepresentation));
    }
}
