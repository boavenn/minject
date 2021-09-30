package com.github.boavenn.minject.configuration.generic;

import com.github.boavenn.minject.ClassKey;
import com.github.boavenn.minject.binding.BindingProviderBuilder;
import com.github.boavenn.minject.binding.BindingRegistry;
import com.github.boavenn.minject.configuration.Binder;
import com.github.boavenn.minject.configuration.Module;
import com.github.boavenn.minject.configuration.RegistrationStrategy;
import com.github.boavenn.minject.scope.ScopeHandler;
import com.github.boavenn.minject.scope.ScopeRegistry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor(staticName = "using")
public class GenericBinder implements Binder {
    @Getter
    private final Set<Module> installedModules = new HashSet<>();
    private final BindingRegistry bindingRegistry;
    private final ScopeRegistry scopeRegistry;
    private final RegistrationStrategy registrationStrategy;

    @Override
    public <T> BindingProviderBuilder<T> bind(ClassKey<T> classKey) {
        var isRegistered = bindingRegistry.isRegistered(classKey);
        return registrationStrategy.register(() -> bindingRegistry.bind(classKey), isRegistered, classKey.toString());
    }

    @Override
    public <T> BindingProviderBuilder<T> bind(Class<T> cls) {
        var isRegistered = bindingRegistry.isRegistered(cls);
        return registrationStrategy.register(() -> bindingRegistry.bind(cls), isRegistered, cls.getName());
    }

    @Override
    public void bindScope(Class<? extends Annotation> scope, ScopeHandler scopeHandler) {
        var isRegistered = scopeRegistry.isRegistered(scope);
        registrationStrategy.register(() -> {
            scopeRegistry.registerScope(scope, scopeHandler);
            return null; // So supplier is of <Void> type
        }, isRegistered, scope.getName());
    }

    @Override
    public void install(Module module) {
        if (!installedModules.contains(module)) {
            installedModules.add(module);
            module.configure(this);
        }
    }
}
