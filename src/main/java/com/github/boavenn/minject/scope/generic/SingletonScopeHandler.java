package com.github.boavenn.minject.scope.generic;

import com.github.boavenn.minject.injector.ClassKey;
import com.github.boavenn.minject.scope.ScopeHandler;
import lombok.NoArgsConstructor;

import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(staticName = "empty")
public class SingletonScopeHandler implements ScopeHandler {
    private final Map<ClassKey<?>, Object> singletons = new HashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> Provider<T> scopeProvider(ClassKey<T> classKey, Provider<T> unscopedProvider) {
        if (!singletons.containsKey(classKey)) {
            T instance = unscopedProvider.get();
            singletons.put(classKey, instance);
        }

        return () -> (T) singletons.get(classKey);
    }
}
