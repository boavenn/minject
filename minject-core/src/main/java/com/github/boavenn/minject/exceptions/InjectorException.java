package com.github.boavenn.minject.exceptions;

import com.github.boavenn.minject.ClassKey;

import java.lang.annotation.Annotation;

public class InjectorException extends RuntimeException {
    public InjectorException(String message) {
        super(message);
    }

    public static InjectorException bindingNotRegistered(ClassKey<?> classKey) {
        return new InjectorException("Binding for key <%s> is not registered".formatted(classKey.toString()));
    }

    public static InjectorException scopeNotRegistered(Class<? extends Annotation> scope) {
        return new InjectorException("Scope handler for scope <%s> is not registered".formatted(scope.toString()));
    }
}
