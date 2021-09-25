package com.github.boavenn.minject;

public class InjectionException extends RuntimeException {
    public static final String INJECTABLE_METHOD_INVOCATION_MSG = "Couldn't reflectively invoke an injectable method";
    public static final String INJECTABLE_FIELD_SET_MSG = "Coulnd't reflectively set an injectable field";
    public static final String INJECTABLE_CONSTRUCTOR_INVOCATION_MSG = "Couldn't reflectively invoke an injectable constructor";

    public InjectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public static InjectionException injectableMethodInvocation(Throwable cause) {
        return new InjectionException(INJECTABLE_METHOD_INVOCATION_MSG, cause);
    }

    public static InjectionException injectableFieldSet(Throwable cause) {
        return new InjectionException(INJECTABLE_FIELD_SET_MSG, cause);
    }

    public static InjectionException injectableConstructorInvocation(Throwable cause) {
        return new InjectionException(INJECTABLE_CONSTRUCTOR_INVOCATION_MSG, cause);
    }
}
