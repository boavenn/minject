package com.github.boavenn.minject;

import lombok.NoArgsConstructor;

import javax.inject.Provider;

@NoArgsConstructor(staticName = "empty")
public class UnscopedScopeHandler implements ScopeHandler {
    @Override
    public <T> Provider<T> scopeProvider(ClassKey<T> classKey, Provider<T> unscopedProvider) {
        return () -> unscopedProvider.get();
    }
}
