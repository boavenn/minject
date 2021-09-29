package com.github.boavenn.minject;

import com.github.boavenn.minject.exceptions.TypeException;
import com.github.boavenn.minject.utils.Types;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.inject.Provider;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
@EqualsAndHashCode
public class TypeLiteral<T> {
    private final Class<T> rawType;
    private final Type type;

    @SuppressWarnings("unchecked")
    protected TypeLiteral() {
        // Returns provided type literal (i.e. type <T> provided in between angle brackets
        // when creating anonymous inner class)
        this.type = extractTypeInfoFrom(getClass());
        this.rawType = (Class<T>) Types.getRawTypeOf(type);
    }

    private static Type extractTypeInfoFrom(Class<?> subclass) {
        List<Type> types = Types.getSuperclassTypesOf(subclass);
        if (types.isEmpty()) {
            throw TypeException.missingTypeParameter();
        }
        return types.get(0);
    }

    @SuppressWarnings("unchecked")
    private TypeLiteral(Type type) {
        this.type = type;
        this.rawType = (Class<T>) Types.getRawTypeOf(type);
    }

    public static <T> TypeLiteral<T> of(Class<T> cls) {
        return new TypeLiteral<>(cls);
    }

    public static TypeLiteral<?> of(Type type) {
        return new TypeLiteral<>(type);
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
