package com.github.boavenn.minject;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Getter
@EqualsAndHashCode
class TypeLiteral<T> {
    private final Class<T> rawType;
    private final Type type;

    @SuppressWarnings("unchecked")
    public TypeLiteral() {
        this.type = getSuperclassTypeOf(getClass());
        this.rawType = (Class<T>) getRawTypeOf(type);
    }

    @SuppressWarnings("unchecked")
    private TypeLiteral(Type type) {
        this.type = type;
        this.rawType = (Class<T>) getRawTypeOf(type);
    }

    public static <T> TypeLiteral<T> of(Class<T> cls) {
        return new TypeLiteral<>(cls);
    }

    private static Class<?> getRawTypeOf(Type type) {
        if (type instanceof Class<?> classType) {
            return classType;
        } else if (type instanceof ParameterizedType parameterizedType) {
            return (Class<?>) parameterizedType.getRawType();
        }
        throw new RuntimeException();
    }

    private static Type getSuperclassTypeOf(Class<?> subclass) {
        Type superclassType = subclass.getGenericSuperclass();
        if (superclassType instanceof ParameterizedType parameterizedSuperclassType) {
            // Returns provided type literal (i.e. type <T> provided in between angle brackets)
            return parameterizedSuperclassType.getActualTypeArguments()[0];
        }
        throw new RuntimeException();
    }
}
