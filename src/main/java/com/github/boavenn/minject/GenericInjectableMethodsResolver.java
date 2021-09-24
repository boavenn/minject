package com.github.boavenn.minject;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenericInjectableMethodsResolver implements InjectableMethodsResolver {
    public static GenericInjectableMethodsResolver create() {
        return new GenericInjectableMethodsResolver();
    }

    @Override
    public <T> List<Method> findInjectableMethodsIn(Class<T> cls) {
        return Arrays.stream(cls.getDeclaredMethods())
                .filter(method -> !isAbstract(method))
                .filter(method -> method.isAnnotationPresent(Inject.class))
                .toList();
    }

    private boolean isAbstract(Method method) {
        return Modifier.isAbstract(method.getModifiers());
    }
}
