package com.github.boavenn.minject.injector.generic;

import com.github.boavenn.minject.binding.BindingRegistry;
import com.github.boavenn.minject.binding.generic.GenericBindingRegistry;
import com.github.boavenn.minject.injector.Injector;
import com.github.boavenn.minject.instantiation.generic.GenericClassInstantiator;
import com.github.boavenn.minject.scope.ScopeRegistry;
import com.github.boavenn.minject.scope.generic.GenericScopeRegistry;

import java.util.function.Function;

public class GenericInjectorBuilder {
    private Function<Injector, BindingRegistry> bindingRegistryFactoryMethod;
    private Function<Injector, ScopeRegistry> scopeRegistryFactoryMethod;

    public GenericInjectorBuilder() {
        bindingRegistryFactoryMethod = defaultBindingRegistryFactoryMethod();
        scopeRegistryFactoryMethod = defaultScopeRegistryFactoryMethod();
    }

    public GenericInjectorBuilder usingBindingRegistry(Function<Injector, BindingRegistry> bindingRegistryFactoryMethod) {
        this.bindingRegistryFactoryMethod = bindingRegistryFactoryMethod;
        return this;
    }

    public GenericInjectorBuilder usingScopeRegistry(Function<Injector, ScopeRegistry> scopeRegistryFactoryMethod) {
        this.scopeRegistryFactoryMethod = scopeRegistryFactoryMethod;
        return this;
    }

    public GenericInjector build() {
        return new GenericInjector(bindingRegistryFactoryMethod, scopeRegistryFactoryMethod);
    }

    private Function<Injector, BindingRegistry> defaultBindingRegistryFactoryMethod() {
        return injector -> GenericBindingRegistry.using(GenericClassInstantiator.using(injector).build());
    }

    private Function<Injector, ScopeRegistry> defaultScopeRegistryFactoryMethod() {
        return injector -> GenericScopeRegistry.empty();
    }
}
