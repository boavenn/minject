package com.github.boavenn.minject.injector;

import com.github.boavenn.minject.ClassKey;

import javax.inject.Provider;

public interface Injector {
    <T> T getInstanceOf(ClassKey<T> classKey);
    <T> T getInstanceOf(Class<T> cls);
    <T> Provider<T> getProviderOf(ClassKey<T> classKey);
    <T> Provider<T> getProviderOf(Class<T> cls);
}
