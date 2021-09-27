package com.github.boavenn.minject.scope.generic;

import com.github.boavenn.minject.injector.ClassKey;
import com.github.boavenn.minject.scope.ScopeHandler;
import lombok.NoArgsConstructor;

import javax.inject.Provider;

@NoArgsConstructor(staticName = "empty")
public class UnscopedScopeHandler implements ScopeHandler {
    @Override
    public <T> Provider<T> scopeProvider(ClassKey<T> classKey, Provider<T> unscopedProvider) {
        return unscopedProvider;
    }
}
