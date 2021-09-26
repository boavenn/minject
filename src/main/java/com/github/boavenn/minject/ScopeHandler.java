package com.github.boavenn.minject;

import javax.inject.Provider;

public interface ScopeHandler {
    <T> Provider<T> scopeProvider(ClassKey<T> classKey, Provider<T> unscopedProvider);
}
