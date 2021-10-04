package com.github.boavenn.minject.exceptions;

public class ClassKeyException extends RuntimeException {
    public ClassKeyException(String message) {
        super(message);
    }

    public static ClassKeyException methodOfVoidReturnType() {
        return new ClassKeyException("Couldn't create class key from method with void return type");
    }

    public static ClassKeyException nonProviderKey() {
        return new ClassKeyException("Class key is not of provider type");
    }
}
