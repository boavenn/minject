package com.github.boavenn.minject.instantiation.generic;

import com.github.boavenn.minject.exceptions.InjectionPointException;
import com.github.boavenn.minject.instantiation.InjectableConstructorResolver;
import lombok.NoArgsConstructor;

import javax.inject.Inject;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(staticName = "create")
public class GenericInjectableConstructorResolver implements InjectableConstructorResolver {
    @Override
    @SuppressWarnings("unchecked")
    public <T> Constructor<T> findInjectableConstructorIn(Class<T> cls) {
        var declaredConstructors = cls.getDeclaredConstructors();

        if (declaredConstructors.length == 1) {
            return (Constructor<T>) declaredConstructors[0];
        }

        var annotatedConstructors = getAnnotatedConstructorsFrom(declaredConstructors);
        var numberOfAnnotatedConstructors = annotatedConstructors.size();

        if (numberOfAnnotatedConstructors == 1) {
            return (Constructor<T>) annotatedConstructors.get(0);
        }

        if (numberOfAnnotatedConstructors > 1) {
            throw InjectionPointException.multipleAnnotatedConstructorsFound();
        }

        throw InjectionPointException.noInjectableConstructorFound();
    }

    private List<Constructor<?>> getAnnotatedConstructorsFrom(Constructor<?>[] constructors) {
        return Arrays.stream(constructors)
                .filter(constructor -> constructor.isAnnotationPresent(Inject.class))
                .toList();
    }
}
