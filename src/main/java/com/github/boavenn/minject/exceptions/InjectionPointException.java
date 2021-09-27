package com.github.boavenn.minject.exceptions;

public class InjectionPointException extends RuntimeException {
    public InjectionPointException(String message) {
        super(message);
    }

    public static InjectionPointException noInjectableConstructorFound() {
        return new InjectionPointException("No injectable constructor has been found");
    }

    public static InjectionPointException multipleAnnotatedConstructorsFound() {
        return new InjectionPointException("Multiple annotated constructors have been found");
    }
}
