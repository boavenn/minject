package com.github.boavenn.minject.scope;

import com.github.boavenn.minject.injector.ClassKey;

import javax.inject.Provider;

public interface ScopeHandler {
    <T> Provider<T> scopeProvider(ClassKey<T> classKey, Provider<T> unscopedProvider);
}
