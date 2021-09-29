package com.github.boavenn.minject.scope.generic;

import com.github.boavenn.minject.ClassKey;
import com.github.boavenn.minject.scope.ScopeHandler;
import lombok.NoArgsConstructor;

import javax.inject.Provider;

@NoArgsConstructor(staticName = "empty")
public class UnscopedScopeHandler implements ScopeHandler {
    @Override
    public <T> Provider<? extends T> scopeProvider(ClassKey<T> classKey, Provider<? extends T> unscopedProvider) {
        return unscopedProvider;
    }
}
