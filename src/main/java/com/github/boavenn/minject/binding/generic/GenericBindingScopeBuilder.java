package com.github.boavenn.minject.binding.generic;

import com.github.boavenn.minject.scope.Unscoped;
import com.github.boavenn.minject.binding.Binding;
import com.github.boavenn.minject.binding.BindingScopeBuilder;
import com.github.boavenn.minject.ClassKey;
import lombok.RequiredArgsConstructor;

import javax.inject.Provider;
import java.lang.annotation.Annotation;
import java.util.function.Consumer;

@RequiredArgsConstructor
public class GenericBindingScopeBuilder<T> implements BindingScopeBuilder {
    private final ClassKey<T> classKey;
    private final Provider<? extends T> provider;
    private final Consumer<Binding<? super T>> bindingConsumer;

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
