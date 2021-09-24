package com.github.boavenn.minject;

public class ClassKeyCreationException extends RuntimeException {
    public static final String VOID_METHOD_MSG = "Given method is of void return type";

    public ClassKeyCreationException(String message) {
        super(message);
    }

    public static ClassKeyCreationException voidMethod() {
        return new ClassKeyCreationException(VOID_METHOD_MSG);
    }
}
