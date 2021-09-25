package com.github.boavenn.minject;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GenericBindingRegistry implements BindingRegistry {
    private final Map<ClassKey<?>, Binding<?>> bindings = new HashMap<>();
    private final ClassInstantiator classInstantiator;

    public static GenericBindingRegistry using(ClassInstantiator classInstantiator) {
        return new GenericBindingRegistry(classInstantiator);
    }

    @Override
    public <T> BindingProviderBuilder<T> bind(ClassKey<T> classKey) {
        return new GenericBindingProviderBuilder<>(classKey, this::registerBinding, classInstantiator);
    }

    @Override
    public <T> BindingProviderBuilder<T> bind(Class<T> cls) {
        return bind(ClassKey.of(cls));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Optional<Binding<T>> getBindingFor(ClassKey<T> classKey) {
        var binding = bindings.get(classKey);
        return Optional.ofNullable((Binding<T>) binding);
    }

    @Override
    public <T> Optional<Binding<T>> getBindingFor(Class<T> cls) {
        return getBindingFor(ClassKey.of(cls));
    }

    @Override
    public <T> boolean isRegistered(ClassKey<T> classKey) {
        return bindings.containsKey(classKey);
    }

    @Override
    public <T> boolean isRegistered(Class<T> cls) {
        return isRegistered(ClassKey.of(cls));
    }

    @Override
    public <T> void registerBinding(Binding<T> binding) {
        bindings.put(binding.getClassKey(), binding);
    }
}
