package com.github.boavenn.minject;

import lombok.RequiredArgsConstructor;

import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class GenericBindingScopeBuilder<T> implements BindingScopeBuilder {
    private final ClassKey<T> classKey;
    private final Provider<T> provider;
    private final Consumer<Binding<?>> bindingConsumer;

    @Override
    public void in(Class<? extends Annotation> scope) {
        var binding = GenericBinding.of(classKey, provider, scope);
        bindingConsumer.accept(binding);
    }

    @Override
    public void unscoped() {
        in(Unscoped.class);
    }
}
