package com.github.boavenn.minject.scope.generic;

import com.github.boavenn.minject.ClassKey;
import com.github.boavenn.minject.scope.ScopeHandler;
import lombok.NoArgsConstructor;

import javax.inject.Provider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(staticName = "empty")
public class SingletonScopeHandler implements ScopeHandler {
    private final Map<ClassKey<?>, Object> singletons = new ConcurrentHashMap<>();

    @Override
    @SuppressWarnings("unchecked")
    public <T> Provider<? extends T> scopeProvider(ClassKey<T> classKey, Provider<? extends T> unscopedProvider) {
        return () -> (T) singletons.computeIfAbsent(classKey, key -> unscopedProvider.get());
    }
}
