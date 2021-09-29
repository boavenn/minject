package com.github.boavenn.minject.scope.generic;

import com.github.boavenn.minject.ClassKey;
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
    public <T> Provider<? extends T> scopeProvider(ClassKey<T> classKey, Provider<? extends T> unscopedProvider) {
        if (!singletons.containsKey(classKey)) {
            T instance = unscopedProvider.get();
            singletons.put(classKey, instance);
        }

        return () -> (T) singletons.get(classKey);
    }
}
