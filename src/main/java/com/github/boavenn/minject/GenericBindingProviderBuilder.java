package com.github.boavenn.minject;

import lombok.RequiredArgsConstructor;

import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class GenericBindingProviderBuilder<T> implements BindingProviderBuilder<T> {
    private final ClassKey<T> classKey;
    private final Consumer<Binding<? super T>> bindingConsumer;
    private final ClassInstantiator classInstantiator;

    @Override
    public <U extends T> BindingScopeBuilder to(Class<U> cls) {
        Provider<U> provider = () -> classInstantiator.instantiateObjectOf(cls);
        return new GenericBindingScopeBuilder<>(classKey, provider, bindingConsumer);
    }

    @Override
    public <U extends T> BindingScopeBuilder toProvider(Provider<U> provider) {
        return new GenericBindingScopeBuilder<>(classKey, provider, bindingConsumer);
    }

    @Override
    public <U extends T> void toInstance(U instance) {
        Provider<T> provider = () -> instance;
        var binding = GenericBinding.of(classKey, provider, Singleton.class);
        bindingConsumer.accept(binding);
    }
}
