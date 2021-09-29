package com.github.boavenn.minject.configuration.generic;

import com.github.boavenn.minject.ClassKey;
import com.github.boavenn.minject.binding.BindingProviderBuilder;
import com.github.boavenn.minject.binding.BindingRegistry;
import com.github.boavenn.minject.configuration.Binder;
import com.github.boavenn.minject.scope.ScopeHandler;
import com.github.boavenn.minject.scope.ScopeRegistry;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;

@RequiredArgsConstructor(staticName = "using")
public class GenericBinder implements Binder {
    private final BindingRegistry bindingRegistry;
    private final ScopeRegistry scopeRegistry;

    @Override
    public <T> BindingProviderBuilder<T> bind(ClassKey<T> classKey) {
        return bindingRegistry.bind(classKey);
    }

    @Override
    public <T> BindingProviderBuilder<T> bind(Class<T> cls) {
        return bindingRegistry.bind(cls);
    }

    @Override
    public void bindScope(Class<? extends Annotation> scope, ScopeHandler scopeHandler) {
        scopeRegistry.registerScope(scope, scopeHandler);
    }
}