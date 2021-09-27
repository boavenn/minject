package com.github.boavenn.minject;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.inject.Provider;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

@Getter
@EqualsAndHashCode
public class TypeLiteral<T> {
    private final Class<T> rawType;
    private final Type type;

    @SuppressWarnings("unchecked")
    protected TypeLiteral() {
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

    public static TypeLiteral<?> of(Type type) {
        return new TypeLiteral<>(type);
    }

    private static Class<?> getRawTypeOf(Type type) {
        if (type instanceof Class<?> classType) {
            return classType;
        } else if (type instanceof ParameterizedType parameterizedType) {
            return (Class<?>) parameterizedType.getRawType();
        }
        throw new IllegalArgumentException("Couldn't get raw type of type " + type.getTypeName());
    }

    private static Type getSuperclassTypeOf(Class<?> subclass) {
        Type superclassType = subclass.getGenericSuperclass();
        if (superclassType instanceof ParameterizedType parameterizedSuperclassType) {
            // Returns provided type literal (i.e. type <T> provided in between angle brackets)
            return parameterizedSuperclassType.getActualTypeArguments()[0];
        }
        throw new RuntimeException("Given class is missing type parameter");
    }

    public TypeLiteral<?> getInnerType() {
        if (type instanceof ParameterizedType parameterizedType) {
            var typeArgs = parameterizedType.getActualTypeArguments();
            if (typeArgs.length == 1) {
                var innerType = typeArgs[0];
                return TypeLiteral.of(innerType);
            }
            throw new RuntimeException("Couldn't get inner type from parameterized type with multiple type args");
        }
        throw new RuntimeException("Couldn't get inner type of nonparameterized type");
    }

    @SuppressWarnings("unchecked")
    public TypeLiteral<Provider<T>> asProviderType() {
        return (TypeLiteral<Provider<T>>) of(ProviderType.of(getType()));
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class ProviderType implements ParameterizedType {
        private final Type providedType;

        public static ProviderType of(Type type) {
            return new ProviderType(type);
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{providedType};
        }

        @Override
        public Type getRawType() {
            return Provider.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof ParameterizedType other)) return false;
            return Arrays.equals(getActualTypeArguments(), other.getActualTypeArguments())
                    && Objects.equals(getRawType(), other.getRawType())
                    && Objects.equals(getOwnerType(), other.getOwnerType());
        }

        @Override
        public int hashCode() {
            int typeArgsHash = Arrays.hashCode(getActualTypeArguments());
            int ownerTypeHash = Objects.hashCode(getOwnerType());
            int rawTypeHash = Objects.hashCode(getRawType());
            return typeArgsHash ^ ownerTypeHash ^ rawTypeHash;
        }
    }
}
