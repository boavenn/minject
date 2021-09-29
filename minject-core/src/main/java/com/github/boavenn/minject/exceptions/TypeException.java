package com.github.boavenn.minject.exceptions;

import java.lang.reflect.Type;

public class TypeException extends RuntimeException {
    public TypeException(String message) {
        super(message);
    }

    public static TypeException missingTypeParameter() {
        return new TypeException("Given class is missing type parameter");
    }

    public static TypeException cannotGetRawTypeOf(Type type) {
        return new TypeException("Couldn't get raw type of type <%s>".formatted(type));
    }

    public static TypeException nestedTypeOfNonparameterizedType() {
        return new TypeException("Cannot get nested type from nonparameterized type");
    }
}
