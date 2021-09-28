package com.github.boavenn.minject.injector.generic;

import com.github.boavenn.minject.binding.BindingRegistry;
import com.github.boavenn.minject.binding.generic.GenericBindingRegistry;
import com.github.boavenn.minject.injector.Injector;
import com.github.boavenn.minject.instantiation.generic.GenericClassInstantiator;
import com.github.boavenn.minject.scope.ScopeRegistry;
import com.github.boavenn.minject.scope.generic.GenericScopeRegistry;

import java.util.function.Function;

public class GenericInjectorBuilder {
    private Function<Injector, BindingRegistry> bindingRegistryConfiguration;
    private ScopeRegistry scopeRegistry;

    public GenericInjectorBuilder() {
        bindingRegistryConfiguration = injector -> {
            var instantiator = GenericClassInstantiator.using(injector).build();
            return GenericBindingRegistry.using(instantiator);
        };
        scopeRegistry = GenericScopeRegistry.empty();
    }

    public GenericInjectorBuilder using(Function<Injector, BindingRegistry> bindingRegistryConfiguration) {
        this.bindingRegistryConfiguration = bindingRegistryConfiguration;
        return this;
    }

    public GenericInjectorBuilder using(ScopeRegistry scopeRegistry) {
        this.scopeRegistry = scopeRegistry;
        return this;
    }

    public GenericInjector build() {
        return new GenericInjector(bindingRegistryConfiguration, scopeRegistry);
    }
}
