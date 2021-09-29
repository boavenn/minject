package com.github.boavenn.minject.utils;

import com.github.boavenn.minject.exceptions.TypeException;
import com.github.boavenn.minject.TypeLiteral;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public abstract class Types {
    public static Class<?> getRawTypeOf(Type type) {
        if (type instanceof Class<?> classType) {
            return classType;
        } else if (type instanceof ParameterizedType parameterizedType) {
            return (Class<?>) parameterizedType.getRawType();
        }
        throw TypeException.cannotGetRawTypeOf(type);
    }

    public static List<Type> getSuperclassTypesOf(Class<?> subclass) {
        Type superclassType = subclass.getGenericSuperclass();
        if (superclassType instanceof ParameterizedType parameterizedSuperclassType) {
            var typeArgs = parameterizedSuperclassType.getActualTypeArguments();
            return Arrays.stream(typeArgs).toList();
        }
        return Collections.emptyList();
    }

    public static List<? extends TypeLiteral<?>> getTypeLiteralsOfNestedTypesIn(TypeLiteral<?> typeLiteral) {
        if (typeLiteral.getType() instanceof ParameterizedType parameterizedType) {
            var typeArgs = parameterizedType.getActualTypeArguments();
            return Arrays.stream(typeArgs)
                         .map(TypeLiteral::of)
                         .toList();
        }
        throw TypeException.nestedTypeOfNonparameterizedType();
    }
}
