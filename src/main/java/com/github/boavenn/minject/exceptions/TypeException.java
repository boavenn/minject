package com.github.boavenn.minject.exceptions;

import java.lang.reflect.Type;

public class TypeException extends RuntimeException {
    public static final String MISSING_TYPE_PARAM_MSG = "Given class is missing type parameter";
    public static final String CANNOT_GET_RAW_TYPE_MSG = "Couldn't get raw type of type <%s>";
    public static final String NESTED_TYPE_OF_NONPARAMETERIZED_MSG = "Cannot get nested type from nonparameteried type";

    public TypeException(String message) {
        super(message);
    }

    public static TypeException missingTypeParameter() {
        return new TypeException(MISSING_TYPE_PARAM_MSG);
    }

    public static TypeException cannotGetRawTypeOf(Type type) {
        return new TypeException(CANNOT_GET_RAW_TYPE_MSG.formatted(type));
    }

    public static TypeException nestedTypeOfNonparameterizedType() {
        return new TypeException(NESTED_TYPE_OF_NONPARAMETERIZED_MSG);
    }
}
