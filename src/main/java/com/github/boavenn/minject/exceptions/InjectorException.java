package com.github.boavenn.minject.exceptions;

import com.github.boavenn.minject.injector.ClassKey;

import java.lang.annotation.Annotation;

public class InjectorException extends RuntimeException {
    private static final String BINDING_NOT_REGISTERED_MSG = "Binding for key <%s> is not registered";
    private static final String SCOPE_NOT_REGISTERED_MSG = "Scope handler for scope <%s> is not registered";

    public InjectorException(String message) {
        super(message);
    }

    public static InjectorException bindingNotRegistered(ClassKey<?> classKey) {
        return new InjectorException(BINDING_NOT_REGISTERED_MSG.formatted(classKey.toString()));
    }

    public static InjectorException scopeNotRegistered(Class<? extends Annotation> scope) {
        return new InjectorException(SCOPE_NOT_REGISTERED_MSG.formatted(scope.toString()));
    }
}
