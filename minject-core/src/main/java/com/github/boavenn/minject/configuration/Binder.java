package com.github.boavenn.minject.configuration;

import com.github.boavenn.minject.ClassKey;
import com.github.boavenn.minject.binding.BindingProviderBuilder;
import com.github.boavenn.minject.scope.ScopeHandler;

import java.lang.annotation.Annotation;

public interface Binder {
    <T> BindingProviderBuilder<T> bind(ClassKey<T> classKey);
    <T> BindingProviderBuilder<T> bind(Class<T> cls);
    void bindScope(Class<? extends Annotation> scope, ScopeHandler scopeHandler);
    void install(Module module);
}
