package com.github.boavenn.minject.binding.generic;

import com.github.boavenn.minject.instantiation.ClassInstantiator;
import com.github.boavenn.minject.binding.Binding;
import com.github.boavenn.minject.binding.BindingProviderBuilder;
import com.github.boavenn.minject.binding.BindingRegistry;
import com.github.boavenn.minject.ClassKey;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor(staticName = "using")
public class GenericBindingRegistry implements BindingRegistry {
    private final Map<ClassKey<?>, Binding<?>> bindings = new HashMap<>();
    private final ClassInstantiator classInstantiator;

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
