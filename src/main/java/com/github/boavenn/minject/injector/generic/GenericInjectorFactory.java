package com.github.boavenn.minject.injector.generic;

import com.github.boavenn.minject.binding.BindingRegistry;
import com.github.boavenn.minject.configuration.ConfigurationModule;
import com.github.boavenn.minject.configuration.generic.GenericBinder;
import com.github.boavenn.minject.injector.Injector;
import com.github.boavenn.minject.scope.ScopeRegistry;
import com.github.boavenn.minject.scope.Unscoped;
import com.github.boavenn.minject.scope.generic.SingletonScopeHandler;
import com.github.boavenn.minject.scope.generic.UnscopedScopeHandler;

import javax.inject.Singleton;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class GenericInjectorFactory {
    private final List<ConfigurationModule> modules = new LinkedList<>();

    public void addModules(Collection<ConfigurationModule> modules) {
        this.modules.addAll(modules);
    }

    public GenericInjector create() {
        var injector = GenericInjector.usingDefaults();

        var bindingRegistry = injector.getBindingRegistry();
        var scopeRegistry = injector.getScopeRegistry();

        registerInjector(bindingRegistry, injector);
        registerDefaultScopes(scopeRegistry);

        var injectorBinder = GenericBinder.using(bindingRegistry, scopeRegistry);
        modules.forEach(module -> module.configure(injectorBinder));

        return injector;
    }

    private void registerInjector(BindingRegistry bindingRegistry, Injector injector) {
        bindingRegistry.bind(Injector.class)
                       .toInstance(injector);
    }

    private void registerDefaultScopes(ScopeRegistry scopeRegistry) {
        scopeRegistry.registerScope(Unscoped.class, UnscopedScopeHandler.empty());
        scopeRegistry.registerScope(Singleton.class, SingletonScopeHandler.empty());
    }
}
