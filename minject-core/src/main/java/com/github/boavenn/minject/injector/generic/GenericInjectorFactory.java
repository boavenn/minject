package com.github.boavenn.minject.injector.generic;

import com.github.boavenn.minject.binding.BindingRegistry;
import com.github.boavenn.minject.configuration.Binder;
import com.github.boavenn.minject.configuration.Module;
import com.github.boavenn.minject.configuration.ModuleProcessor;
import com.github.boavenn.minject.configuration.RegistrationPolicy;
import com.github.boavenn.minject.configuration.generic.GenericBinder;
import com.github.boavenn.minject.configuration.generic.GenericModuleProcessor;
import com.github.boavenn.minject.injector.Injector;
import com.github.boavenn.minject.scope.ScopeRegistry;
import com.github.boavenn.minject.scope.Unscoped;
import com.github.boavenn.minject.scope.generic.SingletonScopeHandler;
import com.github.boavenn.minject.scope.generic.UnscopedScopeHandler;

import javax.inject.Singleton;
import java.util.*;

public class GenericInjectorFactory {
    private static final RegistrationPolicy DEFAULT_REGISTRATION_POLICY = RegistrationPolicy.THROW;
    private static final List<ModuleProcessor> DEFAULT_PROCESSORS = List.of(new GenericModuleProcessor());

    private final Set<Module> initialModules = new HashSet<>();
    private final List<ModuleProcessor> moduleProcessors = new LinkedList<>();

    public GenericInjectorFactory() {
        moduleProcessors.addAll(DEFAULT_PROCESSORS);
    }

    public GenericInjectorFactory addModules(Collection<Module> modules) {
        initialModules.addAll(modules);
        return this;
    }

    public GenericInjectorFactory addModuleProcessors(Collection<ModuleProcessor> moduleProcessors) {
        this.moduleProcessors.addAll(moduleProcessors);
        return this;
    }

    public GenericInjector create() {
        var injector = GenericInjector.usingDefaults();

        var bindingRegistry = injector.getBindingRegistry();
        var scopeRegistry = injector.getScopeRegistry();

        registerInjector(bindingRegistry, injector);
        registerDefaultScopes(scopeRegistry);

        var binder = GenericBinder.using(bindingRegistry, scopeRegistry, DEFAULT_REGISTRATION_POLICY.getStrategy());
        configureModules(binder);

        var installedModules = binder.getInstalledModules();
        processModules(installedModules, binder, injector);

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

    private void configureModules(GenericBinder binder) {
        initialModules.forEach(binder::install);
    }

    private void processModules(Iterable<Module> modules, Binder binder, Injector injector) {
        for (var moduleProcessor : moduleProcessors) {
            modules.forEach(module -> moduleProcessor.process(module, binder, injector));
        }
    }
}
