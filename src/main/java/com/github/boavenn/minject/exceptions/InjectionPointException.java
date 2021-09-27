package com.github.boavenn.minject.exceptions;

public class InjectionPointException extends RuntimeException {
    public static final String CONSTR_NOT_FOUND_MSG = "No injectable constructor has been found";
    public static final String MULTIPLE_CONSTR_FOUND_MSG = "Multiple annotated constructors have been found";

    public InjectionPointException(String message) {
        super(message);
    }

    public static InjectionPointException noInjectableConstructorFound() {
        return new InjectionPointException(CONSTR_NOT_FOUND_MSG);
    }

    public static InjectionPointException multipleAnnotatedConstructorsFound() {
        return new InjectionPointException(MULTIPLE_CONSTR_FOUND_MSG);
    }
}
