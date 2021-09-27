package com.github.boavenn.minject.exceptions;

public class InjectionException extends RuntimeException {
    public InjectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public static InjectionException injectableMethodInvocation(Throwable cause) {
        return new InjectionException("Couldn't reflectively invoke an injectable method", cause);
    }

    public static InjectionException injectableFieldValueSet(Throwable cause) {
        return new InjectionException("Couldn't reflectively set an injectable field value", cause);
    }

    public static InjectionException injectableConstructorInvocation(Throwable cause) {
        return new InjectionException("Couldn't reflectively invoke an injectable constructor", cause);
    }
}
