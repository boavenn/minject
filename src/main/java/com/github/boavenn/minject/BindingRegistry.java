package com.github.boavenn.minject;

import java.util.Optional;

public interface BindingRegistry {
    <T> BindingProviderBuilder<T> bind(ClassKey<T> classKey);
    <T> BindingProviderBuilder<T> bind(Class<T> cls);
    <T> Optional<Binding<T>> getBindingFor(ClassKey<T> classKey);
    <T> Optional<Binding<T>> getBindingFor(Class<T> cls);
    <T> boolean isRegistered(ClassKey<T> classKey);
    <T> boolean isRegistered(Class<T> cls);
    <T> void registerBinding(Binding<T> binding);
}
