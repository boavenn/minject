package com.github.boavenn.minject.injector.generic;

import com.github.boavenn.minject.ClassKey;
import com.github.boavenn.minject.binding.Binding;
import com.github.boavenn.minject.binding.BindingRegistry;
import com.github.boavenn.minject.exceptions.InjectorException;
import com.github.boavenn.minject.injector.Injector;
import com.github.boavenn.minject.scope.ScopeHandler;
import com.github.boavenn.minject.scope.ScopeRegistry;
import com.github.boavenn.minject.scope.Unscoped;
import com.github.boavenn.minject.utils.ReflectionUtils;
import lombok.AccessLevel;
import lombok.Getter;

import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.function.Function;

@Getter(AccessLevel.PACKAGE)
public class GenericInjector implements Injector {
    private final BindingRegistry bindingRegistry;
    private final ScopeRegistry scopeRegistry;

    GenericInjector(Function<Injector, BindingRegistry> bindingRegistryFactoryMethod,
                    Function<Injector, ScopeRegistry> scopeRegistryFactoryMethod) {
        this.bindingRegistry = bindingRegistryFactoryMethod.apply(this);
        this.scopeRegistry = scopeRegistryFactoryMethod.apply(this);
    }

    public static GenericInjector usingDefaults() {
        return builder().build();
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
            var scope = ReflectionUtils.findScopeOn(rawType).orElse(Unscoped.class);
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

    private <T> Binding<T> getBindingFor(ClassKey<T> classKey) {
        return bindingRegistry.getBindingFor(classKey)
                              .orElseThrow(() -> InjectorException.bindingNotRegistered(classKey));
    }

    private ScopeHandler getScopeHandlerFor(Class<? extends Annotation> scope) {
        return scopeRegistry.getScopeHandlerFor(scope)
                            .orElseThrow(() -> InjectorException.scopeNotRegistered(scope));
    }
}
