package com.github.boavenn.minject.binding;

import com.github.boavenn.minject.ClassKey;

import java.util.Map;
import java.util.Optional;

public interface BindingRegistry {
    <T> BindingProviderBuilder<T> bind(ClassKey<T> classKey);
    <T> BindingProviderBuilder<T> bind(Class<T> cls);
    <T> Optional<Binding<T>> getBindingFor(ClassKey<T> classKey);
    <T> Optional<Binding<T>> getBindingFor(Class<T> cls);
    <T> boolean isRegistered(ClassKey<T> classKey);
    <T> boolean isRegistered(Class<T> cls);
    <T> void registerBinding(Binding<T> binding);
    Map<ClassKey<?>, ? extends Binding<?>> getBindingData();
}
