package com.github.boavenn.minject.injector.generic;

import com.github.boavenn.minject.binding.Binding;
import com.github.boavenn.minject.binding.BindingRegistry;
import com.github.boavenn.minject.exceptions.InjectorException;
import com.github.boavenn.minject.injector.ClassKey;
import com.github.boavenn.minject.injector.Injector;
import com.github.boavenn.minject.scope.ScopeHandler;
import com.github.boavenn.minject.scope.ScopeRegistry;
import com.github.boavenn.minject.scope.Unscoped;

import javax.inject.Provider;
import javax.inject.Scope;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;

public class GenericInjector implements Injector {
    private final BindingRegistry bindingRegistry;
    private final ScopeRegistry scopeRegistry;

    GenericInjector(Function<Injector, BindingRegistry> bindingRegistryConfiguration, ScopeRegistry scopeRegistry) {
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
        if (eligibleForImplicitBinding(classKey)) {
            var rawType = (Class<T>) classKey.getRawType();
            var scope = getScopeOf(rawType).orElse(Unscoped.class);
            bindingRegistry.bind(classKey)
                           .to(rawType)
                           .in(scope);
        }

        var binding = getBindingFor(classKey);
        var scopeHandler = getScopeHandlerFor(binding.getScope());

        return (Provider<T>) scopeHandler.scopeProvider(classKey, binding.getProvider());
    }

    @Override
    public <T> Provider<T> getProviderOf(Class<T> cls) {
        return getProviderOf(ClassKey.of(cls));
    }

    private boolean eligibleForImplicitBinding(ClassKey<?> classKey) {
        return !bindingRegistry.isRegistered(classKey) && !classKey.isQualified() && !classKey.isParameterized();
    }

    private Optional<Class<? extends Annotation>> getScopeOf(Class<?> cls) {
        var annotations = cls.getDeclaredAnnotations();
        return Arrays.stream(annotations)
                     .filter(annotation -> annotation.annotationType().isAnnotationPresent(Scope.class))
                     .findFirst()
                     .map(Annotation::annotationType);
    }

    private <T> Binding<T> getBindingFor(ClassKey<T> classKey) {
        return bindingRegistry.getBindingFor(classKey)
                              .orElseThrow(() -> InjectorException.bindingNotRegistered(classKey));
    }

    private ScopeHandler getScopeHandlerFor(Class<? extends Annotation> scope) {
        return scopeRegistry.getScopeHandlerFor(scope)
                            .orElseThrow(() -> InjectorException.scopeNotRegistered(scope));
    }
}
