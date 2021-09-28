package com.github.boavenn.minject.instantiation;

public interface ClassInstantiator {
    <T> T instantiateObjectOf(Class<T> cls);
}
