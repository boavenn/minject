package com.github.boavenn.minject.instantiation.generic;

import com.github.boavenn.minject.instantiation.InjectableMethodsResolver;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(staticName = "create")
public class GenericInjectableMethodsResolver implements InjectableMethodsResolver {
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
