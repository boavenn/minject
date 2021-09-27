package com.github.boavenn.minject.exceptions;

public class ClassKeyCreationException extends RuntimeException {
    public ClassKeyCreationException(String message) {
        super(message);
    }

    public static ClassKeyCreationException methodOfVoidReturnType() {
        return new ClassKeyCreationException("Given method is of void return type");
    }
}
