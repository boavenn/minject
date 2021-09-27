package com.github.boavenn.minject.injector.generic;

import com.github.boavenn.minject.binding.BindingRegistry;
import com.github.boavenn.minject.exceptions.InjectorException;
import com.github.boavenn.minject.injector.ClassKey;
import com.github.boavenn.minject.injector.Injector;
import com.github.boavenn.minject.scope.ScopeRegistry;

import javax.inject.Provider;
import java.util.function.Function;

public class GenericInjector implements Injector {
    private final BindingRegistry bindingRegistry;
    private final ScopeRegistry scopeRegistry;

    GenericInjector(Function<Injector, BindingRegistry> bindingRegistryConfiguration,
                    ScopeRegistry scopeRegistry) {
        this.bindingRegistry = bindingRegistryConfiguration.apply(this);
        this.scopeRegistry = scopeRegistry;
    }

    public static GenericInjectorBuilder builder() {
        return new GenericInjectorBuilder();
    }

    @Override
    public <T> T getInstanceOf(ClassKey<T> classKey) {
        return getProviderOf(classKey).get();
    }

    @Override
    public <T> T getInstanceOf(Class<T> cls) {
        return getInstanceOf(ClassKey.of(cls));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Provider<T> getProviderOf(ClassKey<T> classKey) {
        var binding = bindingRegistry.getBindingFor(classKey)
                                     .orElseThrow(() -> InjectorException.bindingNotRegistered(classKey));
        var scope = binding.getScope();
        var scopeHandler = scopeRegistry.getScopeHandlerFor(scope)
                                        .orElseThrow(() -> InjectorException.scopeNotRegistered(scope));
        return (Provider<T>) scopeHandler.scopeProvider(classKey, binding.getProvider());
    }

    @Override
    public <T> Provider<T> getProviderOf(Class<T> cls) {
        return getProviderOf(ClassKey.of(cls));
    }
}
