package com.github.boavenn.minject.instantiaton.generic;

import com.github.boavenn.minject.instantiaton.InjectableFieldsResolver;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenericInjectableFieldsResolver implements InjectableFieldsResolver {
    public static GenericInjectableFieldsResolver create() {
        return new GenericInjectableFieldsResolver();
    }

    @Override
    public <T> List<Field> findInjectableFieldsIn(Class<T> cls) {
        return Arrays.stream(cls.getDeclaredFields())
                .filter(field -> !isFinal(field))
                .filter(field -> field.isAnnotationPresent(Inject.class))
                .toList();
    }

    private boolean isFinal(Field field) {
        return Modifier.isFinal(field.getModifiers());
    }
}
