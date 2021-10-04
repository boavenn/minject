package com.github.boavenn.minject.injector.generic;

import com.github.boavenn.minject.binding.Binding;
import com.github.boavenn.minject.binding.BindingRegistry;
import com.github.boavenn.minject.configuration.Module;
import com.github.boavenn.minject.configuration.*;
import com.github.boavenn.minject.configuration.generic.GenericBinder;
import com.github.boavenn.minject.configuration.generic.GenericModuleProcessor;
import com.github.boavenn.minject.injector.Injector;
import com.github.boavenn.minject.scope.EagerSingleton;
import com.github.boavenn.minject.scope.ScopeRegistry;
import com.github.boavenn.minject.scope.Unscoped;
import com.github.boavenn.minject.scope.generic.SingletonScopeHandler;
import com.github.boavenn.minject.scope.generic.UnscopedScopeHandler;
import lombok.NoArgsConstructor;

import javax.inject.Singleton;
import java.util.*;

@NoArgsConstructor(staticName = "empty")
public class GenericInjectorFactory {
    private static final RegistrationStrategies DEFAULT_BINDING_REGISTRATION_STRATEGY = RegistrationStrategies.THROW;
    private static final RegistrationStrategies DEFAULT_SCOPE_REGISTRATION_STRATEGY = RegistrationStrategies.THROW;
    private static final List<ModuleProcessor> DEFAULT_MODULE_PROCESSORS = List.of(new GenericModuleProcessor());

    private final Set<Module> initialModules = new HashSet<>();
    private final List<ModuleProcessor> moduleProcessors = new LinkedList<>();

    private RegistrationStrategy bindingRegistrationStrategy = DEFAULT_BINDING_REGISTRATION_STRATEGY;
    private RegistrationStrategy scopeRegistrationStrategy = DEFAULT_SCOPE_REGISTRATION_STRATEGY;

    public static GenericInjectorFactory withDefaults() {
        return GenericInjectorFactory.empty()
                                     .addModuleProcessors(DEFAULT_MODULE_PROCESSORS);
    }

    public GenericInjectorFactory addModules(Collection<Module> modules) {
        initialModules.addAll(modules);
        return this;
    }

    public GenericInjectorFactory addModuleProcessors(Collection<ModuleProcessor> moduleProcessors) {
        this.moduleProcessors.addAll(moduleProcessors);
        return this;
    }

    public GenericInjectorFactory setBindingRegistrationStrategy(RegistrationStrategy strategy) {
        this.bindingRegistrationStrategy = strategy;
        return this;
    }

    public GenericInjectorFactory setScopeRegistrationStrategy(RegistrationStrategy strategy) {
        this.scopeRegistrationStrategy = strategy;
        return this;
    }

    public GenericInjector create() {
        var injector = GenericInjector.usingDefaults();

        var bindingRegistry = injector.getBindingRegistry();
        var scopeRegistry = injector.getScopeRegistry();

        registerInjector(bindingRegistry, injector);
        registerDefaultScopes(scopeRegistry);

        var binder = GenericBinder.using(bindingRegistry,
                                         scopeRegistry,
                                         bindingRegistrationStrategy,
                                         scopeRegistrationStrategy);
        configureModules(binder);

        var installedModules = binder.getInstalledModules();
        processModules(installedModules, binder, injector);

        initializeEagerSingletons(bindingRegistry, injector);

        return injector;
    }

    private void registerInjector(BindingRegistry bindingRegistry, Injector injector) {
        bindingRegistry.bind(Injector.class)
                       .toInstance(injector);
    }

    private void registerDefaultScopes(ScopeRegistry scopeRegistry) {
        scopeRegistry.registerScope(Unscoped.class, UnscopedScopeHandler.empty());

        var singletonScopeHandler = SingletonScopeHandler.empty();
        scopeRegistry.registerScope(Singleton.class, singletonScopeHandler);
        scopeRegistry.registerScope(EagerSingleton.class, singletonScopeHandler);
    }

    private void configureModules(GenericBinder binder) {
        initialModules.forEach(binder::install);
    }

    private void processModules(Iterable<Module> modules, Binder binder, Injector injector) {
        for (var moduleProcessor : moduleProcessors) {
            modules.forEach(module -> moduleProcessor.process(module, binder, injector));
        }
    }

    private void initializeEagerSingletons(BindingRegistry bindingRegistry, Injector injector) {
        Collection<? extends Binding<?>> bindings = bindingRegistry.getBindingData().values();
        bindings.stream()
                .filter(this::isEagerSingleton)
                .map(Binding::getClassKey)
                .forEach(injector::getInstanceOf);
    }

    private boolean isEagerSingleton(Binding<?> binding) {
        return binding.getScope().equals(EagerSingleton.class);
    }
}
